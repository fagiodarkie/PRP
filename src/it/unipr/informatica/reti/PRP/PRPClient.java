package it.unipr.informatica.reti.PRP;

import java.io.*;
import java.net.InetAddress;
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
	public static void main(String args[]) {
	
		
		

	tableManager = new TableManager();
	connections = new NetworkConnectionsManager();
	
		final String Nick;
	//STEP 1 CREATE AND INITIALIZE USER INTERFACE
		
		final UserInterface userInterface = new UserInterface();
		
		Nick = userInterface.getNick();
		
	//STEP 2 READ DATA AND CONNECT TO DAD
		//TODO IMPLEMENT READING DATA FROM BACKUP FILE
		//TODO IMPLEMENT CONNECTION TO DAD
		//TODO ADD DAD TO CONNECTIONS
	//STEP 3 CREATE SERVER LISTENER
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
				// TODO remove test
				//TEST
				//System.out.println(Message);
				//END TEST
				serverComponent.ManageMessageFromUserInterface(Message, Nick);
			}
		});
		
		ParentsManager parentsManager = new ParentsManager();
		
	//faccio partire l'interfaccia grafica
		userInterface.StartReadingFromInput();
	//faccio partire il ServerInterface:
		serverComponent.start();
	}
	
	
	private void SaveTable()
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
