package ru.otus.hw10.dao;

import org.hibernate.Hibernate;
import ru.otus.hw10.api.SessionManager;
import ru.otus.hw10.entity.User;
import ru.otus.hw10.hibernate.dao.Dao;
import ru.otus.hw10.hibernate.exception.DaoException;

import java.util.Optional;

public class UserDao implements Dao<User> {

    private final SessionManager sessionManager;

    public UserDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> get(long id) throws DaoException {
        User user;

        try {
            sessionManager.beginSession();
            user = sessionManager.getCurrentSession().getHibernateSession().find(User.class, id);

            if (user != null) {
                Hibernate.initialize(user.getPhones());
                Hibernate.initialize(user.getAddress());
            }

            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();

            throw new DaoException(e);
        } finally {
            sessionManager.close();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void save(User object) throws DaoException {
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().save(object);
            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();

            throw new DaoException(e);
        } finally {
            sessionManager.close();
        }
    }

    @Override
    public void update(User object) throws DaoException {
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().update(object);
            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();

            throw new DaoException(e);
        } finally {
            sessionManager.close();
        }
    }

    @Override
    public void delete(User object) throws DaoException {
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().delete(object);
            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();

            throw new DaoException(e);
        } finally {
            sessionManager.close();
        }
    }
}
