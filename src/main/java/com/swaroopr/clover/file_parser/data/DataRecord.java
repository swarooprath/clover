package com.swaroopr.clover.file_parser.data;

import java.util.List;

import com.swaroopr.clover.file_parser.spec.DataSpec;

public class DataRecord {
	
	private final DataSpec dataSpec;
	private final List<DataUnit> dataRecord;

	public DataRecord(DataSpec dataSpec, List<DataUnit> dataRecord) {
		this.dataSpec = dataSpec;
		this.dataRecord = dataRecord;
	}

	public DataSpec getDataSpec() {
		return dataSpec;
	}

	public List<DataUnit> getDataRecord() {
		return dataRecord;
	}
}
