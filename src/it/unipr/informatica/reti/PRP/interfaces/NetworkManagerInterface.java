package it.unipr.informatica.reti.PRP.interfaces;

public interface NetworkManagerInterface {

	/*
	 * Module for network managing: know whether a nick is connected and how to reach him. 
	 */
	public abstract boolean isConnected(String nick);
	public abstract NodeInformation getInfoByNick(String nick);
	
}
