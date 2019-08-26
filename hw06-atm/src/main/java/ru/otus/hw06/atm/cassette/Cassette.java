package ru.otus.hw06.atm.cassette;

public interface Cassette {
    FaceValue getFaceValue();

    void add(int faceValueCount);

    void remove(int faceValueCount);

    int getTotalAmount();

    int acceptableWithdrawal(int amount);

    int withdraw(int amount);
}
