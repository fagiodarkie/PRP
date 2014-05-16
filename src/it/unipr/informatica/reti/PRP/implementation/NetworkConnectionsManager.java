package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;

import java.util.*;
public class NetworkConnectionsManager {

	//stato interno
	Hashtable<String, ClientInterface> hashtableNickClient;
	//end stato interno
	
	
	public NetworkConnectionsManager()
	{
		hashtableNickClient = new Hashtable<String, ClientInterface>();
	}
	
	/**
	 * Insert new client to the list
	 * @param 
	 * @param 
	 * @return return false if the nickname is already used
	 */
	public boolean addClient(String nick, ClientManager newClient)
	{
		if(hashtableNickClient.keySet().contains(nick))
			return false;
		
		hashtableNickClient.put(nick,newClient);
		return true;
	}
	
	/**
	 * Remove the client from the list
	 * @param 
	 * @return 
	 */
	public boolean removeClient(String nick)
	{
		if(!hashtableNickClient.keySet().contains(nick))
			return false;
		
		hashtableNickClient.remove(nick);
		return true;
	}
	
	public ClientInterface getClient (String nick)
	{
		if(!hashtableNickClient.keySet().contains(nick))
			return null;
		
		return hashtableNickClient.remove(nick);
		
	}
	public boolean setCliet(String nick, ClientManager newClient)
	{
		if(!hashtableNickClient.keySet().contains(nick))
			return false;
		
		//TODO SETCLIENT
		return true;
	}

}
