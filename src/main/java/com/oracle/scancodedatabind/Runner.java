package com.oracle.scancodedatabind;

import com.oracle.scancodedatabind.pojo.FileEntry;
import com.oracle.scancodedatabind.pojo.FileLicense;
import com.oracle.scancodedatabind.pojo.ScancodeResult;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Runner {

    private final String jsonFilePath;

    private final String outputPath;

    private final String[] known = new String[] {"GPL 2.0 with classpath exception", "CDDL 1.1"};

    public Runner(String jsonFilePath, String outputPath) {
        this.jsonFilePath = jsonFilePath;
        this.outputPath = outputPath;
    }

    public void run() {
        ScancodeResult result = read();
        write(result);
    }

    private ScancodeResult read() {
        Jsonb jsonb = JsonbBuilder.create();
        File scancodeInput = new File(jsonFilePath);
        try (FileInputStream stream = new FileInputStream(scancodeInput)) {
            return jsonb.fromJson(stream, ScancodeResult.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(ScancodeResult result) {

        List<FileEntry> files = result.getFiles().stream().filter(fileEntry -> fileEntry.getType().equals("file"))
                .collect(Collectors.toList());



        List<FileEntry> withLicense = files.stream().filter(fileEntry ->
                fileEntry.getLicenses() != null && fileEntry.getLicenses().size() > 0)
                .collect(Collectors.toList());


        List<FileEntry> knownLicnses = new ArrayList<>();
        List<FileEntry> exceptions = new ArrayList<>();

        withLicense.forEach(fileEntry -> {
            for (FileLicense license : fileEntry.getLicenses()) {
                if (!license.getShort_name().equals(known[0]) && !license.getShort_name().equals(known[1])) {
                    exceptions.add(fileEntry);
                    return;
                }
            }
            knownLicnses.add(fileEntry);
        });

        List<FileEntry> withoutLicense = files.stream().filter(fileEntry ->
                fileEntry.getLicenses() == null || fileEntry.getLicenses().size() == 0)
                .collect(Collectors.toList());

        System.out.println("Entries with known license: " + knownLicnses.size());
        System.out.println("Entries with unknown license: " + exceptions.size());
        System.out.println("Entries without license: " + withoutLicense.size());

        printCsv(knownLicnses, outputPath + "-known-license.csv");
        printCsv(exceptions, outputPath + "-exception-license.csv");
        printCsv(withoutLicense, outputPath + "-without-license.csv");

    }

    private void printCsv(List<FileEntry> entries, String fileName) {
        File output = new File(fileName);
        try (FileWriter outWriter = new FileWriter(output)) {
            entries.forEach((fileEntry -> {
                List<String> values = new ArrayList<>();
                values.add("File: " + fileEntry.getPath());

                fileEntry.getLicenses().forEach((fileLicense -> {
                    values.add("License: " + fileLicense.getShort_name());
                    values.add("Owner: " + fileLicense.getOwner());
                    values.add("Url: " + fileLicense.getUrl());
                }));
                try {
                    CSVUtil.writeLine(outWriter, values);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
