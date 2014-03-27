package it.unipr.informatica.reti.PRP.interfaces;

import java.net.InetAddress;

public abstract class NodeInterface {

	// Getters
	public abstract String getNick();
	public abstract InetAddress getAddress();
	public abstract int getPort();
	// End of getters

	
	// Start of message utility functions
	public abstract void send(String message);
	// End of message utility functions
}
