package ru.otus.jdbc.executor;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.DatabaseSession;
import ru.otus.api.DbExecutor;
import ru.otus.api.SessionManager;
import ru.otus.jdbc.exception.DbExecutorException;
import ru.otus.jdbc.exception.MissingIdAnnotationException;
import ru.otus.jdbc.querybuilder.DynamicQueryBuilder;
import ru.otus.jdbc.reflectiondata.ReflectionData;
import ru.otus.jdbc.reflectiondata.ReflectionDataCacheHolder;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbExecutorImpl<T> implements DbExecutor<T> {

    private static final String ZERO = "0";
    private static final String END = "============END============";
    private final SessionManager sessionManager;
    private final ReflectionDataCacheHolder cacheHolder;
    private Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);

    public DbExecutorImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.cacheHolder = new ReflectionDataCacheHolder();
    }

    @Override
    public void create(T objectData) throws IllegalAccessException, DbExecutorException {
        logger.info("============CREATE OBJECT============");
        ReflectionData reflectionData = cacheHolder.getOrSetIfEmpty(objectData.getClass());

        DynamicQueryBuilder queryBuilder = new DynamicQueryBuilder();

        queryBuilder.insert(reflectionData.getClassName().toLowerCase());

        for (Field field : reflectionData.getFields()) {
            field.setAccessible(true);
            if (!field.equals(reflectionData.getIdField())) {
                queryBuilder.value(field.getName(), field.get(objectData));
            }
        }

        String sql = queryBuilder.build();

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
            throw new DbExecutorException();
        }

        logger.info(END);
    }

    @Override
    public void update(T objectData) throws IllegalAccessException, DbExecutorException {
        logger.info("============UPDATE OBJECT============");
        ReflectionData reflectionData = cacheHolder.getOrSetIfEmpty(objectData.getClass());

        DynamicQueryBuilder queryBuilder = new DynamicQueryBuilder();

        queryBuilder.update(reflectionData.getClassName().toLowerCase());

        for (Field field : reflectionData.getFields()) {
            field.setAccessible(true);
            if (!field.equals(reflectionData.getIdField())) {
                queryBuilder.value(field.getName(), field.get(objectData));
            } else {
                queryBuilder.where(field.getName(), "=", field.get(objectData));
            }
        }

        String sql = queryBuilder.build();

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
            throw new DbExecutorException();
        }

        logger.info(END);
    }

    @Override
    public void createOrUpdate(T objectData) throws IllegalAccessException, DbExecutorException {
        logger.info("============CREATE OR UPDATE OBJECT============");
        ReflectionData reflectionData = cacheHolder.getOrSetIfEmpty(objectData.getClass());

        final Field idField = reflectionData.getIdField();

        idField.setAccessible(true);
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

        ReflectionData reflectionData = cacheHolder.getOrSetIfEmpty(clazz);
        if (!reflectionData.isHasIdAnnotation()) throw new MissingIdAnnotationException();

        DynamicQueryBuilder queryBuilder = new DynamicQueryBuilder();

        String sql = queryBuilder.select("*")
                .from(reflectionData.getClassName().toLowerCase())
                .where(reflectionData.getIdField().getName(), "=", id)
                .build();


        sessionManager.beginSession();

        try (PreparedStatement ps = sessionManager.getCurrentSession()
                .getConnection()
                .prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    final Field[] fields = reflectionData.getFields();

                    for (int i = 0; i < fields.length; i++) {
                        final var field = fields[i];

                        field.setAccessible(true);
                        final Class<?> type = field.getType();
                        final var wrapper = ClassUtils.primitiveToWrapper(type);

                        field.set(objectData, resultSet.getObject(i + 1, wrapper));
                    }
                }
            }

        } catch (SQLException e) {
            logger.error("Error fetching {} with id {}", clazz.getSimpleName(), id);
            logger.error("Error message {}", e.getMessage());
            throw new DbExecutorException();
        }

        sessionManager.commitSession();
        logger.info("Fetched object: {}", objectData);
        logger.info(END);
        return objectData;
    }

    private void prepareValues(PreparedStatement ps, Object[] values) throws SQLException {
        logger.info("Query values: {}", (Object) values);

        for (int i = 0; i < values.length; i++) {
            ps.setObject(i + 1, values[i]);
        }
    }
}
