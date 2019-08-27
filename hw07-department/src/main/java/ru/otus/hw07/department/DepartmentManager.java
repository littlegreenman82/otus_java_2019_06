package ru.otus.hw07.department;

import ru.otus.hw07.atm.Atm;

import java.util.ArrayList;
import java.util.List;

public class DepartmentManager {

    private List<Atm> atmList = new ArrayList<>();

    private AtmBackup backup = new AtmBackup();

    public void attach(Atm atm) {
        atmList.add(atm);

        backup.save(atmList);
    }

    public void detach(Atm atm) {
        atmList.remove(atm);

        backup.save(atmList);
    }

    int balance() {
        int balance = 0;

        for (Atm atm : atmList)
            balance += atm.balance();

        return balance;
    }

    void reset() {
        atmList = backup.restore();
    }
}
