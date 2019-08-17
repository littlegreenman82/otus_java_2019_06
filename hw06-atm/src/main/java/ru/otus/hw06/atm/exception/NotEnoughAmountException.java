package ru.otus.hw06.atm.exception;

public class NotEnoughAmountException extends Exception {
    public NotEnoughAmountException(String message) {
        super(message);
    }
}
