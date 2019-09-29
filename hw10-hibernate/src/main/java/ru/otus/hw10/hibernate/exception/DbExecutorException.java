package ru.otus.hw10.hibernate.exception;

public class DbExecutorException extends Exception {
    public DbExecutorException() {
    }

    public DbExecutorException(String msg) {
        super(msg);
    }
}
