package it.unipr.informatica.reti.PRP.implementation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

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
	
	public boolean connect(String Nick) throws Exception
	{
		try{
		Socket serverSocket = new Socket(serverIP, serverPort);   
		
		serverCommunicationManager = new ClientCommunicationManager(serverSocket,command);
		serverCommunicationManager.SendMessage(MessageFormatter.GenerateHelloMessage(MyNick, MyIP.toString(), MyPort));
		
		//mi preparo per ricevere le informazioni dal padre riguardanti il suo nodo di backup
		BufferedReader in = new BufferedReader(new 
			      InputStreamReader(serverSocket.getInputStream()));
		//aspetto il messaggio
		String Message = in.readLine();
		//spezzo il messaggio
		POJOMessage MessageManager = new POJOMessage(Message);
		
		if(MessageManager.getCode().equals(Constants.MessageBackupNickCode))
		{
			//se è un messaggio di backup allora lo gestisco e mi salvo le informazioni
			backupNick = MessageManager.getSender();
			String IpPort = MessageManager.getData();
			String ipAndPort[] = IpPort.split(Constants.ConnectionInfoDivisor);
			backupIP = InetAddress.getByName(ipAndPort[0]);
			backupPort = Integer.parseInt(ipAndPort[1]);
		}
		else
			throw new Exception("ERRORE");
		return true;
		}
		catch(Exception ex){
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

}



