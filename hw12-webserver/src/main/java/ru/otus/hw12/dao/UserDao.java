package ru.otus.hw12.dao;

import ru.otus.hw12.api.SessionManager;
import ru.otus.hw12.entity.User;
import ru.otus.hw12.hibernate.dao.Dao;

import java.util.List;
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

    @Override
    public List<User> all() {
        return sessionManager.getCurrentSession()
                .getHibernateSession()
                .createQuery("select u from ru.otus.hw12.entity.User u", User.class).getResultList();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
