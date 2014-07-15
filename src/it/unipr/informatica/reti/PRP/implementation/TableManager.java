package it.unipr.informatica.reti.PRP.implementation;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import it.unipr.informatica.reti.PRP.interfaces.NetworkTableInterface;
import it.unipr.informatica.reti.PRP.interfaces.UserInformationsInterface;

public class TableManager implements NetworkTableInterface {

	/**
	 * Class for table management.
	 * 
	 * It serves the purpose of giving information about whether a user is
	 * or is not connected at a given time, how to reach him etc
	 * 
	 * When a change in the network structure occurs, this module MUST be warned
	 * with the appropriate methods.
	 * 
	 * @author Jacopo Freddi
	 *
	 */
	
	/**
	 * private class to implement the table. This shouldn't be of
	 * interest to the user.
	 */
	private class Couple {
		
		private String reachable;
		private String exitInterface;
		
		public Couple(String first, String second) {
			reachable = first;
			exitInterface = second;
		}
		
		public String getReachable() {
			return reachable;
		}
		
		public String getInterface() {
			return exitInterface;
		}
				
		public void resetInterface(String newInterface) {
			exitInterface = newInterface;
		}
	}
	
	/**
	 * actual map, that is looked up to get informations about the network.
	 */
	private List<Couple> howToReach;
	private Hashtable<String, UserInformationsInterface> informations;


	public TableManager()
	{
		howToReach = new LinkedList<TableManager.Couple>();
	}
	
	@Override
	/**
	 * Check if the required user is connected to the network.
	 * The user is identified by its nickname.
	 * 
	 * @param nick the nickname of the user whose status should be checked.
	 * @returns true if the nick is still within reach of the network, false otherwise.
	 * This may be inaccurate due to delays in information propagation.
	 */
	public boolean isItConnected(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().equals(nick))
				return true;
		return false;
	}

	@Override
	/**
	 * Provides the informations about the user whose nickname is specified.  
	 * 
	 * @param nick the nickname of the user whose informations are required
	 * @returns the UserInformationsInterface structure holding the informations
	 * regarding the user. If the user is not connected, null is returned.
	 */
	public UserInformationsInterface getInfoByNick(String nick) {
		if (informations.containsKey(nick))
			return informations.get(nick);
		return null;
	}
	
	@Override
	/**
	 * Saves data for a new node.
	 * 
	 * @param newInformations the UserInformationsInterface structure holding the
	 * relevant informations. If such informations were already store, they are
	 * deleted and updated.
	 */
	public void insertNode(UserInformationsInterface newInformations) {
		if (informations.containsKey(newInformations.getNick()))
			informations.remove(newInformations.getNick());
		informations.put(newInformations.getNick(), newInformations);
	}

	@Override
	/**
	 * Provides informations about how to reach a given user, given its name.
	 * 
	 * @param nick the nickname of the user which should be reached.
	 * @returns the nickname of the node through which we can reach the user.
	 * If the requested user cannot be reached, null is returned.
	 */
	public String howToReach(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().equals(nick)) {
				return howToReach.get(i).getInterface();
			}
		return null;
	}

	@Override
	/**
	 * This method MUST be called when the equivalent message is received.
	 * It updates the table adjusting the necessary informations.
	 * 
	 * @param reached the nickname of the user which can be reached by a new interface.
	 * @param newInterface the nickname of the user by which the node may be reached.
	 */
	public void notifyIsReachedBy(String reached,
			String newInterface) {
		boolean existed = false;
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().equals(reached)) {
				howToReach.get(i).resetInterface(newInterface);
				existed = true;
				break;
			}
		if (!existed)
			howToReach.add(new Couple(reached, newInterface));
	}
	
	/**
	 * Provides a list of nicknames of all the neighbors of the current user. If nobody
	 * is currently connected, an empty list is returned.
	 * 
	 * @return a list of nicknames of the neighbors, if any. Empty list otherwise.
	 */
	@Override
	public List<String> allMyNeighbors() {
		List<String> result = new LinkedList<String>();
		for (int i = 0; i < howToReach.size(); ++i) {
			String current = howToReach.get(i).getInterface();
			if (!result.contains((String)current) )
				result.add(current);
			}
		return result;
	}

	@Override
	/**
	 * This method MUST be called when the equivalent message is received.
	 * It updates the table removing the informations regarding the disconnected user.
	 * Call this method whenever a user disconnects from the client, or when a
	 * message notifying that a user disconnected arrives.
	 * 
	 * @param nick the nickname of the user who disconnected.
	 */
	public void hasDisconnected(String nick) {
		for (int i = howToReach.size() -1; i >= 0; --i) {
			if (howToReach.get(i).getReachable().equals(nick)
				|| howToReach.get(i).getInterface().equals(nick)) 
				howToReach.remove(i);
		}
	}

	@Override
	/**
	 * Provides informations about whether the requested user is directly linked with us.
	 * 
	 * @param nick the nickname of the user whose proximity must be determined
	 * @returns true if the link with the selected user is direct and does not
	 * cross another node. False otherwise (including the case in which the
	 * user is not connected).
	 */
	public boolean isNearMe(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getInterface().equals(nick))
				return true;
		return false;
	}

	@Override
	public List<String> getConnectedNodes() {
		List<String> result = new LinkedList<String>();
		for (int i = 0; i < howToReach.size(); ++i) {
			result.add(howToReach.get(i).getReachable());
		}
		return result;
	}

	
	
}
