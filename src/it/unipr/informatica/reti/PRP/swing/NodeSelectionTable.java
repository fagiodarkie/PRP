package it.unipr.informatica.reti.PRP.swing;

import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class NodeSelectionTable extends JPanel {
	
	/**
	 * Provides a screen in which is possible to select the node
	 * to be used as entry point for the network.
	 */
	
	private JTable table;
	private List<NodeInformation> nodes;
	private SwingApplication application;
	private int selectedNode;
	
	public NodeSelectionTable(SwingApplication a, List<NodeInformation> list) {
		application = a;
		nodes = list;
		
		setOpaque(true);
		setLayout(new BorderLayout());
		table = new JTable();
		table.setModel(new MyModelAdapter(nodes));
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		final ListSelectionModel selectionModel = table.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evento) {
				int selected = selectionModel.getMaxSelectionIndex();

				if(selected >= 0 && selected < nodes.size())
					selectedNode = selected;
				else
					selectedNode = 0;
			}
		});
		
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	public NodeInformation getSelectedNode() {
		return nodes.get(selectedNode);
	}

	public void setNodes(List<NodeInformation> nodeInfos) {
		this.nodes = nodeInfos;
		TableModel tableModel = new MyModelAdapter(nodes);
		table.setModel(tableModel);
	}

	private class MyModelAdapter implements TableModel {
		private List<NodeInformation> nodes;
		
		private MyModelAdapter(List<NodeInformation> infos) {
			this.nodes = infos;
		}
		
		@Override
		public int getColumnCount() {
			return 3;
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex) {
			case 0:
				return "Nickname";
			case 1:
				return "Port";
			case 2:
				return "Address";
			}
			
			throw new IllegalArgumentException();
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch(columnIndex) {
			case 0:
				return String.class;
			case 1:
				return Integer.class;
			case 2:
				return InetAddress.class;
			}

			throw new IllegalArgumentException();
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
		
		@Override
		public int getRowCount() {
			return nodes.size();
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			NodeInformation node = nodes.get(rowIndex);
			
			switch(columnIndex) {
			case 0:
				return node.getNick();
			case 1:
				return node.getPort();
			case 2:
				return node.getAddress();
			}
			
			throw new IllegalArgumentException();
		}
		
		@Override
		public void addTableModelListener(TableModelListener l) {
			// Vuoto
		}
		
		@Override
		public void removeTableModelListener(TableModelListener l) {
			// Vuoto
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			// Vuoto
		}
	}

}
