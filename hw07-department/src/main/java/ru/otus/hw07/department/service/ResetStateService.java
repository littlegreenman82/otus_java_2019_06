package ru.otus.hw07.department.service;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.atm.exception.StateNotFoundException;
import ru.otus.hw07.atm.state.CareTaker;
import ru.otus.hw07.atm.state.Originator;
import ru.otus.hw07.department.base.Service;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class ResetStateService implements Service {

    @Override
    public void visit(Atm atm) {
        SortedSet<Cassette> cassettes = atm.getCassettes();
        Originator originator = atm.getOriginator();
        CareTaker careTaker = atm.getCareTaker();

        TreeSet<Cassette> savedCassettes = new TreeSet<>(
                Comparator.comparingInt((Cassette o) -> o.getFaceValue().getValue()).reversed()
        );

        try {
            for (Cassette cassette : cassettes) {
                originator.getStateFromMemento(careTaker.get(cassette));
                savedCassettes.add(originator.getState());
            }

            atm.setCassettes(savedCassettes);

        } catch (StateNotFoundException e) {
            e.printStackTrace();
        }
    }
}
