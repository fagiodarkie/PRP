package it.unipr.informatica.reti.PRP.interfaces;

public interface NetworkManagerInterface {

	/*
	 * Module for network managing: know whether a nick is connected and how to reach him. 
	 */
	public abstract boolean isConnected(String nick);
	public abstract NodeInformation getInfoByNick(String nick);
	public abstract NodeInformation howToReach(String nick);
	public abstract void isNowReachedBy(NodeInformation reached, NodeInformation newInterface);
	public abstract void isNowReachedBy(String reached, String newInterface);
	public abstract void disconnected(String nick);
	public abstract boolean isNearMe(String nick);
	
}
