package com.oracle.scancodedatabind.pojo;

import javax.json.bind.annotation.JsonbProperty;

public class FileLicense {

    private String key;
    private Double score;
    private String short_name;
    private String owner;
    @JsonbProperty("homepage_url")
    private String url;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileLicense license = (FileLicense) o;

        if (key != null ? !key.equals(license.key) : license.key != null) return false;
        if (short_name != null ? !short_name.equals(license.short_name) : license.short_name != null) return false;
        if (owner != null ? !owner.equals(license.owner) : license.owner != null) return false;
        return url != null ? url.equals(license.url) : license.url == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (short_name != null ? short_name.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
