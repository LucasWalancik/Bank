package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.revature.models.Account;

public class AccountDAO
{
	
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
					Account userAccount = new Account( accountName, funds, isAccepted, accountId );
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
