package ru.otus.jdbc.querybuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class DynamicQueryBuilder {

    private static final String STR_COMMA_SPACE = ", ";

    private String tableName;

    private QueryType queryType;

    private List<String> columns;

    private List<Object> values;
    
    private List<String> whereClauseColumns;
    
    private Logger logger = LoggerFactory.getLogger(DynamicQueryBuilder.class);
    
    public DynamicQueryBuilder() {
        this.columns            = new ArrayList<>();
        this.whereClauseColumns = new ArrayList<>();
        this.values             = new ArrayList<>();
    }
    
    public DynamicQueryBuilder addWhereClauseColumns(String col, Object value) {
        if (col == null || value == null)
            return this;
        
        whereClauseColumns.add(col);
        values.add(value);
        
        return this;
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

    public String query() {
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
        sb.append("SELECT * FROM ").append(tableName).append(" WHERE ");
        
        addColumnsToQueryForUpdate(sb, whereClauseColumns);
    }
    
    private void returnQueryForUpdate(StringBuilder sb) {
        sb.append("UPDATE ").append(tableName).append(" SET ");
        addColumnsToQueryForUpdate(sb, columns);
        sb.append(" WHERE ");
        addColumnsToQueryForUpdate(sb, whereClauseColumns);
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
}