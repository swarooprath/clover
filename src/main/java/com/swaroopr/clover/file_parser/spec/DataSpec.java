package com.swaroopr.clover.file_parser.spec;

import java.util.List;

public class DataSpec {
	
	private final String fileNamePrefix;	
	private final List<DataSpecUnit> spec;
	
	public DataSpec(String fileNamePrefix, List<DataSpecUnit> spec) {
		this.fileNamePrefix = fileNamePrefix;
		this.spec = spec;
	}
	
	public String getFileNamePrefix() {
		return this.fileNamePrefix;
	}

	public List<DataSpecUnit> getSpec() {
		return spec;
	}
}
