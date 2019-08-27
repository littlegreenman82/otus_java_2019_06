package ru.otus.hw07;

import ru.otus.hw07.atm.AtmImpl;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;
import ru.otus.hw07.department.Department;

public class Application {

    public static void main(String[] args) {
        var atm1 = AtmImpl.initState1();
        var atm2 = AtmImpl.initState2();
        var atm3 = AtmImpl.initState3();

        Department department = new Department();
        department.atmList.attach(atm1);
        department.atmList.attach(atm2);
        department.atmList.attach(atm3);

        System.out.println(department.balance());

        try {
            atm1.withdraw(1000);
            atm3.withdraw(30000);
        } catch (UnsupportedFaceValueException | NotEnoughAmountException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(department.balance());
    }
}
