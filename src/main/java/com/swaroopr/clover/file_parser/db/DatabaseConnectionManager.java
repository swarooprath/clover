package com.swaroopr.clover.file_parser.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swaroopr.clover.file_parser.spec.DataSpec;

public class DatabaseConnectionManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSpec.class);
	
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
		File f = new File("jdbc.properties");
		if (!f.exists() && f.canRead()) {
			LOG.error("Cannot read the jdbc.properties file.");
		}
		String url = "";
		try(Reader r = new BufferedReader(new FileReader(f))) {
			props.load(r);
			url = Optional.ofNullable(props.remove("url")).orElse("jdbc:postgresql://localhost/clover").toString();
			return DriverManager.getConnection(url);
		} catch (IOException e) {
			throw new IllegalStateException("jdbc.properties cannot be read.", e);
		} catch (SQLException e) {
			throw new IllegalStateException("Unable to connect to database with url: ." + url, e);
		}
	}
}
