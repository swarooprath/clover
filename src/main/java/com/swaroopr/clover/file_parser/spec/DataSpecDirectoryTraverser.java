package com.swaroopr.clover.file_parser.spec;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSpecDirectoryTraverser<T> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSpecDirectoryTraverser.class);
	
	private final File specDirectory;
	private final AbstractDataSpecFileVisitor<T> dataspecTask;
	
	public DataSpecDirectoryTraverser(File specDirectory, AbstractDataSpecFileVisitor<T> dataspecTask) {
		this.specDirectory = specDirectory;
		this.dataspecTask = dataspecTask;
	}

	public File getSpecDirectory() {
		return specDirectory;
	}

	public AbstractDataSpecFileVisitor<T> getDataspecTask() {
		return dataspecTask;
	}
	
	public void traverse() {
		try {
			Files.walkFileTree(specDirectory.toPath(), new HashSet<>(), 1, this.dataspecTask);
		} catch (IOException e) {
			LOG.error("Unexpected IOException caught while traversing directory with path: " + specDirectory.getAbsolutePath(), e);
			throw new IllegalStateException(e);
		}		
	}
}
