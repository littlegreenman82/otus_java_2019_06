package ru.otus.hw07.department.base;

public abstract class DepartmentBase implements Department {

    private int balance = 0;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
