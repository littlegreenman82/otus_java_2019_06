package ru.otus.hw07.department.base;

import ru.otus.hw07.atm.Atm;

import java.util.List;

public interface Department {

    List<Atm> getAtmList();

    int getBalance();

    void setBalance(int balance);

    void accept(Service service);
}
