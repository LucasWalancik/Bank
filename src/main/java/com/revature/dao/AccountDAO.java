package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.revature.models.Account;
import com.revature.models.User;

public class AccountDAO
{
	public static Account getAccountByID( int ID )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT * FROM accounts WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setInt( 1, ID );
			ResultSet results = statement.executeQuery();
			if( !results.next() )
			{
				return null;
			}
			else
			{
				String name = results.getString( "name" );
				int isAccepted = results.getInt( "is_accepted" );
				double funds = results.getDouble( "funds" );
				Account account = new Account( ID, name, funds, isAccepted );
				return account;
			}
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void deactivateAccount( Account account )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "UPDATE accounts ";
			sql += "SET is_accepted = 0 ";
			sql += "WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setInt( 1, account.getId() );
			statement.executeUpdate();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}
	
	
	
	public static void activateAccount( Account account )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "UPDATE accounts ";
			sql += "SET is_accepted = 1 ";
			sql += "WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setInt( 1, account.getId() );
			statement.executeUpdate();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}
	
	
	public static void deleteAccount( Account account )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "DELETE FROM accounts ";
			sql += "WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setInt( 1, account.getId() );
			statement.executeUpdate();
			
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}
	
	
	public static ArrayList<User> getAccountOwners( Account account )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT users.id, users.type, users.username, users.password ";
			sql += "FROM account_owners ";
			sql += "JOIN users ON account_owners.owner_id = users.id ";
			sql += "WHERE account_owners.account_id = ? ";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setInt( 1, account.getId() );
			ResultSet results = statement.executeQuery();
			
			if( ! results.next() )
			{
				return null;
			}
			
			ArrayList<User> accountOwners = new ArrayList<User>();
			do
			{
				int ID = results.getInt( 1 );
				int type = results.getInt( 2 );
				String username = results.getString( 3 );
				String password = results.getString( 4 );
				User accountOwner = new User( username, password, type, ID );
				accountOwners.add( accountOwner );
			}while( results.next() );
			return accountOwners;
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArrayList<Account> getAcceptedAccounts()
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT * FROM accounts WHERE is_accepted = 1";
			PreparedStatement statement = c.prepareStatement( sql );
			ResultSet results = statement.executeQuery();
			if( ! results.next() )
			{
				return null;
			}
			
			ArrayList<Account> allAccounts = new ArrayList<Account>();
			do
			{
				int ID = results.getInt( "id" );
				String name = results.getString( "name" );
				double funds = results.getDouble( "funds" );
				int is_accepted = results.getInt( "is_accepted" );
				Account account = new Account( ID, name, funds, is_accepted );
				allAccounts.add( account );
				
			}while( results.next() );
			return allAccounts;
		}
		catch( SQLException e )
		{
			return null;
		}
	}
	
	
	public static ArrayList<Account> getNotAcceptedAccounts()
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT * FROM accounts WHERE is_accepted = 0";
			PreparedStatement statement = c.prepareStatement( sql );
			ResultSet results = statement.executeQuery();
			if( ! results.next() )
			{
				return null;
			}
			
			ArrayList<Account> allAccounts = new ArrayList<Account>();
			do
			{
				int ID = results.getInt( "id" );
				String name = results.getString( "name" );
				double funds = results.getDouble( "funds" );
				int is_accepted = results.getInt( "is_accepted" );
				Account account = new Account( ID, name, funds, is_accepted );
				allAccounts.add( account );
				
			}while( results.next() );
			return allAccounts;
		}
		catch( SQLException e )
		{
			return null;
		}
	}
	
	public static boolean isAccountAccepted( int accountID )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "SELECT is_accepted FROM accounts WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setInt( 1, accountID );
			ResultSet results = statement.executeQuery();
			if( ! results.next() )
			{
				return false;
			}
			int is_accepted = results.getInt( "is_accepted" );
			if( is_accepted == 0 )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch( SQLException e )
		{
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static boolean setAccountFunds( double amount, int accountId )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String sql = "UPDATE accounts ";
			sql += "SET funds = ? ";
			sql += "WHERE id = ?";
			PreparedStatement statement = c.prepareStatement( sql );
			statement.setDouble( 1, amount );
			statement.setInt( 2, accountId );
			int numberOfUpdatedRows = statement.executeUpdate();
			if( numberOfUpdatedRows <= 0 )
			{
				System.out.println( "Something went wrong" );
				return false;
			}
			return true;
		}
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static ArrayList<Account> getUserAccounts( int userId )
	{
		try
		{
			Connection c = ConnectionManager.getConnection();
			String statement = "SELECT name, id, funds, is_accepted ";
			statement += "FROM accounts ";
			statement += "JOIN account_owners ON account_owners.account_id = accounts.id ";
			statement += "WHERE account_owners.owner_id = ?";
			PreparedStatement statementForUserAccounts;
			statementForUserAccounts = c.prepareStatement( statement );
			statementForUserAccounts.setInt( 1, userId );
			ResultSet results = statementForUserAccounts.executeQuery();
			if( results.next() == false )
			{
				return null;
			}
			else
			{
				ArrayList<Account> userAccounts = new ArrayList<Account>();
				do
				{
					String accountName = results.getString( "name" );
					int accountId = results.getInt( "id" );
					double funds = results.getDouble( "funds" );
					int isAccepted = results.getInt( "is_accepted" );
					Account userAccount = new Account( accountId, accountName, funds, isAccepted );
					userAccounts.add( userAccount );
				}while( results.next() );
				return userAccounts;
				
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void saveAccount( Account accountToSave, int...ownerIds )
	{
		try 
		{
			Connection c = ConnectionManager.getConnection();
			PreparedStatement preparedStatement;
			String insertSQL = "INSERT INTO accounts( name, funds, is_accepted ) VALUES (?,?,?)";
			preparedStatement = c.prepareStatement( insertSQL, Statement.RETURN_GENERATED_KEYS );

			preparedStatement.setString( 1, accountToSave.getName() );
			preparedStatement.setDouble( 2, accountToSave.getFunds() );
			preparedStatement.setInt( 3, accountToSave.getIsAccepted() );
			
			int howManyRows = preparedStatement.executeUpdate();
			ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
			generatedKeys.next();
			int accountId = generatedKeys.getInt( 1 );
			for( int ownerId : ownerIds )
			{
				preparedStatement = c.prepareStatement("INSERT INTO account_owners( owner_id, account_id ) VALUES "
						+ "(?,?)");
				preparedStatement.setInt( 1, ownerId );
				preparedStatement.setInt( 2, accountId );
				preparedStatement.executeUpdate();
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
