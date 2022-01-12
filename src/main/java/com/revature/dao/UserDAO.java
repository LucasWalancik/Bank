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
	
	public static User signIn( String username, String password )
	{
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
