	package it.unipr.informatica.reti.PRP.implementation;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.interfaces.ClientCommunicationManagerInterface;
import it.unipr.informatica.reti.PRP.interfaces.ServerInterface;
import it.unipr.informatica.reti.PRP.utils.Constants;
import it.unipr.informatica.reti.PRP.utils.MessageFormatter;

//START SERVER CLASS
public class ServerComponent implements ServerInterface {

	//START STATO INTERNO
	private ServerSocket serverSocket;
	private ClientCommunicationManagerInterface commandClientCommunicationManagerInterface;
	private String nick;
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
	public ServerComponent(ClientCommunicationManagerInterface command, String nick)
	{
		this.commandClientCommunicationManagerInterface = command;
		this.nick = nick;
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
				
				
				
				
				Constants.tableManager.notifyIsReachedBy(c.getNick(), c.getNick());
				Constants.connections.addClient(c.getNick(), c);
				
				//ora che ho aggiornato la mia tabella devo inviare al nuovo nodo la mia tabella in modo che sappia chi può raggiungere
				//se la tabella contiene soltantoil nuovo nodo non invio niente altrimente invio tutti tranne il nuovo nodo 
				
				String TableMessage = MessageFormatter.GenerateTableMessage(Constants.tableManager, c.getNick());
				
				//TODO REMOVE TEST
				System.out.println("nick del client: " + c.getNick());
				System.out.println("Messaggio tabella " + TableMessage);
				
				
				//se il messaggio ottenuto non è vuoto allora lo mando al client
				if(TableMessage != null && !TableMessage.isEmpty())
					c.sendMessage(TableMessage);
				
				//AGGIORNO I MIEI VICINI CHE E' ARRIVATO UN NUOVO NODO
				
				//estraggo i vicini a cui inviarlo
				List<String> neighbors = Constants.tableManager.allMyNeighbors();
				//tolgo il nodo dal quale ho ricevuto il messaggio
				neighbors.remove(c.getNick().trim());
				
				String reachableMessage = MessageFormatter.GenerateReachableMessage(c.getNick());
				
				//TODO REMOVE TEST
				System.out.println("il messaggio da inviare ai vicini: "+ reachableMessage);
				
				//invio a tutti i vicini il messaggio di reachable
				for(String neighbor : neighbors)
				{
					Constants.connections.sendMessage(neighbor, reachableMessage);
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
		Constants.connections.stopListening();
		return null;
	}
	//END CONNECTION MANAGEMENT METHOD


	/**
	 * Manages the messages coming from the interface, redirecting or processing them.
	 * 
	 * @param POJOMessage the message to be processed.
	 * @param MyNick nickname to which the message should be sent, if any (?)
	 */
	public void ManageMessageFromUserInterface(String Message)
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
				
				//TODO implementare send broadcast del messaggio ricevuto da riga di comando
			
			}
			else
			{
				for(String nick : Commands)
				{
					//TODO implementare  il send to point to point con messaggio ricevuto da riga di comando
					//sendPointToPoint(Messaggio, nick);
					
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
	 * @param POJOMessage messaggio da inviare
	 */
	private void sendBroadcast(String Message)
	{
		for(String nick : Constants.tableManager.allMyNeighbors())
		{
			String newMessage = MessageFormatter.GenerateBroadcastMessage(nick, Message);
			Constants.connections.getClient(nick).sendMessage(newMessage);
		}
	}

	/**
	 * permette d'inviare un messaggio a tutti  i nodi vicini tranne a nickNoNA
	 * @param POJOMessage messagio da inviare
	 * @param nickNoNA nick a cui non inviarlo
	 */
	private void sendBroadcast(String Message, String nickNoNA)
	{

		for(String sendToNick : Constants.tableManager.allMyNeighbors())
		{
			if(sendToNick != nickNoNA)
			{
				String newMessage = MessageFormatter.GeneratePointToPointMessage(nick, sendToNick, Message);
				Constants.connections.getClient(sendToNick).sendMessage(newMessage);
			}
		}
	}
	
	/**
	 * invia un messaggio ad un utente
	 * @param POJOMessage messaggio da inviare
	 * @param Nick nickname dell'utente a cui inviarlo
	 */
	private void sendPointToPoint(String Message, String sendToNick)
	{
	
		//lo devo mandare sulla linea corretta
		String newMessage = MessageFormatter.GeneratePointToPointMessage(nick, sendToNick, Message);
		Constants.connections.sendMessage(Constants.tableManager.howToReach(sendToNick), newMessage);
	}
	
	//FINE SEZIONE INVIO MESSAGGI
	
	/**
	 * Manages the receiving of messages sent from the other client, parent included.
	 *
	 * @param POJOMessage received message
	 * @param Client nickname of the client who sent the message.
	 */
	public void ManageMessage(String Message,String Client)
	{
		
		//TODO REMOVE TEST
		System.out.println("messaggio ricevuto: " + Message + "\n ricevuto da: " + Client);
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
				
				//TODO fare point to point
				/*
				if(!Constants.tableManager.isItConnected(messageManagement.getReceiver()))
				{
					//il messaggio è rivolto a me e quindi lo invio all'interfaccia grafica
					this.commandClientCommunicationManagerInterface.SendMessage(Client + ":" + messageManagement.getData());
				}
				else
				{
					Constants.connections.sendMessage(Constants.tableManager.howToReach(messageManagement.getReceiver()), Message);
				}*/
			break;

			
			
		/*messaggio BROADCAST     */
			case Constants.MessageBroadcastCode :
				
				//TODO fare broadcast
				
				/*sendBroadcast(Message, Client);
				List<String> list = Constants.tableManager.allMyNeighbors();
				list.remove(Client);
				for(String nick : list)
				{
					Constants.connections.sendMessage(nick, Message);
				}*/
				
			break;
			
			
			
			/*messaggio BACKUP NICK   */
			case Constants.MessageBackupNickCode:
				//DO NOTHING: ONLY PARENT CAN SEND BACKUP NICK
			break;
			
			
			
			/*messaggio REACHABLE     */
			case Constants.MessageReachableCode :
			{
				//Aggiorno la mia tabella
				Constants.tableManager.notifyIsReachedBy(messageManagement.getData(), Client);
				
				//estraggo i vicini a cui inviarlo
				List<String> neighbors = Constants.tableManager.allMyNeighbors();
				//tolgo il nodo dal quale ho ricevuto il messaggio
				neighbors.remove(Client.trim());
				
				//aggiorno i miei vicini mandando lo stesso messaggio
				for(String neighbor : neighbors)
				{
					Constants.connections.sendMessage(neighbor, Message);
				}
				
				//TODO fare reachable
				/*
				System.out.println("aggiungo alla lista: " + messageManagement.getSender()+"\n reggiungibile da: " + Client);
				Constants.tableManager.notifyIsReachedBy(messageManagement.getSender(), Client);
				String isReachableMessage = MessageFormatter.GenerateReachableMessage(messageManagement.getSender());
				//ottengo tutti i nodi a me vicini
				List<String> myNeighbors = Constants.tableManager.allMyNeighbors();
				
				//estraggo dalla lista il nodo che mi ha inviato la sua tabella
				myNeighbors.remove(Client);
				
				for(String nick : myNeighbors)
				{
					//invio la tabella agli altri nodi vicini
					if(nick != null)
						Constants.connections.sendMessage(isReachableMessage, Client);
				}*/
			}
				break;
			
				/*messaggio NOT REACHABLE */
			case Constants.MessageNotReachableCode :
				
				
				//TODO fare not reachable
				
				/*
				if(Constants.tableManager.isItConnected(messageManagement.getSender()))
				{
					Constants.tableManager.hasDisconnected(messageManagement.getSender());
					String isNotReachableMessage = MessageFormatter.GenerateNotReachableMessage(messageManagement.getSender());
					//ottengo tutti i nodi a me vicini
					List<String> neighbors = Constants.tableManager.allMyNeighbors();
					
					//estraggo dalla lista il nodo che mi ha inviato la sua tabella
					neighbors.remove(Client);
					
					for(String nick : neighbors)
					{
						//invio la tabella agli altri nodi vicini
						if(nick != null)
							Constants.connections.sendMessage(isNotReachableMessage, Client);
					}
				}
				else
				{
					//DO NOTHING 
				} */

				break;
				
			/*messaggio TABLE   */      
				case Constants.MessageTableCode:
				{
					
					//ottengo le informazioni della tabella
					String tabella = messageManagement.getData();
					
					//estraggo i vicini a cui inviarlo
					List<String> neighbors = Constants.tableManager.allMyNeighbors();
					//tolgo il nodo dal quale ho ricevuto il messaggio
					neighbors.remove(Client.trim());
					String reachableMessage="";
					
					//controllo se sono multiple
					if(tabella.contains(":"))
					{
						List<String> nicks = new ArrayList<String>(Arrays.asList(tabella.split(":")));
						for(String newNick : nicks)
						{
							if(newNick != null && !newNick.trim().isEmpty())
							{
								//aggiungo il nuovo nick alla tabella
								Constants.tableManager.notifyIsReachedBy(newNick, Client);
								
								//creo il messaggio di aggiornamento
								reachableMessage = MessageFormatter.GenerateReachableMessage(newNick);
								
								for(String neighbor : neighbors)
								{
									Constants.connections.sendMessage(neighbor, reachableMessage);
								}
								
							}
						}
					}
					else
					{
						if(tabella != null && !tabella.trim().isEmpty())
						{
							//aggiungo il nuovo nick alla tabella
							Constants.tableManager.notifyIsReachedBy(tabella, Client);
							
							//AGGIORNO I MIEI VICINI
							
							//creo il messaggio di aggiornamento
							reachableMessage = MessageFormatter.GenerateReachableMessage(tabella);
							
							for(String neighbor : neighbors)
							{
								Constants.connections.sendMessage(neighbor, reachableMessage);
							}
						}
					}
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
		 if(Constants.tableManager.isNearMe(nick)){
				//tolgo l'interfaccia con il client
			 Constants.connections.removeClient(nick);
			}
			//aggiorno la tabella delle interfacce
		 Constants.tableManager.hasDisconnected(nick);
		 
			//invio all'interfaccia utente il comando di stampare il messaggio che l'utente si è disconnesso
			commandClientCommunicationManagerInterface.SendMessage(nick+" is disconnected");
	}
}
//END SERVER CLASS

