package com.swaroopr.clover.file_parser.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

public class DatabaseConnectionManager {
	
	private static final DatabaseConnectionManager INSTANCE = new DatabaseConnectionManager();
	
	private DatabaseConnectionManager() {
		init();
	}
	
	public final void init() {
		String jdbcDriverClassName = System.getProperty("jdbc.drivers", "org.postgresql.Driver");
		try {
			Class.forName(jdbcDriverClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Failed to load JDBC driver class: " + jdbcDriverClassName, e);
		}
	}
	
	public static DatabaseConnectionManager getInstance() {
		return INSTANCE;
	}
	
	public Connection getConnection() {
		Properties props = new Properties();
		try(InputStream is = getClass().getResourceAsStream("jdbc.properties")) {
			props.load(is);
			String url = Optional.ofNullable(props.remove("url")).orElse("jdbc:postgresql://localhost/clover").toString();
			return DriverManager.getConnection(url, props);
		} catch (IOException e) {
			throw new IllegalStateException("jdbc.properties cannot be read.", e);
		} catch (SQLException e) {
			throw new IllegalStateException("Unable to connect to database.", e);
		}
	}
}
