package com.swaroopr.clover.file_parser.spec;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDataSpecFileVisitor<T> implements FileVisitor<Path> {
	
	private static final Logger LOG = LoggerFactory.getLogger(DataSpec.class);
	
	public abstract T execute(DataSpec dataSpec);
	
	@Override
	public final FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		LOG.warn("Unexpected postVisitDirectory method invoked for file with name: " + dir.getFileName());
		return FileVisitResult.CONTINUE;
	}

	@Override
	public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {	
		LOG.warn("Unexpected directory found with name: " + dir.getFileName());
		return FileVisitResult.SKIP_SIBLINGS;
	}

	@Override
	public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		if (file.toFile().canRead()) {
			Optional<DataSpec> spec = readSpec(file,attrs);
			if (spec.isPresent()) {
				execute(spec.get());
			}
		} else {
			LOG.error("Cannot read file with name: " + file.getFileName());
		}
		return  FileVisitResult.CONTINUE;
	}

	@Override
	public final FileVisitResult visitFileFailed(Path file, IOException exc) {
		LOG.error("Failed to process file with name: " + file.getFileName(), exc);
		return FileVisitResult.CONTINUE;
	}
	
	public Optional<DataSpec> readSpec(Path file, BasicFileAttributes attrs) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException e) {
			LOG.error(String.format("UnexpectedIOException caught while reading file with name: .", file.getFileName()), e);
		}
		if (lines == null || lines.size() < 2) {
			return Optional.empty();
		}
		if (!"\"column name\",width,datatype".equals(lines.get(0))) {
			LOG.error("Unexpected format of header line %s for file with name %s.", lines.get(0), file.getFileName());
		}
		lines.remove(0);
		String fileName = file.getName(-1).toString();
		List<DataSpecUnit> specRecords = new ArrayList<>();
		for (String line: lines) {
			String[] parts = line.split(",");
			if (parts.length != 3) {
				LOG.error("Unexpected line %s found in spec file with name %s.", line, fileName);
				return Optional.empty();
			}
			String columnName = parts[0];
			int width = -1;
			DataType dataType = null;
			try {
			  width = Integer.parseInt(parts[1]);
			} catch (NumberFormatException ex) {
				LOG.error("Unexpected width found in line %s in spec file with name %s.", line, fileName);
				return Optional.empty();
			}
			try {
				dataType = DataType.valueOf(parts[2]);
			} catch (IllegalArgumentException ex) {
				LOG.error("Unexpected dataType found in line %s in spec file with name %s.", line, fileName);
				return Optional.empty();
			}
			specRecords.add(new DataSpecUnit(columnName, width, dataType));
		}
		return Optional.of(new DataSpec(fileName, specRecords));
	}
}
