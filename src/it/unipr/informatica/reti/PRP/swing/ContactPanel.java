package it.unipr.informatica.reti.PRP.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class ContactPanel extends JPanel {
	
	private SwingApplication parentPanel;
	private JTextField contacts;

	public ContactPanel(SwingApplication swingApplication) {
		parentPanel = swingApplication;
		
		setLayout(new BorderLayout());
		contacts = new JTextField();
		contacts.setText("myFriends");
		contacts.setVisible(true);
		contacts.setEditable(false);
		add(contacts, BorderLayout.CENTER);
	}

	public void loggedOn(String loggedNick) {
		// TODO Auto-generated method stub
		
	}

}
