package it.unipr.informatica.reti.PRP.interfaces;

public interface NetInterface {
	
	/**	Interface for node-related tasks, such as connecting and sending messages.
	 */

	/**
	 * Basic method to send a message inside the channel.
	 * @param message the formatted, marshalled message which must be sent.
	 */
	public abstract void send(String message);
	// TODO write the other methods
}
