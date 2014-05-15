package it.unipr.informatica.reti.PRP.interfaces;


public interface Command {
	public void ManageMessage(String Message,String Client);
	public void ManageMessage(String PartsOfMessage[]);
	public void ManageDisconnection(String Name);
}
