package it.unipr.informatica.reti.PRP.implementation;

import it.unipr.informatica.reti.PRP.interfaces.Command;
import it.unipr.informatica.reti.PRP.utils.Constants;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ParentsManager {
	
	ParentClientManager parentClientManager;
	
	public void connect(String Nick, int Port, InetAddress IP, Command command, String MyNick, int MyPort, InetAddress MyIp)
	{
		parentClientManager = new ParentClientManager(Nick, Port, IP, MyNick, Integer.toString(MyPort ), MyIp, command, this);
	}
	public void connect(String File, String MyNick, int MyPort, InetAddress MyIp, Command command)
	{
		List<String> nodi = readTable();
		for(String nodo : nodi )
		{
			String nodoInfo[] = nodo.split(Constants.FileBackupInformationDivisor);
			try
			{
				this.connect(nodoInfo[0], Integer.parseInt(nodoInfo[1]), InetAddress.getByName(nodoInfo[2]), command, MyNick, MyPort, MyIp);
				break;
			}
			catch(Exception e)
			{}
		}
		
	}
	static private List<String> readTable()
	{
		List<String> informations = new List<String>() {
			
			@Override
			public <T> T[] toArray(T[] a) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object[] toArray() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<String> subList(int fromIndex, int toIndex) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String set(int index, String element) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean retainAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean removeAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public String remove(int index) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean remove(Object o) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public ListIterator<String> listIterator(int index) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ListIterator<String> listIterator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int lastIndexOf(Object o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Iterator<String> iterator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int indexOf(Object o) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String get(int index) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean containsAll(Collection<?> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean contains(Object o) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean addAll(int index, Collection<? extends String> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean addAll(Collection<? extends String> c) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void add(int index, String element) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean add(String e) {
				// TODO Auto-generated method stub
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return informations;
	}

	
	public ParentClientManager getParent()
	{
		return parentClientManager;
	}
}
