package com.oracle.scancodedatabind.pojo;

import javax.json.bind.annotation.JsonbProperty;
import java.util.Objects;

public class FileLicense {

    private String key;
    private Double score;
    private String short_name;
    private String owner;
    @JsonbProperty("homepage_url")
    private String url;

    @JsonbProperty("matched_text")
    private String matchedText;

    @JsonbProperty("start_line")
    private String startLine;

    @JsonbProperty("end_line")
    private String endLine;


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

    public String getMatchedText() {
        return matchedText;
    }

    public void setMatchedText(String matchedText) {
        this.matchedText = matchedText;
    }

    public String getStartLine() {
        return startLine;
    }

    public void setStartLine(String startLine) {
        this.startLine = startLine;
    }

    public String getEndLine() {
        return endLine;
    }

    public void setEndLine(String endLine) {
        this.endLine = endLine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileLicense that = (FileLicense) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(short_name, that.short_name) &&
                Objects.equals(owner, that.owner) &&
                Objects.equals(url, that.url) &&
                Objects.equals(matchedText, that.matchedText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, short_name, owner, url, matchedText);
    }
}
