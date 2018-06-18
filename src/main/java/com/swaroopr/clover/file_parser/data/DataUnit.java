package com.swaroopr.clover.file_parser.data;

import com.swaroopr.clover.file_parser.spec.DataSpecUnit;

public class DataUnit<T> {

	private final DataSpecUnit dataSpecUnit;
	private final T value;

	public DataUnit(DataSpecUnit dataSpecUnit, T value) {
		this.dataSpecUnit = dataSpecUnit;
		this.value = value;
	}
	
	public DataSpecUnit getDataSpecUnit() {
		return dataSpecUnit;
	}
	
	public T getValue() {
		return value;
	}
}
