package ru.otus.hw07;

import ru.otus.hw07.atm.AtmImpl;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;
import ru.otus.hw07.department.DepartmentImpl;

import static java.lang.System.out;

public class Application {
    private static final String LINE_DELIMITER = "-----------------";

    public static void main(String[] args) {
        var atm1 = AtmImpl.initState1();
        var atm2 = AtmImpl.initState2();
        var atm3 = AtmImpl.initState3();

        DepartmentImpl department = new DepartmentImpl();
        department.attach(atm1, atm2, atm3);

        department.saveState();

        out.println(LINE_DELIMITER);
        out.println("Базовый баланс: " + department.getBalance());
        out.println(LINE_DELIMITER);

        try {
            atm1.withdraw(1000);
            atm3.withdraw(30000);
        } catch (UnsupportedFaceValueException | NotEnoughAmountException e) {
            out.println(e.getMessage());
        }

        out.println("Баланс после снятия: " + department.getBalance());
        out.println(LINE_DELIMITER);

        department.resetState();

        out.println("Баланс после отката к сохраненному состоянию: " + department.getBalance());
    }
}
