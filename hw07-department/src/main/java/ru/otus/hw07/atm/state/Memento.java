package ru.otus.hw07.atm.state;

class Memento {
    private byte[] state;

    Memento(byte[] state) {
        this.state = state;
    }

    byte[] getState() {
        return state;
    }
}
