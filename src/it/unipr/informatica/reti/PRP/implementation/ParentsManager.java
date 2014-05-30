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
import java.util.List;
import java.util.ListIterator;

public class ParentsManager {
	
	ParentClientManager parentClientManager;
	NetworkConnectionsManager connectionsManager;
	TableManager tableManager;
	Command command;
	public ParentsManager(NetworkConnectionsManager connectionsManager,TableManager tableManager, Command command)
	{
		this.connectionsManager = connectionsManager;
		this.command= command;
		this.tableManager = tableManager;
	}
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
		}catch (Exception ex)
		{
			return false;
		}
		//avviso che il genitore è raggiungibile tramite me
		command.manageMessage(MessageFormatter.GenerateReachableMessage(parentClientManager.getNick()), parentClientManager.MyNick);
		tableManager.notifyIsReachedBy(parentClientManager.getNick(), parentClientManager.getNick());
		connectionsManager.addClient(parentClientManager.getNick(),parentClientManager);
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
	 * legge da file le informazioni riguardanti i client noti salvate nella sessione precedente
	 * @return restituisce una lista di stringhe contenenti le informazioni dei client
	 */
	static private List<String> readTable()
	{
		List<String> informations = new List<String>() {
			
			@Override
			public <T> T[] toArray(T[] a) {
				return null;
			}
			
			@Override
			public Object[] toArray() {
				return null;
			}
			
			@Override
			public List<String> subList(int fromIndex, int toIndex) {
				return null;
			}
			
			@Override
			public int size() {
				return 0;
			}
			
			@Override
			public String set(int index, String element) {
				return null;
			}
			
			@Override
			public boolean retainAll(Collection<?> c) {
				return false;
			}
			
			@Override
			public boolean removeAll(Collection<?> c) {
				return false;
			}
			
			@Override
			public String remove(int index) {
				return null;
			}
			
			@Override
			public boolean remove(Object o) {
				return false;
			}
			
			@Override
			public ListIterator<String> listIterator(int index) {
				return null;
			}
			
			@Override
			public ListIterator<String> listIterator() {
				return null;
			}
			
			@Override
			public int lastIndexOf(Object o) {
				return 0;
			}
			
			@Override
			public Iterator<String> iterator() {
				return null;
			}
			
			@Override
			public boolean isEmpty() {
				return false;
			}
			
			@Override
			public int indexOf(Object o) {
				return 0;
			}
			
			@Override
			public String get(int index) {
				return null;
			}
			
			@Override
			public boolean containsAll(Collection<?> c) {
				return false;
			}
			
			@Override
			public boolean contains(Object o) {
				return false;
			}
			
			@Override
			public void clear() {
				
			}
			
			@Override
			public boolean addAll(int index, Collection<? extends String> c) {
				return false;
			}
			
			@Override
			public boolean addAll(Collection<? extends String> c) {
				return false;
			}
			
			@Override
			public void add(int index, String element) {
				
			}
			
			@Override
			public boolean add(String e) {
				return false;
			}
		};
			
		
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
