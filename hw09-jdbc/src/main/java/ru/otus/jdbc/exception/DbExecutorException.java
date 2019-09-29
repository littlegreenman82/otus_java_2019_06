package ru.otus.jdbc.exception;

public class DbExecutorException extends Exception {
    public DbExecutorException() {}
    
    public DbExecutorException(String msg) {
        super(msg);
    }
}
