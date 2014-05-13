package it.unipr.informatica.reti.PRP.UserInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.unipr.informatica.reti.PRP.interfaces.UserInterfaceCommandManager;

public class UserInterface {

	public UserInterface(UserInterfaceCommandManager commandManager)
	{
		Initialize(commandManager);
	}
	
	private void Initialize(UserInterfaceCommandManager commandManager)
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
	{	
		System.out.println(Message);
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
	 
			while((Message=br.readLine())!=null){
				interfaceCommandManager.ManageInput(Message);
			}
	 
		}catch(IOException io){
			io.printStackTrace();
		}	
	}
}