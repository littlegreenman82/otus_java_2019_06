package ru.otus.hw07.department.service;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.atm.state.CareTaker;
import ru.otus.hw07.atm.state.Originator;
import ru.otus.hw07.department.base.Service;

import java.util.SortedSet;

public class SaveStateService implements Service {
    @Override
    public void visit(Atm atm) {
        SortedSet<Cassette> cassettes = atm.getCassettes();
        Originator originator = atm.getOriginator();
        CareTaker careTaker = atm.getCareTaker();

        for (Cassette cassette : cassettes) {
            originator.setState(cassette);
            careTaker.add(cassette, originator.saveStateToMemento());
        }
    }
}
