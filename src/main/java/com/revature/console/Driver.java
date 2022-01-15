package com.revature.console;

import com.revature.models.Bank;
import com.revature.models.User;

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
			User loggedInUser = LWBank.signIn();
			int userType = loggedInUser.getType();
			switch( userType )
			{
			case 1:
				LWBank.customerMenu();
				break;
				
			case 2:
				//LWBank.employeeMenu();
				break;
				
			case 999:
				//LWBank.adminMenu();
				break;
			}
			break;
			
		case 3:
			return;
		}
	}

}
