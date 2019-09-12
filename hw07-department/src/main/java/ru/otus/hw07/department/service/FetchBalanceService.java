package ru.otus.hw07.department.service;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.department.base.BalanceService;

import java.util.SortedSet;

public class FetchBalanceService implements BalanceService {

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
