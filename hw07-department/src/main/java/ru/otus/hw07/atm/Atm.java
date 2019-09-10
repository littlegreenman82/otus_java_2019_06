package ru.otus.hw07.atm;

import ru.otus.hw07.atm.cassette.Cassette;
import ru.otus.hw07.atm.cassette.FaceValue;
import ru.otus.hw07.atm.exception.NotEnoughAmountException;
import ru.otus.hw07.atm.exception.UnsupportedFaceValueException;
import ru.otus.hw07.atm.state.CareTaker;
import ru.otus.hw07.atm.state.Originator;
import ru.otus.hw07.department.base.BalanceService;
import ru.otus.hw07.department.base.Service;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public interface Atm {

    SortedSet<Cassette> getCassettes();

    void addCassette(Cassette cassette);

    void setCassettes(TreeSet<Cassette> cassettes);

    Originator getOriginator();

    void setOriginator(Originator originator);

    CareTaker getCareTaker();

    void setCareTaker(CareTaker careTaker);

    void deposit(FaceValue faceValue, int banknotesCount) throws UnsupportedFaceValueException;

    void accept(Service service);

    int accept(BalanceService service);

    Map<FaceValue, Integer> withdraw(int amount) throws UnsupportedFaceValueException, NotEnoughAmountException;
}
