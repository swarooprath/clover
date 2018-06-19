package com.swaroopr.clover.file_parser.data;

import java.util.List;

import com.swaroopr.clover.file_parser.spec.DataSpec;

public class DataRecord {
	
	private final DataSpec dataSpec;
	private final List<DataUnit<?>> dataRecordUnitList;

	public DataRecord(DataSpec dataSpec, List<DataUnit<?>> dataRecordUnitList) {
		this.dataSpec = dataSpec;
		this.dataRecordUnitList = dataRecordUnitList;
	}

	public DataSpec getDataSpec() {
		return dataSpec;
	}

	public List<DataUnit<?>> getDataRecordUnitList() {
		return dataRecordUnitList;
	}
}
