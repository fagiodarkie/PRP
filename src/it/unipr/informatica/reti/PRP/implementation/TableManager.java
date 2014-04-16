package it.unipr.informatica.reti.PRP.implementation;

import java.util.Map;

import it.unipr.informatica.reti.PRP.interfaces.NetworkManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class TableManager implements NetworkManagerInterface {

	// TODO FAGIO
	
	private SwingApplication application;
	private Map<NodeInformation, NodeInformation> howToReach;
	
	public TableManager(SwingApplication swingApplication) {
		application = swingApplication;
	}

	@Override
	public boolean isConnected(String nick) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NodeInformation getInfoByNick(String nick) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String howToReach(String Nick) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
