package it.unipr.informatica.reti.PRP.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.clientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class ClientCommunicationManager implements clientCommunicationManagerInterface {

			ClientWorker clientWorker;
			Thread t ;
	public ClientCommunicationManager(Socket socket,Command comandoGestioneMessaggi) throws Exception
	{
		//creo il worker da passare al thread per la gestione della ocmunicazione
	    clientWorker = new ClientWorker(
	    		new BufferedReader(new 
	    			      InputStreamReader(socket.getInputStream())),
			      new PrintWriter(socket.getOutputStream(), true),
			      comandoGestioneMessaggi,this);
	    //creo il thread per la gestione della comunicazione in parallelo
		t= new Thread(clientWorker);
		//faccio partire il thread
		t.start();
		
	}

	@Override
	public Boolean SendMessage(String Message) {
		return clientWorker.sendMessage(Message);
	}

	public void stopListening()
	{
		
		t.stop();
	}
	
}



//START CLIENT WORKER CLASS
class ClientWorker implements Runnable {
	private Socket client;
	BufferedReader in = null;
	PrintWriter out = null;
	Command command;
	String message;
	ClientCommunicationManager clientMan;
	//Constructor
	ClientWorker(Socket client,Command comando, ClientCommunicationManager clientManager) {
	  this.client = client;
	  command = comando;
	  this.clientMan = clientManager;
	  try{
		    in = new BufferedReader(new 
		      InputStreamReader(client.getInputStream()));
		    out = new 
		      PrintWriter(client.getOutputStream(), true); 
		  } catch (IOException e) {
		   comando.ManageDisconnection("");
		   e.printStackTrace();
		  }
	  }
	public ClientWorker(BufferedReader input,PrintWriter output,Command comando,ClientCommunicationManager clientManager) throws Exception {
		in = input;
		out = output;
		command=comando;

		  this.clientMan = clientManager;
		message = in.readLine();
		String partsOfMessage[] = message.split(Constants.MessagePartsDivisor);
		if(partsOfMessage[0].contains("0"))
		{
			comando.ManageMessage(partsOfMessage);
		}
		else
			throw new Exception("ERRORE");
	}
	
	public void run(){
		
	
	  //leggo il primo messaggio che deve essere un HELLO
	  try {
		message = in.readLine();
		
			while(message != null){
			    try{

					command.ManageMessage(message,"");
			        message = in.readLine();
			      
			      if(message == null)
			      {
			    	  command.ManageDisconnection("");
			    	  break;
			      }
			     }catch (IOException e) {
			       command.ManageDisconnection("");
			       break;
			     }
			}
			
			System.out.println("il client si è disconnesso");
		
		} catch (IOException e1) {
			command.ManageDisconnection("");
			clientMan.stopListening();
		}
	  
	}
	
	public Boolean sendMessage(String Message)
	{
		  try{
		  out.println(Message);
		  return true;
		  }
		  catch(Exception e)
		  {
			  return false;
		  }
	}
}
//END CLIENT WORKER CLASS