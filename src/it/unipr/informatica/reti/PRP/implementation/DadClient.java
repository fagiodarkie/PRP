package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;
import java.net.Socket;
import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.Command;

public class DadClient implements ClientInterface {

	String serverNick;
	int serverPort;
	InetAddress serverIP;
	Command command;
	ClientCommunicationManager serverCommunicationManager ;
	public DadClient(String Nick,int Port,InetAddress IP,Command command)
	{
		this.serverIP = IP;
		this.serverPort = Port;
		this.serverNick = Nick;
		this.command = command;
	}
	public boolean Connect() throws Exception
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
	public Boolean SendMessage(String Message) {
		return serverCommunicationManager.SendMessage(Message);
	}

}



