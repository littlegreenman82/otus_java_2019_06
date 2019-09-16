package ru.otus.hw08.serializer.exception;

public class UnsupportedObjectType extends Exception {
    public UnsupportedObjectType() {
    }

    public UnsupportedObjectType(String message) {
        super(message);
    }
}
