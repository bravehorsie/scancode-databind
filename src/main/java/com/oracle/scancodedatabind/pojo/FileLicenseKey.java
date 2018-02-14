package com.oracle.scancodedatabind.pojo;

import java.util.Objects;

public class FileLicenseKey {

    private String key;
    private String shortName;
    private String owner;
    private String url;
    private String matchedText;

    public FileLicenseKey(FileLicense fileLicense) {
        this.key = fileLicense.getKey();
        this.shortName = fileLicense.getShort_name();
        this.owner = fileLicense.getOwner();
        this.url = fileLicense.getUrl();
        this.matchedText = fileLicense.getMatchedText();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMatchedText() {
        return matchedText;
    }

    public void setMatchedText(String matchedText) {
        this.matchedText = matchedText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileLicenseKey that = (FileLicenseKey) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(shortName, that.shortName) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, shortName, owner, url);
    }
}
