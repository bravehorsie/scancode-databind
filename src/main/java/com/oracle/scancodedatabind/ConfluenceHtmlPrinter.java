package com.oracle.scancodedatabind;

import com.oracle.scancodedatabind.pojo.FileCopyright;
import com.oracle.scancodedatabind.pojo.FileEntry;
import com.oracle.scancodedatabind.pojo.FileLicense;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfluenceHtmlPrinter {

    Map<String, Map<LicenseGroupKey, List<FileEntry>>> files = new HashMap<>();

    private String outputFilePath;

    public ConfluenceHtmlPrinter(Collection<FileEntry> entries, String output) {
        this.outputFilePath = output + "-confluence-table.html";
        for (FileEntry fileEntry : entries) {
            Map<LicenseGroupKey, List<FileEntry>> fileLicenseKeyListMap = files.computeIfAbsent(fileEntry.getExtension(), k -> new HashMap<>());
            //TODO getCopyrights().get(0) is incorrect, but this is how current table is composed on wikis
            LicenseGroupKey key = new LicenseGroupKey(fileEntry.getHighestScoreLicenses().values(),
                    fileEntry.getCopyrights().size() > 0 ?
                            fileEntry.getCopyrights().get(0) : null);
            List<FileEntry> fileEntries = fileLicenseKeyListMap.computeIfAbsent(key, k -> new ArrayList<>());
            fileEntries.add(fileEntry);
        }
    }

    public void printHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table style=\"width:100.0%;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">No. of Files</div>\n" +
                "  </th>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">Ext</div>\n" +
                "  </th>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">Copyright</div>\n" +
                "  </th>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">License</div>\n" +
                "  </th>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">Name</div>\n" +
                "  </th>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">Notes</div>\n" +
                "  </th>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">Next Steps</div>\n" +
                "  </th>\n" +
                "  <th colspan=\"1\">\n" +
                "     <div class=\"tablesorter-header-inner\">Status</div>\n" +
                "  </th>\n" +
                "</tr>\n");

        for (Map.Entry<String, Map<LicenseGroupKey, List<FileEntry>>> fileEntry : files.entrySet()) {
            int inSpan = 0;
            for (Map.Entry<LicenseGroupKey, List<FileEntry>> licensesEntry : fileEntry.getValue().entrySet()) {
                sb.append("<tr>\n");
                List<FileEntry> files = licensesEntry.getValue();
                if (inSpan == 0) {
                    inSpan = fileEntry.getValue().entrySet().size();
                    sb.append("<td rowspan=\"" + inSpan + "\">" + countEntryFileSize(fileEntry.getValue()) + "</td>\n");
                    sb.append("<td rowspan=\"" + inSpan + "\">" + fileEntry.getKey() + "</td>\n");
                    inSpan--;
                } else {
                    inSpan--;
                }

                sb.append("<td rowspan=\"1\">"+getCopyright(licensesEntry.getKey().getFileLicenseList().get(0).getFileCopyright())+"</td>\n");
                //print licenses
                sb.append("<td rowspan=\"1\"><ol>\n");
                for (LicenseWithCopyright license : licensesEntry.getKey().getFileLicenseList()) {
                    sb.append("<li>" + license.getFileLicense().getShort_name() + "</li>\n");
                }
                sb.append("</ol></td>\n");
                sb.append("<td rowspan=\"1\">");
                //print files
                sb.append("<ac:structured-macro ac:name=\"code\">\n" +
                        "        <ac:parameter ac:name=\"linenumbers\">true</ac:parameter>\n" +
                        "        <ac:parameter ac:name=\"language\">bash</ac:parameter>\n" +
                        "        <ac:parameter ac:name=\"collapse\">"+(files.size() > 8) +"</ac:parameter>\n" +
                        "        <ac:plain-text-body><![CDATA[");
                for (FileEntry file : files) {
                    //TODO remove absolute path with param
                    sb.append(file.getPath().replace("/home/roma/dev/java/metro/", "") + "\n");
                }
                sb.append("      ]]></ac:plain-text-body>\n" +
                        "      </ac:structured-macro>\n");
                sb.append("</td>");
                sb.append("<td rowspan=\"1\"></td>\n");
                sb.append("<td rowspan=\"1\"></td>\n");
                sb.append("<td rowspan=\"1\"></td>\n");
                sb.append("</tr>\n");
            }

        }
        sb.append("</tbody></table>");
        writeFile(sb);
    }

    private void writeFile(StringBuilder content) {
        File file = new File(outputFilePath);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCopyright(FileCopyright fileCopyright) {
        if (fileCopyright == null || fileCopyright.getHolders().size() == 0) {
            return "N/A";
        }
        final StringBuilder sb = new StringBuilder();
        fileCopyright.getHolders().stream().forEach(s -> sb.append(s).append("\n"));
        return sb.toString();
    }

    private int countEntryFileSize(Map<LicenseGroupKey, List<FileEntry>> extensionEntry) {
        List<FileEntry> allFileEntries = new ArrayList<>();
        extensionEntry.values().forEach(allFileEntries::addAll);
        return allFileEntries.size();
    }

    private static final class LicenseGroupKey {
        private List<LicenseWithCopyright> fileLicenseList = new ArrayList<>();

        public LicenseGroupKey(Collection<FileLicense> fileLicenseList, FileCopyright fileCopyright) {
            for(FileLicense license : fileLicenseList) {
                this.fileLicenseList.add(new LicenseWithCopyright(license, fileCopyright));
            }
        }

        public List<LicenseWithCopyright> getFileLicenseList() {
            return fileLicenseList;
        }

        public void setFileLicenseList(List<LicenseWithCopyright> fileLicenseList) {
            this.fileLicenseList = fileLicenseList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LicenseGroupKey that = (LicenseGroupKey) o;
            return Objects.equals(fileLicenseList, that.fileLicenseList);
        }

        @Override
        public int hashCode() {

            return Objects.hash(fileLicenseList);
        }
    }

    private static final class LicenseWithCopyright {
        private final FileLicense fileLicense;
        private final FileCopyright fileCopyright;

        public LicenseWithCopyright(FileLicense fileLicense, FileCopyright fileCopyright) {
            this.fileLicense = fileLicense;
            this.fileCopyright = fileCopyright;
        }

        public FileLicense getFileLicense() {
            return fileLicense;
        }

        public FileCopyright getFileCopyright() {
            return fileCopyright;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LicenseWithCopyright that = (LicenseWithCopyright) o;
            return Objects.equals(fileLicense, that.fileLicense);
        }

        @Override
        public int hashCode() {

            return Objects.hash(fileLicense);
        }
    }
}
