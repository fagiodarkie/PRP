package it.unipr.informatica.reti.PRP.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import it.unipr.informatica.reti.PRP.interfaces.CacheInterface;
import it.unipr.informatica.reti.PRP.interfaces.NetworkMantainer;
import it.unipr.informatica.reti.PRP.interfaces.PRPApplication;
import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.MessageInterface;
import it.unipr.informatica.reti.PRP.interfaces.NetworkManagerInterface;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class SwingApplication implements PRPApplication {

	// Start of application components.
	private ClientInterface clientInterface;
	private NetworkManagerInterface network;
	private NetworkMantainer mantainer;
	private CacheInterface cache;
	private MessageInterface composedMessage;
	// End of application components.
	
	// Start of graphic components.
	private JFrame mainWindow;
	private JDialog loginDialog;
	private LogPanel logPanel;
	private ContactPanel contactPanel;
	private ConversationPanel conversationPanel;
	private ChatBox chatBox;
	// End of graphic components.
	
	private String Nick;
		
	
	@Override
	public void run() {
		
		// Start of graphic component creation
		logPanel = new LogPanel(this);
		contactPanel = new ContactPanel(this);
		conversationPanel = new ConversationPanel(this);
		chatBox = new ChatBox(this);
		
		JPanel leftGrid = new JPanel();
		leftGrid.setLayout(new GridLayout(2,1));
		leftGrid.add(logPanel);
		leftGrid.add(contactPanel);
		
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		centralPanel.add(conversationPanel, BorderLayout.CENTER);
		centralPanel.add(chatBox, BorderLayout.SOUTH);
		
		mainWindow = new JFrame();
		mainWindow.setTitle("PRP Client");
		mainWindow.setSize(700, 500);
		Container contentPane = mainWindow.getContentPane();		
		contentPane.setLayout(new BorderLayout());
		contentPane.add(leftGrid, BorderLayout.WEST);
		contentPane.add(centralPanel, BorderLayout.CENTER);
		mainWindow.setVisible(true);
		// End of graphic component creation
		
		
		
	}
	
	public void loggedOn(String loggedNick) {
		logPanel.loggedOn(loggedNick);
		contactPanel.loggedOn(loggedNick);
	}
	
}
