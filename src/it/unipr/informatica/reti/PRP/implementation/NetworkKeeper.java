package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.NetworkMantainer;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class NetworkKeeper implements NetworkMantainer {

	private SwingApplication application;

	public NetworkKeeper(SwingApplication swingApplication) {
		application = swingApplication;
	}

}
