package ru.otus.hw07.atm;

import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.atm.cassette.CassetteImpl;
import ru.otus.hw07.atm.cassette.FaceValue;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;
import ru.otus.hw07.atm.state.CareTaker;
import ru.otus.hw07.atm.state.Originator;
import ru.otus.hw07.department.base.BalanceService;
import ru.otus.hw07.department.base.Service;
import ru.otus.hw07.department.service.FetchBalanceService;

import java.util.*;

import static java.lang.System.out;

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
            out.println(e.getMessage());
        }

        return atm;
    }

    @Override
    public void deposit(FaceValue faceValue, int banknotesCount) throws UnsupportedFaceValueException {
        var cassette = findCassetteByFaceValue(faceValue);

        cassette.add(banknotesCount);
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
            out.println(e.getMessage());
        }

        return atm;
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
            out.println(e.getMessage());
        }

        return atm;
    }

    @Override
    public void setCassettes(TreeSet<Cassette> cassettes) {
        this.cassettes = cassettes;
    }

    @Override
    public void accept(Service service) {
        service.visit(this);
    }

    @Override
    public int accept(BalanceService service) {
        return service.visit(this);
    }

    @Override
    public Originator getOriginator() {
        return originator;
    }

    @Override
    public void setOriginator(Originator originator) {
        this.originator = originator;
    }

    @Override
    public CareTaker getCareTaker() {
        return careTaker;
    }

    @Override
    public void setCareTaker(CareTaker careTaker) {
        this.careTaker = careTaker;
    }

    private Cassette findCassetteByFaceValue(FaceValue faceValue) throws UnsupportedFaceValueException {
        for (Cassette cassette : cassettes) {
            if (cassette.getFaceValue().equals(faceValue)) {
                return cassette;
            }
        }

        throw new UnsupportedFaceValueException("Кассета с номиналом " + faceValue.getValue() + "не установлена");
    }

    private void checkWithdrawal(int amount) throws NotEnoughAmountException, UnsupportedFaceValueException {


        if (amount > accept(new FetchBalanceService()))
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
}
