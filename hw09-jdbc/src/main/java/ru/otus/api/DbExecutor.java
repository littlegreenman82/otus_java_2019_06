package ru.otus.api;

public interface DbExecutor<T> {

    void create(T objectData) throws IllegalAccessException;

    void update(T objectData);

    void createOrUpdate(T objectData);

    <T1> T1 load(long id, Class<T1> clazz);
}
