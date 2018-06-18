package com.swaroopr.clover.file_parser.data;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swaroopr.clover.file_parser.spec.DataSpec;

public class DataFileVisitor implements FileVisitor<Path> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSpec.class);

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		LOG.warn("Unexpected data directory found with name: " + dir.getFileName());
		return FileVisitResult.SKIP_SIBLINGS;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		LOG.info("Processed data file with name: " + file.getFileName().toString());
		Scanner sc = new Scanner(file);
		while (sc.hasNext()) {
			String line = sc.nextLine();
		}
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
