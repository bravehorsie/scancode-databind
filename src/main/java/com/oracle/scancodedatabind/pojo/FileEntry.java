package com.oracle.scancodedatabind.pojo;

import javax.json.bind.annotation.JsonbProperty;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * File in Scancode result.
 * Represents file license description, etc.
 */
public class FileEntry {

    private String path;

    private String type;

    @JsonbProperty("file_type")
    private String mimeType;

    private Boolean binary;

    private List<FileLicense> licenses;

    private Map<FileLicenseKey, FileLicense> highestScoreLicenses = new HashMap<>();

    private Set<FileCopyright> copyrights = new HashSet<>();

    public Set<FileCopyright> getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(Set<FileCopyright> copyrights) {
        this.copyrights = copyrights;
    }

    public Boolean getBinary() {
        return binary;
    }

    public void setBinary(Boolean binary) {
        this.binary = binary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<FileLicenseKey, FileLicense> getHighestScoreLicenses() {
        return highestScoreLicenses;
    }

    public void setHighestScoreLicenses(Map<FileLicenseKey, FileLicense> highestScoreLicenses) {
        this.highestScoreLicenses = highestScoreLicenses;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<FileLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<FileLicense> licenses) {
        this.licenses = licenses;
    }
}
