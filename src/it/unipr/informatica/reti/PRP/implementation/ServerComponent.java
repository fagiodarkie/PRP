package it.unipr.informatica.reti.PRP.implementation;

import java.io.*;
import java.net.*;
import java.text.MessageFormat;
import java.util.LinkedList;
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
	/**
	 * Network component acting as a "server", taking care of message sending and receiving.
	 * Separated from the user interface component, as these parts could also be split.
	 * 
	 * @param manager the table manager which should be used by the server component
	 * @param connessioni the connection manager through which the server component should communicate
	 * @param command the component from which the server component is taking orders (?).
	 */
	public ServerComponent(TableManager manager,NetworkConnectionsManager connessioni, ClientCommunicationManagerInterface command)
	{
		this.tableManager = manager;
		this.commandClientCommunicationManagerInterface = command;
		this.connections = connessioni;
	}
	//END CONSTRUCTOR

	//START CONNECTION MANAGEMENT METHOD
	/**
	 * Starts listening to the server listen port. The client can link to this port and see the
	 * network as a simple socket in which he can send the messages he wants to send.
	 * After that, it starts accepting connections from the nodes. Beware, the ServerComponent
	 * is the actual node of the network, the user is like a periferal leecher.
	 */
	@Override
	public void start()  {
		
		try{
			serverSocket = new ServerSocket(Constants.PortOfServer);
		} catch (IOException e) {
			System.out.println("Could not listen on port");
			System.exit(-1);
		}
		System.out.println("connessione alla porta avvenuta con successo");

		/* FIXME maybe like this?
		 * 
		 * declare accept as private boolean in the class members;
		 * 
		 * accept = true;
		 * while (accept) {
		 */
		while(true){
			try {
				ClientManager c = new ClientManager(serverSocket.accept(), new Command() {

					@Override
					public void manageMessage(String[] PartsOfMessage) {
						/*DO NOTHING*/
					}

					@Override
					public void manageMessage(String Message,String Client) {

						//gestisco il messaggio appena ricevuto
						ManageMessage(Message, Client);

					}
					@Override
					public void manageDisconnection(String nick) {
						
						//gestisco la disconnessione del client
						ManageDisconnection(nick);
					}

				});
				
				
				connections.addClient(c.getNick(),c);
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
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}


		}



	}
	
	/**
	 * Commands the server to stop listening to the outside.
	 */
	@Override
	public Boolean stop() {
		/* FIXME perché Boolean? volevi dire boolean? è necessario un tipo di ritorno?
		 * 
		 * TODO FINE ASCOLTO
		 * FIXME maybe like this?
		 * 
		 * accept = false;
		 * connections.stopListening(); // this must be implemented
		 */
		return null;
	}
	//END CONNECTION MANAGEMENT METHOD


	/**
	 * Manages the messages coming from the interface, redirecting or processing them.
	 * 
	 * @param Message the message to be processed.
	 * @param MyNick nickname to which the message should be sent, if any (?)
	 */
	public void ManageMessageFromUserInterface(String Message,String MyNick)
	{
		

		if(Message.contains("@") && Message.trim().startsWith("@"))
		{
			
			String partsOfMessage[]= Message.trim().split(" ");
			LinkedList<String> Commands = new LinkedList<String>();
			LinkedList<String> Messages = new LinkedList<String>();
			boolean endCommand = false;
			for(String part : partsOfMessage)
				{
					String temp = part.trim();
					if(temp.startsWith("@") && !temp.equals("@") && !endCommand)
					{	//è un comando e quindi lo inserisco nella lista dei comandi
						Commands.add(part.replace("@", ""));
					}
					else
					{
						Messages.add(part);
						endCommand = true;
					}
				}
		
			//ricreo il messaggio da inviare
			String Messaggio = "";
			for(String m : Messages)
			{
				Messaggio += m + " ";
			}
			
			//TODO remove test code
			commandClientCommunicationManagerInterface.SendMessage("il messaggio e': "+Messaggio);
			
			boolean broadcast = false;
			for(String c : Commands)
			{
				if( c.toUpperCase().equals("BROADCAST"))
				{
					broadcast = true;
					break;
				}
			}
			
			if(broadcast)
			{
				//sendBroadcast(Messaggio);
				//TODO remove test code.
				commandClientCommunicationManagerInterface.SendMessage("messaggio broadcast");
			}
			else
			{
				for(String nick : Commands)
				{
					//sendPointToPoint(Messaggio, nick);
					//TODO remove test code
					commandClientCommunicationManagerInterface.SendMessage("messaggio diretto a:" + nick);
				}
			}
				
		}
		else
		{
			commandClientCommunicationManagerInterface.SendMessage("il comando non e' valido");
		}
	}

	
	
	
	//INIZIO SEZIONE INVIO MESSAGGI
	/**
	 * permette di inviare un messaggio a tutti i nodi vicini
	 * @param Message messaggio da inviare
	 */
	private void sendBroadcast(String Message)
	{
		for(String nick : tableManager.allMyNeighbors())
		{
				connections.getClient(nick).sendMessage(Message);
		}
	}

	/**
	 * permette d'inviare un messaggio a tutti  i nodi vicini tranne a nickNoNA
	 * @param Message messagio da inviare
	 * @param nickNoNA nick a cui non inviarlo
	 */
	private void sendBroadcast(String Message, String nickNoNA)
	{

		for(String nick : tableManager.allMyNeighbors())
		{
			if(nick != nickNoNA)
			{
				connections.getClient(nick).sendMessage(Message);
			}
		}
	}
	
	/**
	 * invia un messaggio ad un utente
	 * @param Message messaggio da inviare
	 * @param Nick nickname dell'utente a cui inviarlo
	 */
	private void sendPointToPoint(String Message, String Nick)
	{
		//lo devo mandare sulla linea corretta
		connections.sendMesage(Nick, Message);
	}
	
	//FINE SEZIONE INVIO MESSAGGI
	/**
	 * Manages the receiving of messages sent from the other client, parent included.
	 *
	 * @param Message received message
	 * @param Client nickname of the client who sent the message.
	 */
	public void ManageMessage(String Message,String Client)
	{
		
		POJOMessage messageManagement;
		try {
			messageManagement = new POJOMessage(Message);
		



		//STEP 2 controllo che genere di messaggio e' e lo gestisco
		switch(messageManagement.getCode())
		{
		
		
		/*messaggio HELLO       */	
			case Constants.MessageHelloCode :
				//DO NOTHING
			break;
			
			
			
		/*messaggio POINT TO POINT*/
			case Constants.MessagePointToPointCode:
				if(!tableManager.isItConnected(messageManagement.getReceiver()))
				{
					//il messaggio è rivolto a me e quindi lo invio all'interfaccia grafica
					this.commandClientCommunicationManagerInterface.SendMessage(Client + ":" + messageManagement.getData());
				}
				else
				{
					//inoltro il messaggio
					sendPointToPoint(Message, tableManager.howToReach(messageManagement.getReceiver()));
				}
			break;

			
			
		/*messaggio BROADCAST     */
			case Constants.MessageBroadcastCode :
				sendBroadcast(Message, Client);

			break;
			
			
			
			/*messaggio BACKUP NICK   */
			case Constants.MessageBackupNickCode:
				//DO NOTHING ONLY PARENT CAN SEND BACKUP NICK
			break;
			
			
			
			/*messaggio REACHABLE     */
			case Constants.MessageReachableCode :
				

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
			/*messaggio NOT REACHABLE */
				case Constants.MessageNotReachableCode :
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
			/*messaggio TABLE   */      
				case Constants.MessageTableCode:
					
					//estraggo la tabella dal messaggio
					String table = messageManagement.getData();
					
					//divido le varie informazioni
					String Rows[] = table.split(":");
					
					//le informazioni devono essere per forza un multiplo di 3
					if(Rows.length % 3 != 0)
						throw new Exception("errore messaggio tabella ( i dati riguardanti la tabella non sono in numero pari)");
					
					//per ogni terna d'informazioni aggiorno la tabella
					for(int i = 0 ; i < Rows.length ; i += 3 )
					{
						/*if(!tableManager.isItConnected(Rows[i]))
							tableManager.notifyIsReachedBy(tableManager.getInfoByNick(Client),
														new UserInformations(Rows[i], Integer.parseInt(Rows[i + 1]), InetAddress.getByName(Rows[i + 2])));
						else
							{
							tableManager.notifyIsReachedBy(tableManager.getInfoByNick(Client),
									new UserInformations(Rows[i], Integer.parseInt(Rows[i + 1]), InetAddress.getByName(Rows[i + 2])));

							}*/
						tableManager.notifyIsReachedBy(tableManager.getInfoByNick(Client),
								new UserInformations(Rows[i], Integer.parseInt(Rows[i + 1]), InetAddress.getByName(Rows[i + 2])));
					}
					
					//genero il messaggio riguardante la mia tabella
					String MyTable = MessageFormatter.GenerateTableMessage(this.tableManager);
					
					//ottengo tutti i nodi a me vicini
					List<String> myNeighbors = tableManager.allMyNeighbors();
					
					//estraggo dalla lista il nodo che mi ha inviato la sua tabella
					myNeighbors.remove(Client);
					
					for(String nick : myNeighbors)
					{
						//invio la tabella agli altri nodi vicini
						connections.sendMesage(nick, MyTable);
					}
					
				break;

		}
		} catch (Exception e) {
			// TODO gestire errore messaggio non valido
			e.printStackTrace();
		}

	}

	/**
	 * Manages the disconnection of a client.
	 * 
	 * @param nick the client who is about to be disconnected.
	 */
	 public void ManageDisconnection(String nick)
	{
		 if(tableManager.isNearMe(nick)){
				//tolgo l'interfaccia con il client
				connections.removeClient(nick);
			}
			//aggiorno la tabella delle interfacce
			tableManager.hasDisconnected(nick);
			//invio all'interfaccia utente il comando di stampare il messaggio che l'utente si è disconnesso
			commandClientCommunicationManagerInterface.SendMessage(nick+" is disconnected");
	}
}
//END SERVER CLASS

