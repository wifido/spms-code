package com.sf.module.common.domain.sql;

public class SqlColumn extends SqlSnippet {
    public static final String ALL_COLUMNS = "*";
    private static final String DISTINCT = "distinct";

    protected SqlColumn(CharSequence content) {
        super(content);
    }

    protected SqlColumn(String format, Object... args) {
        super(format, args);
    }

    public SqlSnippet as(CharSequence aliasName) {
        builder
                .append(" AS ")
                .append(aliasName);

        return this;
    }

    public static SqlColumn column(CharSequence columnName) {
        return new SqlColumn(columnName);
    }

    public static SqlColumn column(CharSequence tableNameOrTableAlias, CharSequence columnName) {
        return new SqlColumn("%s.%s", tableNameOrTableAlias, columnName);
    }
    
    public static SqlColumn to_char_column(CharSequence tableNameOrTableAlias, CharSequence columnName, CharSequence columnAlias) {
        return new SqlColumn("to_char(%s.%s, 'yyyy-mm-dd HH24:mi:ss') %s ", tableNameOrTableAlias, columnName, columnAlias);
    }

    public static SqlColumn column(CharSequence tableNameOrTableAlias, CharSequence columnName, CharSequence columnAlias) {
        return new SqlColumn("%s.%s %s", tableNameOrTableAlias, columnName, columnAlias);
    }

    public static SqlColumn count(CharSequence columnName) {
        return new SqlColumn("COUNT(%s)", columnName);
    }

    public static SqlColumn count(CharSequence tableName, CharSequence columnName) {
        return new SqlColumn("COUNT(%s.%s)", tableName, columnName);
    }

    public static SqlColumn max(CharSequence tableName, CharSequence columnName, CharSequence columnAlias) {
        return new SqlColumn("MAX(%s.%s) %s", tableName, columnName, columnAlias);
    }

    public static SqlColumn quote(CharSequence content) {
        return new SqlColumn("\"%s\"", content);
    }

    public static SqlColumn dateColumn(String columnName, String format) {
        return new SqlColumn(" to_char(%s,'%s') %s ", columnName, format, columnName);
    }

    public static SqlColumn distinctColumn(CharSequence tableNameOrTableAlias, CharSequence columnName, CharSequence columnAlias) {
        return new SqlColumn("%s %s.%s %s", DISTINCT, tableNameOrTableAlias, columnName, columnAlias);
    }

    public static SqlColumn distinctColumn(CharSequence tableNameOrTableAlias, CharSequence columnName) {
        return new SqlColumn("%s %s.%s ", DISTINCT, tableNameOrTableAlias, columnName);
    }
}
