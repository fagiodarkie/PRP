package it.unipr.informatica.reti.PRP.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ConversationPanel extends JPanel {

	
	private SwingApplication parentApplication;
	private JTextArea chat;

	public ConversationPanel(SwingApplication swingApplication) {
		parentApplication = swingApplication;
		chat = new JTextArea();
		chat.setEditable(false);
		
		setLayout(new BorderLayout());
		add(chat, BorderLayout.CENTER);
	}

}
