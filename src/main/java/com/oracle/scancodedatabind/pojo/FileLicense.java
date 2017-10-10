package com.oracle.scancodedatabind.pojo;

public class FileLicense {

    private String key;
    private Double score;
    private String short_name;
    /*private String category;
    private String owner;
    private String homepage_url;
    private String text_url;
    private String reference_url;
    private String spdx_license_key;
    private String spdx_url;*/

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
}
