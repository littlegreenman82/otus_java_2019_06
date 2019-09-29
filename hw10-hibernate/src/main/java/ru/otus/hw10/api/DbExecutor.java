package ru.otus.hw10.api;

import ru.otus.hw10.hibernate.exception.DbExecutorException;

public interface DbExecutor<T> {

    void create(T objectData) throws DbExecutorException;

    void update(T objectData) throws DbExecutorException;

    void createOrUpdate(T objectData) throws DbExecutorException;

    <T1> T1 load(long id, Class<T1> clazz) throws Exception;

    void delete(T objectData) throws DbExecutorException;
}
