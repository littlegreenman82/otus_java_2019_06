package ru.otus.hw13.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(long id);

    void save(T object);

    void update(T object);

    void delete(T object);

    List<T> findAll();
}
