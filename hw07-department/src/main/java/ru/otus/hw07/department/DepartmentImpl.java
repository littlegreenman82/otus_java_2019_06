package ru.otus.hw07.department;

import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.department.base.DepartmentBase;
import ru.otus.hw07.department.base.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DepartmentImpl extends DepartmentBase {

    private List<Atm> atmList = new ArrayList<>();

    public List<Atm> getAtmList() {
        return atmList;
    }

    @Override
    public void accept(Service service) {
        service.visit(this);
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
