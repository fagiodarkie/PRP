package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;
import java.net.Socket;
import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.Command;

public class ParentClientManager implements ClientInterface {

	//informazioni DAD
	String serverNick;
	int serverPort;
	InetAddress serverIP;
	
	
	//informazioni BACKUP
	String backupNick;
	int backupPort;
	InetAddress backupIP;
	
	Command command;
	
	ClientCommunicationManager serverCommunicationManager ;
	
	public ParentClientManager(String Nick,int Port,InetAddress IP,Command command)
	{
		this.serverIP = IP;
		this.serverPort = Port;
		this.serverNick = Nick;
		this.command = command;
	}
	
	public boolean connect() throws Exception
	{
		
		Socket serverSocket = new Socket(serverIP, serverPort);   
		
		serverCommunicationManager = new ClientCommunicationManager(serverSocket,command);
		
		return true;
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

}



