package it.unipr.informatica.reti.PRP.userInterface;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;

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
	{	//TODO remove test
		//TEST
		System.out.println("Messaggio ricevuto dal server:'" + Message + "'");
		//END TEST
		System.out.println(Message);
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