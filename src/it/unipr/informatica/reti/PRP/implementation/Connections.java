package it.unipr.informatica.reti.PRP.implementation;

import java.util.List;

import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.NetworkManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class Connections implements ClientInterface {

	/**
	 * Module abstracting the communication with other clients.
	 * All messages sent or received pass through Connections, which redirects
	 * them in the right direction (routing, grapical user interface, table update, ...)
	 */
	
	private SwingApplication application;

	/**
	 * Default constructor for Connections in gui version of the application.
	 * 
	 * @param swingApplication class containing the Connections module.
	 */
	public Connections(SwingApplication swingApplication) {
		application = swingApplication;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unicastMessage(String nickname, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void multicastMessage(List<String> nicks, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcastMessage(String message) {
		// TODO Auto-generated method stub
		
	}


}
