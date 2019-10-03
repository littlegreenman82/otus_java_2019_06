package ru.otus.hw10.hibernate.exception;

public class DaoException extends Exception {
    public DaoException() {
    }

    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(Exception e) {
        super(e);
    }
}
