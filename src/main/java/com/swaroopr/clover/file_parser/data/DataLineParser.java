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
					} else if (sc.hasNext()) {
						// TODO: this is buggy and needs to be fixed. Solution: write my own scanner / parser
						String nextString = sc.findInLine("\\s*\\d");
						if (null == nextString) {
							return logErrorAndReturnEmpty(line);
						}
						nextString = nextString.trim();
						if (nextString.length() == 0) {
							return logErrorAndReturnEmpty(line);
						}
						char c = nextString.charAt(0);
						if (c == '1' || c == '0') {
							boolean value = (c == '1');
							result.add(new DataUnit<Boolean>(dataSpecUnit, value));
						} else {
							return logErrorAndReturnEmpty(line);
						}
					} else {
						return logErrorAndReturnEmpty(line);
					}
					break;
				case INTEGER:
					if (sc.hasNextInt()) {
						int number = sc.nextInt();
						result.add(new DataUnit<Integer>(dataSpecUnit, number));
					} else {
						return logErrorAndReturnEmpty(line);
					}
					break;
				case TEXT:
					if (sc.hasNext()) {
						String text = sc.next();
						result.add(new DataUnit<String>(dataSpecUnit, text));
					} else {
						return logErrorAndReturnEmpty(line);
					}
					break;
				}
			}
		}
		return Optional.of(new DataRecord(dataSpec, result));
	}

	private Optional<DataRecord> logErrorAndReturnEmpty(String line) {
		LOG.error("Unable to parse input line with value \"{}\" for the spec: {}.", line, dataSpec.getFileNamePrefix());
		return Optional.empty();
	}
}
