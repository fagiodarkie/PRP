package it.unipr.informatica.reti.PRP.implementation;

import java.io.*;
import java.net.*;
import java.util.Hashtable;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.server;
import it.unipr.informatica.reti.PRP.utils.Constants;

//START SERVER CLASS
public class ServerComponent implements server {
	//START STATO INTERNO
	private ServerSocket serverSocket;
	Hashtable<String, Client> hashTableNickClients = new Hashtable<String, Client>();
	Hashtable<String,String> hashTableNickNick = new Hashtable<String, String>();
		
	//END STATO INTERNO
	
	//START CONSTRUCTOR
	//END CONSTRUCTOR
	
	//START CONNECTION MANAGEMENT METHOD
	@Override
	public void start()  {
		//Step 1 
		try{
		    serverSocket = new ServerSocket(Constants.PortOfServer);
		  } catch (IOException e) {
		    System.out.println("Could not listen on port");
		    System.exit(-1);
		  }
		System.out.println("connessione alla porta avvenuta con successo");
		
		  while(true){
			  try {
				Client c = new Client(serverSocket.accept(), new Command() {
					
					@Override
					public void ManageMessage(String[] PartsOfMessage) {
						PartsOfMessage[0]="5";
						String newMessage = PartsOfMessage[0]+ Constants.MessagePartsDivisor+
											PartsOfMessage[1]+ Constants.MessagePartsDivisor+
											PartsOfMessage[2];
						SendAll(newMessage);
					}
					
					@Override
					public void ManageMessage(String Message,String Client) {
						//STEP 1 scompongo il messaggio
						String partsOfMessage[] = Message.split(Constants.MessagePartsDivisor);
						//STEP 2 controllo che genere di messaggio e'
						switch(partsOfMessage[0])
						{
						case "1":
							ServerComponent.this.SendTo(partsOfMessage[2], Message);
							break;
						case "2"://messaggio Broadcast quindi invio a tutti
							for (String nick : hashTableNickClients.keySet()) {
								hashTableNickClients.get(nick).SendMessage(Message);
							}
							break;
						case "3":
							//do nothing
							break;
						case "4":
							//TODO gestione nick backup
							break;
						case "5":
							hashTableNickNick.put(Client, partsOfMessage[1]);
							for (String nick : hashTableNickClients.keySet()) {
								if(nick != partsOfMessage[1])
								hashTableNickClients.get(nick).SendMessage(Message);
							}
							break;
						case "6":
							//rimuovo il collegamento e invio
							if(hashTableNickClients.keySet().contains(partsOfMessage[1]))
							{
								hashTableNickNick.remove(partsOfMessage[1]);
							}
							SendAll(Message);
							
							break;
						case "7":
							break;
						
						}
						//STEP 3 gestisco il messaggio
						
					}
					@Override
					public void ManageDisconnection(String Name) {
						// TODO Gestire Disconnessione
						System.out.println(Name+" si è disconnesso");
					}
					
				});
				
				 ServerComponent.this.hashTableNickClients.put(c.getNick(), c);
		         ServerComponent.this.hashTableNickNick.put(c.getNick(), c.getNick());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 	
			
		  }
		
		
		
	}
	@Override
	public Boolean stop() {
		// TODO Auto-generated method stub
		return null;
	}
	//END CONNECTION MANAGEMENT METHOD
	
	//START MESSAGES MANAGEMENT
	public Boolean SendTo(String nick,String message)
	{
		//se la chiave passata è un nick valido
		if (hashTableNickClients.keySet().contains(nick))
			return hashTableNickClients.get(nick).SendMessage(message);
		else//altrimenti guardo se è un ip valido
			if(hashTableNickNick.keySet().contains(nick))
				return hashTableNickClients.get(hashTableNickNick.get(nick)).SendMessage(message);
		//altrimenti mi è impossibile inviare un messaggio
		return false;
	}
	public void SendAll(String message)
	{}
	//END MESSAGES MANAGEMENT
}
//END SERVER CLASS

