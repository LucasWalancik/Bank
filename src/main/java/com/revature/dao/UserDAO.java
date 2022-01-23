package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.revature.models.User;

public class UserDAO
{	
	
	
	public static User getUserByID( int ID )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT * FROM users WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setInt( 1, ID );
			ResultSet results = statement.executeQuery();
			if( ! results.next() )
			{
				return null;
			}
			else
			{
				String username = results.getString( "username" );
				String password = results.getString( "password" );
				int type = results.getInt( "type" );
				User user = new User( username, password, type, ID );
				return user;
			}
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return null;
		}
	}
	public static boolean updateUser( int ID, String username, String password )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setString( 1 , username );
			statement.setString( 2 , password );
			statement.setInt( 3 , ID );
			int numberOfUpdatedRows = statement.executeUpdate();
			if( 1 == numberOfUpdatedRows )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static User getUserByUsername( String username, int type )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT * FROM users WHERE username = ? AND type = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setString( 1, username );
			statement.setInt( 2, type );
			
			ResultSet results = statement.executeQuery();
			if( ! results.next() )
			{
				return null;
			}

			
			int id = results.getInt( "id" );			
			String password = results.getString( "password" );
			User user = new User( username, password, type, id );
			return user;
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static ArrayList<User> getEveryCustomer()
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT * FROM users WHERE type = 1";
			PreparedStatement statement = c.prepareStatement( sql );
			ResultSet results = statement.executeQuery();
			if( ! results.next() )
			{
				return null;
			}
			ArrayList<User> customers = new ArrayList<User>();
			do
			{
				int id = results.getInt( "id" );
				int type = results.getInt( "type" );
				String username = results.getString( "username" );
				String password = results.getString( "password" );
				User customer = new User( username, password, type, id );
				customers.add( customer );
			}while( results.next() );
			return customers;
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return null;
		}
	}
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
			preparedStatement = c.prepareStatement("SELECT * FROM users WHERE username LIKE ? and password LIKE ?");
			preparedStatement.setString( 1, usernameToValidate );
			preparedStatement.setString( 2, passwordToValidate );
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
	
	
	public static boolean removeUser( String username, int type )
	{
		try 
		{
			Connection c = ConnectionManager.getConnection();
			PreparedStatement preparedStatement;
			preparedStatement = c.prepareStatement( "DELETE FROM users WHERE username LIKE ? AND type = ?" );
			preparedStatement.setString( 1, username );
			preparedStatement.setInt( 2, type );
			int numberOfDeletedRows = preparedStatement.executeUpdate();
			if( 1 == numberOfDeletedRows )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}	
	}
	
	
	public static int signUp( String username, String password, int type )
	{
		try 
		{
			Connection c = ConnectionManager.getConnection();
			PreparedStatement preparedStatement;
			String sql = "INSERT INTO users( type, username, password ) VALUES(?,?,?)";
			preparedStatement = c.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			preparedStatement.setInt( 1, type );
			preparedStatement.setString( 2, username );
			preparedStatement.setString( 3, password );
			int howManyRowsWereInserted = preparedStatement.executeUpdate();
			
			if( 1 == howManyRowsWereInserted )
			{
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				generatedKeys.next();
				int accountId = generatedKeys.getInt( 1 );
				return accountId;
			}
			else
			{
				return -1;
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
}
