package ru.otus.hw11.hibernate.exception;

public class MissingIdAnnotationException extends RuntimeException {
    public MissingIdAnnotationException() {
    }

    public MissingIdAnnotationException(String message) {
        super(message);
    }
}
