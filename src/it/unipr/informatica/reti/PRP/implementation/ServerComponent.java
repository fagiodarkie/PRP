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
	private Boolean hasDad;
	private String dadNick;
	private String dadIP;
	private String dadPort;
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
		this.hasDad = false;
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

				
				//se ho un nodo padre invio al nodo il nodo di backup( mio padre )
				if(hasDad)
				{
					String backupNickMessage = MessageFormatter.GenerateBackupMessage(dadNick,dadIP, dadPort);
					c.sendMessage(backupNickMessage);
				}
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
	public void ManageMessageFromUserInterface(String Message,String MyNick)
	{
		//TODO REMOVE TEST CODE
		System.out.println("messaggio da analizzare: " + Message);

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
			String MessaggeToSend = "";
			for(String m : Messages)
			{
				MessaggeToSend += m + " ";
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

				//Genero il messaggio broadcast
				String MessageBroadcast = MessageFormatter.GenerateBroadcastMessage(MyNick, MessaggeToSend);
				//invio il messaggio
				sendToAllMyNeighbors(MessageBroadcast, "");

			}
			else
			{
				for(String nick : Commands)
				{
					//genero il messaggio da mandare
					String MessagePointToPoint = MessageFormatter.GeneratePointToPointMessage(MyNick, nick, MessaggeToSend); 
					//lo mando all'interfaccia corretta
					Constants.connections.sendMessage(Constants.tableManager.howToReach(nick), MessagePointToPoint);
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
	 * permette d'inviare un messaggio a tutti  i nodi vicini tranne a nickNoNA
	 * @param POJOMessage messagio da inviare
	 * @param nickNoNA nick a cui non inviarlo
	 */
	private void sendToAllMyNeighbors(String Message, String nickNoNA)
	{
		//estraggo i vicini a cui inviarlo
		List<String> neighbors = Constants.tableManager.allMyNeighbors();
		//tolgo il nodo dal quale ho ricevuto il messaggio
		neighbors.remove(nickNoNA.trim());

		//aggiorno i miei vicini mandando lo stesso messaggio
		for(String neighbor : neighbors)
		{
			Constants.connections.sendMessage(neighbor, Message);
		}
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

			{
				String receiver = messageManagement.getReceiver();

				if(!Constants.tableManager.isItConnected(receiver))
				{

					//il messaggio è rivolto a me e quindi lo invio all'interfaccia grafica
					this.commandClientCommunicationManagerInterface.SendMessage(messageManagement.getSender() + ":" + messageManagement.getData());
				}
				else
				{
					String interfaceToSend = Constants.tableManager.howToReach(receiver);

					if(interfaceToSend!= null && !interfaceToSend.isEmpty())
						Constants.connections.sendMessage(interfaceToSend, Message);
				}
			}


			break;



			/*messaggio BROADCAST     */
			case Constants.MessageBroadcastCode :

				//invio all'interfaccia grafica il messaggio
				this.commandClientCommunicationManagerInterface.SendMessage(messageManagement.getSender() + ":" + messageManagement.getData());
				
				//invio il messaggio a tutti i miei vicini tranne quello che me l'ha inviato
				sendToAllMyNeighbors(Message, Client);
				
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


				sendToAllMyNeighbors(Message, Client);


				commandClientCommunicationManagerInterface.SendMessage(messageManagement.getData()+" is connected");
			}
			break;

			/*messaggio NOT REACHABLE */
			case Constants.MessageNotReachableCode :
			{
				String nick = messageManagement.getData();
				//se è un mio vicino allora lo rimuovo anche dalle connessioni
				if(Constants.tableManager.isNearMe(nick))
					Constants.connections.removeClient(nick);

				//Aggiorno la mia tabella togliendo il nick
				Constants.tableManager.hasDisconnected(nick);

				sendToAllMyNeighbors(Message, Client);

				commandClientCommunicationManagerInterface.SendMessage(nick+" is disconnected");
				
				

			}
			break;

			/*messaggio TABLE   */      
			case Constants.MessageTableCode:
			{

				//ottengo le informazioni della tabella
				String tabella = messageManagement.getData();

				String reachableMessage="";

				//controllo se sono multiple
				if(tabella.contains(":"))
				{
					List<String> nicks = new ArrayList<String>(Arrays.asList(tabella.split(":")));
					nicks.remove(nick);
					for(String newNick : nicks)
					{
						if(newNick != null && !newNick.trim().isEmpty() )
						{
							//aggiungo il nuovo nick alla tabella
							Constants.tableManager.notifyIsReachedBy(newNick, Client);

							//creo il messaggio di aggiornamento
							reachableMessage = MessageFormatter.GenerateReachableMessage(newNick);

							sendToAllMyNeighbors(reachableMessage, Client);
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

						sendToAllMyNeighbors(reachableMessage, Client);

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


		String notReachableMessage = MessageFormatter.GenerateNotReachableMessage(nick);

		//invio a tutti i miei vicini (tranne quello che si è disconnesso) il messaggio di NotReachable
		sendToAllMyNeighbors(notReachableMessage, nick);
		
		/*
		//ottengo tutti i nodi che erano raggiungibili dal nodo che è scomparso
		List<String> reachableNodes = Constants.tableManager.getAllNodesReachableFrom(nick);
		//TODO REMOVE TEST
		System.out.println("list of reachable nodes size:"+reachableNodes.size());
		
		//per ogni nodo che era raggiungibile
		for(String node : reachableNodes)
		{
			//creo il messaggio che avverte che era raggiungibile
			notReachableMessage = MessageFormatter.GenerateNotReachableMessage(node);
			
			//lo invio a tutti i miei vicini (ovviamente il vicino che si era staccato è già stato cancellato
			sendToAllMyNeighbors(notReachableMessage, "");
			
			//tolgo anche il collegamento dalla tabella
			Constants.tableManager.hasDisconnected(node);
		}*/
		
		//invio all'interfaccia utente il comando di stampare il messaggio che l'utente si è disconnesso
		commandClientCommunicationManagerInterface.SendMessage(nick+" is disconnected");

	}
	
	public void setHasDad()
	{
		this.hasDad = false;
		this.dadIP = "";
		this.dadNick = "";
		this.dadPort = "";
	}
	public void setHasDad(String dadNick, String dadIp, String dadPort)
	{
		this.hasDad = true;
		this.dadIP = dadIp;
		this.dadNick = dadNick;
		this.dadPort = dadPort;
	}
}
//END SERVER CLASS

