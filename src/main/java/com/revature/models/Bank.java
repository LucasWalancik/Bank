package com.revature.models;

import java.util.ArrayList;
import java.util.Scanner;

import com.revature.dao.AccountDAO;
import com.revature.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Bank 
{
	private static final Logger logger = LogManager.getLogger( Bank.class );
	private int numberOfClients;
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
	
	
	private int getId( int ownID )
	{
		int id = -1;
		do {
			System.out.println( "Please enter an id (0 to exit): " );
			while( !userInput.hasNextInt() )
			{
				userInput.next();
				printSomethingBad( "A valid id is an integer, not whatever that is" );
			}
			id = userInput.nextInt();
			userInput.nextLine();
			if( id < 0) 
			{
				printSomethingBad( "A valid id has to be greater than 0" );
			}
			if( id == ownID )
			{
				printSomethingBad( "Second ID can not be the same as yours" );
			}
			
		}while( id < 0 || id == ownID );
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
		while( true )
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
				return;
			}
		}

	}
	
	
	
	public void employeeMenu()
	{
		while( true )
		{
			printMenu( "LWBank Employee Dashboard" );
			System.out.println( "1. View all customer accounts" );
			System.out.println( "2. Manage non accepted bank accounts" );
			System.out.println( "3. Exit" );
			int employeeOption = getMenuOption( 4 );
			switch( employeeOption )
			{
			case 1:
				showAllCustomerAccounts();
				break;
				
			case 2:
				showNotAcceptedBankAccounts();
				break;
				
			case 3:
				return;
			}
		}
	}
	
	
	public void adminMenu()
	{
		while( true )
		{
			printMenu( "LWBank Admin Dashboard" );
			System.out.println( "1. View every accepted bank account" );
			System.out.println( "2. View every non accepted bank account" );
			System.out.println( "3. Exit" );
			int employeeOption = getMenuOption( 4 );
			switch( employeeOption )
			{
			case 1:
				showAcceptedBankAccounts();
				break;
				
			case 2:
				showNotAcceptedBankAccounts();
				break;
				
			case 3:
				return;
			}
		}
	}
	
	
	
	public void showAcceptedBankAccounts()
	{
		printMenu( "Managing INACTIVE accounts" );
		ArrayList<Account> acceptedAccounts = AccountDAO.getAcceptedAccounts();
		if( null == acceptedAccounts )
		{
			printSomethingBad( "There is no accepted account" );
			return;
		}
		
		int accountCounter = 0;
		for( Account account : acceptedAccounts )
		{
			System.out.println( ++accountCounter + ". " + account.getName() );
		}
		
		System.out.println();
		System.out.println(++accountCounter + ". Exit");
		int employeeOption = getMenuOption(accountCounter);
		if ( employeeOption == accountCounter)
		{
			return;
		}
		
		Account chosenAccount = acceptedAccounts.get( employeeOption - 1);
		adminAccountMenu( chosenAccount );
	}
	
	
	
	public void adminAccountMenu( Account account )
	{
		ArrayList<User> accountOwners = AccountDAO.getAccountOwners( account );
		if( null == accountOwners )
		{
			printSomethingBad( "This account has NO owners!" );
		}
		else
		{
			if( 1 == accountOwners.size() )
			{
				printMenu( "Editing Personal Account" );
				System.out.println( "| Account Owner: " + accountOwners.get( 0 ).getUsername() + " |" );
			}
			else
			{
				printMenu( "Editing Joint Account" );
				System.out.println( "| First Account Owner: " + accountOwners.get( 0 ).getUsername() + " |" );
				System.out.println( "| Second Account Owner: " + accountOwners.get( 1 ).getUsername() + " |" );	
			}
			System.out.println( "| Account Name: " + account.getName() + " |" );
			System.out.println( "| Account ID: " + account.getId() + " |" );
			System.out.println( "| Account Funds: $" + account.getFunds() + " |" );
			System.out.println();
			System.out.println( "1. Deactivate account" );
			System.out.println( "2. Withdraw money" );
			System.out.println( "3. Deposit money" );
			System.out.println( "4. Tranfser money" );
			System.out.println( "5. Go back" );
			
			int adminOption = getMenuOption( 5 );
			switch( adminOption )
			{
			case 1:
				AccountDAO.deactivateAccount( account );
				break;
				
			case 2:
				withdraw( account );
				break;
				
			case 3:
				deposit( account );
				break;
				
			case 4:
				transfer( account );
				break;
				
			case 5:
				return;
			}
		}
	}
	
	public void showAllCustomerAccounts()
	{
		printMenu( "Viewing customer accounts" );
		ArrayList<User> customerAccounts = UserDAO.getEveryCustomer();
		if( null == customerAccounts )
		{
			System.out.println( "There are no customers" );
		}
		else
		{
			int customerCounter = 0;
			for( User customer : customerAccounts )
			{
				System.out.println( ++customerCounter + ". " + customer.getUsername() );
			}
			System.out.println();
			System.out.println(++customerCounter + ". Exit");
			int employeeOption = getMenuOption( customerCounter );
			if ( employeeOption == customerCounter )
			{
				return;
			}
			User chosenCustomer = customerAccounts.get( employeeOption -1 );
			showCustomerDetails( chosenCustomer );
		}
	}
	
	private void showCustomerDetails( User customer )
	{
		printMenu( "Customer Details" );
		System.out.println( "| Username: " + customer.getUsername() + " |" );
		System.out.println( "| Password: " + customer.getPassword() + " |" );
		System.out.println( "| ID: " + customer.getId() + " |" );

		ArrayList<Account> customerAccounts = AccountDAO.getUserAccounts( customer.getId() );
		if( null == customerAccounts )
		{
			System.out.println();
			System.out.println( "This customer has no bank accounts" );
		}
		else
		{
			System.out.println();
			System.out.println( "Customer bank accounts: " );
			int accountCounter = 0;
			for( Account customerAccount : customerAccounts )
			{
				System.out.println( ++accountCounter + ". " + customerAccount );
			}
		}
	}
	
	
	public void showNotAcceptedBankAccounts()
	{
		printMenu( "Managing INACTIVE accounts" );
		ArrayList<Account> notAcceptedAccounts = AccountDAO.getNotAcceptedAccounts();
		if( null == notAcceptedAccounts )
		{
			printSomethingBad( "Every account is accepted" );
			return;
		}
		
		int accountCounter = 0;
		for( Account account : notAcceptedAccounts )
		{
			System.out.println( ++accountCounter + ". " + account.getName() );
		}
		
		System.out.println();
		System.out.println(++accountCounter + ". Exit");
		int employeeOption = getMenuOption(accountCounter);
		if ( employeeOption == accountCounter)
		{
			return;
		}
		
		Account chosenAccount = notAcceptedAccounts.get( employeeOption - 1);
		employeeAccountMenu( chosenAccount );
	}
	
	
	private void employeeAccountMenu( Account account )
	{		
		ArrayList<User> accountOwners = AccountDAO.getAccountOwners( account );
		if( null == accountOwners )
		{
			printSomethingBad( "This account has NO owners!" );
		}
		else
		{
			if( 1 == accountOwners.size() )
			{
				printMenu( "Activating Personal Account" );
				System.out.println( "| Account Owner: " + accountOwners.get( 0 ).getUsername() + " |" );
			}
			else
			{
				printMenu( "Activating Joint Account" );
				System.out.println( "| First Account Owner: " + accountOwners.get( 0 ).getUsername() + " |" );
				System.out.println( "| Second Account Owner: " + accountOwners.get( 1 ).getUsername() + " |" );	
			}
			System.out.println( "| Account Name: " + account.getName() + " |" );
			System.out.println( "| Account ID: " + account.getId() + " |" );
			System.out.println();
			System.out.println( "1. Activate account" );
			System.out.println( "2. Reject application(DELETE ACCOUNT)" );
			System.out.println( "3. Go back" );
			int employeeOption = getMenuOption( 3 );
			switch( employeeOption )
			{
			case 1:
				AccountDAO.activateAccount( account );
				break;
				
			case 2:
				AccountDAO.deleteAccount( account );
				break;
				
			case 3:
				break;
			}
		}
		
	}
	
	
	
	private void applyForAccount()
	{
		while( true )
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
				return;
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
			secondOwnerId = getId( loggedInUser.getId() );
			if( secondOwnerId == 0 )
			{
				return;
			}
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
		while( true )
		{
			ArrayList<Account> userAccounts = AccountDAO.getUserAccounts(loggedInUser.getId());
			if (userAccounts == null) {
				printSomethingBad("You have no accounts");
				return;
			}
			printMenu("List of your accounts");
			int accountCounter = 0;
			for (Account userAccount : userAccounts) {
				System.out.println(++accountCounter + ". " + userAccount);
			}
			System.out.println();
			System.out.println(++accountCounter + ". Exit");
			int userOption = getMenuOption(accountCounter);
			if (userOption == accountCounter) {
				return;
			}
			Account chosenAccount = userAccounts.get(userOption - 1);
			customerAccountMenu( chosenAccount );
		}
	}
	
	
	private void customerAccountMenu( Account account )
	{
		if( account.getIsAccepted() == 0 )
		{
			printSomethingBad( "This account is not active ");
			return;
		}
		while( true )
		{
			printMenu( account.toString() );
			System.out.println( "1. Deposit money" );
			System.out.println( "2. Withdraw money" );
			System.out.println( "3. Transfer money" );
			System.out.println( "4. Return to accounts list" );
			int userOption = getMenuOption( 4 );
			switch( userOption )
			{
			case 1:
				deposit( account );
				break;
				
			case 2:
				withdraw( account );
				break;
				
			case 3:
				transfer( account );
				break;
				
			case 4:
				return;
			}	
		}
		
	}
	
	
	private void transfer( Account account )
	{	
		boolean isAccountAccepted = false;
		while( ! isAccountAccepted )
		{
			printMenu( "Tranfser" );
			int secondAccountId = getId( account.getId() );
			if( secondAccountId == 0 )
			{
				return;
			}
			isAccountAccepted = AccountDAO.isAccountAccepted( secondAccountId );
			
			if( ! isAccountAccepted )
			{
				printSomethingBad( "This account is not accepted or does not exist" );
			}
			else
			{
				double transferAmount = getMoney( account.getFunds() );
				if( transferAmount == 0 )
				{
					return;
				}
				double prevAmount = account.getFunds();
				account.setFunds( prevAmount - transferAmount );
				AccountDAO.setAccountFunds( account.getFunds(), account.getId() );
				AccountDAO.setAccountFunds( transferAmount, secondAccountId );
				printSomethingGood( "$" + transferAmount + " have been tranfsered" );
				logger.debug( loggedInUser.getUsername() + " transfered $" + transferAmount + " from " + account.getName() + " to account which ID is: " + secondAccountId );
				return;
			}
		}
	}
	
	
	
	private void withdraw( Account account )
	{
		printMenu( "Withdrawal" );
		double amount = getMoney( account.getFunds() );
		if( amount == 0 )
		{
			return;
		}
		double prevAmount = account.getFunds();
		account.setFunds( prevAmount - amount );
		if( ! AccountDAO.setAccountFunds( account.getFunds(), account.getId() ) )
		{
			printSomethingBad( "Something went wrong. Please try again" );
		}
		else
		{
			printSomethingGood( "The money withdrawal was successful" );
			logger.debug( loggedInUser.getUsername() + " withdrew $" + amount + " from " + account.getName() + " account." );
		}
	}
	
	
	private void deposit( Account account )
	{
		printMenu( "Depositing" );
		double amount = getMoney( Double.POSITIVE_INFINITY );
		if( 0 == amount )
		{
			return;
		}
		double prevAmount = account.getFunds();
		account.setFunds( prevAmount + amount );
		if( ! AccountDAO.setAccountFunds( account.getFunds(), account.getId() ) )
		{
			printSomethingBad( "Something went wrong. Please try again" );
		}
		else
		{
			printSomethingGood( "The money deposit was successful" );
			logger.debug( loggedInUser.getUsername() + " deposited $" + amount + " to " + account.getName() + " account." );
		}
	}
	
	private double getMoney( double max )
	{
		double amount = 0;
		do {
			System.out.println( "Please enter the desired amount(0 for exit):" );
			while( ! userInput.hasNextDouble() )
			{
				userInput.next();
				System.out.println( "Your input has to be a number" );
			}
			amount = userInput.nextDouble();
			userInput.nextLine();
			if( amount > max )
			{
				printSomethingBad( "You do not have that much funds" );
			}
			if( amount < 0 )
			{
				printSomethingBad( "Your desired amount has to be positive" );
			}
		}while( amount < 0 || amount > max );
		
		return amount;
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
