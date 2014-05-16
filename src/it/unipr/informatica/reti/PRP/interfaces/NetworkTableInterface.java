package it.unipr.informatica.reti.PRP.interfaces;

public interface NetworkTableInterface {

	/**
	 * Module for network managing: know whether a nick is connected and how to reach him.
	 * Also notify user connection and disconnection. 
	 */
	public abstract boolean isConnected(String nick);
	public abstract UserInformationsInterface getInfoByNick(String nick);
	public abstract UserInformationsInterface howToReach(String nick);
	public abstract void isNowReachedBy(UserInformationsInterface reached, UserInformationsInterface newInterface);
	public abstract void isNowReachedBy(String reached, String newInterface);
	public abstract void disconnected(String nick);
	public abstract boolean isNearMe(String nick);
	
}
