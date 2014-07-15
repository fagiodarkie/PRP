package it.unipr.informatica.reti.PRP.interfaces;

import java.net.InetAddress;

public interface ClientInterface {
	
	/**
	 * Interface that provides access to the network.
	 */
	
	public String getNick();
	public int getPort();
	public InetAddress getIP();
	public Boolean sendMessage(String message);
	public void stop();
}
