package com.swaroopr.clover.file_parser.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swaroopr.clover.file_parser.spec.DataSpec;
import com.swaroopr.clover.file_parser.spec.DataSpecUnit;

public class DataLineParser {

	private static final Logger LOG = LoggerFactory.getLogger(DataLineParser.class);

	private DataSpec dataSpec;

	public DataLineParser(DataSpec dataSpec) {
		this.dataSpec = dataSpec;
	}

	public Optional<DataRecord> parse(String line) {
		List<DataUnit<?>> result = new ArrayList<>();
		try (Scanner sc = new Scanner(line)) {
			sc.useDelimiter("\\s+");
			for (DataSpecUnit dataSpecUnit : dataSpec.getSpec()) {
				switch (dataSpecUnit.getDataType()) {
				case BOOLEAN:
					if (sc.hasNextBoolean()) {
						result.add(new DataUnit<Boolean>(dataSpecUnit, sc.nextBoolean()));
						break;
					} else if (sc.hasNextByte()) {
						byte b = sc.nextByte();
						if (b == 1 || b == 0) {
							boolean value = b == 1;
							result.add(new DataUnit<Boolean>(dataSpecUnit, value));
						} else {
							logError(line);
							return Optional.empty();
						}
					} else {
						logError(line);
						return Optional.empty();
					}
					break;
				case INTEGER:
					if (sc.hasNextInt()) {
						int number = sc.nextInt();
						result.add(new DataUnit<Integer>(dataSpecUnit, number));
					} else {
						logError(line);
						return Optional.empty();
					}
					break;
				case TEXT:
					if (sc.hasNext()) {
						String text = sc.next();
						result.add(new DataUnit<String>(dataSpecUnit, text));
					} else {
						logError(line);
						return Optional.empty();
					}
					break;
				}
			}
		}
		return Optional.of(new DataRecord(dataSpec, result));
	}

	private void logError(String line) {
		LOG.error("Unable to parse input line with value \"%s\" for the spec: %s.", line, dataSpec);
	}
}
