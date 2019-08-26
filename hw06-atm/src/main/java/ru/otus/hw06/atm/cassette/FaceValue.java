package ru.otus.hw06.atm.cassette;

public enum FaceValue {
    FIFTY(50), HUNDRED(100), FIVE_HUNDRED(500), THOUSAND(1000);

    private final int value;

    FaceValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
