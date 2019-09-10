package ru.otus.hw07.department;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.department.base.BalanceService;
import ru.otus.hw07.department.base.Department;
import ru.otus.hw07.department.base.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DepartmentImpl implements Department {

    private List<Atm> atmList = new ArrayList<>();

    public List<Atm> getAtmList() {
        return atmList;
    }

    @Override
    public void accept(Service service) {
        service.visit(this);
    }

    @Override
    public int accept(BalanceService service) {
        return service.visit(this);
    }

    public void attach(Atm atm) {
        atmList.add(atm);
    }

    public void attach(Atm ...atmList) {
        this.atmList.addAll(Arrays.asList(atmList));
    }

    public void detach(Atm atm) {
        atmList.remove(atm);
    }

}
