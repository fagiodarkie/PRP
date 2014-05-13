package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.NetworkMantainer;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class NetworkKeeper implements NetworkMantainer {
	
	/**
	 * Module that provides "interrupts" for network management:
	 * 1) every 5 minutes, it orders to broadcast the table.
	 */

	private SwingApplication application;

	public NetworkKeeper(SwingApplication swingApplication) {
		application = swingApplication;
	}

}
