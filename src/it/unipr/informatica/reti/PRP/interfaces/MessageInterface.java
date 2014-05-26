package it.unipr.informatica.reti.PRP.interfaces;

import java.security.Timestamp;

public abstract interface MessageInterface {

	/**
	 * Abstract interface for a message.
	 * Getter for the basic informations of the message are mandatory.
	 */
	public abstract String getSender();
	public abstract String getReceiver();
	public abstract Timestamp getTimestamp();
	public abstract String getCode();
	public abstract String getData();
}
