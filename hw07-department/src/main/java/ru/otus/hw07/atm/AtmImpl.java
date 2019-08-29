package ru.otus.hw07.atm;

import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.atm.cassette.CassetteImpl;
import ru.otus.hw07.atm.cassette.FaceValue;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.StateNotFoundException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;
import ru.otus.hw07.atm.state.CareTaker;
import ru.otus.hw07.atm.state.Originator;

import java.util.*;

public class AtmImpl implements Atm {

    private TreeSet<Cassette> cassettes = new TreeSet<>(
            Comparator.comparingInt((Cassette o) -> o.getFaceValue().getValue()).reversed()
    );

    private Originator originator = new Originator();

    private CareTaker careTaker = new CareTaker();

    @Override
    public SortedSet<Cassette> getCassettes() {
        return cassettes;
    }

    @Override
    public void addCassette(Cassette cassette) {
        this.cassettes.add(cassette);
    }

    @Override
    public void deposit(FaceValue faceValue, int banknotesCount) throws UnsupportedFaceValueException {
        var cassette = findCassetteByFaceValue(faceValue);

        cassette.add(banknotesCount);
    }

    @Override
    public int balance() {
        int balance = 0;

        for (Cassette cassette : cassettes) {
            balance += cassette.balance();
        }

        return balance;
    }

    @Override
    public void reset() throws StateNotFoundException {
        TreeSet<Cassette> savedCassettes = new TreeSet<>(
                Comparator.comparingInt((Cassette o) -> o.getFaceValue().getValue()).reversed()
        );

        for (Cassette cassette : cassettes) {
            originator.getStateFromMemento(careTaker.get(cassette));
            savedCassettes.add(originator.getState());
        }

        cassettes = savedCassettes;
    }

    @Override
    public void saveState() {
        for (Cassette cassette : cassettes) {
            originator.setState(cassette);
            careTaker.add(cassette, originator.saveStateToMemento());
        }
    }

    @Override
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

    private Cassette findCassetteByFaceValue(FaceValue faceValue) throws UnsupportedFaceValueException {
        for (Cassette cassette : cassettes) {
            if (cassette.getFaceValue().equals(faceValue)) {
                return cassette;
            }
        }

        throw new UnsupportedFaceValueException("Кассета с номиналом " + faceValue.getValue() + "не установлена");
    }

    public static Atm initState1() {
        var atm = new AtmImpl();

        atm.addCassette(new CassetteImpl(FaceValue.FIFTY));
        atm.addCassette(new CassetteImpl(FaceValue.THOUSAND));
        atm.addCassette(new CassetteImpl(FaceValue.HUNDRED));
        atm.addCassette(new CassetteImpl(FaceValue.FIVE_HUNDRED));

        try {
            atm.deposit(FaceValue.FIFTY, 10);
            atm.deposit(FaceValue.THOUSAND, 10);
            atm.deposit(FaceValue.HUNDRED, 10);
            atm.deposit(FaceValue.FIVE_HUNDRED, 10);
        } catch (UnsupportedFaceValueException e) {
            System.out.println(e.getMessage());
        }

        return atm;
    }

    public static Atm initState2() {
        var atm = new AtmImpl();

        atm.addCassette(new CassetteImpl(FaceValue.FIFTY));
        atm.addCassette(new CassetteImpl(FaceValue.THOUSAND));
        atm.addCassette(new CassetteImpl(FaceValue.HUNDRED));
        atm.addCassette(new CassetteImpl(FaceValue.FIVE_HUNDRED));

        try {
            atm.deposit(FaceValue.FIFTY, 20);
            atm.deposit(FaceValue.THOUSAND, 20);
            atm.deposit(FaceValue.HUNDRED, 20);
            atm.deposit(FaceValue.FIVE_HUNDRED, 20);
        } catch (UnsupportedFaceValueException e) {
            System.out.println(e.getMessage());
        }

        return atm;
    }

    public static Atm initState3() {
        var atm = new AtmImpl();

        atm.addCassette(new CassetteImpl(FaceValue.FIFTY));
        atm.addCassette(new CassetteImpl(FaceValue.THOUSAND));
        atm.addCassette(new CassetteImpl(FaceValue.HUNDRED));
        atm.addCassette(new CassetteImpl(FaceValue.FIVE_HUNDRED));

        try {
            atm.deposit(FaceValue.FIFTY, 30);
            atm.deposit(FaceValue.THOUSAND, 30);
            atm.deposit(FaceValue.HUNDRED, 30);
            atm.deposit(FaceValue.FIVE_HUNDRED, 30);
        } catch (UnsupportedFaceValueException e) {
            System.out.println(e.getMessage());
        }

        return atm;
    }
}
