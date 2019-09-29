package ru.otus.jdbc.querybuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DynamicQueryBuilder {

    private static final String STR_COMMA_SPACE = ", ";
    private static final String STR_WHITESPACE = " ";

    private String tableName;

    private QueryType queryType;

    private List<String> selectedColumns;

    private List<String> columns;

    private List<Object> values;

    private Map<String, String> whereClauseWithOperator;

    private Logger logger = LoggerFactory.getLogger(DynamicQueryBuilder.class);

    public DynamicQueryBuilder() {
        this.values = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.selectedColumns = new ArrayList<>();
        this.whereClauseWithOperator = new HashMap<>();
    }

    public DynamicQueryBuilder where(String col, String operator, Object value) {
        if (col == null || value == null || operator == null)
            return this;

        whereClauseWithOperator.put(col, operator);
        values.add(value);

        return this;
    }

    public DynamicQueryBuilder select(String... columns) {
        this.queryType = QueryType.SELECT;

        for (String column : columns) {
            if (column != null) selectedColumns.add(column);
        }

        return this;
    }

    public DynamicQueryBuilder insert(String tableName) {
        this.queryType = QueryType.INSERT;
        this.tableName = tableName;
        return this;
    }

    public DynamicQueryBuilder update(String tableName) {
        this.queryType = QueryType.UPDATE;
        this.tableName = tableName;
        return this;
    }

    public DynamicQueryBuilder from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Boolean value(String colName, Object colValue) {
        columns.add(colName);
        values.add(colValue);

        return Boolean.TRUE;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();

        logger.info("Generating sql");

        switch (this.queryType) {
            case SELECT:
                returnQueryForSelect(sb);
                break;
            case UPDATE:
                returnQueryForUpdate(sb);
                break;
            case INSERT:
                returnQueryForInsert(sb);
                break;
            default:
                break;
        }

        logger.info("Generated sql: {}", sb);

        return sb.toString();
    }

    public DynamicQueryBuilder resetColumnsList() {
        columns.clear();
        return this;
    }

    public Object[] getValues() {
        return values.toArray();
    }

    private void returnQueryForSelect(StringBuilder sb) {
        sb.append("SELECT ");
        addSelectsToQuery(sb, selectedColumns);
        sb.append(" FROM ").append(tableName).append(" WHERE ");
        addWhereClauseWithOperator(sb, whereClauseWithOperator);
    }

    private void returnQueryForUpdate(StringBuilder sb) {
        sb.append("UPDATE ").append(tableName).append(" SET ");
        addColumnsToQueryForUpdate(sb, columns);
        sb.append(" WHERE ");
        addWhereClauseWithOperator(sb, whereClauseWithOperator);
    }

    private void returnQueryForInsert(StringBuilder sb) {

        sb.append("INSERT INTO ").append(tableName).append(" ( ");

        for (String column : columns) {
            sb.append(column).append(STR_COMMA_SPACE);
        }

        sb.deleteCharAt(sb.length() - 2); //remove last comma.
        sb.append(" ) VALUES (");

        for (int i = 0; i < columns.size(); i++) {
            sb.append("?").append(STR_COMMA_SPACE);
        }

        sb.deleteCharAt(sb.length() - 2);
        sb.append(" ) ");

    }

    public enum QueryType {
        SELECT,
        INSERT,
        UPDATE
    }

    private void addColumnsToQueryForUpdate(StringBuilder sb, List<String> cols) {
        for (int i = 0; i < cols.size(); i++) {
            sb.append(cols.get(i)).append(" =?");
            if (i < (cols.size() - 1)) {
                sb.append(STR_COMMA_SPACE);
            }
        }
    }

    private void addSelectsToQuery(StringBuilder sb, List<String> cols) {
        for (int i = 0; i < cols.size(); i++) {
            sb.append(cols.get(i));
            if (i < (cols.size() - 1)) {
                sb.append(STR_COMMA_SPACE).append(" ");
            }
        }
    }

    private void addWhereClauseWithOperator(StringBuilder sb, Map<String, String> map) {
        final var entries = map.entrySet();

        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            sb.append(STR_WHITESPACE)
                    .append(entry.getKey())
                    .append(STR_WHITESPACE)
                    .append(entry.getValue())
                    .append(STR_WHITESPACE)
                    .append("?");

            if (i < (entries.size() - 1)) {
                sb.append(" AND");
            }

            i++;
        }
    }
}