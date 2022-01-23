package com.revature.controllers;

import java.util.ArrayList;

import com.revature.dao.AccountDAO;
import com.revature.dao.UserDAO;
import com.revature.models.Account;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class AccountController
{
	Javalin app;
	
	public AccountController( Javalin app )
	{
		this.app = app;
		
		app.get( "/accounts", getEveryAccount );
		app.get( "/accounts/{id}", getOneAccount );
		app.post( "/accounts{owner_id}", createPersonalAccount );
		app.post( "/accounts/{first_id}/{second_id}", createJointAccount );
		app.put( "/accounts/{account_id}", updateAccount );
		app.delete( "/accounts/{account_id}", deleteAccount );
	}
	
	public Handler deleteAccount = ctx ->
	{
		try
		{
			int ID = Integer.parseInt( ctx.pathParam( "account_id" ) );
			Account accountToDelete = AccountDAO.getAccountByID( ID );
			if( null == accountToDelete )
			{
				ctx.result( "There is no such account" );
				ctx.status( 404 );
			}
			else
			{
				boolean success = AccountDAO.deleteAccount( accountToDelete );
				if( success )
				{
					ctx.result( "Account was deleted" );
					ctx.status( 200 );
				}
				else
				{
					ctx.result( "Account could not be deleted" );
					ctx.status( 500 );
				}
			}
		}
		catch( NumberFormatException e )
		{
			ctx.result( "ID has to be a number" );
			ctx.status( 404 );
		}
	};
	
	public Handler updateAccount = ctx ->
	{
		try
		{
			int ID = Integer.parseInt( ctx.pathParam( "account_id" ) );
			Account toUpdate = ctx.bodyAsClass( Account.class );
			String newName = toUpdate.getName();
			if( null == newName )
			{
				throw new Exception( "Name is null" );
			}
			
			Account oldAccount = AccountDAO.getAccountByID( ID );
			if( null == oldAccount )
			{
				ctx.result( "There is no such account" );
				ctx.status( 404 );
			}
			else
			{
				boolean success = AccountDAO.updateAccount( ID, newName );
				if( success )
				{
					ctx.result( "Account was updated" );
					ctx.status( 200 );
				}
				else
				{
					ctx.result( "Account could not be updated" );
					ctx.status( 500 );
				}
			}
		}
		catch( NumberFormatException e )
		{
			ctx.result( "Account ID has to be a number" );
			ctx.status( 404 );
		}
		catch( Exception e )
		{
			ctx.result( "You have to include NAME parameter" );
			ctx.status( 404 );
		}
	};

	
	
	public Handler createJointAccount = ctx ->
	{
		try
		{
			Account newAccount = ctx.bodyAsClass( Account.class );
			if( null == newAccount.getName() )
			{
				throw new Exception( "Account name is null" );
			}
			int firstID = Integer.parseInt( ctx.pathParam( "first_id" ) );
			int secondID = Integer.parseInt( ctx.pathParam( "second_id" ) );
			System.out.println( "First: " + firstID + "x" );
			System.out.println( "Second: " + secondID + "x" );
			
			boolean firstExists = UserDAO.doesCustomerExist( firstID );
			boolean secondExists = UserDAO.doesCustomerExist( secondID );
			if( firstExists && secondExists )
			{
				boolean success = AccountDAO.saveAccount( newAccount, firstID, secondID );
				if( success )
				{
					ctx.result( "Account was created" );
					ctx.status( 201 );
				}
				else
				{
					ctx.result( "Account could not be created" );
					ctx.status( 500 );
				}
			}
			else
			{
				ctx.result( "At least one of your users does not exist" );
				ctx.status( 404 );
			}
		}
		catch( NumberFormatException e )
		{
			ctx.result( "Owner ID has to be a number" );
			ctx.status( 404 );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			ctx.result( "To create an account, you need a name " );
			ctx.status( 404 );
		}
	};
	
	
	public Handler createPersonalAccount = ctx ->
	{
		try
		{
			Account newAccount = ctx.bodyAsClass( Account.class );
			if( null == newAccount.getName() )
			{
				throw new Exception( "Account name is null" );
			}
			int ownerID = Integer.parseInt( ctx.pathParam( "owner_id" ) );
			newAccount.setFunds( 0 );
			boolean success = AccountDAO.saveAccount( newAccount, ownerID );
			if( success )
			{
				ctx.result( "Account was created" );
				ctx.status( 201 );
			}
			else
			{
				ctx.result( "Account could not be created" );
				ctx.status( 500 );
			}
		}
		catch( NumberFormatException e )
		{
			ctx.result( "Owner ID has to be a number" );
			ctx.status( 404 );
		}
		catch( Exception e )
		{
			ctx.result( "To create an account, you need a name " );
			ctx.status( 404 );
		}
	};
	
	public Handler getOneAccount = ctx ->
	{
		try
		{
			int accountID = Integer.parseInt( ctx.pathParam( "id" ) );
			Account account = AccountDAO.getAccountByID( accountID );
			if( null == account )
			{
				ctx.result( "There is no such account" );
				ctx.status( 404 );
			}
			else
			{
				ctx.json( account );
				ctx.status( 200 );
			}
		}
		catch( NumberFormatException e )
		{
			ctx.result( "Account ID must be a number" );
			ctx.status( 404 );
		}
		
	};
	
	public Handler getEveryAccount = ctx ->
	{
		ArrayList<Account> acceptedAccounts = AccountDAO.getAcceptedAccounts();
		ArrayList<Account> notAcceptedAccounts = AccountDAO.getNotAcceptedAccounts();
		acceptedAccounts.addAll( notAcceptedAccounts);
		ctx.json( acceptedAccounts );
	};
	
}
