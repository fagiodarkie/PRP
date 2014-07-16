package it.unipr.informatica.reti.PRP;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import it.unipr.informatica.reti.PRP.implementation.NetworkConnectionsManager;
import it.unipr.informatica.reti.PRP.implementation.ParentClientManager;
import it.unipr.informatica.reti.PRP.implementation.ParentsManager;
import it.unipr.informatica.reti.PRP.implementation.ServerComponent;
import it.unipr.informatica.reti.PRP.implementation.TableManager;
import it.unipr.informatica.reti.PRP.implementation.TableManager2;
import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.UserInformationsInterface;
import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;
import it.unipr.informatica.reti.PRP.interfaces.ClientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.userInterface.UserInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class PRPClient {

	
	public static void main(String args[]) throws UnknownHostException {

		Constants.tableManager = new TableManager2();
		Constants.connections = new NetworkConnectionsManager();
	
		final String Nick;
		//CREATE AND INITIALIZE USER INTERFACE
		
		final UserInterface userInterface = new UserInterface();
		
		Nick = userInterface.getNick();
		
		//CREATE SERVER LISTENER
		final ServerComponent serverComponent = new ServerComponent( new ClientCommunicationManagerInterface() {
			
			@Override
			public Boolean SendMessage(String Message) {
				userInterface.PrintMessage(Message);
				return null;
			}
		}, Nick);
		
		userInterface.setCommand(new UserInterfaceCommandManager() {
			
			@Override
			public void ManageInput(String Message) {
					switch (Message.toUpperCase())
					{
					case "EXIT":
						//prima di uscire salvo i dati dei nodi che conosco
						SaveTable();
						
						//successivamente termino il programma
						System.exit(0);
						break;
					
					case "HELP":
						userInterface.PrintMessage("PRPClient coded by Alessio Bortolotti and Jacopo Freddi\n");
						userInterface.PrintMessage("@nick message		: send message to user with the nickname equals to nick");
						userInterface.PrintMessage("@broadcast message	: send message to all user");
						userInterface.PrintMessage("nicks list			: get the list of connected users");
						userInterface.PrintMessage("exit				: exit from the chat network");
						userInterface.PrintMessage("!!!WARNING: nick are case sensitive!!!");
						break;
					
					case "NICKS LIST":
						String MessageToUserInterface = "";
						//per ogni nodo a me conosciuto inserisco il nickname nem messaggio
						List<String> l = Constants.tableManager.getConnectedNodes();
						System.out.println("list size:" + l.size());
						for(String nick : l)
							MessageToUserInterface += nick + ",";
						//mando il messaggio all'interfaccia grafica
						userInterface.PrintMessage(MessageToUserInterface);
						break;
						
					default:
						serverComponent.ManageMessageFromUserInterface(Message,userInterface.getNick());
						
					
					}
					
				
			}
		});
		
		//creo il componente che gestirà (e ricollegherà in caso d'errore) il padre
		ParentsManager parentsManager = new ParentsManager(new Command() {
			
			@Override
			public void manageMessage(String[] PartsOfMessage) {
				//DO NOTHING 
			}
			
			@Override
			public void manageMessage(String Message, String Client) {
				//giro il messaggio al server in modo che lo gestisca
				serverComponent.ManageMessage(Message, Client);
			}
			
			@Override
			public void manageDisconnection(String Name) {
				//giro il messaggio al  server
				serverComponent.ManageDisconnection(Name);
			}
		},
		new Command() {
			
			@Override
			public void manageMessage(String[] PartsOfMessage) {
				if(PartsOfMessage != null)
					serverComponent.setHasDad(PartsOfMessage[0], PartsOfMessage[1], PartsOfMessage[2]);
				else
					serverComponent.setHasDad();
			}
			
			@Override
			public void manageMessage(String Message, String Client) {
				// DO NOTHING
				
			}
			
			@Override
			public void manageDisconnection(String Name) {
				// DO NOTHING
				
			}
		});
		
		
		
		//controllo se l'utente ha scelto di connettersi manualmente
		if( userInterface.getConnessioneManuale())
		{

			
			if(!parentsManager.connect(userInterface.getNickManuale(), 
								   userInterface.getPortManuale(), 
								   userInterface.getIPManuale(), 
								   Nick, 
								   Constants.PortOfServer, 
								   InetAddress.getByName("127.0.0.1") 
								   ))
			{
				userInterface.PrintMessage("impossibile connettersi al server richiesto: il presente nodo sta per diventare un server principale");
				serverComponent.setHasDad();
			}
			else
			{
				ParentClientManager parent = parentsManager.getParent();
				serverComponent.setHasDad(parent.getNick(), parent.getIP().toString().replace("\\", "").replace("/", ""),String.valueOf(parent.getPort()));
						
			}
		}
		else
			//comando al gestore di cercare un nodo libero tra quelli salvati precedentemente al quale connettermi
			if(!parentsManager.connect(Constants.PathOfTableBackupFile + "//" + Constants.NameOfTableBackupFile, //path del file di backup della tabella
									Nick, //mio nick con cui autenticarmi
									Constants.PortOfServer, //porta a cui far autenticare il server 
									InetAddress.getByName("127.0.0.1") //mio ip
									))
			{
				userInterface.PrintMessage("nessun server a cui connettersi: il presente nodo sta per diventare un server principale");
				serverComponent.setHasDad();
			}
			else
			{
				ParentClientManager parent = parentsManager.getParent();
				serverComponent.setHasDad(parent.getNick(), parent.getIP().toString().replace("\\", "").replace("/", ""),String.valueOf(parent.getPort()));
						
			}
		//faccio partire l'interfaccia grafica
		userInterface.StartReadingFromInput();
		
		//faccio partire il ServerInterface:
		serverComponent.start();
	}
	
	public static void SaveTable()
	{

		List<String> listaNick = Constants.tableManager.allMyNeighbors();
		
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.PathOfTableBackupFile+"//"+Constants.NameOfTableBackupFile, true)))) {
			for(String nick : listaNick)
			{
				
				ClientInterface client = Constants.connections.getClient(nick);
				
				out.println(client.getNick() + Constants.FileBackupInformationDivisor + 
							client.getPort()+ Constants.FileBackupInformationDivisor + 
							client.getIP().toString() );
			}
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
		
		
	}
}
