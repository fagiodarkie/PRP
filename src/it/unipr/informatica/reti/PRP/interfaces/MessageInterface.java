package it.unipr.informatica.reti.PRP.interfaces;

import java.security.Timestamp;

public abstract class MessageInterface {

	public abstract String getSender();
	public abstract String getReceiver();
	public abstract Timestamp getTimestamp();
	public abstract MessageCode getCode();
	public abstract String getData();
}
