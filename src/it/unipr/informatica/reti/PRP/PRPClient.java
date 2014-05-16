package it.unipr.informatica.reti.PRP;

import it.unipr.informatica.reti.PRP.UserInterface.UserInterface;
import it.unipr.informatica.reti.PRP.implementation.Connections;
import it.unipr.informatica.reti.PRP.implementation.ServerComponent;
import it.unipr.informatica.reti.PRP.implementation.TableManager;
import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;
import it.unipr.informatica.reti.PRP.interfaces.clientCommunicationManagerInterface;

public class PRPClient {

	public static void main(String args[]) {
	
		
	TableManager tableManager = new TableManager();
	Connections connections = new Connections();
		
	//STEP 1 CREATE AND INITIALIZE USER INTERFACE
		
		final UserInterface userInterface = new UserInterface(new UserInterfaceCommandManager() {
			
			@Override
			public void ManageInput(String Message) {
				// TODO Auto-generated method stub
			}
		});
		
	//STEP 2 READ DATA AND CONNECT TO DAD
		
	//STEP 3 CREATE SERVER LISTENER
		ServerComponent serverComponent = new ServerComponent(tableManager,connections, new clientCommunicationManagerInterface() {
			
			@Override
			public Boolean SendMessage(String Message) {
				userInterface.PrintMessage(Message);
				return null;
			}
		});
	
	//faccio partire il server:
		serverComponent.start();
	}
	
}
