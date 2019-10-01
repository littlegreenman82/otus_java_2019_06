package ru.otus.hw10.dao;

import org.hibernate.Hibernate;
import ru.otus.hw10.api.SessionManager;
import ru.otus.hw10.entity.User;
import ru.otus.hw10.hibernate.dao.Dao;

import java.util.Optional;

public class UserDao implements Dao<User> {

    private final SessionManager sessionManager;

    public UserDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> get(long id) {
        User user = null;

        try {
            sessionManager.beginSession();
            user = sessionManager.getCurrentSession().getHibernateSession().find(User.class, id);

            Hibernate.initialize(user.getPhones());
            Hibernate.initialize(user.getAddress());

            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void save(User object) {
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().save(object);
            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }
    }

    @Override
    public void update(User object) {
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().update(object);
            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }
    }

    @Override
    public void delete(User object) {
        try {
            sessionManager.beginSession();
            sessionManager.getCurrentSession().getHibernateSession().delete(object);
            sessionManager.commitSession();
        } catch (RuntimeException e) {
            sessionManager.rollbackSession();
        } finally {
            sessionManager.close();
        }
    }
}
