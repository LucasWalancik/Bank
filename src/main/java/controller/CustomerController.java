package controller;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class CustomerController
{
	public static void main(String[] args )
	{
		Javalin app = Javalin.create().start(7070);	
		app.post("/users/form", postUser);
	}
	
	
	public static Handler postUser = ctx -> {
		String username = ctx.formParam("username");
		String password = ctx.formParam("password");
		
		System.out.println( "Username: " + username );
	};
	
}
