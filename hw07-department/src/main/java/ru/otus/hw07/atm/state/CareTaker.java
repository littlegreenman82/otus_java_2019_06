package ru.otus.hw07.atm.state;

import ru.otus.hw07.atm.cassette.Cassette;

import java.util.HashMap;
import java.util.Map;

public class CareTaker {
    private Map<Cassette, Memento> mementoList = new HashMap<>();

    public void add(Cassette cassette, Memento state) {
        mementoList.put(cassette, state);
    }

    public Memento get(Cassette cassette) {
        return mementoList.get(cassette);
    }
}
