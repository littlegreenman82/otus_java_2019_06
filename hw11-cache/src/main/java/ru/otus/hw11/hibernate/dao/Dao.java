package ru.otus.hw11.hibernate.dao;

import ru.otus.hw11.entity.User;

import java.util.Optional;

public interface Dao<T> {
    Optional<User> get(long id);

    void save(T object);

    void update(T object);

    void delete(T object);
}
