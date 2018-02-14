package com.oracle.scancodedatabind;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class Main {

    public static final String JSON_FILE = "jsonFile";
    public static final String OUTPUT = "outputFile";
    public static final String LICENSE = "license";
    public static final String IGNORE = "ignore";


    public static void main(String[] args) throws IOException {

        Options options = new Options();
        options.addOption(new Option(JSON_FILE, true, "Json file produced by the scancode-toolkit" +
                "\n   (has to be run with  \"-c -l -i --full-root --license-text -f json\" arguments)"));
        options.addOption(OUTPUT, true, "Path to output file.");
        options.addOption(LICENSE, true, "Path to file containing license which will be used to check files wiht \"EXCEPTION\" license.");
        options.addOption(IGNORE, true, "Ignore files containing pattern. Patterns are separated by ','.");



        CommandLineParser parser = new BasicParser();
        CommandLine line;
        try {
            // parse the command line arguments
            line = parser.parse(options, args);
            if (!line.hasOption(JSON_FILE) || !line.hasOption(OUTPUT)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "scancode-databind", options);
                return;
            }
        }
        catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            return;
        }

        Runner runner = new Runner(line.getOptionValue(JSON_FILE), line.getOptionValue(OUTPUT));
        if (line.hasOption(LICENSE)) {
            runner.setCopyrightTemplate(line.getOptionValue(LICENSE));
        }
        if (line.hasOption(IGNORE)) {
            runner.setIgnores(line.getOptionValue(IGNORE));
        }
        runner.run();
    }


}
