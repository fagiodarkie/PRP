package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.utils.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParentsManager {
	
	ParentClientManager parentClientManager;
	Command command;
	Command commandToNotifyDadChange;
	/**
	 * Constructor. This manager is different from the ClientManager as the socket used differ a little.
	 * This object manages the communication with the node to which the client first connected (from
	 * now on called "parent" node).
	 * 
	 * @param connectionsManager the connections manager to which it should communicate topology changes and messages
	 * @param tableManager the table manager to which notifications should be given.
	 * @param command 
	 */
	public ParentsManager(Command command, Command commandToNotifyDadChange)
	{
		this.command= command;
		this.commandToNotifyDadChange = commandToNotifyDadChange;
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
		if(Nick == null || IP == null || Port == -1)
			return false;
		
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
			});
			parentClientManager.connect();
		}catch (Exception ex)
		{
			return false;
		}
		
		Constants.tableManager.notifyIsReachedBy(Nick, Nick);
		Constants.connections.addClient(Nick,parentClientManager);


		return true;
	}
	
	/**
	 * Tries to connect to every node specified in the backup file: the first available node is kept as father.
	 * @param File URL to the backup file
	 * @param MyNick the internal nickname
	 * @param MyPort the internal listen port
	 * @param MyIp the internal IP address
	 * @throws UnknownHostException if the chosen node is unavailable
	 * @throws NumberFormatException if the file is not properly formatted
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
					ex.printStackTrace();
					//il file non � formattato correttamente
					return false;
				} 
				catch (UnknownHostException e) {
					e.printStackTrace();
					return false;
				}
				
				int port = Integer.parseInt(nodoInfo[1]);
				
				if (this.connect(nodoInfo[0], port, address ,  MyNick, MyPort, MyIp))
				{
					Constants.tableManager.notifyIsReachedBy(parentClientManager.getNick(), parentClientManager.getNick());
					Constants.connections.addClient(parentClientManager.getNick(), parentClientManager);
					return true;
				}
				
			}
		}
		//se arrivo a questo punto o il file non � presente o non sono riuscito a connettermi a nessun file 
		//allora non ho modo di connettermi a nessun server allora restituisco false
		return false;
		
	}
	
	/**
	 * Connection to the backup node
	 */
	public void riconnetti()
	{
		//mi salvo le informazioni riguardanti il nodo di backup
		int port = parentClientManager.getBackupPort();
		String nick = parentClientManager.getBackupNick();
		InetAddress IP = parentClientManager.getBackupIP();
		String Myport = parentClientManager.getMyPort();
		String Mynick = parentClientManager.getMyNick();
		InetAddress MyIP = parentClientManager.getMyIP();
		//riconnetto il padre
		if(!this.connect(nick, port, IP,  Mynick,Integer.parseInt(Myport), MyIP))
			{
			System.out.println("impossibile riconnettermi al padre quindi diventiamo root");
			this.commandToNotifyDadChange.manageMessage(null);
			}
		else
		{//devo aggiornare i miei figli sul nuovo nodo di backup
			ArrayList<String> arrInfo = new ArrayList<>();
			arrInfo.add(nick);
			arrInfo.add(IP.toString().replace("\\","//").replace("/", ""));
			arrInfo.add(String.valueOf(port));
			this.commandToNotifyDadChange.manageMessage(arrInfo.toArray(new String[arrInfo.size()]));
		}
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
