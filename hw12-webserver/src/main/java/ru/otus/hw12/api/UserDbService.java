package ru.otus.hw12.api;

import ru.otus.hw12.entity.User;
import ru.otus.hw12.hibernate.exception.DbServiceException;

import java.util.List;
import java.util.Optional;

public interface UserDbService {

    Optional<User> get(long id) throws DbServiceException;

    void save(User object) throws DbServiceException;

    void update(User object) throws DbServiceException;

    void delete(User object) throws DbServiceException;

    List<User> findAll() throws DbServiceException;
}
