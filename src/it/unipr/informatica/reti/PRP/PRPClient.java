package it.unipr.informatica.reti.PRP;

import it.unipr.informatica.reti.PRP.UserInterface.UserInterface;
import it.unipr.informatica.reti.PRP.implementation.ServerComponent;
import it.unipr.informatica.reti.PRP.interfaces.PRPApplication;
import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;

public class PRPClient {

	public static void main(String args[]) {
	
	//STEP 1 CREATE AND INITIALIZE USER INTERFACE
		
		UserInterface userInterface = new UserInterface(new UserInterfaceCommandManager() {
			
			@Override
			public void ManageInput(String Message) {
				// TODO Auto-generated method stub
				
			}
		});
		
	//STEP 2 READ DATA AND CONNECT TO DAD
		
	//STEP 3 CREATE SERVER LISTENER
		ServerComponent serverComponent = new ServerComponent();
	
	
	}
	
}
