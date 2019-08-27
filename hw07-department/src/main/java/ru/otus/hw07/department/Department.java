package ru.otus.hw07.department;

public class Department {

    public final DepartmentManager atmList;

    public Department() {
        this.atmList = new DepartmentManager();
    }

    public void reset() {
        this.atmList.reset();
    }

    public int balance() {
        return this.atmList.balance();
    }
}
