package com.swaroopr.clover.file_parser.db;

import java.sql.Connection;

public class DatabaseConnectionManager {
	
	public Connection getConnection() {
       Class.forName("com.mysql.jdbc.Driver");

	      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		
	}

}
