package ru.otus.hw15.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw15.dao.UserDao;
import ru.otus.hw15.entity.User;
import ru.otus.hw15.exception.DbServiceException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public Optional<User> get(long id) throws DbServiceException {
        try {
            return userDao.get(id);
        } catch (Exception e) {
            throw new DbServiceException();
        }
    }

    @Transactional
    @Override
    public void save(User object) throws DbServiceException {
        try {
            userDao.save(object);
        } catch (Exception e) {
            throw new DbServiceException();
        }
    }

    @Transactional
    @Override
    public void update(User object) throws DbServiceException {
        try {
            userDao.update(object);
        } catch (Exception e) {
            throw new DbServiceException();
        }
    }

    @Transactional
    @Override
    public void delete(User object) throws DbServiceException {
        try {
            userDao.delete(object);
        } catch (Exception e) {
            throw new DbServiceException();
        }
    }

    @Transactional
    @Override
    public List<User> findAll() throws DbServiceException {
        try {
            return userDao.findAll();
        } catch (Exception e) {
            throw new DbServiceException();
        }
    }
}
