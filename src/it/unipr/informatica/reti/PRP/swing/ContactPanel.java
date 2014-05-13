package it.unipr.informatica.reti.PRP.swing;

import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class ContactPanel extends JPanel {
	
	/**
	 * List of connected nodes.
	 */
	
	private SwingApplication parentPanel;
	private JTextField contacts;
	private List<NodeInformation> contactList;

	public ContactPanel(SwingApplication swingApplication,
			List<NodeInformation> nicks) {
		parentPanel = swingApplication;
		contactList = nicks;
		
		// TODO manage the contact list
		setLayout(new BorderLayout());
		contacts = new JTextField();
		contacts.setText(contactList.get(0).getNick());
		contacts.setVisible(true);
		contacts.setEditable(false);
		add(contacts, BorderLayout.CENTER);
	}


	public void loggedOn(String loggedNick) {
		// TODO Auto-generated method stub
		
	}

}
