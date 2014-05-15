package it.unipr.informatica.reti.PRP.utils;

import java.util.List;

public class MessageFormatter {
	public static String GenerateHelloMessage(String Nick,String Ip,String Port)
	{
		return Constants.MessagePointToPointCode + Constants.MessagePartsDivisor +
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
		return Constants.MessageNotReachableCode + Constants.MessagePartsDivisor + Nick;
	}
	
	public static String GenerateReachableMessage(String Nick)
	{
		return Constants.MessageReachableCode + Constants.MessagePartsDivisor + Nick;
	}	
	public static String GenerateNotReachableMessage(String Nick)
	{
		return Constants.MessageNotReachableCode + Constants.MessagePartsDivisor + Nick;
	}
	
	public static String GenerateTableMessage(List<String> NickAndPort)
	{
		//TODO CREATE FUNCTION WITH NICK AND PORT IN TWO SEPARATED LIST
		String Message = Constants.MessageTableCode+Constants.MessagePartsDivisor;
		
		for(String nick : NickAndPort)
		{
			Message += nick;
		}
		return Message;
	}
	
}
