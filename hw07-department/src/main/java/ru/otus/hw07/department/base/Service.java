package ru.otus.hw07.department.base;

import ru.otus.hw07.atm.Atm;

public interface Service {

    void visit(Department department);

    void visit(Atm atm);
}
