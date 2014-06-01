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
	@Override
	public Boolean stop() {
		// TODO FINE ASCOLTO
		return null;
	}
	//END CONNECTION MANAGEMENT METHOD


	/**
	 * Gestisce i messassi ricevuti dall'interfaccia grafica
	 * @param Message
	 * @param MyNick
	 */
	public void ManageMessageFromUserInterface(String Message,String MyNick)
	{
		//TODO remove test
		//TEST
		System.out.println("messaggio ricevuto dall'interfaccia grafica --> messaggio='"+ Message +"'; nick='"+ MyNick +"'");
		commandClientCommunicationManagerInterface.SendMessage(Message + " - " + MyNick);
		//END TEST
		

		if(Message.contains("@") && Message.trim().startsWith("@"))
		{
			System.out.println("CONTIENE UN COMANDO");	
			
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
				//TODO completare invio parte broadcast
			}
			else
			{
				//TODO completare invio multicast
				
			}
				
		}
		else
		{
			commandClientCommunicationManagerInterface.SendMessage("il comando non e' valido");
		}
	}

	
	/**
	 * Gestisce l'arrivo dei messaggi passati dai vari client (compreso il genitore)
	 * @param Message messaggio ricevuto
	 * @param Client nick del client che ha mandato il messaggio
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
					//lo devo mandare sulla linea corretta
					connections.sendMesage(tableManager.howToReach(messageManagement.getReceiver()), Message);
				}
			break;

			
			
		/*messaggio BROADCAST     */
			case Constants.MessageBroadcastCode :

				for(String nick : tableManager.allMyNeighbors())
				{
					if(nick != Client)
					{
						connections.getClient(nick).sendMessage(Message);
					}
				}

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
	 * Gestisce la disconnessione di un client
	 * @param nick
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

