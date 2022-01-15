package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.User;

public class UserDAO
{
	public static boolean isUsernameTaken( String username )
	{
		try 
		{
			Connection c = ConnectionManager.getConnection();
			PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM users WHERE username = ?");
			preparedStatement.setString(1, username);
			ResultSet results = preparedStatement.executeQuery();
			
			if (results.next())
			{
				return true;
			}
			return false;
			
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	public static boolean doesCustomerExist( int userId )
	{
		Connection c = ConnectionManager.getConnection();
		PreparedStatement checkIfUserExists;
		try {
			checkIfUserExists = c.prepareStatement( "SELECT * FROM users WHERE id = ? and type = 1" );
			checkIfUserExists.setInt( 1, userId );
			ResultSet results = checkIfUserExists.executeQuery();
			return results.next();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static User validateCredentials( String usernameToValidate, String passwordToValidate )
	{
		try 
		{
			Connection c = ConnectionManager.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = c.prepareStatement("SELECT * FROM users ");
			ResultSet results = preparedStatement.executeQuery();
			// ResultSet cursor is set BEFORE the first row!!!!!!!!!!!!
			boolean isThereAnyResult = results.next();
			if(  isThereAnyResult == false )// There are no more rows
			{
				return null;
			}
			else
			{
				String username = results.getString( "username" );
				String password = results.getString( "password" );
				int type = results.getInt( "type" );
				int id = results.getInt( "id" );
				User loggedInUser = new User( username, password, type, id );
				return loggedInUser;
			}
		}
		catch (SQLException e)
		{
			System.out.println( "Something terrible happened with database while validating credentials" );
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	public static void signUp( String username, String password, int type )
	{

		try 
		{
			Connection c = ConnectionManager.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = c.prepareStatement("INSERT INTO users( type, username, password ) VALUES "
					+ "(?,?,?)");
			preparedStatement.setInt( 1, type );
			preparedStatement.setString( 2, username );
			preparedStatement.setString( 3, password );
			int howManyRows = preparedStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}
}
