package it.unipr.informatica.reti.PRP.implementation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.utils.Constants;
import it.unipr.informatica.reti.PRP.utils.MessageFormatter;

public class ParentClientManager implements ClientInterface {

	//informazioni DAD
	String serverNick;
	int serverPort;
	InetAddress serverIP;
	
	
	//informazioni BACKUP
	String backupNick;
	int backupPort;
	InetAddress backupIP;
	
	//mie informazioni
	String MyNick;
	String MyPort;
	InetAddress MyIP;
	Command command;
	
	ClientCommunicationManager serverCommunicationManager ;
	
	/**
	 * Extracts the backup informations from a message and updates relevant informations.
	 * 
	 * @param pojoMessage
	 */
	private void UpdateBackupInformation(POJOMessage pojoMessage)
	{
		//se � un messaggio di backup allora lo gestisco e mi salvo le informazioni
		backupNick = pojoMessage.getSender();
		String IpPort = pojoMessage.getData();
		String ipAndPort[] = IpPort.split(Constants.ConnectionInfoDivisor);
		try {
			backupIP = InetAddress.getByName(ipAndPort[0]);
		} catch (UnknownHostException e) {
			// ora come ora, se va male il peggio che pu� succedere � che non sappiamo a chi connetterci: amen.
			e.printStackTrace();
		}
		backupPort = Integer.parseInt(ipAndPort[1]);
	
	}
	
	/**
	 * Constructor.
	 * 
	 * @param Nick the nickname of the user from which we accept connection request.
	 * @param Port the listen port of the user from which we accept connection request.
	 * @param IP the listen address of the user from which we accept connection request.
	 * @param MyNick our nickname.
	 * @param MyPort our listen port.
	 * @param MyIP our listen address.
	 * @param command
	 */
	public ParentClientManager(String Nick,int Port,InetAddress IP,String MyNick,String MyPort,InetAddress MyIP,Command command)
	{
		this.serverIP = IP;
		this.serverPort = Port;
		this.serverNick = Nick;
		this.command = command;
		
		this.MyIP = MyIP;
		this.MyNick = MyNick;
		this.MyPort = MyPort;
	}
	
	/**
	 * connects to the user whose nickname is specified.
	 * 
	 * @param Nick the nickname of the user to which we want to connect.
	 * @return true if all went well. False otherwise
	 * @throws Exception if the message does not contain the backup nickname.
	 */
	public boolean connect() throws Exception
	{
		try{
			Socket serverSocket = new Socket(serverIP, serverPort);   
			
			
			//APRO I CANALI DI COMUNICAZIONE CON IL SERVER
			serverCommunicationManager = new ClientCommunicationManager(serverSocket,new Command() {
				
				@Override
				public void manageMessage(String[] PartsOfMessage) {
					command.manageMessage(PartsOfMessage);
				}
				
				@Override
				public void manageMessage(String Message, String Client) {
	
					POJOMessage pojoMessage;
					try {
						pojoMessage = new POJOMessage(Message);
					
					
						if( pojoMessage.getCode().equals(Constants.MessageBackupNickCode))
						{
							UpdateBackupInformation(pojoMessage);
						}
						else command.manageMessage(Message, Client);
					} catch (Exception e) {
						// TODO gestire errore messaggio non valido
						e.printStackTrace();
					}
					
				}
				
				@Override
				public void manageDisconnection(String Name) {
					command.manageDisconnection(Name);
				}
			},true);
			
			
			
			
			serverCommunicationManager.SendMessage(MessageFormatter.GenerateHelloMessage(MyNick, MyIP.getHostAddress(), MyPort));
			
			
			//mi preparo per ricevere le informazioni dal padre riguardanti il suo nodo di backup
			BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			
			
			return true;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	@Override
	public InetAddress getIP() {
		return serverIP;
	}
	
	@Override
	public String getNick() {
		return serverNick;
	}
	
	@Override
	public int getPort() {
		return serverPort;
	}
	@Override
	public Boolean sendMessage(String Message) {
		return serverCommunicationManager.SendMessage(Message);
	}

	@Override
	public void stop() {
		serverCommunicationManager.stopListening();
	}
}



