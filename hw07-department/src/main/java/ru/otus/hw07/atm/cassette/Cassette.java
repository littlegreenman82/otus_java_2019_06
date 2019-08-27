package ru.otus.hw07.atm.cassette;

public interface Cassette {
    FaceValue getFaceValue();

    void add(int banknotesCount);

    void remove(int banknotesCount);

    int balance();

    int acceptableWithdrawal(int amount);

    int withdraw(int amount);
}
