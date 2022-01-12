package com.revature.models;

import java.util.Scanner;

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
	
	
	
	public void signIn()
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
			loggedInUser = validateCredentials( username, password );
			if( loggedInUser == null )
			{
				System.out.println();
				System.out.println( "###############################################" );
				System.out.println( "Your username and password do not match." );
				System.out.println( "Please try again" );
				System.out.println( "###############################################" );
				System.out.println();
			}
		}while( loggedInUser == null );
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("----------------------------------------------------------");
		System.out.println( "Welcome back " + loggedInUser.username );
		System.out.println("----------------------------------------------------------");
		System.out.println();
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
