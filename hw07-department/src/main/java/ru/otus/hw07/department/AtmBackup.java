package ru.otus.hw07.department;

import ru.otus.hw07.atm.Atm;

import java.util.List;

class AtmBackup {

    private List<Atm> state;

    void save(List<Atm> machines) {
        this.state = machines;
    }

    List<Atm> restore() {
        return this.state;
    }
}
