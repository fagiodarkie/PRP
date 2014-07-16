package it.unipr.informatica.reti.PRP.implementation;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import it.unipr.informatica.reti.PRP.interfaces.NetworkTableInterface;

public class TableManager2 implements NetworkTableInterface{

	
	Hashtable<String, String> howToReach;
	public TableManager2() {
		howToReach = new Hashtable<>();
	}
	
	@Override
	public boolean isItConnected(String nick) {
		
		return howToReach.containsKey(nick);
		
	}

	@Override
	public String howToReach(String nick) {
		
		if(howToReach.containsKey(nick))
			return howToReach.get(nick);
		
		return null;
	}

	@Override
	public void notifyIsReachedBy(String reached, String newInterface) {
		if(reached != null && !reached.trim().isEmpty() &&
		   newInterface != null && !newInterface.trim().isEmpty())
		howToReach.put(reached.trim(), newInterface.trim());
	}

	@Override
	public void hasDisconnected(String nick) {
		if(nick != null && !nick.isEmpty())
		howToReach.remove(nick);
	}

	@Override
	public boolean isNearMe(String nick) {
		if(nick != null && !nick.isEmpty())
			if(howToReach.containsKey(nick))
			{
				return howToReach.get(nick).equals(nick);
			}
		return false;
	}

	@Override
	public List<String> getConnectedNodes() {
		
		return new ArrayList<String>( howToReach.keySet());

	}

	@Override
	public List<String> allMyNeighbors() {
		List<String> neighbors = new LinkedList<>();
		
		for(String nick : howToReach.keySet())
		 if(howToReach.get(nick).equals(nick))
			 neighbors.add(nick);
		
		return neighbors;
	}

}
