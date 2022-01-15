package com.revature.models;

public class Account
{
    private int id;
	private String name;
	private double funds;
	private int isAccepted;
	
	public Account( String name, double funds, int isAccepted )
	{
		this.name = name;
		this.funds = funds;
		this.isAccepted = isAccepted;
	}
	
	public Account( String name, double funds, int isAccepted, int id )
	{
		this.name = name;
		this.funds = funds;
		this.isAccepted = isAccepted;
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}
	
	public double getFunds()
	{
		return this.funds;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getIsAccepted()
	{
		return this.isAccepted;
	}
	
	public String toString()
	{
		String result = "";
		if( this.isAccepted == 0 )
		{
			result += "(INACTIVE) ";
		}
		result += "| Name: " + this.name + " | Funds: $" + this.funds + " | ID: " + this.id;
		return result;
	}
	
}