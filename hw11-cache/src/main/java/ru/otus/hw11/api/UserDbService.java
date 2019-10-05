package ru.otus.hw11.api;

import ru.otus.hw11.entity.User;
import ru.otus.hw11.hibernate.exception.DbServiceException;

import java.util.Optional;

public interface UserDbService {

    Optional<User> get(long id) throws DbServiceException;

    void save(User object) throws DbServiceException;

    void update(User object) throws DbServiceException;

    void delete(User object) throws DbServiceException;
}
