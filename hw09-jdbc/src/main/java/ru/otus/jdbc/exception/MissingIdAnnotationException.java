package ru.otus.jdbc.exception;

public class MissingIdAnnotationException extends RuntimeException {
    public MissingIdAnnotationException() {
    }

    public MissingIdAnnotationException(String message) {
        super(message);
    }
}
