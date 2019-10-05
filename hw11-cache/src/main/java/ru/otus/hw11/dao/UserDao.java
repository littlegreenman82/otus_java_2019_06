package ru.otus.hw11.dao;

import ru.otus.hw11.api.SessionManager;
import ru.otus.hw11.entity.User;
import ru.otus.hw11.hibernate.dao.Dao;

import java.util.Optional;

public class UserDao implements Dao<User> {

    private final SessionManager sessionManager;

    public UserDao(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(
                sessionManager.getCurrentSession().getHibernateSession().find(User.class, id)
        );
    }

    @Override
    public void update(User object) {
        sessionManager.getCurrentSession().getHibernateSession().update(object);
    }

    @Override
    public void save(User object) {
        sessionManager.getCurrentSession().getHibernateSession().save(object);
    }

    @Override
    public void delete(User object) {
        sessionManager.getCurrentSession().getHibernateSession().delete(object);
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
