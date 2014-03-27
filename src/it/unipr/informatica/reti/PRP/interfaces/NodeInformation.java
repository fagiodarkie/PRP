package it.unipr.informatica.reti.PRP.interfaces;

import java.net.InetAddress;



public interface NodeInformation {

	// Basic information for a node
	public abstract String getNick();
	public abstract int getPort();
	public abstract InetAddress getAddress();
	
}
