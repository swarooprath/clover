package com.swaroopr.clover.file_parser.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.swaroopr.clover.file_parser.data.DataRecord;
import com.swaroopr.clover.file_parser.data.DataUnit;
import com.swaroopr.clover.file_parser.spec.DataSpec;
import com.swaroopr.clover.file_parser.spec.DataSpecUnit;

public class DatabaseManager {
	
	private final DatabaseConnectionManager databaseConnectionManager;
	private final Connection connection;
	
	public DatabaseManager(DatabaseConnectionManager databaseConnectionManager) {
		this.databaseConnectionManager = databaseConnectionManager;
		this.connection = databaseConnectionManager.getConnection();
	}
	
	public void createTable(DataSpec dataSpec) throws SQLException {
		String createTableSql = String.format("CREATE TABLE %s (%s)", getTableName(dataSpec), generateColumnDefinition(dataSpec));
		this.connection.createStatement().executeUpdate(createTableSql);
	}
	
	private Object generateColumnDefinition(DataSpec dataSpec) {
		List<String> columnDefinitions = new ArrayList<>();
		for (DataSpecUnit dataSpecUnit : dataSpec.getSpec()) {
			String columnName = dataSpecUnit.getColumnName();
			switch (dataSpecUnit.getDataType()) {
			case BOOLEAN:
				columnDefinitions.add(String.format("%s boolean", columnName));
				break;
			case INTEGER:
				columnDefinitions.add(String.format("%s int", columnName));
				break;
			case TEXT:
				columnDefinitions.add(String.format("%s varchar32(%d)", columnName, dataSpecUnit.getWidth()));
				break;
			default:
				break;
			}
		}
		return String.join(", ", columnDefinitions);
	}

	public void insertRecord(DataRecord dataRecord) throws SQLException {
		String insertStatement = String.format("insert into %s ()", getTableName(dataRecord.getDataSpec()), generateColumnNameValueString(dataRecord));
		this.connection.createStatement().executeUpdate(insertStatement);
	}

	private String generateColumnNameValueString(DataRecord dataRecord) {
		List<String> columnNameValueList = new ArrayList<>();
		for (DataUnit dataUnit : dataRecord.getDataRecord()) {
			DataSpecUnit dataSpecUnit = dataUnit.getDataSpecUnit();
			String columnName = dataSpecUnit.getColumnName();
			switch (dataSpecUnit.getDataType()) {
			case BOOLEAN:
				columnNameValueList.add(String.format("%s %s", columnName, (boolean) dataUnit.getValue() ? "true" : "false"));
				break;
			case INTEGER:
				columnNameValueList.add(String.format("%s %s", columnName, dataUnit.getValue().toString()));
				break;
			case TEXT:
				columnNameValueList.add(String.format("%s \"%s\"", columnName, dataUnit.getValue().toString()));
				break;
			}
		}
		return String.join(", ", columnNameValueList);
	}
	
	private String getTableName(DataSpec dataSpec) {
		String tableName = dataSpec.getFileNamePrefix();
		tableName = tableName.replaceAll("\\s+", "_");				
		return tableName.toLowerCase();
	}
}
