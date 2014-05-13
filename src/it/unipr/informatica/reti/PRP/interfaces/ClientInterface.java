package it.unipr.informatica.reti.PRP.interfaces;

import java.util.List;

public interface ClientInterface {
	
	/**
	 * Interface that provides access to the network.
	 */
	
	// Connection functions
	/**
	 * Default connection to the network.
	 * Hint: let the user select a node to which it may try to connect
	 * instead of leaving it to the default.
	 */
	public abstract void connect();
	/**
	 * Disconnection procedure: frees the acquired sockets.
	 */
	public abstract void disconnect();
	// End of connection functions
	
	// Start message send functions
	/**
	 * Simple sender function. May be called by multicastMessage multiple times.
	 * @param nickname the user to which the message must be sent
	 * @param message the message to be sent
	 */
	public void unicastMessage(String nickname, String message);
	/**
	 * This method may simply replicates unicastMessage for every nick, but may also implement
	 * a flooding system.
	 * @param nicks the users to which the message must be sent
	 * @param message the message to be sent
	 */
	public void multicastMessage(List<String> nicks, String message);
	/**
	 * Simple broadcast sender. Instead of replicating unicastMessage, the use of 
	 * MessageCode.BROADCAST and flooding is recommended.
	 * @param message the message to be broadcasted
	 */
	public void broadcastMessage(String message);
	// End of message send functions
}
