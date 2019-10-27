package ru.otus.hw12.hibernate.exception;

public class MissingIdAnnotationException extends RuntimeException {
    public MissingIdAnnotationException() {
    }

    public MissingIdAnnotationException(String message) {
        super(message);
    }
}
