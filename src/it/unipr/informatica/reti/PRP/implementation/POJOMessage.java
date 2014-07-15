package it.unipr.informatica.reti.PRP.implementation;

import java.security.Timestamp;

import it.unipr.informatica.reti.PRP.interfaces.MessageInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class POJOMessage implements MessageInterface {

	private String code;
	private String sender;
	private String receiver;
	private String data;

	/**
	 * POJOMessage implementation: informations about
	 * - sender
	 * - destination
	 * - message type
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

	public POJOMessage(String Message) throws Exception{
		
		this();
		String partsOfMessaggio[] = Message.split(Constants.MessagePartsDivisor);

		code = partsOfMessaggio[0];
		switch ( partsOfMessaggio[0] )
		{
			case Constants.MessageHelloCode:
				if(partsOfMessaggio.length < 3)
					throw new Exception("messaggio non valido");
				else{
					sender = partsOfMessaggio[1];
					data = partsOfMessaggio[2];
					receiver = "";
					
				}
				break;
			case Constants.MessagePointToPointCode:
				if(partsOfMessaggio.length < 4)
					throw new Exception("messaggio non valido");
				else{
					sender = partsOfMessaggio[1];
					data = partsOfMessaggio[3];
					receiver = partsOfMessaggio[2];
					
				}
				break;
			
			case Constants.MessageBroadcastCode:
				if(partsOfMessaggio.length < 3)
					throw new Exception("messaggio non valido");
				else{
					sender = partsOfMessaggio[1];
					data = partsOfMessaggio[2];
					receiver = "";
					
				}
				break;
			
			case Constants.MessageBackupNickCode:
				if(partsOfMessaggio.length < 4)
					throw new Exception("messaggio non valido");
				else{
					data = partsOfMessaggio[2] + ":" + partsOfMessaggio[3]; // IP:Port
					sender = partsOfMessaggio[1];
					receiver = "";
					
				}
				break;
				
			case Constants.MessageReachableCode:
				if(partsOfMessaggio.length < 2)
					throw new Exception("messaggio non valido");
				else{
					sender = partsOfMessaggio[1];
					data = "";
					receiver = "";
					
				}
				break;
			
			case Constants.MessageNotReachableCode:
				if(partsOfMessaggio.length < 2)
					throw new Exception("messaggio non valido");
				else{
					data = partsOfMessaggio[2];
					sender = "";
					receiver = "";
					
				}
				break;
			

			case Constants.MessageTableCode:
				if(partsOfMessaggio.length < 2)
					throw new Exception("messaggio non valido");
				else{
					data = partsOfMessaggio[2];
					sender = "";
					receiver = "";
					
				}
				break;
			/*
			case 4:
					code = partsOfMessaggio[0];
					sender = partsOfMessaggio[1];
					receiver = partsOfMessaggio[2];
					data = partsOfMessaggio[3];
					break;
			case 3:
					code = partsOfMessaggio[0];
					sender = partsOfMessaggio[1];
					data = partsOfMessaggio[2];
					break;
			case 2:
				code = partsOfMessaggio[0];
				data = partsOfMessaggio[1];
				break;
			*/
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
	 * This feature is left to do to the ones who'll come after us.
	 * 
	 * @return the time in which this message was generated
	 */
	@Override
	public Timestamp getTimestamp() {
		// TODO optional feature
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
