package it.unipr.informatica.reti.PRP.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class LogPanel extends JPanel {
	
	private JTextField field;
	private SwingApplication parentApplication;
	
	public LogPanel(SwingApplication swingApplication) {
		
		parentApplication = swingApplication;
		
		setLayout(new BorderLayout());
		field = new JTextField();
		field.setText("Logs");
		field.setVisible(true);
		field.setEditable(false);
		add(field, BorderLayout.CENTER);
	}

	public void loggedOn(String loggedNick) {
		// TODO Auto-generated method stub
		
	}

}
