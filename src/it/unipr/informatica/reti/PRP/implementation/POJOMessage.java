package it.unipr.informatica.reti.PRP.implementation;

import java.security.Timestamp;

import it.unipr.informatica.reti.PRP.interfaces.MessageCode;
import it.unipr.informatica.reti.PRP.interfaces.MessageInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class POJOMessage implements MessageInterface {

	private String code;
	private String sender;
	private String receiver;
	private String data;

	/**
	 * Message implementation: informations about
	 * - sender
	 * - destination
	 * - message type
	 * - actual data
	 * 
	 * are provided.
	 */
	
	public POJOMessage()
	{
		code = "";
		sender = "";
		receiver = "";
		data = "";
		
	}
	public POJOMessage(String Message){
		
		this();
		
		String partsOfMessaggio[] = Message.split(Constants.MessagePartsDivisor);
		
		switch ( partsOfMessaggio.length )
		{
		case 4:
				data = partsOfMessaggio[3];
		case 3:
				receiver = partsOfMessaggio[2];
		case 2:
				sender = partsOfMessaggio[1];
		case 1:
				code = partsOfMessaggio[0];
		
		}
	}
	
	/**
	 * @return the sender nickname, if any.
	 */
	@Override
	public String getSender() {
		return sender;
	}

	/**
	 * @return the destination nickname, if any.
	 */
	@Override
	public String getReceiver() {
		return receiver;
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
	public String getCode() {
		return code;
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
		return data;
	}

	
}
