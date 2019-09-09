package ru.otus.hw07.department.service;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.department.base.Department;
import ru.otus.hw07.department.base.Service;

import java.util.List;
import java.util.SortedSet;

public class UpdateBalanceService implements Service {
    @Override
    public void visit(Department department) {

        List<Atm> atmList = department.getAtmList();

        int balance = 0;

        for (Atm atm : atmList) {
            atm.accept(this);

            balance += atm.getBalance();
        }

        department.setBalance(balance);
    }

    @Override
    public void visit(Atm atm) {
        SortedSet<Cassette> cassettes = atm.getCassettes();

        int balance = 0;
        for (Cassette cassette : cassettes) {
            balance += cassette.balance();
        }

        atm.setBalance(balance);
    }
}
