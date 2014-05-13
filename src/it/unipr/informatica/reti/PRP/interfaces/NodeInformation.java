package it.unipr.informatica.reti.PRP.interfaces;

import java.net.InetAddress;

public interface NodeInformation {

	/**
	 * Basic informations for a node:
	 * - Nickname
	 * - Address
	 * - Port
	 * 
	 * This interface declare as mandatory the getter for these three informations
	 * and provides a method to compare two NodeInformations.
	 */
	public abstract String getNick();
	public abstract int getPort();
	public abstract InetAddress getAddress();
	
	public abstract boolean equals(NodeInformation i);
	
}
