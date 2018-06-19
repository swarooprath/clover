package com.swaroopr.clover.file_parser.data;

import com.swaroopr.clover.file_parser.spec.DataSpec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swaroopr.clover.file_parser.spec.AbstractDataSpecFileVisitor;

public class DataSpecFileVisitorImplementation extends AbstractDataSpecFileVisitor<DataSpecResult> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSpec.class);
	
	private final File dataDirectory;
	
	public DataSpecFileVisitorImplementation(File dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	@Override
	public DataSpecResult execute(DataSpec dataSpec) {
		try {
			DataFileVisitor dataFileVisitor = new DataFileVisitor(dataSpec);
			dataFileVisitor.init();
			Files.walkFileTree(dataDirectory.toPath(), new HashSet<>(), 1, dataFileVisitor);
		} catch (IOException e) {
			LOG.error("Unexpected IOException caught while traversing directory with path: " + dataDirectory.getAbsolutePath(), e);
			return DataSpecResult.FAILURE;
		} catch (SQLException e) {
			LOG.error("Unexpected SQLException caught while trying to create table for the spec: " + dataSpec.getFileNamePrefix(), e);
			return DataSpecResult.FAILURE;
		}	
		return DataSpecResult.SUCCESS;
	}
}
