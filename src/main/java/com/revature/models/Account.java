package com.revature.models;

public class Account
{
    private int id;
	private String name;
	private double funds;
	private int isAccepted;
	
	public Account()
	{
		
	}
	
	public Account( int ID )
	{
		this.id = ID;
	}
	public Account( String name, double funds, int isAccepted )
	{
		this.name = name;
		this.funds = funds;
		this.isAccepted = isAccepted;
	}
	
	public Account( int id, String name, double funds, int isAccepted )
	{
		this.name = name;
		this.funds = funds;
		this.isAccepted = isAccepted;
		this.id = id;
	}

	public void setFunds( double funds )
	{
		this.funds = funds;
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
