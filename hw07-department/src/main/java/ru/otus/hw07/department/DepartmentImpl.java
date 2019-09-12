package ru.otus.hw07.department;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.department.base.Department;
import ru.otus.hw07.department.service.FetchBalanceService;
import ru.otus.hw07.department.service.ResetStateService;
import ru.otus.hw07.department.service.SaveStateService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DepartmentImpl implements Department {

    private final FetchBalanceService fetchBalanceService = new FetchBalanceService();
    private final ResetStateService resetStateService = new ResetStateService();
    private final SaveStateService saveStateService = new SaveStateService();

    private List<Atm> atmList = new ArrayList<>();

    public List<Atm> getAtmList() {
        return atmList;
    }

    @Override
    public void saveState() {
        for (Atm atm : atmList) {
            atm.accept(saveStateService);
        }
    }

    @Override
    public void resetState() {
        for (Atm atm : atmList) {
            atm.accept(resetStateService);
        }
    }

    @Override
    public int getBalance() {
        int balance = 0;
        for (Atm atm : atmList) {
            balance += atm.accept(fetchBalanceService);
        }

        return balance;
    }

    public void attach(Atm atm) {
        atmList.add(atm);
    }

    public void attach(Atm... atmList) {
        this.atmList.addAll(Arrays.asList(atmList));
    }

    public void detach(Atm atm) {
        atmList.remove(atm);
    }

}
