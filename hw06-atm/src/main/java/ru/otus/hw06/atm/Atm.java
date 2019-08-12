package ru.otus.hw06.atm;

import ru.otus.hw06.atm.cassette.Cassette;
import ru.otus.hw06.atm.cassette.CassetteInterface;
import ru.otus.hw06.atm.cassette.FaceValue;

import java.util.*;

@SuppressWarnings("WeakerAccess")
public class Atm {

    private final List<CassetteInterface> cassettes = new ArrayList<>();

    private void addCassette(CassetteInterface cassette) {
        this.cassettes.add(cassette);
    }

    private CassetteInterface findCassetteByFaceValue(FaceValue faceValue) {
        for (CassetteInterface cassette : cassettes) {
            if (cassette.getFaceValue().equals(faceValue)) {
                return cassette;
            }
        }

        throw new IllegalArgumentException();
    }

    private void sortCassettesDesc() {
        cassettes.sort(Comparator.comparingInt((CassetteInterface o) -> o.getFaceValue().getValue()).reversed());
    }

    public List<CassetteInterface> getCassettes() {
        return cassettes;
    }

    public void deposit(FaceValue faceValue, int banknotesCount) {
        CassetteInterface cassette = findCassetteByFaceValue(faceValue);
        cassette.add(banknotesCount);
    }

    public Map<Integer, Integer> withdraw(int amount) throws IllegalArgumentException {
        if (amount > balance())
            throw new IllegalArgumentException("Недостаточно суммы для выдачи");

        Map<Integer, Integer> withdraw = new TreeMap<>(Collections.reverseOrder());

        for (CassetteInterface cassette : cassettes) {
            int withdrawBanknotesCount = cassette.withdraw(amount);

            amount -= withdrawBanknotesCount * cassette.getFaceValue().getValue();

            int alreadyWithdrawBanknotesCount = 0;
            if (withdraw.containsKey(cassette.getFaceValue().getValue())) {
                alreadyWithdrawBanknotesCount = withdraw.get(cassette.getFaceValue().getValue());
            }

            withdraw.put(cassette.getFaceValue().getValue(), withdrawBanknotesCount + alreadyWithdrawBanknotesCount);
        }

        if (amount != 0) {
            throw new IllegalArgumentException("Сумма должна быть кратной: "
                                                       + cassettes.get(cassettes.size() - 1).getFaceValue().getValue());
        }


        return withdraw;
    }

    public int balance() {
        int balance = 0;

        for (CassetteInterface cassette : cassettes) {
            balance += cassette.getTotalAmount();
        }

        return balance;
    }

    public static Atm init() {
        var atm = new Atm();

        atm.addCassette(new Cassette(FaceValue.FIFTY));
        atm.addCassette(new Cassette(FaceValue.THOUSAND));
        atm.addCassette(new Cassette(FaceValue.HUNDRED));
        atm.addCassette(new Cassette(FaceValue.FIVE_HUNDRED));

        atm.sortCassettesDesc();

        return atm;
    }
}
