package ru.otus.hw10.hibernate.dao;

import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(long id);

    void save(T object);

    void update(T object);

    void delete(T object);
}
