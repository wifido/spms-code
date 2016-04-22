package com.sf.module.common.domain.sql;

import com.google.common.base.Joiner;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.sf.module.common.domain.Constants.COMMA;

public class QueryStatement extends SqlSnippet {
    public static final String DESCENDING = "DESC";
    public static final String ASCENDING = "ASC";
    public static final String IN = "IN";

    protected QueryStatement(boolean isDistinct, CharSequence... columns) {
        super();

        builder
                .append(isDistinct ? "SELECT DISTINCT " : "SELECT ")
                .append(Joiner.on(',').join(columns));
    }

    public static QueryStatement select(CharSequence... columns) {
        return new QueryStatement(false, columns);
    }

    public static QueryStatement selectDistinct(CharSequence... columns) {
        return new QueryStatement(true, columns);
    }

    public QueryStatement from(CharSequence tableName) {
        builder
                .append(" FROM ")
                .append(tableName);

        return this;
    }

    public static class TableAlias {
        private final String tableName;
        private final String tableAlias;

        public TableAlias(String tableName, String tableAlias) {
            this.tableName = tableName;
            this.tableAlias = tableAlias;
        }
    }

    public QueryStatement from(TableAlias... tableAlias) {
        builder
                .append(" FROM ")
                .append(Joiner.on(COMMA).join(injectAlias(tableAlias)))
                .append(" ");
        return this;
    }

    private List<CharSequence> injectAlias(TableAlias[] tableAlias) {
        List<CharSequence> tablesWithAlias = newArrayList();
        for (TableAlias tableAlia : tableAlias) {
            tablesWithAlias.add(tableAlia.tableName + " " + tableAlia.tableAlias);
        }
        return tablesWithAlias;
    }

    public QueryStatement from(CharSequence... tableNames) {
        builder
                .append(" FROM ")
                .append(Joiner.on(COMMA).join(injectAlias(tableNames)))
                .append(" ");
        return this;
    }

    private List<CharSequence> injectAlias(CharSequence... tableNames) {
        List<CharSequence> tablesWithAlias = newArrayList();
        for (CharSequence tableName : tableNames) {
            tablesWithAlias.add(tableName + " " + tableName);
        }

        return tablesWithAlias;
    }

    public QueryStatement fromChildTable(CharSequence tableName) {
        builder
                .append(" FROM (")
                .append(tableName)
                .append(" )");

        return this;
    }

    public QueryStatement join(CharSequence tableName) {
        builder
                .append(" JOIN ")
                .append(tableName);

        return this;
    }

    public QueryStatement leftJoin(CharSequence tableName) {
        builder
                .append(" LEFT JOIN ")
                .append(tableName);

        return this;
    }

    public QueryStatement on(CharSequence leftHand, CharSequence rightHand) {
        builder
                .append(" ON ")
                .append(leftHand).append(" = ").append(rightHand);
        return this;
    }

    public QueryStatement where(CharSequence criteria) {
        builder
                .append(" WHERE ")
                .append("(")
                .append(criteria)
                .append(")");

        return this;
    }

    public QueryStatement startWithBy(CharSequence criteria) {
        builder
                .append(" START WITH ")
                .append(criteria)
                .append(" =?");

        return this;
    }
    
    public QueryStatement startWith(CharSequence column) {
        builder
                .append(" START WITH ")
                .append(column);

        return this;
    }

    public QueryStatement connectBy(CharSequence criteria) {
        builder
                .append(" CONNECT BY PRIOR ")
                .append(criteria);

        return this;
    }

    public QueryStatement or(CharSequence criteria) {
        builder
                .append(" OR ")
                .append("(")
                .append(criteria)
                .append(")");

        return this;
    }

    public QueryStatement and(CharSequence criteria) {
        builder
                .append(" AND ")
                .append("(")
                .append(criteria)
                .append(")");

        return this;
    }

    public QueryStatement groupBy(CharSequence column) {
        builder
                .append(" GROUP BY ")
                .append(column);

        return this;
    }

    public QueryStatement orderByAscending(CharSequence... columns) {
        builder
                .append(" ORDER BY ")
                .append(Joiner.on(',').join(columns))
                .append(" ")
                .append(QueryStatement.ASCENDING);

        return this;
    }
    
    public QueryStatement orderBy(CharSequence column, CharSequence orderType) {
        builder
                .append(" ORDER BY ")
                .append(column)
                .append(" ")
                .append(orderType);

        return this;
    }

    public QueryStatement limit(int numberOfResults) {
        builder
                .append(" LIMIT ")
                .append(numberOfResults);

        return this;
    }

    public SqlQuery toQuery() {
        return new SqlQuery(builder.toString());
    }
    public SqlQuery toTable() {
        return new SqlQuery("( " +builder.toString() +" )");
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public QueryStatement unionAll(String s) {
        builder
                .append(" union all ")
                .append(s);
        return this;
    }
}