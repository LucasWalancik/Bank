package com.revature.models;

public class User
{
	private String username;
	private String password;
	private int type;
	private int id;
	
	public User( String username, String password, int type, int id )
	{
		this.username = username; 
		this.password = password;
		this.type = type;
		this.id = id;
	}
	
}
