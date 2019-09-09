package ru.otus.hw07;

import ru.otus.hw07.atm.AtmImpl;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;
import ru.otus.hw07.department.DepartmentImpl;
import ru.otus.hw07.department.service.ResetStateService;
import ru.otus.hw07.department.service.SaveStateService;
import ru.otus.hw07.department.service.UpdateBalanceService;

import static java.lang.System.out;

public class Application {
    private static final String LINE_DELIMITER = "-----------------";

    public static void main(String[] args) {
        var atm1 = AtmImpl.initState1();
        var atm2 = AtmImpl.initState2();
        var atm3 = AtmImpl.initState3();

        DepartmentImpl department = new DepartmentImpl();
        department.attach(atm1, atm2, atm3);

        department.accept(new SaveStateService());
        department.accept(new UpdateBalanceService());

        out.println(LINE_DELIMITER);
        out.println("Базовый баланс: " + department.getBalance());
        out.println(LINE_DELIMITER);

        try {
            atm1.withdraw(1000);
            atm3.withdraw(30000);
        } catch (UnsupportedFaceValueException | NotEnoughAmountException e) {
            out.println(e.getMessage());
        }

        department.accept(new UpdateBalanceService());
        out.println("Баланс после снятия: " + department.getBalance());
        out.println(LINE_DELIMITER);

        department.accept(new ResetStateService());


        department.accept(new UpdateBalanceService());
        out.println("Баланс после отката к сохраненному состоянию: " + department.getBalance());
    }
}
