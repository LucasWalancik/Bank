package com.revature.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.revature.dao.UserDAO;
import com.revature.models.User;

public class Tester {

	@Test
	public void signInTest()
	{
		String[] usernames = { "admin", "Walancik", "Emp_Walancik", "Allison" };
		String[] passwords = { "password", "123", "123", "meowmeow" };
		int[] ids = { 7, 1, 2, 3 };
		int[] types = { 999, 1, 2, 1 };
		int howManyUsers = usernames.length;
		for( int i = 0; i < howManyUsers; i++ )
		{
			User madeByHand = new User( usernames[ i ], passwords[ i ], types[ i ], ids[ i ] );
			User fromSignIn = UserDAO.validateCredentials( usernames[ i ], passwords[ i ] );
			boolean areTheyTheSame = madeByHand.equals( fromSignIn );
			assertTrue( areTheyTheSame );
		}
		
		//User adminUser = new User( "admin", "password", 999, 7 );
		//User adminUserToCheck = UserDAO.validateCredentials( "admin", "password" );
		//System.out.println( "XXX: " + adminUser.equals( adminUserToCheck ) + " XXX" );
		//assertTrue( adminUser.equals( adminUserToCheck ) );
		//assertEquals( adminUser, adminUserToCheck );
	}
	
	
	@Test
	public void signUpWithExistingUsernameTest()
	{
		String username = "admin";
		String password = "password";
		int type = 999;
		int didItWork = UserDAO.signUp(username, password, type);
		assertTrue( didItWork == -1 );
	}
	
	
	
	@Test
	public void signUpTest()
	{
		String username = "testUsernameJustForTesting";
		String password = "testPassword";
		int type = 999;
		UserDAO.removeUser( username, type );
		int testUserId = UserDAO.signUp( username, password, type );
		User testUserMadeByMe = new User( username, password, type, testUserId );
		User testUserFromSigIn = UserDAO.validateCredentials( username, password );
		boolean areTheyTheSame = testUserMadeByMe.equals( testUserFromSigIn );
		assertTrue( areTheyTheSame );
	}
}
