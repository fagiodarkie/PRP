package it.unipr.informatica.reti.PRP.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import it.unipr.informatica.reti.PRP.implementation.Connections;
import it.unipr.informatica.reti.PRP.implementation.NetworkKeeper;
import it.unipr.informatica.reti.PRP.implementation.TableManager;
import it.unipr.informatica.reti.PRP.implementation.UserInformations;
import it.unipr.informatica.reti.PRP.interfaces.NetworkMantainer;
import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;
import it.unipr.informatica.reti.PRP.interfaces.PRPApplication;
import it.unipr.informatica.reti.PRP.interfaces.ClientInterface;
import it.unipr.informatica.reti.PRP.interfaces.MessageInterface;
import it.unipr.informatica.reti.PRP.interfaces.NetworkManagerInterface;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

public class SwingApplication implements PRPApplication {

	/**
	 * Main application component.
	 * Wraps the gui components, as well as the network modules,
	 * and manages the conversation between them.
	 */
	
	// Start of application components.
	private ClientInterface clientInterface;
	private NetworkManagerInterface network;
	private NetworkMantainer mantainer;
	private MessageInterface composedMessage;
	private List<NodeInformation> nicks;
	// End of application components.
	
	// Start of graphic components.
	private JFrame mainWindow;
	private LogPanel logPanel;
	private ContactPanel contactPanel;
	private ConversationPanel conversationPanel;
	private ChatBox chatBox;
	// End of graphic components.
	
	// Other variables
	private NodeInformation myIdentity;
	private Semaphore sem;
	
	public SwingApplication() {
		sem = new Semaphore(0);
	}
	
	@Override
	public void run() {

		// TODO manage nick list
		nicks = new ArrayList<NodeInformation>();
		nicks.add(new UserInformations("Ciccio", 123, InetAddress.getLoopbackAddress() ));
		nicks.add(new UserInformations("Mamma", 123, InetAddress.getLoopbackAddress() ));
		nicks.add(new UserInformations("Nonna Papera", 123, InetAddress.getLoopbackAddress() ));
		nicks.add(new UserInformations("Roberto Bagnara", 123, InetAddress.getLoopbackAddress() ));

		// Nick selection procedure
		getIdentityInformations();
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Start of graphic component creation
		logPanel = new LogPanel(this);
		contactPanel = new ContactPanel(this, nicks);
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
		mainWindow.setTitle("PRP Client - " + myIdentity.getNick());
		mainWindow.setSize(700, 500);
		Container contentPane = mainWindow.getContentPane();		
		contentPane.setLayout(new BorderLayout());
		contentPane.add(leftGrid, BorderLayout.WEST);
		contentPane.add(centralPanel, BorderLayout.CENTER);
		mainWindow.setVisible(true);
		// End of graphic component creation
		
		// Start of network component creation
		clientInterface = new Connections(this);
		mantainer = new NetworkKeeper(this);
		network = new TableManager(this);
		// End of network component creation
		
		
	}
	
	private void getIdentityInformations() {
		
		final JPopupMenu loginWindow = new JPopupMenu();
				
		final NodeSelectionTable nodeTable = new NodeSelectionTable(this, nicks);
		JButton selectButton = new JButton();
		selectButton.setText("This is my Identity");
		selectButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				myIdentity = nodeTable.getSelectedNode();
				loginWindow.setVisible(false);
				sem.release();
				// I'm too good
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(selectButton);
		
		loginWindow.setSize(400, 300);
		loginWindow.setLayout(new BorderLayout());
		loginWindow.add(nodeTable, BorderLayout.CENTER);
		loginWindow.add(buttonPanel, BorderLayout.SOUTH);
		loginWindow.setVisible(true);
		
		
	}
	
	public void loggedOn(String loggedNick) {
		logPanel.loggedOn(loggedNick);
		contactPanel.loggedOn(loggedNick);
	}
	
}
