package it.unipr.informatica.reti.PRP.interfaces;

import it.unipr.informatica.reti.PRP.implementation.Client;

public interface Command {
	public void ManageMessage(String Message,String Client);
	public void ManageMessage(String PartsOfMessage[]);
	public void ManageDisconnection(String Name);
}
