package it.unipr.informatica.reti.PRP.interfaces;

import java.net.InetAddress;

public interface NodeInformation {

	/*
	 * Basic informations for a node:
	 * - Nickname
	 * - Address
	 * - Port
	 */
	public abstract String getNick();
	public abstract int getPort();
	public abstract InetAddress getAddress();
	
}
