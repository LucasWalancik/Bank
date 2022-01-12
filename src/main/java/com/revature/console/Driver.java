package com.revature.console;

import com.revature.models.Bank;

public class Driver
{
	public static void main( String[] args )
	{
		Bank LWBank = new Bank();
		int userOption = LWBank.mainMenu();
		switch( userOption )
		{
		case 1:
			LWBank.signUp();
			break;
			
		case 2:
			//LWBank.sigIn();
			break;
			
		case 3:
			return;
		}
	}

}
