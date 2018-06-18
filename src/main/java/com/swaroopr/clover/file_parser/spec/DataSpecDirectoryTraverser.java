package com.swaroopr.clover.file_parser.spec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSpecDirectoryTraverser<T> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSpecDirectoryTraverser.class);
	
	private final File directory;
	private final AbstractDataSpecFileVisitor<T> dataspecTask;
	
	private DataSpecDirectoryTraverser(File directory, AbstractDataSpecFileVisitor<T> dataspecTask) {
		this.directory = directory;
		this.dataspecTask = dataspecTask;
	}

	public File getDirectory() {
		return directory;
	}

	public AbstractDataSpecFileVisitor<T> getDataspecTask() {
		return dataspecTask;
	}
	
	public void traverse() {
		try {
			Files.walkFileTree(directory.toPath(), new HashSet<>(), 1, this.dataspecTask);
		} catch (IOException e) {
			LOG.error("Unexpected IOException caught while traversing directory with path: " + directory.getAbsolutePath(), e);
			throw new IllegalStateException(e);
		}		
	}
}
