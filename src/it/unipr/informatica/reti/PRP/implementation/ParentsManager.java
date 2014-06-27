package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.utils.Constants;
import it.unipr.informatica.reti.PRP.utils.MessageFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class ParentsManager {
	
	ParentClientManager parentClientManager;
	NetworkConnectionsManager connectionsManager;
	TableManager tableManager;
	Command command;
	
	/**
	 * Constructor. This manager is different from the ClientManager as the socket used differ a little.
	 * This object manages the communication with the node to which the client first connected (from
	 * now on called "parent" node).
	 * 
	 * @param connectionsManager the connections manager to which it should communicate topology changes and messages
	 * @param tableManager the table manager to which notifications should be given.
	 * @param command (?)
	 */
	public ParentsManager(NetworkConnectionsManager connectionsManager,TableManager tableManager, Command command)
	{
		this.connectionsManager = connectionsManager;
		this.command= command;
		this.tableManager = tableManager;
	}
	
	/**
	 * Connect to the specified client. It will serve as a gate for the network.
	 * 
	 * @param Nick nickname of the client to which we want to connect.
	 * @param Port listen port of the client to which we want to connect. 
	 * @param IP listen address of the client to which we want to connect.
	 * @param MyNick our nickname.
	 * @param MyPort our listen port.
	 * @param MyIp our listen address.
	 * @return true if the connection went well, false otherwise.
	 */
	public Boolean connect(String Nick, int Port, InetAddress IP, String MyNick, int MyPort, InetAddress MyIp)
	{
		try
		{
			parentClientManager = new ParentClientManager(Nick, Port, IP, MyNick, Integer.toString(MyPort ), MyIp, new Command() {
				
				@Override
				public void manageMessage(String[] PartsOfMessage) {
					//propago il messaggio ricevuto
					command.manageMessage(PartsOfMessage);
					
				}
				
				@Override
				public void manageMessage(String Message, String Client) {
					//propago il messaggio ricevuto
					command.manageMessage(Message, Client);
					
				}
				
				@Override
				public void manageDisconnection(String Name) {
					//per prima cosa propago il messaggio 
					command.manageDisconnection(Name);
					//successivamente mi connetto al nodo di backup
					try
					{
						riconnetti();
					}
					catch(Exception e)
					{
						
						//DO NOTHING in this case we lost the server's slice of network 
					}
					
				}
			}, this);
			parentClientManager.connect(MyNick);
		}catch (Exception ex)
		{
			return false;
		}
		//avviso che il genitore è raggiungibile tramite me
		//TODO SCOMMENTARE SEZIONE INSERIMENTO IN TABELLA CLIENT
		/*
		command.manageMessage(MessageFormatter.GenerateReachableMessage(parentClientManager.getNick()), parentClientManager.MyNick);
		tableManager.notifyIsReachedBy(parentClientManager.getNick(), parentClientManager.getNick());
		connectionsManager.addClient(parentClientManager.getNick(),parentClientManager);
		*/
		//TODO REMOVE TEST
		System.out.print("test--> connessione al padre avvenuta con successo");
		return true;
	}
	
	/**
	 * prova a connettersi a tutti i nodi presenti nel file di backup e il primo disponibile lo utilizza come padre
	 * @param File
	 * @param MyNick
	 * @param MyPort
	 * @param MyIp
	 * @throws UnknownHostException 
	 * @throws NumberFormatException 
	 */
	public Boolean connect(String File, String MyNick, int MyPort, InetAddress MyIp)
	{
		File f = new File(File);
		if(f.exists() && !f.isDirectory()) { 
			
			List<String> nodi = readTable();
			
			if(nodi.size() == 0)
				//se non sono presenti informazioni riguardanti i nodi restituisco false;
				return false;
			for(String nodo : nodi )
			{
				String nodoInfo[] = nodo.split(Constants.FileBackupInformationDivisor);
				InetAddress address;
				//provo a convertire il valore che ho nel file nell'indirizzo dell'host
				try
				{
					address = InetAddress.getByName(nodoInfo[2]);
				}
				catch(NumberFormatException ex)
				{
					//il file non è formattato correttamente
					return false;
				} 
				catch (UnknownHostException e) {
					return false;
				}
				
				if (this.connect(nodoInfo[0], Integer.parseInt(nodoInfo[1]), address ,  MyNick, MyPort, MyIp))
				{
					connectionsManager.addClient(parentClientManager.getNick(),parentClientManager);
					return true;
				}
				
			}
		}
		//se arrivo a questo punto o il file non è presente o non sono riuscito a connettermi a nessun file 
		//allora non ho modo di connettermi a nessun server allora restituisco false
		return false;
		
	}
	
	/**
	 * If something goes wrong, we try to connect again, using the same informations.
	 */
	public void riconnetti()
	{
		//mi salvo le informazioni riguardanti il nodo di backup
		int port = parentClientManager.backupPort;
		String nick = parentClientManager.backupNick;
		InetAddress IP = parentClientManager.backupIP;
		String Myport = parentClientManager.MyPort;
		String Mynick = parentClientManager.MyNick;
		InetAddress MyIP = parentClientManager.MyIP;
		//riconnetto il padre
		this.connect(nick, port, IP,  Mynick,Integer.parseInt(Myport), MyIP);
	}
	
	/**
	 * Read from file the informations about known clients, which may have been saved in the previous session (if any).
	 * 
	 * @return a list of strings containing the existing clients' informations.
	 */
	static private List<String> readTable()
	{
		
		LinkedList<String> informations = new LinkedList<String>();
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(Constants.PathOfTableBackupFile+"//"+Constants.NameOfTableBackupFile));
			String line;
			while ((line = br.readLine()) != null) {
			   informations.add(line);
			}
			br.close();
		
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		return informations;
	}

	
	public ParentClientManager getParent()
	{
		return parentClientManager;
	}
}
