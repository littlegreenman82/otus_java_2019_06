package ru.otus.hw07.department;

import ru.otus.hw07.atm.exception.StateNotFoundException;

public class Department {

    public final DepartmentManager atmList;

    public Department() {
        this.atmList = new DepartmentManager();
    }

    public void reset() throws StateNotFoundException {
        this.atmList.reset();
    }

    public int balance() {
        return this.atmList.balance();
    }

    public void saveState() { this.atmList.saveState(); }
}
