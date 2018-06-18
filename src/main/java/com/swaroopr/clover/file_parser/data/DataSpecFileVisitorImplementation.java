package com.swaroopr.clover.file_parser.data;

import com.swaroopr.clover.file_parser.spec.DataSpec;
import com.swaroopr.clover.file_parser.spec.AbstractDataSpecFileVisitor;

public class DataSpecFileVisitorImplementation extends AbstractDataSpecFileVisitor<DataSpecResult> {

	@Override
	public DataSpecResult execute(DataSpec dataSpec) {
		
		return DataSpecResult.SUCCESS;
	}
}
