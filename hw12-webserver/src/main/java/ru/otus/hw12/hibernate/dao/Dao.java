package ru.otus.hw12.hibernate.dao;

import ru.otus.hw12.entity.User;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<User> get(long id);

    void save(T object);

    void update(T object);

    void delete(T object);

    List<T> all();
}
