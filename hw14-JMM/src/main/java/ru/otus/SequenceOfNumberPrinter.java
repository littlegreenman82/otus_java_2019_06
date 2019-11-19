package ru.otus;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("WeakerAccess")
public class SequenceOfNumberPrinter {
    private static final int START_VALUE = 1;
    private static final int END_VALUE = 10;
    private static final int EXIT_VALUE = 0;

    private static final boolean TO_START_VALUE_DIRECTION = false;
    private static final boolean TO_END_VALUE_DIRECTION = true;

    private final Thread thread1;
    private final Thread thread2;

    private AtomicInteger atomicCounter1 = new AtomicInteger(START_VALUE);
    private AtomicInteger atomicCounter2 = new AtomicInteger(START_VALUE);

    private AtomicBoolean needIncrementAtomic = new AtomicBoolean();

    public SequenceOfNumberPrinter() {
        this.thread1 = new Thread(() -> printExecutor(atomicCounter1), "Thread#1");
        this.thread2 = new Thread(() -> printExecutor(atomicCounter2), "Thread#2");
        turnAroundCounting(TO_END_VALUE_DIRECTION);
    }

    public void print() {
        thread1.start();
        thread2.start();
    }

    private synchronized void printExecutor(AtomicInteger atomicCounter) {
        while (atomicCounter.get() != EXIT_VALUE) {
            if (countersHaveReachedEnd()) {
                turnAroundCounting(TO_START_VALUE_DIRECTION);
                skipEndValue();
            }

            System.out.printf(
                    "%s: %d%n",
                    Thread.currentThread().getName(),
                    needIncrementAtomic.get() ? atomicCounter.getAndIncrement() : atomicCounter.getAndDecrement()
            );

            if (isAnyThreadWaiting()) {
                notifyAll();
            }

            if (atomicCounter.get() != EXIT_VALUE) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void skipEndValue() {
        atomicCounter1.set(END_VALUE - 1);
        atomicCounter2.set(END_VALUE - 1);
    }

    private boolean countersHaveReachedEnd() {
        return atomicCounter1.get() > END_VALUE && atomicCounter2.get() > END_VALUE;
    }

    private boolean isAnyThreadWaiting() {
        return thread1.getState().equals(Thread.State.WAITING) || thread2.getState().equals(Thread.State.WAITING);
    }

    private void turnAroundCounting(boolean flag) {
        this.needIncrementAtomic.set(flag);
    }
}
