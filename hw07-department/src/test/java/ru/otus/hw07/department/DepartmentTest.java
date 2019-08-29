package ru.otus.hw07.department;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw07.atm.Atm;
import ru.otus.hw07.atm.AtmImpl;
import ru.otus.hw07.atm.exception.StateNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    private Department department;

    private Atm atm1;
    private Atm atm2;
    private Atm atm3;

    @BeforeEach
    void setUp() {
        atm1 = AtmImpl.initState1();
        atm2 = AtmImpl.initState2();
        atm3 = AtmImpl.initState3();

        Department department = new Department();
        department.atmList.attach(atm1, atm2, atm3);
        this.department = department;
    }

    @Test
    void reset_ifStateIsUndefined() {
        assertThrows(StateNotFoundException.class, () -> department.reset());
    }

    @Test
    void reset_ifStateIsDefined() {
        department.saveState();
        assertDoesNotThrow(() -> department.reset());
    }

    @Test
    void reset_ifAmountWasWithdrawn() {
        department.saveState();

        int balanceOnSavedState = department.balance();

        assertDoesNotThrow(() -> {
            atm1.withdraw(5000);
            atm2.withdraw(1000);
            atm3.withdraw(10000);
            department.reset();
        });

        assertEquals(balanceOnSavedState, department.balance());
    }

    @Test
    void balance() {
        assertEquals(99000, department.balance());
    }
}