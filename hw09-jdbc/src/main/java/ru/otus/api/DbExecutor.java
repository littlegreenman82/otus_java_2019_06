package ru.otus.api;

public interface DbExecutor<T> {

    void create(T objectData) throws IllegalAccessException;
    
    void update(T objectData) throws IllegalAccessException;
    
    void createOrUpdate(T objectData) throws IllegalAccessException;
    
    <T1> T1 load(long id, Class<T1> clazz) throws Exception;
}
