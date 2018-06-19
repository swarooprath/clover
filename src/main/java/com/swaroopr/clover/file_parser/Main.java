package com.swaroopr.clover.file_parser;

import java.io.File;
import java.util.Optional;

import org.apache.commons.cli.*;

import com.google.common.base.Preconditions;
import com.swaroopr.clover.file_parser.data.DataSpecFileVisitorImplementation;
import com.swaroopr.clover.file_parser.data.DataSpecResult;
import com.swaroopr.clover.file_parser.spec.DataSpecDirectoryTraverser;

public class Main {
	
	public static void main(String[] args) {
		
        Options options = new Options();

        Option input = new Option("s", "spec", true, "spec directory path");
        input.setRequired(false);
        
        Option output = new Option("d", "data", true, "data directory path");
        output.setRequired(false);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            String specDirectoryPath = isValidDirectory(Optional.ofNullable(cmd.getOptionValue("spec")).orElse("./spec"));
            String dataDirectoryPath = isValidDirectory(Optional.ofNullable(cmd.getOptionValue("data")).orElse("./data"));
            
            DataSpecDirectoryTraverser<DataSpecResult> dataSpecDirectoryTraverser = new DataSpecDirectoryTraverser<>(
            		new File(specDirectoryPath), 
            		new DataSpecFileVisitorImplementation(new File(dataDirectoryPath))
    		);
			dataSpecDirectoryTraverser.traverse();;
        	
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Please enter valid options.", options);
            System.exit(1);
        }
	}

	private static String isValidDirectory(String directoryPath) {
		File directory = new File(directoryPath);
		Preconditions.checkArgument(directory.exists() && directory.isDirectory() && directory.canRead(), "Invalid directory at path: " + directoryPath);
		return directoryPath;
	}
}
