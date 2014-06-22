package it.unipr.informatica.reti.PRP.userInterface;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;
import it.unipr.informatica.reti.PRP.utils.Constants;

public class UserInterface {

	String Nick;
	UserInterfaceCommandManager command ;
	public UserInterface()
	{
		System.out.print("Nickname con cui registrarsi:");
		BufferedReader br = 
                new BufferedReader(new InputStreamReader(System.in));
		try{
		Nick = br.readLine();
		}
		catch(IOException e)
		{}
		System.out.print("utilizzare la porta di defalut \""+Constants.PortOfServer+"\"(y/n):");
		try
		{
			String utilizzoporta = br.readLine();
			System.out.println(utilizzoporta);
			if(utilizzoporta.toLowerCase().equals("y")){
				System.out.println("verra' utilizzata la porta di default");
			}
			else if(utilizzoporta.toLowerCase().equals("n")){
				System.out.print("porta da utilizzare:");
				String port = br.readLine();
				try
				{
					Constants.PortOfServer = Integer.parseInt(port);
				}
				catch(NumberFormatException e)
				{
					System.out.println("impossibile convertire il valore inserito in un numero di porta");
					System.out.println("l'applicazione verra' chiusa");
					System.exit(0);
				}

				System.out.println("porta di ascolto impostata a:"+Constants.PortOfServer);
			}
			else
			{
				System.out.println("il valore inserito non e' corretto");
				System.out.println("l'applicazione verra' chiusa");
				System.exit(0);
			}
		}
		catch(IOException e)
		{}
	}
	
	public void StartReadingFromInput()
	{
		
		initialize(command);
		
	}
	private void initialize(UserInterfaceCommandManager commandManager)
	{
		InputManager inputManager = new InputManager(commandManager);
		Thread t = new Thread(inputManager);
		t.start();
		
	}
	
	/**
	 * send the message to the output stream
	 * @param Message
	 */
	public void PrintMessage(String Message)
	{	System.out.println(Message);
	}
	
	public String getNick () 
	{
		return Nick;
	}
	public void setCommand(UserInterfaceCommandManager commandManager)
	{
		command = commandManager;
	}
}



class InputManager implements Runnable
{
	UserInterfaceCommandManager interfaceCommandManager;
	public InputManager(UserInterfaceCommandManager commandManager)
	{
		this.interfaceCommandManager = commandManager;
	}
	@Override
	public void run() {
		try{
			BufferedReader br = 
	                      new BufferedReader(new InputStreamReader(System.in));
	 
			String Message;
			System.out.print(">");
			while((Message=br.readLine())!=null){
				interfaceCommandManager.ManageInput(Message);
				System.out.print(">");
			}
	 
		}catch(IOException io){
			io.printStackTrace();
		}	
	}
}