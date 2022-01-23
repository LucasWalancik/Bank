package com.revature.console;

import com.revature.controllers.AccountController;
import com.revature.controllers.CustomerController;
import com.revature.controllers.EmployeeController;
import com.revature.models.Bank;
import com.revature.models.User;

import io.javalin.Javalin;

public class Driver
{
	public static void main( String[] args )
	{
		Javalin app = Javalin.create().start(7070);
		CustomerController cc = new CustomerController( app );
		AccountController ac = new AccountController( app );
		EmployeeController ec = new EmployeeController( app );
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
				LWBank.employeeMenu();
				break;
				
			case 999:
				LWBank.adminMenu();
				break;
			}
			break;
			
		case 3:
			return;
		}
		System.out.println( "Goodbye" );
	}

}
