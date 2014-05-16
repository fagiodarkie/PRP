package it.unipr.informatica.reti.PRP.interfaces;


public interface Command {
	public void manageMessage(String Message,String Client);
	public void manageMessage(String PartsOfMessage[]);
	public void manageDisconnection(String Name);
}
