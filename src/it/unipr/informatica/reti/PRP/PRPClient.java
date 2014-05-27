package it.unipr.informatica.reti.PRP;

import it.unipr.informatica.reti.PRP.implementation.NetworkConnectionsManager;
import it.unipr.informatica.reti.PRP.implementation.ParentClientManager;
import it.unipr.informatica.reti.PRP.implementation.ServerComponent;
import it.unipr.informatica.reti.PRP.implementation.TableManager;
import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;
import it.unipr.informatica.reti.PRP.interfaces.ClientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.userInterface.UserInterface;

public class PRPClient {

	
	public static void main(String args[]) {
	
		
		

	TableManager tableManager = new TableManager();
	NetworkConnectionsManager connections = new NetworkConnectionsManager();
	
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
		
	//faccio partire l'interfaccia grafica
		userInterface.StartReadingFromInput();
	//faccio partire il ServerInterface:
		serverComponent.start();
	}
	
	
	
	
}
