package ru.otus.hw13.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.otus.hw13.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao implements Dao<User> {

    private final SessionFactory sessionFactory;

    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession().find(User.class, id)
        );
    }

    @Override
    public void save(User object) {
        sessionFactory.getCurrentSession().save(object);
    }

    @Override
    public void update(User object) {
        sessionFactory.getCurrentSession().update(object);
    }

    @Override
    public void delete(User object) {
        sessionFactory.getCurrentSession().delete(object);
    }

    @Override
    public List<User> findAll() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("select u from ru.otus.hw13.entity.User u", User.class)
                .getResultList();
    }
}
