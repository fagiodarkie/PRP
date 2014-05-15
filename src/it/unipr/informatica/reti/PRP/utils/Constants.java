package it.unipr.informatica.reti.PRP.utils;

public class Constants {
	/**
	 * porta a cui si connette il server
	 */
	public static int PortOfServer = 3333;
	/**
	 * porta a cui si connette l'interfaccia grafica dell'input 
	 */
	public static int PortOfInputClient = 3334;
	/**
	 * porta a cui si connette l'interfaccia di output
	 */
	public static int PortOfOutputClient = 3335;
	/**
	 * stringa di divisione tra le parti del messaggio
	 */
	public final static String MessagePartsDivisor = ":-:";
	/**
	 * Stringa di divisione delle informazioni ip e porta
	 */
	public final static String ConnectionInfoDivisor =":";
	/**
	 * codice identificativo dei messaggi HELLO
	 */
	public final static String MessageHelloCode ="0";
	/**
	 * codice identificativo dei messaggi Point to Point
	 */
	public final static String MessagePointToPointCode = "1";
	/**
	 * codice identificativo dei messaggi Broadcast
	 */
	public final static String MessageBroadcastCode = "2";
	/**
	 * codice identificativo dei messaggi BAckup 
	 */
	public final static String MessageBackupNickCode ="4";
	/**
	 * codice identificativo dei messaggi Reachable
	 */
	public final static String MessageReachableCode="5";
	/**
	 * codice identificativo dei messaggi Not Reachable
	 */
	public final static String MessageNotReachableCode="6";
	/**
	 * codice identificativo dei messaggi Table
	 */
	public final static String MessageTableCode = "7";
	
}
