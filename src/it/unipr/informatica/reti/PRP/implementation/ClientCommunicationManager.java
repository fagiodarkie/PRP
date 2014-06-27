package it.unipr.informatica.reti.PRP.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.ClientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class ClientCommunicationManager implements ClientCommunicationManagerInterface {

	ClientWorker clientWorker;
	Thread t ;
	
	/**
	 * create a socket wrapping which extends the socket functionalities for client communications
	 * 
	 * @param socket the socket to be wrapped
	 * @param comandoGestioneMessaggi the controller
	 * @throws Exception
	 */
	public ClientCommunicationManager(Socket socket,Command comandoGestioneMessaggi,boolean isDad) throws Exception
	{
		//creo il worker da passare al thread per la gestione della ocmunicazione
	    clientWorker = new ClientWorker(
	    								new BufferedReader(new 
	    												InputStreamReader(socket.getInputStream())),
	    								new PrintWriter(socket.getOutputStream(), true),
	    								comandoGestioneMessaggi,
	    								this,
	    								isDad);
	    //creo il thread per la gestione della comunicazione in parallelo
		t= new Thread(clientWorker);
		//faccio partire il thread
		t.start();
		
	}

	/**
	 * send a message to this user.
	 * Notice: this is not limited to user-defined messages. Topology change messages also,
	 * when correctly spelled, must pass through this method. 
	 * 
	 * @return true if the message is correctly sent.
	 */
	@Override
	public Boolean SendMessage(String Message) {
		return clientWorker.sendMessage(Message);
	}

	/**
	 * Orders this socket wrapping to stop listening for user messages.
	 * This is necessary when disconnecting from the network.
	 */
	public void stopListening()
	{
		
		t.stop();
		// TODO check if t.interrupt(); works as well, as it is not deprecated
	}
	
}



//START CLIENT WORKER CLASS
class ClientWorker implements Runnable {
	
	/**
	 * Thread implementing connectivity between our client and another user's. 
	 */
	
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
		    
		    //TODO IMPLEMENTARE GESTIONE ARRIVO HELLO NON ANCORA FATTO ma questo costruttore non lo usiamo...
		  } catch (IOException e) {
		   comando.manageDisconnection("");
		   e.printStackTrace();
		  }
	  }
	
	public ClientWorker(BufferedReader input,PrintWriter output,Command comando,ClientCommunicationManager clientManager, boolean isDad) throws Exception {
		in = input;
		out = output;
		command=comando;

		  this.clientMan = clientManager;
		  // TODO  SCOMMENTARE SEZIONE RICEZIONE MESSAGGIO HELLO
		  
		if(!isDad)
		{
			message = in.readLine();
			//TODO REMOVE CODICE TEST
			System.out.println("Messaggio ricevuto: " + message);
			String partsOfMessage[] = message.split(Constants.MessagePartsDivisor);
			
			//TODO REMOVE CODE TEST
			System.out.println(partsOfMessage[0]);
			if(partsOfMessage[0].equals(Constants.MessageHelloCode))
			{
				comando.manageMessage(partsOfMessage);
			}
			else
				throw new Exception("ERRORE");
		}
	}
	
	public void run(){
		
	
	  try {
		message = in.readLine();
		
			while(message != null){
			    try{

					command.manageMessage(message,"");
			        message = in.readLine();
			      
			      if(message == null)
			      {
			    	  command.manageDisconnection("");
			    	  break;
			      }
			     }catch (IOException e) {
			       command.manageDisconnection("");
			       break;
			     }
			}
			
			System.out.println("il client si è disconnesso");
		
		} catch (IOException e1) {
			command.manageDisconnection("");
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