package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class ClientManager implements ClientInterface{
	
	ClientCommunicationManager clientCommunicationManager;
	String clientNick;
	int clientPort;
	InetAddress clientIP;
	
	/**
	 * Client communication manager. This object is used to communicate with users who connects to us.
	 * 
	 * @param socket the socket returned by the listen() method of the listen socket.
	 * @param comandoGestioneMessaggi the event controller
	 * @throws Exception
	 */
	public ClientManager(Socket socket,final Command comandoGestioneMessaggi) throws Exception
	{
		clientCommunicationManager = new ClientCommunicationManager(socket,new Command() {
			
			@Override
			public void manageMessage(String[] PartsOfMessage)  {
			 //TODO remove
				StringBuilder b = new StringBuilder();
				for(String s : PartsOfMessage)
					b.append(s);
				System.out.println(b.toString());
				
			 //OTTENGO IL NICK
			 ClientManager.this.clientNick = PartsOfMessage[1];
			 
			 //TODO REMOVE TEST
			 System.out.println("nick: "+ClientManager.this.clientNick);
			 
			 //ESTRAPOLO LE ALTRE INFORMAZIONI
			 String ConnectionInfo[] = PartsOfMessage[2].split(Constants.ConnectionInfoDivisor);
			 try {
				 //OTTENGO L'IP
				ClientManager.this.clientIP = InetAddress.getByName(ConnectionInfo[0]);

				 //TODO REMOVE TEST
				 System.out.println("nick: "+ClientManager.this.clientNick);
				 
			 } catch (UnknownHostException e) {
				// TODO gestione ip non valido
				e.printStackTrace();
			 }
			 //OTTENGO LA PORTA
			 ClientManager.this.clientPort = Integer.parseInt(ConnectionInfo[1].replace('\n', ' ').trim());

			 //TODO REMOVE TEST
			 System.out.println("nick: "+ClientManager.this.clientNick);
			 
			 //dopo aver estratto le informazioni per creare il client segnalo più in alto che si è un connesso
			 //un nuovo client
			 comandoGestioneMessaggi.manageMessage(PartsOfMessage);
			}
			
			@Override
			public void manageMessage(String Message,String Client) {
				comandoGestioneMessaggi.manageMessage(Message,ClientManager.this.getNick());
				
			}
			@Override
			public void manageDisconnection(String Name) {
				//propago la disconnessione
				comandoGestioneMessaggi.manageDisconnection(ClientManager.this.getNick());
				
			}
		},false);
		
		System.out.println("connessione ricevuta");
		
		
	}
	
	/**
	 * @return the nickname of the user to which we are connected through this client.
	 */
	@Override
	public String getNick()
	{
		return this.clientNick;
	}

	/**
	 * @return the listen port of the user to which we are connected through this client.
	 */
	@Override
	public int getPort()
	{
		return this.clientPort;
	}

	/**
	 * @return the listen address of the user to which we are connected through this client.
	 */
	@Override
	public InetAddress getIP()
	{
		return this.clientIP;
	}
	
	/**
	 * @return true if the message is successfully sent.
	 */
	@Override
	public Boolean sendMessage(String message)
	{
		return clientCommunicationManager.SendMessage(message);
	}

}
