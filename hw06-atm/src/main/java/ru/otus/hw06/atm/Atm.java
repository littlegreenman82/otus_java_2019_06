package ru.otus.hw06.atm;

import ru.otus.hw06.atm.cassette.Cassette;
import ru.otus.hw06.atm.cassette.CassetteImpl;
import ru.otus.hw06.atm.cassette.FaceValue;
import ru.otus.hw06.atm.exception.NotEnoughAmountException;
import ru.otus.hw06.atm.exception.UnsupportedFaceValueException;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class Atm {

    private final TreeSet<Cassette> cassettes = new TreeSet<>(
            Comparator.comparingInt((Cassette o) -> o.getFaceValue().getValue()).reversed()
    );

    private Atm(){}

    public SortedSet<Cassette> getCassettes() {
        return cassettes;
    }

    public void deposit(FaceValue faceValue, int banknotesCount) {
        Cassette cassette = findCassetteByFaceValue(faceValue);
        cassette.add(banknotesCount);
    }

    public int balance() {
        int balance = 0;

        for (Cassette cassette : cassettes) {
            balance += cassette.getTotalAmount();
        }

        return balance;
    }

    public Map<FaceValue, Integer> withdraw(int amount) throws NotEnoughAmountException, UnsupportedFaceValueException {

        Map<FaceValue, Integer> withdraw = new TreeMap<>(Collections.reverseOrder());

        checkWithdrawal(amount);

        for (Cassette cassette : cassettes) {
            int withdrawBanknotesCount = cassette.withdraw(amount);

            amount -= withdrawBanknotesCount * cassette.getFaceValue().getValue();

            int alreadyWithdrawBanknotesCount = 0;
            if (withdraw.containsKey(cassette.getFaceValue())) {
                alreadyWithdrawBanknotesCount = withdraw.get(cassette.getFaceValue());
            }

            withdraw.put(cassette.getFaceValue(), withdrawBanknotesCount + alreadyWithdrawBanknotesCount);
        }

        return withdraw;
    }

    private void checkWithdrawal(int amount) throws NotEnoughAmountException, UnsupportedFaceValueException {
        if (amount > balance())
            throw new NotEnoughAmountException("Недостаточно суммы для выдачи");

        for (Cassette cassette : cassettes) {
            int withdrawBanknotesCount = cassette.acceptableWithdrawal(amount);
            amount -= withdrawBanknotesCount * cassette.getFaceValue().getValue();
        }

        if (amount != 0) {
            throw new UnsupportedFaceValueException("Сумма должна быть кратной: "
                                                            + cassettes.last().getFaceValue().getValue());
        }
    }

    private void addCassette(Cassette cassette) {
        this.cassettes.add(cassette);
    }

    private Cassette findCassetteByFaceValue(FaceValue faceValue) {
        for (Cassette cassette : cassettes) {
            if (cassette.getFaceValue().equals(faceValue)) {
                return cassette;
            }
        }

        throw new IllegalArgumentException();
    }

    public static Atm init() {
        var atm = new Atm();

        atm.addCassette(new CassetteImpl(FaceValue.FIFTY));
        atm.addCassette(new CassetteImpl(FaceValue.THOUSAND));
        atm.addCassette(new CassetteImpl(FaceValue.HUNDRED));
        atm.addCassette(new CassetteImpl(FaceValue.FIVE_HUNDRED));

        return atm;
    }
}
