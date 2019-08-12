package ru.otus.hw06.atm.cassette;

public interface CassetteInterface {
    FaceValue getFaceValue();

    void add(int faceValueCount);

    void remove(int faceValueCount);

    int getTotalAmount();

    int withdraw(int amount);
}
