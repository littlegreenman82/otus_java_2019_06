package ru.otus.hw15.exception;

public class DbServiceException extends Exception {
    public DbServiceException() {
    }

    public DbServiceException(String message) {
        super(message);
    }
}
