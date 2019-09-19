package ru.otus.jdbc.executor;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.DatabaseSession;
import ru.otus.api.DbExecutor;
import ru.otus.api.SessionManager;
import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.exception.MissingIdAnnotationException;
import ru.otus.jdbc.querybuilder.DynamicQueryBuilder;
import ru.otus.jdbc.reflectiondata.ReflectionData;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DbExecutorImpl<T> implements DbExecutor<T> {
    
    private static final String                     ZERO            = "0";
    private static final String                     END             = "============END============";
    private final        SessionManager             sessionManager;
    private              Logger                     logger          = LoggerFactory.getLogger(DbExecutorImpl.class);
    private              Map<Class, ReflectionData> reflectionCache = new HashMap<>();
    
    public DbExecutorImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    public static void createUserTable(SessionManager sessionManagerJdbc) {
        sessionManagerJdbc.beginSession();
        
        
        try (PreparedStatement pst = sessionManagerJdbc.getCurrentSession()
                                                       .getConnection()
                                                       .prepareStatement("create table user(id long auto_increment, name varchar(50), age int)")) {
            
            pst.executeUpdate();
            sessionManagerJdbc.commitSession();
        } catch (SQLException e) {
            e.printStackTrace();
            sessionManagerJdbc.rollbackSession();
        }
    }
    
    public static void createAccountTable(SessionManagerJdbc sessionManagerJdbc) {
        sessionManagerJdbc.beginSession();
        
        try (PreparedStatement pst = sessionManagerJdbc.getCurrentSession()
                                                       .getConnection()
                                                       .prepareStatement("create table account(no long auto_increment, type varchar(255), rest number)")) {
            
            pst.executeUpdate();
            sessionManagerJdbc.commitSession();
        } catch (SQLException e) {
            e.printStackTrace();
            sessionManagerJdbc.rollbackSession();
        }
    }
    
    @Override
    public void create(T objectData) throws IllegalAccessException {
        logger.info("============CREATE OBJECT============");
        ReflectionData reflectionData = getReflectionData(objectData.getClass());
        
        DynamicQueryBuilder queryBuilder = new DynamicQueryBuilder();
    
        queryBuilder.setQueryType(DynamicQueryBuilder.QueryType.INSERT)
                    .setTableName(reflectionData.getClassName().toLowerCase());
        
        for (Field field : reflectionData.getFields()) {
            field.setAccessible(true);
            if (!field.equals(reflectionData.getIdField())) {
                queryBuilder.addColumn(field.getName(), field.get(objectData));
            }
        }
    
        String sql = queryBuilder.query();
    
        sessionManager.beginSession();
        DatabaseSession currentSession = sessionManager.getCurrentSession();
    
        try (PreparedStatement ps = currentSession.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
            prepareValues(ps, queryBuilder.getValues());
        
            logger.info("Executing query...");
            ps.executeUpdate();
        
            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                reflectionData.getIdField().setAccessible(true);
                reflectionData.getIdField().set(objectData, rs.getLong(1));
            }
            logger.info("Object created: {}", objectData);
            sessionManager.commitSession();
        } catch (SQLException e) {
            logger.error("Object creating error {} with message {}", objectData, e.getMessage());
            sessionManager.rollbackSession();
        }
    
        logger.info(END);
    }
    
    @Override
    public void update(T objectData) throws IllegalAccessException {
        logger.info("============UPDATE OBJECT============");
        ReflectionData reflectionData = getReflectionData(objectData.getClass());
        
        DynamicQueryBuilder queryBuilder = new DynamicQueryBuilder();
        
        queryBuilder.setQueryType(DynamicQueryBuilder.QueryType.UPDATE)
                    .setTableName(reflectionData.getClassName().toLowerCase());
        
        for (Field field : reflectionData.getFields()) {
            field.setAccessible(true);
            if (!field.equals(reflectionData.getIdField())) {
                queryBuilder.addColumn(field.getName(), field.get(objectData));
            } else {
                queryBuilder.addWhereClauseColumns(field.getName(), field.get(objectData));
            }
        }
        
        String sql = queryBuilder.query();
        
        sessionManager.beginSession();
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        
        try (PreparedStatement ps = currentSession.getConnection().prepareStatement(sql)) {
            
            prepareValues(ps, queryBuilder.getValues());
            
            logger.info("Executing query...");
            
            ps.executeUpdate();
            sessionManager.commitSession();
            
            logger.info("Object updated: {}", objectData);
        } catch (SQLException e) {
            logger.error("Object updating error {} with message {}", objectData, e.getMessage());
            sessionManager.rollbackSession();
        }
        
        logger.info(END);
    }
    
    @Override
    public void createOrUpdate(T objectData) throws IllegalAccessException {
        logger.info("============CREATE OR UPDATE OBJECT============");
        ReflectionData reflectionData = getReflectionData(objectData.getClass());
        
        final Field idField = reflectionData.getIdField();
        
        final Object id = idField.get(objectData);
        
        if (ClassUtils.isPrimitiveOrWrapper(idField.getType())) {
            if (id == null || id.toString().equals(ZERO)) {
                create(objectData);
            } else {
                update(objectData);
            }
        }
    }
    
    @Override
    public <T1> T1 load(long id, Class<T1> clazz) throws Exception {
        logger.info("============LOAD OBJECT FROM DB============");
        T1 objectData = clazz.getConstructor().newInstance();
        
        ReflectionData reflectionData = getReflectionData(clazz);
        if (!reflectionData.isHasIdAnnotation()) throw new MissingIdAnnotationException();
        
        DynamicQueryBuilder queryBuilder = new DynamicQueryBuilder();
        
        String sql = queryBuilder.setQueryType(DynamicQueryBuilder.QueryType.SELECT)
                                 .setTableName(reflectionData.getClassName().toLowerCase())
                                 .addWhereClauseColumns(reflectionData.getIdField().getName(), id)
                                 .query();
        
        
        sessionManager.beginSession();
        
        try (PreparedStatement ps = sessionManager.getCurrentSession()
                                                  .getConnection()
                                                  .prepareStatement(sql)) {
            ps.setLong(1, id);
            
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    final Field[] fields = reflectionData.getFields();
                    
                    for (int i = 0; i < fields.length; i++) {
                        getParameter(i + 1, resultSet, fields[i], objectData);
                    }
                }
            }
            
        }
        
        sessionManager.commitSession();
        logger.info("Fetched object: {}", objectData);
        logger.info(END);
        return objectData;
    }
    
    private void prepareValues(PreparedStatement ps, Object[] values) throws SQLException {
        logger.info("Query values: {}", (Object) values);
        
        for (int i = 0; i < values.length; i++) {
            addParameter(i + 1, ps, values[i]);
        }
    }
    
    
    private ReflectionData getReflectionData(Class<?> aClass) {
        ReflectionData reflectionData;
        
        if (reflectionCache.containsKey(aClass)) {
            reflectionData = reflectionCache.get(aClass);
        } else {
            Field[] declaredFields = aClass.getDeclaredFields();
            
            reflectionData = new ReflectionData();
            reflectionData.setFields(declaredFields);
            reflectionData.setClassName(aClass.getSimpleName());
            
            for (Field field : declaredFields) {
                Id declaredAnnotation = field.getDeclaredAnnotation(Id.class);
    
                if (declaredAnnotation != null) {
                    reflectionData.setHasIdAnnotation(true);
                    reflectionData.setIdField(field);
                }
            }
            
            reflectionCache.put(aClass, reflectionData);
        }
        
        if (!reflectionData.isHasIdAnnotation()) throw new MissingIdAnnotationException();
        
        return reflectionData;
    }
    
    private void addParameter(int index, PreparedStatement ps, Object value) throws SQLException {
        Class<?> aClass = value.getClass();
        
        if (aClass.equals(Byte.class)) {
            ps.setByte(index, (Byte) value);
        } else if (aClass.equals(Short.class)) {
            ps.setShort(index, (Short) value);
        } else if (aClass.equals(Integer.class)) {
            ps.setInt(index, (Integer) value);
        } else if (aClass.equals(Long.class)) {
            ps.setLong(index, (Long) value);
        } else if (aClass.equals(Float.class)) {
            ps.setFloat(index, (Float) value);
        } else if (aClass.equals(Double.class)) {
            ps.setDouble(index, (Double) value);
        } else if (aClass.equals(BigDecimal.class)) {
            ps.setBigDecimal(index, (BigDecimal) value);
        } else if (aClass.equals(Boolean.class)) {
            ps.setBoolean(index, (Boolean) value);
        } else if (aClass.equals(String.class) || aClass.equals(Character.class)) {
            ps.setString(index, value.toString());
        }
    }
    
    private void getParameter(int index, ResultSet rs, Field field, Object object) throws SQLException, IllegalAccessException {
        field.setAccessible(true);
        
        final ResultSetMetaData metaData   = rs.getMetaData();
        final String            columnType = metaData.getColumnClassName(index);
        
        if (columnType.equals(Byte.class.getName())) {
            field.set(object, rs.getByte(index));
        } else if (columnType.equals(Short.class.getName())) {
            field.set(object, rs.getShort(index));
        } else if (columnType.equals(Integer.class.getName())) {
            field.set(object, rs.getInt(index));
        } else if (columnType.equals(Long.class.getName())) {
            field.set(object, rs.getLong(index));
        } else if (columnType.equals(Float.class.getName())) {
            field.set(object, rs.getFloat(index));
        } else if (columnType.equals(Double.class.getName())) {
            field.set(object, rs.getDouble(index));
        } else if (columnType.equals(BigDecimal.class.getName())) {
            field.set(object, rs.getBigDecimal(index));
        } else if (columnType.equals(Boolean.class.getName())) {
            field.set(object, rs.getBoolean(index));
        } else if (columnType.equals(String.class.getName()) || columnType.equals(Character.class.getName())) {
            field.set(object, rs.getString(index));
        }
    }
   
}
