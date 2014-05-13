package it.unipr.informatica.reti.PRP.implementation;

import java.util.List;
import it.unipr.informatica.reti.PRP.interfaces.NetworkManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class TableManager implements NetworkManagerInterface {

	// TODO FAGIO

	/**
	 * Class for table management.
	 * 
	 * It serves the purpose of giving information about whether a user is
	 * or is not connected at a given time, how to reach him etc
	 * 
	 * When a change in the network structure occurs, this module MUST be warned
	 * with the appropriate methods.
	 * 
	 * @author darkfagio
	 *
	 */
	
	/**
	 * private class to implement the table. This shouldn't be of
	 * interest to the user.
	 * 
	 */
	private class Couple {
		
		private NodeInformation reachable;
		private NodeInformation exitInterface;
		
		public Couple(NodeInformation first, NodeInformation second) {
			reachable = first;
			exitInterface = second;
		}
		
		public NodeInformation getReachable() {
			return reachable;
		}
		
		public NodeInformation getInterface() {
			return exitInterface;
		}
		
		public boolean isNearMe() {
			return reachable.equals(exitInterface);
		}
		
		public void resetInterface(NodeInformation newInterface) {
			exitInterface = newInterface;
		}
	}
	
	/**
	 * parent module for gui application. All notifications regarding a gui
	 * update must be forwarded to the gui.
	 */
	private SwingApplication application;
	/**
	 * actual map, that is looked up to get informations about the network.
	 */
	private List<Couple> howToReach;
	
	/**
	 * gui constructor
	 * @param swingApplication the gui parent, to which updates regarding gui must be notified
	 */
	public TableManager(SwingApplication swingApplication) {
		application = swingApplication;
	}

	@Override
	/**
	 * @param nick the nick of the user whose status should be checked.
	 * @returns true if the nick is still within reach of the network.
	 * This may be inaccurate due to delays in information propagation.
	 */
	public boolean isConnected(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick))
				return true;
		return false;
	}

	@Override
	/**
	 * 
	 * @param nick the nickname of the user whose informations are required
	 * @returns the NodeInformation structure holding the informations
	 * regarding the user. If the user is not connected, null is returned.
	 */
	public NodeInformation getInfoByNick(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick))
				return howToReach.get(i).getReachable();
		return null;
	}

	@Override
	/**
	 * Provides informations about how to reach a given user, given its name.
	 * 
	 * @param nick the nickname of the user which should be reached.
	 * @returns the NodeInformation structure holding the informations
	 * regarding the node which can reach the user. If the requested
	 * user cannot be reached, null is returned.
	 */
	public NodeInformation howToReach(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick)) {
				return howToReach.get(i).getInterface();
			}
		return null;
	}

	@Override
	/**
	 * This method MUST be called when the equivalent message is received.
	 * It updates the table adjusting the necessary informations.
	 * 
	 * @param reached the NodeInformation structure holding the informations regarding
	 * the nick which can be reached by a new interface.
	 * @param newInterface the NodeInformation structure holding the informations
	 * regarding the new interface by which the node may be reached.
	 */
	public void isNowReachedBy(NodeInformation reached,
			NodeInformation newInterface) {
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

	@Override
	/**
	 * This method MUST be called when the equivalent message is received.
	 * It updates the table adjusting the necessary informations.
	 * 
	 * @param reached the marhsalled NodeInformation of the user which can be reached by a new interface.
	 * @param newInterface the marshalled NodeInformation of the user by which the node may be reached.
	 */
	public void isNowReachedBy(String reached, String newInterface) {
		NodeInformation reachedNode = new UserInformations(reached);
		NodeInformation howToReachIt = new UserInformations(newInterface);
		isNowReachedBy(reachedNode, howToReachIt);
		
	}

	@Override
	/**
	 * This method MUST be called when the equivalent message is received.
	 * It updates the table removing the informations regarding the disconnected user.
	 * 
	 * @param nick the nickname of the user who disconnected.
	 */
	public void disconnected(String nick) {
		for (int i = 0; i < howToReach.size(); ++i) {
			if (howToReach.get(i).getReachable().getNick().equals(nick)) 
				howToReach.remove(i);
			else if (howToReach.get(i).getInterface().getNick().equals(nick)) 
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
			if (howToReach.get(i).getInterface().getNick().equals(nick))
				return true;
		return false;
	}

	
	
}
