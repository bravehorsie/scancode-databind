package com.oracle.scancodedatabind.pojo;

import java.util.List;

/**
 * File in Scancode result.
 * Represents file license description, etc.
 */
public class FileEntry {

    private String path;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private List<FileLicense> licenses;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<FileLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<FileLicense> licenses) {
        this.licenses = licenses;
    }
}
