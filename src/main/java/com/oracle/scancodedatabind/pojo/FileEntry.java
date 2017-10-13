package com.oracle.scancodedatabind.pojo;

import javax.json.bind.annotation.JsonbProperty;
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

    private Set<FileLicense> licenses;

    private Set<FileCopyright> copyrights;

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

    public Set<FileLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(Set<FileLicense> licenses) {
        this.licenses = licenses;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
