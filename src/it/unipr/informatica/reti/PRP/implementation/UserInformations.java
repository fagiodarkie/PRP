package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;

import it.unipr.informatica.reti.PRP.interfaces.UserInformationsInterface;

public class UserInformations implements UserInformationsInterface {

	/**
	 * The implementation of UserInformations used in this project.
	 * Other, more or less performant versions could be developed.
	 */
	
	/**
	 * the nickname of the User described by this object.
	 * The network works assuming that no nickname is duplicated, therefore
	 * anyone who (willingly or not) chooses a nickname already connected to
	 * the network will destabilize it.
	 */
	private String nick;
	
	/**
	 * Every nicnkame is assumed to have a fixed port. Its listen socket
	 * should bind to this port and never to other ones.
	 */
	private int port;
	
	/**
	 * The IP address of the listening socket. This too should not change. 
	 * Moreover, it should be the actual IP address, and not 'localhost'.
	 * Unless one wants to create a local network, in which case it is fine.
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
	 * "copy" constructor from another UserInformationsInterface derived.
	 * 
	 * @param identity the UserInformationsInterface from which the informations should be copied.
	 */
	public UserInformations(UserInformationsInterface identity) {
		nick = identity.getNick();
		port = identity.getPort();
		address = identity.getAddress();
	}

	/**
	 * Constructor from a marshalled UserInformation.
	 * 
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
	public boolean equals(UserInformationsInterface i) {
		return nick.equals(i.getNick())
			&& (port == i.getPort())
			&& address.equals(i.getAddress());
	}

}
