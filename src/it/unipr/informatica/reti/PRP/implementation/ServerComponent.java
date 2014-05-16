package it.unipr.informatica.reti.PRP.implementation;

import java.io.*;
import java.net.*;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.ClientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.ServerInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;

//START SERVER CLASS
public class ServerComponent implements ServerInterface {
	
	//START STATO INTERNO
	private ServerSocket serverSocket;
	TableManager tableManager ;
	ClientCommunicationManagerInterface commandClientCommunicationManagerInterface;
	NetworkConnectionsManager connections;
	//END STATO INTERNO
	
	//START CONSTRUCTOR
	public ServerComponent(TableManager manager,NetworkConnectionsManager connessioni, ClientCommunicationManagerInterface command)
	{
		this.tableManager = manager;
		this.commandClientCommunicationManagerInterface = command;
		this.connections = connessioni;
	}
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
				ClientManager c = new ClientManager(serverSocket.accept(), new Command() {
					
					@Override
					public void manageMessage(String[] PartsOfMessage) {
						//TODO INVIARE A TUTTI IL MESSAGGIO IL REACHABLE
					}
					
					@Override
					public void manageMessage(String Message,String Client) {
						
						//STEP 1 scompongo il messaggio
						String partsOfMessage[] = Message.split(Constants.MessagePartsDivisor);
						
						//STEP 2 controllo che genere di messaggio e' e lo gestisco
						switch(partsOfMessage[0])
						{
						case Constants.MessageHelloCode :
							//TODO GESTIONE ARRIVO HELLO
							break;
						case Constants.MessageBroadcastCode :

							//TODO IMPLEMENT BROADCAST;
							
							break;
						case Constants.MessageBackupNickCode:
							//TODO gestione nick backup
							break;
						case Constants.MessageReachableCode :
							//TODO GESTIONE ARRIVO MESSAGGIO REACHABLE 
							break;
						case Constants.MessageNotReachableCode :
							//TODO GESTIONE ARRIVO MESSAGGIO NOTREACHABLE 
							
							break;
						case Constants.MessageTableCode:
							//TODO GESTIONE ARRIVO MESSAGGIO TABLE
							break;
						
						}
						
					}
					@Override
					public void manageDisconnection(String nick) {
						if(tableManager.isNearMe(nick)){
							//tolgo l'interfaccia con il client
							connections.removeClient(nick);
						}
						//aggiorno la tabella delle interfacce
						tableManager.hasDisconnected(nick);
						//invio all'interfaccia utente il comando di stampare il messaggio che l'utente si è disconnesso
						commandClientCommunicationManagerInterface.SendMessage(nick+" is disconnected");
						
					}
					
				});
				connections.addClient(c.getNick(),c);
				tableManager.notifyIsReachedBy(c.getNick(), c.getNick());
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
	
	
	//GESTIONE MESSAGGI
	}
//END SERVER CLASS

