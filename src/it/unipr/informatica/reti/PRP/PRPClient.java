package it.unipr.informatica.reti.PRP;

import it.unipr.informatica.reti.PRP.interfaces.PRPApplication;
import it.unipr.informatica.reti.PRP.swing.SwingApplication;

public class PRPClient {

	public static void main(String args[]) {
		PRPApplication client = new SwingApplication();
		
		client.run();
	}
	
}
