package it.unipr.informatica.reti.PRP.implementation;

import java.net.InetAddress;

import it.unipr.informatica.reti.PRP.interfaces.NodeInformation;

public class UserInformations implements NodeInformation {

	private String nick;
	private int port;
	private InetAddress address;
	
	public UserInformations(String newNick, int newPort, InetAddress inetAddress) {
		nick = newNick;
		port = newPort;
		address = inetAddress;
	}
	
	
	public UserInformations(NodeInformation identity) {
		nick = identity.getNick();
		port = identity.getPort();
		address = identity.getAddress();
	}


	@Override
	public String getNick() {
		return nick;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public InetAddress getAddress() {
		return address;
	}

}
