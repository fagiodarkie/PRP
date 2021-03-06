package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class ClientManager implements ClientInterface {
	
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
				
				
				//OTTENGO IL NICK
				clientNick = PartsOfMessage[1];
			 
				
			 
				//ESTRAPOLO LE ALTRE INFORMAZIONI
				String ConnectionInfo[] = PartsOfMessage[2].split(Constants.ConnectionInfoDivisor);
				try {
					 //OTTENGO L'IP
					clientIP = InetAddress.getByName(ConnectionInfo[0]);
	
					
					 
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				//OTTENGO LA PORTA
				clientPort = Integer.parseInt(ConnectionInfo[1].replace('\n', ' ').trim());
				
				
				 
				//dopo aver estratto le informazioni per creare il client segnalo pi� in alto che si � un connesso
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

	@Override
	public void stop() {
		clientCommunicationManager.stopListening();
	}

}
