package ru.otus.hw12.service;

import org.hibernate.Hibernate;
import ru.otus.hw12.api.UserDbService;
import ru.otus.hw12.dao.UserDao;
import ru.otus.hw12.entity.User;
import ru.otus.hw12.hibernate.exception.DbServiceException;

import java.util.List;
import java.util.Optional;

public class UserDbServiceImpl implements UserDbService {
    private final UserDao userDao;

    public UserDbServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<User> get(long id) throws DbServiceException {
        try (var sessionManager = userDao.getSessionManager()) {
            try {
                sessionManager.beginSession();
                Optional<User> optionalUser = userDao.get(id);
                sessionManager.commitSession();
                return optionalUser;
            } catch (RuntimeException e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public void save(User object) throws DbServiceException {
        try (var sessionManager = userDao.getSessionManager()) {
            try {
                sessionManager.beginSession();
                userDao.save(object);
                sessionManager.commitSession();
            } catch (RuntimeException e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public void update(User object) throws DbServiceException {
        try (var sessionManager = userDao.getSessionManager()) {
            try {
                sessionManager.beginSession();
                userDao.update(object);
                sessionManager.commitSession();
            } catch (RuntimeException e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public void delete(User object) throws DbServiceException {
        try (var sessionManager = userDao.getSessionManager()) {
            try {
                sessionManager.beginSession();
                userDao.delete(object);
                sessionManager.commitSession();
            } catch (RuntimeException e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public List<User> findAll() throws DbServiceException {
        try (var sessionManager = userDao.getSessionManager()) {
            try {
                sessionManager.beginSession();
                var all = userDao.findAll();
                all.forEach(user -> Hibernate.initialize(user.getPhones()));
                sessionManager.commitSession();

                return all;
            } catch (RuntimeException e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }
}
