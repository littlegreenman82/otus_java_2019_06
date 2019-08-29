package ru.otus.hw07;

import ru.otus.hw07.atm.AtmImpl;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.StateNotFoundException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;
import ru.otus.hw07.department.Department;

public class Application {
    private static final String LINE_DELIMITER = "-----------------";

    public static void main(String[] args) {
        var atm1 = AtmImpl.initState1();
        var atm2 = AtmImpl.initState2();
        var atm3 = AtmImpl.initState3();

        Department department = new Department();
        department.atmList.attach(atm1, atm2, atm3);

        department.saveState();

        System.out.println(LINE_DELIMITER);
        System.out.println("Базовый баланс: " + department.balance());
        System.out.println(LINE_DELIMITER);

        try {
            atm1.withdraw(1000);
            atm3.withdraw(30000);
        } catch (UnsupportedFaceValueException | NotEnoughAmountException e) {
            System.out.println(e.getMessage());
        }


        System.out.println("Баланс после снятия: " + department.balance());
        System.out.println(LINE_DELIMITER);

        try {
            department.reset();
        } catch (StateNotFoundException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Баланс после отката к сохраненному состоянию: " + department.balance());
    }
}
