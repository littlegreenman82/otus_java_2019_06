package ru.otus.hw07.atm.state;

import org.apache.commons.lang3.SerializationUtils;
import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.atm.exception.StateNotFoundException;

public class Originator {
    private byte[] state;

    public Originator(){}

    public Cassette getState() {
        return SerializationUtils.deserialize(state);
    }

    public void setState(Cassette state) {
        this.state = SerializationUtils.serialize(state);
    }

    public Memento saveStateToMemento() {
        return new Memento(state);
    }

    public void getStateFromMemento(Memento memento) throws StateNotFoundException {
        if (memento == null)
            throw new StateNotFoundException("Before restore, state must be saved");

        this.state = memento.getState();
    }
}
