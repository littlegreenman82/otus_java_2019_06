package ru.otus.hw06.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.atm.cassette.Cassette;
import ru.otus.hw06.atm.cassette.FaceValue;
import ru.otus.hw06.atm.exception.NotEnoughAmountException;
import ru.otus.hw06.atm.exception.UnsupportedFaceValueException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        for (Cassette cassette : atm.getCassettes()) {
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
        assertDoesNotThrow(() -> {
            atm.deposit(FaceValue.FIFTY, 5);
            atm.deposit(FaceValue.HUNDRED, 100);
        });

        int balance = (FaceValue.HUNDRED.getValue() * 100) + (FaceValue.FIFTY.getValue() * 5);

        assertEquals(atm.balance(), balance);
    }

    @Test
    void withdraw() {
        assertDoesNotThrow(() -> {
            atm.deposit(FaceValue.FIFTY, 10);
            atm.deposit(FaceValue.HUNDRED, 10);
            atm.deposit(FaceValue.FIVE_HUNDRED, 10);
            atm.deposit(FaceValue.THOUSAND, 10);
        });


        Map<FaceValue, Integer> withdraw = null;

        try {
            withdraw = atm.withdraw(10650);
        } catch (NotEnoughAmountException | UnsupportedFaceValueException e) {
            System.out.println(e.getMessage());
        }

        assertEquals(5850, atm.balance());
        assertNotNull(withdraw);

        withdraw.forEach((FaceValue faceValue, Integer count) -> {
                             if (faceValue.getValue() == 1000) {
                                 assertEquals(10, count.intValue());
                             }
                             if (faceValue.getValue() == 500) {
                                 assertEquals(1, count.intValue());
                             }
                             if (faceValue.getValue() == 100) {
                                 assertEquals(1, count.intValue());
                             }
                             if (faceValue.getValue() == 50) {
                                 assertEquals(1, count.intValue());
                             }
                         }
                        );
    }
}
