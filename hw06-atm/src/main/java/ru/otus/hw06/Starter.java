package ru.otus.hw06;

import ru.otus.hw06.atm.Atm;
import ru.otus.hw06.atm.cassette.FaceValue;

import java.util.Map;

public class Starter {

    public static void main(String[] args) {
        var atm = Atm.init();

        atm.deposit(FaceValue.FIFTY, 10);
        atm.deposit(FaceValue.HUNDRED, 10);
        atm.deposit(FaceValue.FIVE_HUNDRED, 10);
        atm.deposit(FaceValue.THOUSAND, 10);

        System.out.println("Баланс терминала: " + atm.balance());
        System.out.println("----------------------------------");

        int withdrawAmount = 14650;
        System.out.println("Выдача: " + withdrawAmount);
        try {
            Map<Integer, Integer> withdrawBanknotes = atm.withdraw(withdrawAmount);
            withdrawBanknotes.forEach((key, count) ->
                                              System.out.println("Купюр номиналом: " + key +
                                                                         ", кол-во: " + count +
                                                                         " (" + (key * count) + ")"
                                                                )
                                     );
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при выдачи: " + e.getMessage());
        }
        System.out.println("----------------------------------");
        System.out.println("Остаток на счете: " + atm.balance());
    }
}
