package com.oracle.scancodedatabind;

import com.oracle.scancodedatabind.pojo.FileCopyright;
import com.oracle.scancodedatabind.pojo.FileEntry;
import com.oracle.scancodedatabind.pojo.FileLicense;
import com.oracle.scancodedatabind.pojo.FileLicenseKey;
import com.oracle.scancodedatabind.pojo.ScancodeResult;
import org.glassfish.copyright.Copyright;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Runner {

    private static final Logger logger = Logger.getLogger(Runner.class.getName());

    private final String jsonFilePath;

    private final String outputPath;

    private File copyrightTemplate;

    private String[] ignore = new String[]{};

    private final String[] known = new String[] {"GPL 2.0 with classpath exception", "CDDL 1.1"};

    private final String knownCopyrightHolder = "Oracle and/or its affiliates.";

    private boolean generateHtml = true;

    private String stripPath;


    public Runner(String jsonFilePath, String outputPath) {
        this.jsonFilePath = jsonFilePath;
        this.outputPath = outputPath;
    }

    public void setStripPath(String stripPath) {
        this.stripPath = stripPath;
    }

    public void setCopyrightTemplate(String copyrightTemplatePath) {
        File cprTemplate = new File(copyrightTemplatePath);
        if (!cprTemplate.isFile()) {
            throw new RuntimeException("Template file not found: " + copyrightTemplatePath);
        }
        this.copyrightTemplate = cprTemplate;
    }

    public void setIgnores(String pattern) {
        this.ignore = pattern.split(",");
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

        List<FileEntry> files = result.getFiles().stream().filter(fileEntry -> fileEntry.getType().equals("file") && !ignoreFile(fileEntry))
                .collect(Collectors.toList());

        //postprocess licenses only get with highest score
        for (FileEntry file : files) {
            for (FileLicense license : file.getLicenses()) {
                FileLicenseKey key = new FileLicenseKey(license);
                FileLicense existing = file.getHighestScoreLicenses().get(key);
                if (existing == null || existing.getScore() < license.getScore()) {
                    file.getHighestScoreLicenses().put(key, license);
                }
            }
        }

        List<FileEntry> withLicense = files.stream().filter(fileEntry ->
                fileEntry.getHighestScoreLicenses().size() > 0)
                .collect(Collectors.toList());


        List<FileEntry> knownLicnses = new ArrayList<>();
        List<FileEntry> exceptions = new ArrayList<>();

        withLicense.forEach(fileEntry -> {
            boolean licenseMatch = checkLicense(fileEntry.getHighestScoreLicenses().values());
            if (licenseMatch || (knownCopyrightHolder(fileEntry) && checkLicenseHeader(fileEntry))) {
                knownLicnses.add(fileEntry);
            } else {
                exceptions.add(fileEntry);
            }
        });

        List<FileEntry> withoutLicense = files.stream().filter(fileEntry ->
                fileEntry.getHighestScoreLicenses() == null || fileEntry.getHighestScoreLicenses().size() == 0)
                .collect(Collectors.toList());

        System.out.println("Entries with known license: " + knownLicnses.size());
        System.out.println("Entries with unknown license: " + exceptions.size());
        System.out.println("Entries without license: " + withoutLicense.size());

        printCsv(knownLicnses, outputPath + "-known-license.csv");
        printCsv(exceptions, outputPath + "-exception-license.csv");
        printCsv(withoutLicense, outputPath + "-without-license.csv");
        printHtml(exceptions, "-exceptions.html");
        printHtml(withoutLicense, "-no-license.html");
    }

    private boolean knownCopyrightHolder(FileEntry fileEntry) {
        for (FileCopyright copyright : fileEntry.getCopyrights()) {
            for (String holder : copyright.getHolders()) {
                if (!knownCopyrightHolder.equals(holder)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printHtml(Collection<FileEntry> entries, String suffix) {
        ConfluenceHtmlPrinter confluenceHtmlPrinter = new ConfluenceHtmlPrinter(entries, outputPath+ "-"+suffix, stripPath);
        confluenceHtmlPrinter.printHtml();
    }

    private boolean checkLicense(Collection<FileLicense> licenses) {
        for (FileLicense license : licenses) {
            if (!license.getShort_name().equals(known[0]) && !license.getShort_name().equals(known[1])) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLicenseHeader(FileEntry fileEntry) {
        if (copyrightTemplate == null) {
            return false;
        }
        File f = new File(fileEntry.getPath());
        Copyright c = new Copyright();
        c.correctTemplate = copyrightTemplate;
        c.ignoreYear = true;

        try {
            c.check(f);
            return c.errors == 0;
        } catch (IOException e) {
            logger.severe("Can't check: "+f.getPath());
            return false;
        }
    }

    private boolean ignoreFile(FileEntry fileEntry) {
        for (String pattern : ignore) {
            if (fileEntry.getPath().contains(pattern)) {
                return true;
            }
        }
        return false;
    }

    private void printCsv(List<FileEntry> entries, String fileName) {
        File output = new File(fileName);
        try (FileWriter outWriter = new FileWriter(output)) {
            entries.forEach((fileEntry -> {
                List<String> values = new ArrayList<>();
                String filePath = stripPath != null ?
                        fileEntry.getPath().replace(stripPath, "")
                        :fileEntry.getPath();
                values.add("File: " + filePath);

                fileEntry.getHighestScoreLicenses().values().forEach((fileLicense -> {
                    values.add("License: " + fileLicense.getShort_name());
                    values.add("Owner: " + fileLicense.getOwner());
                    values.add("Url: " + fileLicense.getUrl());
                }));

                fileEntry.getCopyrights().forEach(fileCopyright -> {
                    if (fileCopyright.getStatements().size() > 0 || fileCopyright.getHolders().size() > 0) {
                        values.add("Copyrights: " + fileCopyright.getStatements());
                        values.add("Holders: " + fileCopyright.getHolders());
                    }
                });

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
