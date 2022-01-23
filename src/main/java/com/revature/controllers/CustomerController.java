package com.revature.controllers;

import java.sql.Connection;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.dao.ConnectionManager;
import com.revature.dao.UserDAO;
import com.revature.models.User;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class CustomerController
{
	Javalin app;
	
	public CustomerController( Javalin app ) 
	{
		this.app = app;
		
		app.get( "/customers/{username}", getCustomerByUsername );
		app.get( "/customers", getCustomersList );
		app.delete( "/customers/{username}", deleteCustomer );
		app.post( "/customers", createNewCustomer );
		app.put( "/customers/{ID}", updateCustomer );
	}
	
	
	public Handler updateCustomer = ctx -> {
		try
		{
			User customerToUpdate = ctx.bodyAsClass( User.class );
			String username = customerToUpdate.getUsername();
			String password = customerToUpdate.getPassword();
			int ID = Integer.parseInt( ctx.pathParam( "ID" ) );
			User userToUpdate = UserDAO.getUserByID( ID );
			if( null == userToUpdate )
			{
				ctx.result( "There is no such user" );
				ctx.status( 404 );
			}
			else
			{
				boolean success = UserDAO.updateUser( ID, username, password );
				if( success )
				{
					ctx.result( "User was updated" );
					ctx.status( 202 );
				}
				else
				{
					ctx.result( "User could not be updated" );
					ctx.status( 500 );
				}
			}
		}
		catch( NumberFormatException e )
		{
			ctx.result( "ID must be a number" );
			ctx.status( 404 );
		}
		catch( Exception e )
		{
			
		}
	};
	
	
	public Handler createNewCustomer = ctx -> {
		try
		{
			User newCustomer = ctx.bodyAsClass( User.class );
			String username = newCustomer.getUsername();
			String password = newCustomer.getPassword();
			boolean userExists = UserDAO.isUsernameTaken( username );
			if( userExists )
			{
				ctx.result( "User" + username + " already exists" );
				ctx.status( 404 );
			}
			else
			{
				int userId = UserDAO.signUp( username, password, 1 );
				if( -1 == userId )
				{
					ctx.result( "User " + username + " was not created" );
					ctx.status( 500 );
				}
				else
				{
					ctx.result( "User " + username + " was created" );
					ctx.status( 201 );
				}
			}
		}
		catch( Exception e )
		{
			ctx.result( "To create user, you have to pass Username and Password" );
			ctx.status( 404 );
		}

	};
	
	public Handler deleteCustomer = ctx -> {
		String username = ctx.pathParam( "username" );
		boolean userExists = UserDAO.isUsernameTaken( username );
		if( userExists )
		{
			boolean removalWasSuccessful = UserDAO.removeUser( username, 1 );
			if( removalWasSuccessful )
			{
				ctx.result( "User " + username + " was deleted" );
				ctx.status( 200 );
			}
			else
			{
				ctx.result( "User was not deleted" );
				ctx.status( 500 );
			}
		}
		else
		{
			ctx.result( "User " + username + " does not exist" );
			ctx.status( 404 );
		}
	};
	
	
	public Handler getCustomersList = ctx -> {
		
		ArrayList<User> customers = UserDAO.getEveryCustomer();
		if( null == customers )
		{
			ctx.result( "404 No customers in my database" );
			ctx.status( 404 );
		}
		else
		{
			ctx.json( customers );
			ctx.status( 200 );
		}
	};
	
	
	public Handler getCustomerByUsername = ctx -> {
		User customer = UserDAO.getUserByUsername( ctx.pathParam("username"), 1 );
		if( null == customer )
		{
			ctx.result( "404, No user found" );
			ctx.status( 404 );
			//Do something when there is no user
		}
		else
		{
			ctx.json( customer );
			ctx.status(200);
		}

	};

}
