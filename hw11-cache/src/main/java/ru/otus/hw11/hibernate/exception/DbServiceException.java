package ru.otus.hw11.hibernate.exception;

public class DbServiceException extends Exception {
    public DbServiceException() {
    }

    public DbServiceException(String msg) {
        super(msg);
    }

    public DbServiceException(Exception e) {
        super(e);
    }
}
