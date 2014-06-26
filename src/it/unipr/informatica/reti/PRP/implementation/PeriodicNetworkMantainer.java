package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.PeriodicNetworkMaintenanceInterface;

public class PeriodicNetworkMantainer implements PeriodicNetworkMaintenanceInterface {
	
	/**
	 * Module that provides "interrupts" for network management:
	 * every 5 minutes, it orders to broadcast the table.
	 * 
	 * By now, it is considered a useless class, adding little to the program stability,
	 * therefore it is left blank.
	 * Its implementation should be simple enough, however. Just put here a timer
	 * for any message or event which should be sent or occur systematically (every tot time).
	 * 
	 */

}
