package it.unipr.informatica.reti.PRP.implementation;

import java.util.List;

import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.NetworkManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class Connections implements ClientInterface {

	
	
	private SwingApplication application;

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
