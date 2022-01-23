package com.revature.controllers;

import java.sql.Connection;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dao.AccountDAO;
import com.revature.dao.ConnectionManager;
import com.revature.dao.UserDAO;
import com.revature.models.Account;
import com.revature.models.User;

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
	}

	
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
