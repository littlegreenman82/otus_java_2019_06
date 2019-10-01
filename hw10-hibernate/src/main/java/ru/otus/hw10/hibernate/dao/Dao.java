package ru.otus.hw10.hibernate.dao;

import ru.otus.hw10.hibernate.exception.DaoException;

import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(long id) throws DaoException;

    void save(T object) throws DaoException;

    void update(T object) throws DaoException;

    void delete(T object) throws DaoException;
}
