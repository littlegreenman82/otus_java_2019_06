package ru.otus.hw10.hibernate.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw10.api.DbExecutor;
import ru.otus.hw10.api.SessionManager;
import ru.otus.hw10.hibernate.exception.DbExecutorException;

public class DbExecutorImpl<T> implements DbExecutor<T> {

    private static final String END = "============END============";
    private final SessionManager sessionManager;
    private Logger logger = LoggerFactory.getLogger(DbExecutorImpl.class);

    public DbExecutorImpl(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void create(T objectData) throws DbExecutorException {
        logger.info("============CREATE OBJECT============");
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().save(objectData);
            sessionManager.commitSession();

            logger.info("Object created {}", objectData);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Error creating object {}", objectData);
            sessionManager.rollbackSession();
            throw new DbExecutorException();
        } finally {
            sessionManager.close();
        }

        logger.info(END);
    }

    @Override
    public void update(T objectData) throws DbExecutorException {
        logger.info("============UPDATE OBJECT============");
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().update(objectData);
            sessionManager.commitSession();

            logger.info("Object updated {}", objectData);
        } catch (Exception e) {
            sessionManager.rollbackSession();
            logger.error("Error updating object {}", objectData);
            throw new DbExecutorException();
        } finally {
            sessionManager.close();
        }

        logger.info(END);
    }

    @Override
    public void createOrUpdate(T objectData) throws DbExecutorException {
        logger.info("============CREATE OR UPDATE OBJECT============");

        try {
            sessionManager.beginSession();

            sessionManager.getCurrentSession().getHibernateSession().saveOrUpdate(objectData);
            sessionManager.commitSession();

            logger.info("Object created or updated {}", objectData);
        } catch (Exception e) {
            sessionManager.rollbackSession();
            logger.error("Error creating or updating {}", objectData);
            throw new DbExecutorException();
        } finally {
            sessionManager.close();
        }
    }

    @Override
    public <T1> T1 load(long id, Class<T1> clazz) throws DbExecutorException {
        logger.info("============LOAD OBJECT FROM DB============");
        T1 objectData = null;

        try {
            sessionManager.beginSession();
            objectData = sessionManager.getCurrentSession().getHibernateSession().get(clazz, id);

            logger.info("Object loaded {}", objectData);
        } catch (Exception e) {
            logger.error("Error loading {}", objectData);
            throw new DbExecutorException(e.getMessage());
        } finally {
            sessionManager.close();
        }

        logger.info("Fetched object: {}", objectData);
        logger.info(END);

        return objectData;
    }

    @Override
    public void delete(T objectData) throws DbExecutorException {
        logger.info("============DELETE OBJECT============");
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().remove(objectData);
            sessionManager.commitSession();

            logger.info("Object deleted {}", objectData);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error("Error deleting object {}", objectData);
            throw new DbExecutorException();
        } finally {
            sessionManager.close();
        }

        logger.info(END);
    }
}
