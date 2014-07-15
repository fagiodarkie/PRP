package it.unipr.informatica.reti.PRP.interfaces;

import java.util.List;

public interface NetworkTableInterface {

	/**
	 * Module for network managing: know whether a nick is connected and how to reach him.
	 * Also notify user connection and disconnection. 
	 */
	public boolean isItConnected(String nick);
	public UserInformationsInterface getInfoByNick(String nick);
	public String howToReach(String nick);
	public void notifyIsReachedBy(String reached, String newInterface);
	public void hasDisconnected(String nick);
	public boolean isNearMe(String nick);
	public List<String> getConnectedNodes();
	public List<String> allMyNeighbors();
	void insertNode(UserInformationsInterface informations);
}
