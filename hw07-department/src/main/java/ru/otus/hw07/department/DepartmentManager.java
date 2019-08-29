package ru.otus.hw07.department;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.atm.exception.StateNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DepartmentManager {

    private List<Atm> atmList = new ArrayList<>();

    public void attach(Atm atm) {
        atmList.add(atm);
    }

    public void attach(Atm ...atmList) {
        this.atmList.addAll(Arrays.asList(atmList));
    }

    public void detach(Atm atm) {
        atmList.remove(atm);
    }

    int balance() {
        int balance = 0;

        for (Atm atm : atmList)
            balance += atm.balance();

        return balance;
    }

    void reset() throws StateNotFoundException {
        for (Atm atm: atmList)
            atm.reset();
    }

    public void saveState() {
        for (Atm atm : atmList)
            atm.saveState();
    }
}
