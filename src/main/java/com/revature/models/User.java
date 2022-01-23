package com.revature.models;

public class User
{
	private String username;
	private String password;
	private int type;
	private int id;
	
	public User()
	{
		
	}
	
	
	public User( String username, String password, int type, int id )
	{
		this.username = username; 
		this.password = password;
		this.type = type;
		this.id = id;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public String toString()
	{
		String result = "Username: " + this.username + " password: " + this.password + " id: " + this.id;
		return result;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public boolean equals( User other )
	{
		boolean usernamesMatching = this.getUsername().equals( other.getUsername() );
		boolean passwordsMatching = this.getPassword().equals( other.getPassword() );
		boolean idsMatching = this.getId() == other.getId();
		boolean typesMatching = this.getType() == other.getType();
		if( usernamesMatching && passwordsMatching && idsMatching && typesMatching )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
