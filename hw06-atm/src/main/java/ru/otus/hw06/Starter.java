package ru.otus.hw06;

import ru.otus.hw06.atm.Atm;
import ru.otus.hw06.atm.cassette.FaceValue;
import ru.otus.hw06.atm.exception.NotEnoughAmountException;
import ru.otus.hw06.atm.exception.UnsupportedFaceValueException;

import java.util.Map;

import static java.lang.System.out;

public class Starter {

    public static void main(String[] args) {
        var atm = Atm.init();

        atm.deposit(FaceValue.FIFTY, 10);
        atm.deposit(FaceValue.HUNDRED, 10);
        atm.deposit(FaceValue.FIVE_HUNDRED, 10);
        atm.deposit(FaceValue.THOUSAND, 10);

        out.println("Баланс терминала: " + atm.balance());
        out.println("----------------------------------");

        int withdrawAmount = 14650;
        out.println("Выдача: " + withdrawAmount);
        try {
            Map<FaceValue, Integer> withdrawBanknotes = atm.withdraw(withdrawAmount);
            withdrawBanknotes.forEach((faceValue, count) ->
                                              out.println("Купюр номиналом: " + faceValue.getValue() +
                                                                         ", кол-во: " + count +
                                                                  " (" + (faceValue.getValue() * count) + ")"
                                                                )
                                     );
        } catch (UnsupportedFaceValueException | NotEnoughAmountException e) {
            out.println("Ошибка при выдачи: " + e.getMessage());
        }
        out.println("----------------------------------");
        out.println("Остаток на счете: " + atm.balance());
    }
}
