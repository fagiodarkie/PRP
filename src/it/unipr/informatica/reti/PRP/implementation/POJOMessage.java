package it.unipr.informatica.reti.PRP.implementation;

import java.security.Timestamp;

import it.unipr.informatica.reti.PRP.interfaces.MessageCode;
import it.unipr.informatica.reti.PRP.interfaces.MessageInterface;

public class POJOMessage implements MessageInterface {

	// TODO FAGIO

	/**
	 * Message implementation: informations about
	 * - sender
	 * - destination
	 * - message type
	 * - actual data
	 * 
	 * are provided.
	 */
	
	/**
	 * @return the sender nickname, if any.
	 */
	@Override
	public String getSender() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the destination nickname, if any.
	 */
	@Override
	public String getReceiver() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the time in which this message was generated
	 */
	@Override
	public Timestamp getTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @return the message type, as enum.
	 */
	@Override
	public MessageCode getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the actual data of the message.
	 * This data should be interpreted in relationship with the
	 * message type: in a broadcast message getData() returns
	 * the message body, while in a table message getData() contains
	 * a marshalled table.
	 */
	@Override
	public String getData() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
