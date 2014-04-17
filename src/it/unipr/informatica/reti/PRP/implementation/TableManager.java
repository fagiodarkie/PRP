package it.unipr.informatica.reti.PRP.implementation;

import java.util.List;
import it.unipr.informatica.reti.PRP.interfaces.NetworkManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class TableManager implements NetworkManagerInterface {

	// TODO FAGIO
	
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
	
	private SwingApplication application;
	private List<Couple> howToReach;
	
	public TableManager(SwingApplication swingApplication) {
		application = swingApplication;
	}

	@Override
	public boolean isConnected(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick))
				return true;
		return false;
	}

	@Override
	public NodeInformation getInfoByNick(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick))
				return howToReach.get(i).getReachable();
		return null;
	}

	@Override
	public NodeInformation howToReach(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick)) {
				return howToReach.get(i).getInterface();
			}
		return null;
	}

	@Override
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
	public void isNowReachedBy(String reached, String newInterface) {
		NodeInformation reachedNode = new UserInformations(reached);
		NodeInformation howToReachIt = new UserInformations(newInterface);
		isNowReachedBy(reachedNode, howToReachIt);
		
	}

	@Override
	public void disconnected(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick)) {
				howToReach.remove(i);
				break;
			}
	}

	@Override
	public boolean isNearMe(String nick) {
		for (int i = 0; i < howToReach.size(); ++i)
			if (howToReach.get(i).getReachable().getNick().equals(nick))
				return howToReach.get(i).isNearMe();
		return false;
	}

	
	
}
