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
	
	public ClientManager(Socket socket,final Command comandoGestioneMessaggi) throws Exception
	{
		clientCommunicationManager = new ClientCommunicationManager(socket,new Command() {
			
			@Override
			public void manageMessage(String[] PartsOfMessage)  {
			 ClientManager.this.clientNick = PartsOfMessage[1];
			 String ConnectionInfo[] = PartsOfMessage[2].split(Constants.ConnectionInfoDivisor);
			 try {
				ClientManager.this.clientIP = InetAddress.getByName(ConnectionInfo[0]);
			} catch (UnknownHostException e) {
				// TODO gestione ip non valido
				e.printStackTrace();
			}
			 ClientManager.this.clientPort = Integer.parseInt(ConnectionInfo[1].replace('\n', ' ').trim());
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
	public Boolean sendMessage(String message)
	{
		return clientCommunicationManager.SendMessage(message);
	}

}