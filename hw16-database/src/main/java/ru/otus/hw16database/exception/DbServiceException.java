package ru.otus.hw16database.exception;

public class DbServiceException extends Exception {
    public DbServiceException() {
    }

    public DbServiceException(String message) {
        super(message);
    }
}
