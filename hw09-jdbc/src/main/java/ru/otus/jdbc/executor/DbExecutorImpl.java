package ru.otus.jdbc.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.DatabaseSession;
import ru.otus.api.DbExecutor;
import ru.otus.api.SessionManager;
import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.exception.MissingIdAnnotationException;
import ru.otus.jdbc.querybuilder.DynamicQueryBuilder;
import ru.otus.jdbc.reflectiondata.ReflectionData;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DbExecutorImpl<T> implements DbExecutor<T> {

    private final SessionManager sessionManager;
    private Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);
    private Map<Class, ReflectionData> reflectionCache = new HashMap<>();

    public DbExecutorImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void create(T objectData) throws IllegalAccessException {
        ReflectionData reflectionData = getReflectionData(objectData);
        if (!reflectionData.isHasIdAnnotation()) throw new MissingIdAnnotationException();

        String tableName = objectData.getClass().getSimpleName().toLowerCase();

        DynamicQueryBuilder queryBuilder = new DynamicQueryBuilder();

        queryBuilder.setQueryType(DynamicQueryBuilder.QueryType.INSERT)
                .setTableName(tableName);

        for (Field field : reflectionData.getFields()) {
            field.setAccessible(true);
            if (!field.equals(reflectionData.getIdField())) {
                queryBuilder.addColumn(field.getName(), field.get(objectData));
            }
        }

        String sql = queryBuilder.query();
        DatabaseSession currentSession = sessionManager.getCurrentSession();

        try (PreparedStatement ps = currentSession.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Object[] values = queryBuilder.getValues();

            int index = 1;
            for (Object value : values) {
                addParameter(index, ps, value);
                index++;
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                rs.next();
                reflectionData.getIdField().setAccessible(true);
                reflectionData.getIdField().set(objectData, rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(T objectData) {

    }

    @Override
    public void createOrUpdate(T objectData) {

    }

    @Override
    public <T1> T1 load(long id, Class<T1> clazz) {
        return null;
    }

    private ReflectionData getReflectionData(T objectData) {
        ReflectionData reflectionData;
        Class<?> aClass = objectData.getClass();

        if (reflectionCache.containsKey(aClass)) {
            reflectionData = reflectionCache.get(aClass);
        } else {
            Field[] declaredFields = aClass.getDeclaredFields();

            reflectionData = new ReflectionData();
            reflectionData.setFields(declaredFields);

            for (Field field : declaredFields) {
                Id declaredAnnotation = field.getDeclaredAnnotation(Id.class);

                if (declaredAnnotation != null) {
                    reflectionData.setHasIdAnnotation(true);
                    reflectionData.setIdField(field);
                }
            }

            reflectionCache.put(aClass, reflectionData);
        }

        return reflectionData;
    }

    private void addParameter(int index, PreparedStatement ps, Object value) throws SQLException {
        Class<?> aClass = value.getClass();

        if (aClass.equals(Byte.class)) {
            ps.setByte(index, (byte) value);
        } else if (aClass.equals(Short.class)) {
            ps.setShort(index, (short) value);
        } else if (aClass.equals(Integer.class)) {
            ps.setInt(index, (int) value);
        } else if (aClass.equals(Long.class)) {
            ps.setLong(index, (long) value);
        } else if (aClass.equals(Float.class)) {
            ps.setFloat(index, (float) value);
        } else if (aClass.equals(Double.class)) {
            ps.setDouble(index, (double) value);
        } else if (aClass.equals(Boolean.class)) {
            ps.setBoolean(index, (boolean) value);
        } else if (aClass.equals(String.class) || aClass.equals(Character.class)) {
            ps.setString(index, value.toString());
        }
    }
}
