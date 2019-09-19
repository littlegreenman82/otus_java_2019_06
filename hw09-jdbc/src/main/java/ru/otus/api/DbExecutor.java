package ru.otus.api;

import ru.otus.jdbc.exception.DbExecutorException;

public interface DbExecutor<T> {
    
    void create(T objectData) throws IllegalAccessException, DbExecutorException;
    
    void update(T objectData) throws IllegalAccessException, DbExecutorException;
    
    void createOrUpdate(T objectData) throws IllegalAccessException, DbExecutorException;
    
    <T1> T1 load(long id, Class<T1> clazz) throws Exception;
}
