package com.revature.models;

import java.util.ArrayList;
import java.util.Scanner;

import com.revature.dao.AccountDAO;
import com.revature.dao.UserDAO;



public class Bank 
{
	private int nubmerOfClients;
	Scanner userInput;
	User loggedInUser;
	
	public Bank()
	{
		userInput = new Scanner( System.in );
	}
	
	public int mainMenu()
	{
		printMenu( "Welcome to the LW Bank", "No Clients trusted us so far" );
		System.out.println( "1.Sing up" );
		System.out.println( "2.Sign in" );
		System.out.println( "3.Exit" );
		
		int userOption = getMenuOption( 3 );
		return userOption;
	}
	
	
	private int getId()
	{
		int id = -1;
		do {
			System.out.println( "Please enter an id: " );
			while( !userInput.hasNextInt() )
			{
				userInput.next();
				printSomethingBad( "A valid id is an integer, not whatever that is" );
			}
			id = userInput.nextInt();
			userInput.nextLine();
			if( id <= 0) 
			{
				printSomethingBad( "A valid id has to be greater than 0" );
			}
		}while( id <= 0 );
		return id;
	}
	
	private int getMenuOption( int highestOption )
	{
		int chosenOption = 0;
		do {
			System.out.println( "Please enter one of the available options" );
			while( ! userInput.hasNextInt() )
			{
				userInput.next();
				System.out.println( "Please enter one of the available options" );
			}
			chosenOption = userInput.nextInt();
			userInput.nextLine();
			
		}while( chosenOption < 1 || chosenOption > highestOption );
		
		return chosenOption;
	}

	
	public void signUp()
	{
		printMenu( "Registration Form" );
		String username = getUsername();
		String password = getPassword();
		UserDAO.signUp(username, password, 1 );
		System.out.println();
		System.out.println();
		System.out.println( "Thank you for registering in our bank" );
	}
	
	
	
	public User signIn()
	{
		printMenu( "Logging in" );
		String username = "";
		loggedInUser = null;
		do
		{
			System.out.println( "Enter your username" );
			username = userInput.nextLine();
			System.out.println( "Enter your password" );
			String password = userInput.nextLine();
			loggedInUser = UserDAO.validateCredentials( username, password );
			if( loggedInUser == null )
			{
				printSomethingBad( "Your username and password do not match.", "Please try again" );
			}
		}while( loggedInUser == null );
		
		//printMenu( "Welcome back " + loggedInUser.getUsername() );
		return loggedInUser;
	}
	
	
	
	
	private String getUsername()
	{
		String username = "";
		boolean isSure = false;
		
		while( ! isSure )
		{
			System.out.println( "Enter your username: " );	
			username = userInput.nextLine();
			
			System.out.println( "Your chosen username is: #" + username + "#" );
			System.out.println( "Please re-enter your username to cofirm it:" );
			String confirmation = userInput.nextLine();
			
			if( confirmation.equals( username ) )
			{
				if( ! validateUsername( username  ) )
				{
					printSomethingBad( "Username can not contain \\':\\', \\' \\', or \\\" " );
				}
				else if( UserDAO.isUsernameTaken( username ) )
				{
					printSomethingBad( "This username is taken" );
				}
				else
				{
					isSure = true;
				}
			}
			else
			{
				printSomethingBad( "These usernames do not match" );
			}
			
			System.out.println();
		}
		
		return username;
	}
	
	
	
	private String getPassword()
	{
		String password = "";
		boolean isSure = false;
		
		while( ! isSure )
		{
			System.out.println( "Enter your password: " );	
			password = userInput.nextLine();
			
			System.out.println( "Your chosen passwrod is: #" + password + "#" );
			System.out.println( "Please re enter your username to cofirm it:" );
			
			String confirmation = userInput.nextLine();
			if( confirmation.equals( password ) )
			{
				isSure = true;
			}
			else
			{
				printSomethingBad( "Password do not match" );
			}
		}
		return password;
	}
	
	public void customerMenu()
	{
		printMenu( "| Account Details |", "| " + loggedInUser + " |" );
		System.out.println( "1. See your accounts" );
		System.out.println( "2. Apply for new account" );
		System.out.println( "3. Exit" );
		int userOption = getMenuOption( 3 );
		switch( userOption )
		{
		case 1:
			showCustomerAccounts();
			break;
			
		case 2:
			applyForAccount();
			break;
			
		case 3:
			System.exit( 0 );
		}
	}
	
	
	
	
	private void applyForAccount()
	{
		boolean userWantsToLeave = false;
		while( ! userWantsToLeave)
		{
			printMenu( "Applying for an account" );
			System.out.println( "1. Apply for personal account" );
			System.out.println( "2. Apply for joint account" );
			System.out.println( "3. Exit" );
			int userOption = getMenuOption( 3 );
			switch( userOption )
			{
			case 1:
				applyForPersonalAccount();
				break;
				
			case 2:
				applyForJointAccount();
				break;
				
			case 3:
				userWantsToLeave = true;
				break;
			}
		}
		
	}
	
	
	private void applyForPersonalAccount()
	{
		printMenu( "Applying for personal account" );
		System.out.println( "Enter name of your account: " );
		String accountName = userInput.nextLine();
		Account newAccount = new Account( accountName, 0, 0 );
		AccountDAO.saveAccount( newAccount, loggedInUser.getId() );
		System.out.println( "You have succesfully applied for a personal account" );
	}
	
	private void applyForJointAccount()
	{
		printMenu( "Applying for joint account" );
		System.out.println( "Enter name of your account: " );
		String accountName = userInput.nextLine();
		System.out.println( "Enter id of a customer that will be second owner of your bank account: " );
		int secondOwnerId;
		boolean secondOwnerExists = false;
		do
		{
			secondOwnerId = getId();
			secondOwnerExists = UserDAO.doesCustomerExist( secondOwnerId );
			if( ! secondOwnerExists )
			{
				printSomethingBad( "There is no customer with such id" );
			}
		} while( ! secondOwnerExists );

		Account newJointAccount = new Account( accountName, 0, 0 );
		AccountDAO.saveAccount( newJointAccount, loggedInUser.getId(), secondOwnerId );
		printSomethingGood( "You have succesfully applied for a joint account" );
		//System.out.println( "You have succesfully applied for a joint account" );
	}
	
	
	private void showCustomerAccounts()
	{
		ArrayList<Account> loggedInUserAccounts = AccountDAO.getUserAccounts( loggedInUser.getId() );
		if( loggedInUserAccounts == null )
		{
			printSomethingBad( "You have no accounts" );
			return;
		}
		for( Account userAccount : loggedInUserAccounts )
		{
			System.out.println( userAccount );
		}
	}
	
	private boolean validateUsername( String username )
	{
		boolean whatToReturn = true;
		if( username.contains( ":" ) )
		{
			whatToReturn = false;
		}
		if( username.contains( "\"" ) )
		{
			whatToReturn = false;
		}
		if( username.contains( " " ) )
		{
			whatToReturn = false;
		}
		return whatToReturn;
	}
	
	
	private void printSomethingBad( String...whatToPrint )
	{
		System.out.println();
		System.out.println( "###############################################" );
		for( String sentence : whatToPrint )
		{
			System.out.println( sentence );
		}
		System.out.println( "###############################################" );
		System.out.println();
	}
	
	private void printSomethingGood( String...whatToPrint )
	{
		System.out.println();
		System.out.println( "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" );
		for( String sentence : whatToPrint )
		{
			System.out.println( sentence );
		}
		System.out.println( "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" );
		System.out.println();
	}
	
	
	private void printMenu( String... whatToPrint )
	{
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("----------------------------------------------------------");
		for( String sentence : whatToPrint )
		{
			System.out.println( sentence );
		}
		System.out.println("----------------------------------------------------------");
		System.out.println();
	}
	
}
