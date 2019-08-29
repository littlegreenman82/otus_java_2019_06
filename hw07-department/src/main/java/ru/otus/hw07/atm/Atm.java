package ru.otus.hw07.atm;

import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.atm.cassette.FaceValue;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.StateNotFoundException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;

import java.util.Map;
import java.util.SortedSet;

public interface Atm {

    SortedSet<Cassette> getCassettes();

    void addCassette(Cassette cassette);

    void deposit(FaceValue faceValue, int banknotesCount) throws UnsupportedFaceValueException;

    int balance();

    void reset() throws StateNotFoundException;

    void saveState();

    Map<FaceValue, Integer> withdraw(int amount) throws UnsupportedFaceValueException, NotEnoughAmountException;
}
