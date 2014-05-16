package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.PeriodicNetworkMaintenanceInterface;

public class PeriodicNetworkMantainer implements PeriodicNetworkMaintenanceInterface {
	
	/**
	 * Module that provides "interrupts" for network management:
	 * 1) every 5 minutes, it orders to broadcast the table.
	 */

	
	public PeriodicNetworkMantainer() {
	}

}
