package com.swaroopr.clover.file_parser.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swaroopr.clover.file_parser.data.DataRecord;
import com.swaroopr.clover.file_parser.data.DataUnit;
import com.swaroopr.clover.file_parser.spec.DataSpec;
import com.swaroopr.clover.file_parser.spec.DataSpecUnit;

public class DatabaseManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);
	
	private final Connection connection;
	
	public DatabaseManager(DatabaseConnectionManager databaseConnectionManager) {
		this.connection = databaseConnectionManager.getConnection();
	}
	
	public void dropTable(DataSpec dataSpec) {
		String dropTableSql = String.format("DROP TABLE IF EXISTS %s", getTableName(dataSpec));
		try (Statement st = this.connection.createStatement()) {
			st.executeUpdate(dropTableSql);
		} catch (SQLException e) {
			LOG.error(String.format("Failed to drop the table for the statement %s.", dropTableSql), e);
		}
	}
	
	public void createTable(DataSpec dataSpec) throws SQLException {
		String createTableSql = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", getTableName(dataSpec), generateColumnDefinition(dataSpec));
		try (Statement st = this.connection.createStatement()) {
			st.executeUpdate(createTableSql);
		} catch (SQLException e) {
			LOG.error(String.format("Failed to create the table for the statement %s.", createTableSql), e);
		}
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
				columnDefinitions.add(String.format("%s varchar(%d)", columnName, dataSpecUnit.getWidth()));
				break;
			default:
				break;
			}
		}
		return String.join(", ", columnDefinitions);
	}

	public void insertRecord(DataRecord dataRecord) {
		List<String> columnNames = new ArrayList<>();
		List<String> values = new ArrayList<>();
		for (DataUnit<?> dataUnit : dataRecord.getDataRecordUnitList()) {
			DataSpecUnit dataSpecUnit = dataUnit.getDataSpecUnit();
			String columnName = dataSpecUnit.getColumnName();
			columnNames.add(columnName);
			switch (dataSpecUnit.getDataType()) {
			case BOOLEAN:
				values.add((Boolean) dataUnit.getValue() ? "true" : "false");
				break;
			case INTEGER:
				values.add(dataUnit.getValue().toString());
				break;
			case TEXT:
				values.add(String.format("'%s'", dataUnit.getValue().toString()));
				break;
			}
		}
		String columnNamesString = String.join(", ", columnNames);
		String valueString = String.join(", ", values);
		String insertStatement = String.format("insert into %s (%s) VALUES (%s)", getTableName(dataRecord.getDataSpec()), columnNamesString, valueString);
		try (Statement st = this.connection.createStatement()) {
			st.executeUpdate(insertStatement);
		} catch (SQLException e) {
			LOG.error("Failed to insert the record {}.", insertStatement);
		}
	}
	
	private String getTableName(DataSpec dataSpec) {
		String tableName = dataSpec.getFileNamePrefix();
		tableName = tableName.replaceAll("\\s+", "_");				
		return "clover." + tableName.toLowerCase();
	}
	
	@Override
	protected final void finalize() throws Throwable {
		this.connection.close();
	}
}
