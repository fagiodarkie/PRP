package it.unipr.informatica.reti.PRP.utils;

import it.unipr.informatica.reti.PRP.implementation.TableManager;
import it.unipr.informatica.reti.PRP.interfaces.UserInformationsInterface;

import java.util.List;

public class MessageFormatter {
	public static String GenerateHelloMessage(String Nick,String Ip,String Port)
	{
		return Constants.MessageHelloCode + Constants.MessagePartsDivisor +
				Nick + Constants.MessagePartsDivisor + 
				Ip + Constants.ConnectionInfoDivisor + 
				Port;
	}
	
	public static String GeneratePointToPointMessage(String NickFrom,String NickTo, String Message)
	{
		return Constants.MessagePointToPointCode + Constants.MessagePartsDivisor +
				NickFrom + Constants.MessagePartsDivisor + 
				NickTo + Constants.MessagePartsDivisor + 
				Message;
	}
	
	public static String GenerateBroadcastMessage(String Nick,String Message)
	{
		return Constants.MessageBroadcastCode + Constants.MessagePartsDivisor + Nick + Constants.MessagePartsDivisor + Message;
	}
	
	public static String GenerateBackupMessage(String Nick)
	{
		return Constants.MessageBackupNickCode + Constants.MessagePartsDivisor + Nick;
	}
	
	public static String GenerateReachableMessage(String Nick)
	{
		return Constants.MessageReachableCode + Constants.MessagePartsDivisor + Nick;
	}
	
	public static String GenerateNotReachableMessage(String Nick)
	{
		return Constants.MessageNotReachableCode + Constants.MessagePartsDivisor + Nick;
	}
	
	public static String GenerateTableMessage(TableManager tableManager)
	{
		String Message = Constants.MessageTableCode+Constants.MessagePartsDivisor;
		List<String> nodiConosciuti = tableManager.getConnectedNodes();
		int i = 0;
		//se ho più di un nodo allora per quelli interni aggiungo alla fine anche un altro ':'
		if(nodiConosciuti.size() > 1)
			for(i = 0 ;i < (nodiConosciuti.size() - 1); ++i)
			{

				UserInformationsInterface info = tableManager.getInfoByNick(nodiConosciuti.get(i));
				Message += info.getNick();
				Message += ":";
				Message += info.getAddress();
				Message += ":";
				Message += info.getPort();
				Message += ":";
			}
		//per l'ultimo (o per il primo se ce ne fosse solo uno) aggiungo le informazioni ma non aggiungo il ':' finale
		

		
		UserInformationsInterface info = tableManager.getInfoByNick(nodiConosciuti.get(i));
		 
		Message += info.getNick();
		Message += ":";
		Message += info.getAddress();
		Message += ":";
		Message += info.getPort();
		return Message;
	}
	
}
