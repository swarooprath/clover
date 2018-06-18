package com.swaroopr.clover.file_parser.spec;

import java.util.Objects;

import com.google.common.base.Preconditions;

public class DataSpecUnit {
	
	private final String columnName;
	private final int width;
	private final DataType dataType;
	
	public DataSpecUnit(String columnName, int width, DataType dataType) {
		this.columnName = Preconditions.checkNotNull(columnName, "ColumnName cannot be null.");
		Preconditions.checkArgument(width > 0, "Width must be greater than 0.");
		this.width = width;
		this.dataType = Preconditions.checkNotNull(dataType, "DataType cannot be null.");
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public int getWidth() {
		return width;
	}
	
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(columnName, width, dataType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSpecUnit other = (DataSpecUnit) obj;
		return Objects.equals(columnName, other.columnName) && width == other.width && Objects.equals(dataType, other.dataType);
	}
}
