package ru.otus.jdbc.querybuilder;

import java.util.ArrayList;
import java.util.List;


public class DynamicQueryBuilder {

    private static final String STR_COMMA_SPACE = ", ";

    private String tableName;

    private QueryType queryType;

    private List<String> columns;

    private List<Object> values;


    public DynamicQueryBuilder() {
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public DynamicQueryBuilder setQueryType(QueryType queryType) {
        this.queryType = queryType;
        return this;
    }

    public DynamicQueryBuilder setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public Boolean addColumn(String colName, Object colValue) {
        columns.add(colName);
        values.add(colValue);

        return Boolean.TRUE;
    }

    public DynamicQueryBuilder resetColumnsList() {
        columns.clear();
        return this;
    }

    public Object[] getValues() {
        return values.toArray();
    }

    public String query() {
        StringBuilder sb = new StringBuilder();

        switch (this.queryType) {
            case UPDATE:
                returnQueryForUpdate(sb);
                break;
            case INSERT:
                returnQueryForInsert(sb);
                break;
            default:
                break;
        }

        return sb.toString();
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

    private void returnQueryForUpdate(StringBuilder sb) {
        sb.append("UPDATE ").append(tableName).append(" SET ");
        addColumnsToQueryForUpdate(sb, columns);
    }

    private void addColumnsToQueryForUpdate(StringBuilder sb, List<String> cols) {
        for (int i = 0; i < cols.size(); i++) {
            sb.append(cols.get(i)).append(" =?");
            if (i < (cols.size() - 1)) {
                sb.append(STR_COMMA_SPACE);
            }
        }
    }

    public enum QueryType {
        INSERT,
        UPDATE
    }
}