package ru.otus.hw13.exception;

public class DbServiceException extends Exception {
    public DbServiceException() {
    }

    public DbServiceException(String message) {
        super(message);
    }
}
