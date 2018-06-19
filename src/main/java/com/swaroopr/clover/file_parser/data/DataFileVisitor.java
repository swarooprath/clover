package com.swaroopr.clover.file_parser.data;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swaroopr.clover.file_parser.db.DatabaseConnectionManager;
import com.swaroopr.clover.file_parser.db.DatabaseManager;
import com.swaroopr.clover.file_parser.spec.DataSpec;

public class DataFileVisitor implements FileVisitor<Path> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSpec.class);
	
	private boolean isInitialised = false;
	private final DataSpec dataSpec;
	private DataLineParser parser;
	private DatabaseManager dbManager;
	
	
	public DataFileVisitor(DataSpec dataSpec) {
		this.dataSpec = dataSpec;
		this.parser = new DataLineParser(dataSpec);
		this.dbManager = new DatabaseManager(DatabaseConnectionManager.getInstance());
	}
	
	public void init() throws SQLException {
		this.dbManager.createTable(dataSpec);
		isInitialised = true;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		LOG.warn("Unexpected data directory found with name: " + dir.getFileName());
		return FileVisitResult.SKIP_SIBLINGS;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if (!isInitialised) {
			throw new IllegalStateException("DataFileVisitor has not been initialised for the spec: " + this.dataSpec.getFileNamePrefix());
		}
		String fileName = file.getName(-1).toString();
		if (!fileName.startsWith(dataSpec.getFileNamePrefix())) {
			return FileVisitResult.CONTINUE;
		}
		try (Scanner sc = new Scanner(file)) {
			while (sc.hasNext()) {
				String line = sc.nextLine();
				Optional<DataRecord> dataRecord = parser.parse(line);
				if (dataRecord.isPresent()) {
					this.dbManager.insertRecord(dataRecord.get());
				}
			}
		}
		LOG.info("Processed data file with name: " + file.getFileName().toString());
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		LOG.error("Failed to process data file with name: " + file.getFileName(), exc);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
		LOG.warn("Unexpected postVisitDirectory method invoked for data directory with name: " + dir.getFileName());
		return FileVisitResult.CONTINUE;
	}
}
