package it.unipr.informatica.reti.PRP.implementation;

import java.io.*;
import java.net.*;
import java.text.MessageFormat;
import java.util.List;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.ClientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.ServerInterface;
import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;
import it.unipr.informatica.reti.PRP.utils.Constants;
import it.unipr.informatica.reti.PRP.utils.MessageFormatter;

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
						/*DO NOTHING*/
					}
					
					@Override
					public void manageMessage(String Message,String Client) {
						
						
						POJOMessage messageManagement = new POJOMessage(Message);
						
						
						
						//STEP 2 controllo che genere di messaggio e' e lo gestisco
						switch(messageManagement.getCode())
						{
/*messaggio HELLO       */	case Constants.MessageHelloCode :
								//DO NOTHING
								break;
/*messaggio POINT TO POINT*/case Constants.MessagePointToPointCode:
								if(!tableManager.isItConnected(messageManagement.getReceiver()))
								{
									//allora sono io
									//TODO aggiornare l'interfaccia grafica con il nuovo messaggio ricevuto
								}
								else
								{
									//lo devo mandare sulla linea corretta
									connections.sendMesage(tableManager.howToReach(messageManagement.getReceiver()), Message);
								}
								break;
							
/*messaggio BROADCAST     */case Constants.MessageBroadcastCode :
							
								for(String nick : tableManager.allMyNeighbors())
								{
									if(nick != Client)
									{
										connections.getClient(nick).sendMessage(Message);
									}
								}
								
								break;
/*messaggio BACKUP NICK   */case Constants.MessageBackupNickCode:
								 //DO NOTHING ONLY PARENT CAN SEND BACKUP NICK
								break;
/*messaggio REACHABLE     */case Constants.MessageReachableCode :
								//TODO controllare get data di POJOMessage
								
								if(!tableManager.isItConnected(messageManagement.getData()))
								{
								
									String isReachableMessage = MessageFormatter.GenerateReachableMessage(messageManagement.getData());
									for(String nick : tableManager.allMyNeighbors())
									{
										if(nick != Client)
										{
											connections.getClient(nick).sendMessage(isReachableMessage);
										}
									}
								}
								else
								{
									//DO NOTHING 
								}
								break;
/*messaggio NOT REACHABLE */case Constants.MessageNotReachableCode :
								if(tableManager.isItConnected(messageManagement.getData()))
								{
								
									String isNotReachableMessage = MessageFormatter.GenerateNotReachableMessage(messageManagement.getData());
									for(String nick : tableManager.allMyNeighbors())
									{
										if(nick != Client)
										{
											connections.getClient(nick).sendMessage(isNotReachableMessage);
										}
									}
								}
								else
								{
									//DO NOTHING 
								} 
								
								break;
/*messaggio TABLE   */      case Constants.MessageTableCode:
								//TODO manage backuptable
								break;
						
						}
						
					}
					@Override
/*DISCONNESSIONE*/  public void manageDisconnection(String nick) {
						if(tableManager.isNearMe(nick)){
							//tolgo l'interfaccia con il client
							connections.removeClient(nick);
						}
						//aggiorno la tabella delle interfacce
						tableManager.hasDisconnected(nick);
						//invio all'interfaccia utente il comando di stampare il messaggio che l'utente si � disconnesso
						commandClientCommunicationManagerInterface.SendMessage(nick+" is disconnected");
						
					}
					
				});
/*NEW CLIENT*/  connections.addClient(c.getNick(),c);
				tableManager.notifyIsReachedBy(c.getNick(), c.getNick());
				
				//ottengo tutti i nodi raggiungibili da me
				List<String> nodi = tableManager.allMyNeighbors();
				
				//genero il messaggio per aggiornare tutti gli altri
				String isReachableMessage = MessageFormatter.GenerateReachableMessage(c.getNick());
				for ( String nodo : nodi)
				{
					if(connections.getClient(nodo).getNick() != c.getNick())
					{
						connections.getClient(nodo).sendMessage(isReachableMessage);
					}
				}
/*FINE GESTIONE NEW CLIENT*/	
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
		// TODO FINE ASCOLTO
		return null;
	}
	//END CONNECTION MANAGEMENT METHOD
	
	
	//GESTIONE MESSAGGI RICEVUTI DA INTERFACCIA GRAFICA
	public void ManageMessageFromUserInterface(String Message,String MyNick)
	{
		//TODO remove test
		//TEST
			System.out.println("messaggio ricevuto dall'interfaccia grafica --> messaggio='"+ Message +"'; nick='"+ MyNick +"'");
			commandClientCommunicationManagerInterface.SendMessage(Message + " - " + MyNick);
		//END TEST
			
			
	}
	}
//END SERVER CLASS

