package it.unipr.informatica.reti.PRP.swing;

import it.unipr.informatica.reti.PRP.interfaces.CacheInterface;

public class SwingCache implements CacheInterface {

	private SwingApplication application;

	public SwingCache(SwingApplication swingApplication) {
		application = swingApplication;
	}

}
