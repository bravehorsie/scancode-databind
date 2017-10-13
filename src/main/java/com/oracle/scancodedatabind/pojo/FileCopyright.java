package com.oracle.scancodedatabind.pojo;

import java.util.List;

public class FileCopyright {

    private List<String> statements;

    private List<String> holders;

    public List<String> getStatements() {
        return statements;
    }

    public void setStatements(List<String> statements) {
        this.statements = statements;
    }

    public List<String> getHolders() {
        return holders;
    }

    public void setHolders(List<String> holders) {
        this.holders = holders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileCopyright that = (FileCopyright) o;

        if (statements != null ? !statements.equals(that.statements) : that.statements != null) return false;
        return holders != null ? holders.equals(that.holders) : that.holders == null;
    }

    @Override
    public int hashCode() {
        int result = statements != null ? statements.hashCode() : 0;
        result = 31 * result + (holders != null ? holders.hashCode() : 0);
        return result;
    }
}
