package ru.otus.hw07.department.base;

import ru.otus.hw07.atm.Atm;

public interface BalanceService {

    int visit(Department department);

    int visit(Atm atm);
}
