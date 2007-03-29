package com.mongus.util;

public class CreditCard
{
	public static enum Type { AMEX, DinersClub, Discover, enRoute, JCB, MasterCard, VISA };
	
	private static String strip(String number)
	{
		return number.replaceAll("\\D", "");
	}
	
	public static boolean isLuhnValid(String number)
	{
		number = strip(number);
		
		if (number.length() < 13 || number.length() > 16)
			return false;
	
		int sum = 0;
		
		for (int i = 0, length = number.length(); i < length; i++)
		{
			int pos = length - i - 1;
			
			int v = Integer.parseInt(number.substring(pos, pos + 1));
			
			if (i % 2 == 1)
				v *= 2;
	
			sum += v / 10 + v % 10;
		}
		
		return sum % 10 == 0;
	}
	
	public static boolean isValid(String number)
	{
		return getCardType(number) != null;
	}

	public static Type getCardType(String number)
	{
		number = strip(number);
		
		if (!isLuhnValid(number))
			return null;
		
		if (checkCard(number, 15, "34", "37"))
			return Type.AMEX;
		
		if (checkCard(number, 14, "30", "36", "38"))
			return Type.DinersClub;
			
		if (checkCard(number, 16, "6011"))
			return Type.Discover;
		
		if (checkCard(number, 15, "2014", "2149"))
			return Type.enRoute;
		
		if (checkCard(number, 16, "3088","3096","3112","3158","3337","3528"))
			return Type.JCB;
		
		if (checkCard(number, 16, "51", "52", "53", "54", "55"))
			return Type.MasterCard;
		
		if (checkCard(number, 13, "4") || checkCard(number, 16, "4"))
			return Type.VISA;
		
		return null;
	}
	
	private static boolean checkCard(String number, int length, String...prefixes)
	{
		if (number.length() != length)
			return false;
		
		for (String prefix : prefixes)
			if (number.startsWith(prefix))
				return true;
		
		return false;
	}
}
