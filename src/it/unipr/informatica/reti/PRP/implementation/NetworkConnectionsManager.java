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
	 * 
	 * @param nick
	 * @param newClient
	 * @return return false if the nickname is already used
	 */
	public boolean addClient(String nick, ClientInterface newClient)
	{
		if(hashtableNickClient.containsKey(nick))
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
		if(!hashtableNickClient.containsKey(nick))
			return false;
		
		hashtableNickClient.remove(nick);
		return true;
	}
	
	public ClientInterface getClient (String nick)
	{

		//TODO REMOVE TEST
		System.out.println(nick);
		if(!hashtableNickClient.containsKey(nick))
			return null;
		
		return hashtableNickClient.get(nick);
		
	}
	
	public boolean sendMesage(String nick, String Message)
	{
		if(!hashtableNickClient.containsKey(nick))
			return false;
		else
		{
			((ClientInterface)(hashtableNickClient.get(nick))).sendMessage(Message);
			return true;
		}
	}

}
