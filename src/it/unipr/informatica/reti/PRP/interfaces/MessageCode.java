package it.unipr.informatica.reti.PRP.interfaces;

public enum MessageCode {

	/**
	 * Provides a unique criteria to determine the nature of a packet. 
	 * 
	 * 	MESSAGE					the packet is a plain message (point-to-point, unicast)
	 *  BROADCAST				the packet is a broadcast message
	 *	HELLO					the packet is a presentation message including node informations
	 *	BACKUP_NODE				the packet contains information for the backup node for the sender node
	 *	NOW_AVAILABLE			the packet signals that a user is now available through the sender node 
	 *	NOT_AVAILABLE_ANYMORE	the packet signals that a user disconnected from the network
	 *	TABLE_UPDATE			the packet contains the nodes which may be reached through the sender node
	 */
	MESSAGE,
	BROADCAST,
	HELLO,
	BACKUP_NODE,
	NOW_AVAILABLE,
	NOT_AVAILABLE_ANYMORE,
	TABLE_UPDATE
	
}
