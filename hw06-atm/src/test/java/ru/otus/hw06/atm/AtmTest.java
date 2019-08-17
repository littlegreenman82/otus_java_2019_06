package ru.otus.hw06.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw06.atm.cassette.Cassette;
import ru.otus.hw06.atm.cassette.FaceValue;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
    void getCassettes_isSorted_DESC() {
        int last = 100000;
        for (Cassette cassette : atm.getCassettes()) {
            assertThat(cassette.getFaceValue().getValue()).isLessThan(last);
            last = cassette.getFaceValue().getValue();
        }
    }

    @Test
    void deposit() {
        assertDoesNotThrow( () -> {
            atm.deposit(FaceValue.FIFTY, 5);
            atm.deposit(FaceValue.HUNDRED, 100);
        });
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
        AtomicReference<Map<FaceValue, Integer>> withdraw = new AtomicReference<>();

        assertDoesNotThrow(() -> {
            atm.deposit(FaceValue.FIFTY, 10);
            atm.deposit(FaceValue.HUNDRED, 10);
            atm.deposit(FaceValue.FIVE_HUNDRED, 10);
            atm.deposit(FaceValue.THOUSAND, 10);

            withdraw.set(atm.withdraw(10650));
        });


        assertEquals(5850, atm.balance());

        withdraw.get().forEach((FaceValue faceValue, Integer count) -> {
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
