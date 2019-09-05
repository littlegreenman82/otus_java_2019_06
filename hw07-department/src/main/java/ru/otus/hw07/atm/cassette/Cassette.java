package ru.otus.hw07.atm.cassette;

import java.io.Serializable;

public interface Cassette extends Serializable {
    FaceValue getFaceValue();

    void add(int banknotesCount);

    void remove(int banknotesCount);

    int balance();

    int acceptableWithdrawal(int amount);

    int withdraw(int amount);

}
