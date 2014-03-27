package it.unipr.informatica.reti.PRP.interfaces;

import java.util.List;

public interface ClientInterface {
	
	// Connection functions
	public abstract void connect();
	public abstract void disconnect();
	// End of connection functions
	
	// Start message send functions
	public void unicastMessage(String nickname, String message);
	public void multicastMessage(List<String> nicks, String message);
	public void broadcastMessage(String message);
	// End of message send functions
}
