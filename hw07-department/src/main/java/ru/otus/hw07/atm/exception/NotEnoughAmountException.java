package ru.otus.hw07.atm.exception;

public class NotEnoughAmountException extends Exception {
    public NotEnoughAmountException(String message) {
        super(message);
    }
}
