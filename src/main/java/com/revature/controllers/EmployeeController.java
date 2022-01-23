package com.revature.controllers;

import java.util.ArrayList;

import com.revature.dao.UserDAO;
import com.revature.models.User;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class EmployeeController {
	
Javalin app;
	
	public EmployeeController( Javalin app ) 
	{
		this.app = app;
		
		app.get( "/employees", getEveryEmployee );
		/*
		app.get( "/customers/{username}", getCustomerByUsername );
		app.get( "/customers", getCustomersList );
		app.delete( "/customers/{username}", deleteCustomer );
		app.post( "/customers", createNewCustomer );
		app.put( "/customers/{ID}", updateCustomer );
		*/
	}

	public Handler getEveryEmployee = ctx ->
	{
		ArrayList<User> employees = UserDAO.getEverySchmuck( 2 );
		ctx.json( employees );
		ctx.status( 200 );
	};
	
}
