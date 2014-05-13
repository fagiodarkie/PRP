package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;

import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;

public class UserInformations implements NodeInformation {

	/**
	 * 
	 */
	
	/**
	 * 
	 */
	private String nick;
	/**
	 * 
	 */
	private int port;
	/**
	 * 
	 */
	private InetAddress address;
	
	/**
	 * POJO constructor.
	 * 
	 * @param newNick the nickname of the user
	 * @param newPort the listen port of the user
	 * @param inetAddress the IP address of the user.
	 */
	public UserInformations(String newNick, int newPort, InetAddress inetAddress) {
		nick = newNick;
		port = newPort;
		address = inetAddress;
	}
	
	/**
	 * "copy" constructor from another NodeInformation derived.
	 * 
	 * @param identity the NodeInformation from which the informations should be copied.
	 */
	public UserInformations(NodeInformation identity) {
		nick = identity.getNick();
		port = identity.getPort();
		address = identity.getAddress();
	}

	/**
	 * Constructor from a marshalled UserInformation.
	 * @param newInterface the marshalled informations.
	 */
	public UserInformations(String newInterface) {
		// TODO string constructor!
	}


	/**
	 * @return the nickname of the user. 
	 */
	@Override
	public String getNick() {
		return nick;
	}

	/**
	 * @return the listen port of the user. 
	 */
	@Override
	public int getPort() {
		return port;
	}

	/**
	 * @return the listen IP address of the user. 
	 */
	@Override
	public InetAddress getAddress() {
		return address;
	}


	/**
	 * @return true if the two objects shares the same informations.
	 * 			false otherwise. 
	 */
	@Override
	public boolean equals(NodeInformation i) {
		return nick.equals(i.getNick())
			&& (port == i.getPort())
			&& address.equals(i.getAddress());
	}

}
