package ru.otus.hw06.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.atm.cassette.CassetteInterface;
import ru.otus.hw06.atm.cassette.FaceValue;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test ATM")
class AtmTest {

    private Atm atm;

    @BeforeEach
    void setUp() {
        this.atm = Atm.init();
    }

    @Test
    void getCassettes() {
        assertEquals(4, atm.getCassettes().size());
    }

    @Test
    void getCassettes_isSorted_ASC() {
        int last = 100000;
        for (CassetteInterface cassette : atm.getCassettes()) {
            assertThat(cassette.getFaceValue().getValue()).isLessThan(last);
            last = cassette.getFaceValue().getValue();
        }
    }

    @Test
    void deposit() {
        assertDoesNotThrow( () -> this.atm.deposit(FaceValue.FIFTY, 5));
        assertDoesNotThrow( () -> this.atm.deposit(FaceValue.HUNDRED, 100));
    }

    @Test
    void init() {
        var atm = Atm.init();

        assertThat(atm).isInstanceOf(Atm.class);
    }

    @Test
    void balance() {
        atm.deposit(FaceValue.FIFTY, 5);
        atm.deposit(FaceValue.HUNDRED, 100);

        int balance = (FaceValue.HUNDRED.getValue() * 100) + (FaceValue.FIFTY.getValue() * 5);

        assertEquals(atm.balance(), balance);
    }

    @Test
    void withdraw() {
        atm.deposit(FaceValue.FIFTY, 10);
        atm.deposit(FaceValue.HUNDRED, 10);
        atm.deposit(FaceValue.FIVE_HUNDRED, 10);
        atm.deposit(FaceValue.THOUSAND, 10);

        Map<Integer, Integer> withdraw = atm.withdraw(10650);
        assertEquals(atm.balance(), 5850);

        withdraw.forEach((Integer faceValue, Integer count) -> {
                             if (faceValue == 1000) {
                                 assertEquals(count.intValue(), 10);
                             }
                             if (faceValue == 500) {
                                 assertEquals(count.intValue(), 1);
                             }
                             if (faceValue == 100) {
                                 assertEquals(count.intValue(), 1);
                             }
                             if (faceValue == 50) {
                                 assertEquals(count.intValue(), 1);
                             }
                         }
                        );
    }
}
