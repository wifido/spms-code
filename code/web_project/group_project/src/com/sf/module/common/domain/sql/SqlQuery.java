package com.sf.module.common.domain.sql;

public class SqlQuery {
    private final String sql;

    public SqlQuery(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}