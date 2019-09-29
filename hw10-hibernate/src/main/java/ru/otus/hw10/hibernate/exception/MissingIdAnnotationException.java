package ru.otus.hw10.hibernate.exception;

public class MissingIdAnnotationException extends RuntimeException {
    public MissingIdAnnotationException() {
    }

    public MissingIdAnnotationException(String message) {
        super(message);
    }
}
