package it.unipr.informatica.reti.PRP.swing;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChatBox extends JPanel {
	
	/**
	 * Field in which the user can write to the selected node.
	 */

	private SwingApplication parentPanel;
	private JTextField chatField;
	private JButton submit;

	public ChatBox(SwingApplication swingApplication) {
		parentPanel = swingApplication;
		
		// TODO manage well the textbox (three / four lines, enter...)
		
		this.setLayout(new BorderLayout());
		chatField = new JTextField();
		submit = new JButton("Send");
		this.add(chatField, BorderLayout.CENTER);
		this.add(submit, BorderLayout.EAST);
	}

}
