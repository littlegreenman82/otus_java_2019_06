package ru.otus.hw12.hibernate.sessionmanager;

@SuppressWarnings("WeakerAccess")
public class SessionManagerException extends RuntimeException {
    public SessionManagerException(String message) {
        super(message);
    }

    public SessionManagerException(Exception e) {
        super(e);
    }
}
