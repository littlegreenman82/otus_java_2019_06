package ru.otus.hw07.department.service;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.department.base.BalanceService;
import ru.otus.hw07.department.base.Department;

import java.util.List;
import java.util.SortedSet;

public class FetchBalanceService implements BalanceService {
    @Override
    public int visit(Department department) {

        List<Atm> atmList = department.getAtmList();

        int balance = 0;

        for (Atm atm : atmList) {
            balance += atm.accept(this);
        }

        return balance;
    }

    @Override
    public int visit(Atm atm) {
        SortedSet<Cassette> cassettes = atm.getCassettes();

        int balance = 0;
        for (Cassette cassette : cassettes) {
            balance += cassette.balance();
        }

        return balance;
    }
}
