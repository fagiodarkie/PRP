package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class Client implements ClientInterface{
	ClientCommunicationManager clientCommunicationManager;
	String clientNick;
	int clientPort;
	InetAddress clientIP;
	
	public Client(Socket socket,final Command comandoGestioneMessaggi) throws Exception
	{
		clientCommunicationManager = new ClientCommunicationManager(socket,new Command() {
			
			@Override
			public void ManageMessage(String[] PartsOfMessage)  {
			 Client.this.clientNick = PartsOfMessage[1];
			 String ConnectionInfo[] = PartsOfMessage[2].split(Constants.ConnectionInfoDivisor);
			 try {
				Client.this.clientIP = InetAddress.getByName(ConnectionInfo[0]);
			} catch (UnknownHostException e) {
				// TODO gestione ip non valido
				e.printStackTrace();
			}
			 Client.this.clientPort = Integer.parseInt(ConnectionInfo[1].replace('\n', ' ').trim());
			 //dopo aver estratto le informazioni per creare il client segnalo più in alto che si è un connesso
			 //un nuovo client
			 comandoGestioneMessaggi.ManageMessage(PartsOfMessage);
			}
			
			@Override
			public void ManageMessage(String Message,String Client) {
				comandoGestioneMessaggi.ManageMessage(Message,Client.this.getNick());
				
			}
			@Override
			public void ManageDisconnection(String Name) {
				//propago la disconnessione
				comandoGestioneMessaggi.ManageDisconnection(Client.this.getNick());
				
			}
		});
		
		
	}
	
	@Override
	public String getNick()
	{
		return this.clientNick;
	}
	@Override
	public int getPort()
	{
		return this.clientPort;
	}
	@Override
	public InetAddress getIP()
	{
		return this.clientIP;
	}
	@Override
	public Boolean SendMessage(String message)
	{
		return clientCommunicationManager.SendMessage(message);
	}
}
