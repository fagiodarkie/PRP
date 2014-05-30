package it.unipr.informatica.reti.PRP;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Savepoint;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.jws.soap.SOAPBinding.Use;

import com.sun.org.apache.xalan.internal.lib.NodeInfo;

import it.unipr.informatica.reti.PRP.implementation.NetworkConnectionsManager;
import it.unipr.informatica.reti.PRP.implementation.ParentClientManager;
import it.unipr.informatica.reti.PRP.implementation.ParentsManager;
import it.unipr.informatica.reti.PRP.implementation.ServerComponent;
import it.unipr.informatica.reti.PRP.implementation.TableManager;
import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.UserInformationsInterface;
import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;
import it.unipr.informatica.reti.PRP.interfaces.ClientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.userInterface.UserInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class PRPClient {


	static TableManager tableManager ;
	static NetworkConnectionsManager connections ;
	public static void main(String args[]) throws UnknownHostException {
	
		
		

	tableManager = new TableManager();
	connections = new NetworkConnectionsManager();
	
		final String Nick;
	//CREATE AND INITIALIZE USER INTERFACE
		
		final UserInterface userInterface = new UserInterface();
		
		Nick = userInterface.getNick();
		
	//CREATE SERVER LISTENER
		final ServerComponent serverComponent = new ServerComponent(tableManager,connections, new ClientCommunicationManagerInterface() {
			
			@Override
			public Boolean SendMessage(String Message) {
				userInterface.PrintMessage(Message);
				return null;
			}
		});
		
		userInterface.setCommand(new UserInterfaceCommandManager() {
			
			@Override
			public void ManageInput(String Message) {
				if(Message.toUpperCase() == "EXIT")
				{
					//prima di uscire salvo i dati dei nodi che conosco
					SaveTable();
					
					//successivamente termino il programma
					System.exit(0);
				}
				else
				serverComponent.ManageMessageFromUserInterface(Message, Nick);
			}
		});
		
		//creo il componente che gestirà (e ricollegherà in caso d'errore) il padre
		ParentsManager parentsManager = new ParentsManager(connections,new Command() {
			
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
		});

		//comando al gestore di cercare un nodo lilbero tra quelli salvati precedentemente al quale connettermi
		parentsManager.connect(Constants.PathOfTableBackupFile + "//" + Constants.NameOfTableBackupFile, //path del file di backup della tabella
								Nick, //mio nick con cui autenticarmi
								Constants.PortOfServer, //porta a cui far autenticare il server 
								InetAddress.getByName("127.0.0.1") //mio ip
								);
		
	
		//faccio partire l'interfaccia grafica
		userInterface.StartReadingFromInput();
		
		
		//faccio partire il ServerInterface:
		serverComponent.start();
	}
	
	
	public static void SaveTable()
	{

		List<String> listaNick = tableManager.getConnectedNodes();
		
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.PathOfTableBackupFile+"//"+Constants.NameOfTableBackupFile, true)))) {
			for(String nick : listaNick)
			{
				UserInformationsInterface nodeInfo = tableManager.getInfoByNick(nick);
				out.println(nodeInfo.getNick() + Constants.FileBackupInformationDivisor + 
							nodeInfo.getPort()+ Constants.FileBackupInformationDivisor + 
							nodeInfo.getAddress().toString() );
			}
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
		
		
	}
	}
