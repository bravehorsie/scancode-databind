package com.oracle.scancodedatabind;

import com.oracle.scancodedatabind.pojo.FileEntry;
import com.oracle.scancodedatabind.pojo.ScancodeResult;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Runner {

    private final String jsonFilePath;

    private final String outputPath;

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

        List<FileEntry> withoutLicense = files.stream().filter(fileEntry ->
                fileEntry.getLicenses() == null || fileEntry.getLicenses().size() == 0)
                .collect(Collectors.toList());

        System.out.println("Entries with license: " + withLicense.size());
        System.out.println("Entries without license: " + withoutLicense.size());

        printCsv(withLicense, outputPath + "-with-license.csv");
        printCsv(withoutLicense, outputPath + "-without-license.csv");

    }

    private void printCsv(List<FileEntry> entries, String fileName) {
        File output = new File(fileName);
        try (FileWriter outWriter = new FileWriter(output)) {
            entries.forEach((fileEntry -> {
                List<String> values = new ArrayList<>();
                values.add(fileEntry.getPath());

                StringBuilder licenseBuilder = new StringBuilder();
                fileEntry.getLicenses().forEach((fileLicense -> licenseBuilder.append(fileLicense.getShort_name()).append(", ")));
                values.add(licenseBuilder.toString());
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
