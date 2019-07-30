package ru.otus.hw03.examples;

import ru.otus.hw03.annotations.After;
import ru.otus.hw03.annotations.Before;
import ru.otus.hw03.annotations.Test;

public class TestExample {

    @Before
    public void before() {
        System.out.println("Run before");
    }

    @Before
    public void before2() {
        System.out.println("Run before 2");
    }

    @After
    public void after() {
        System.out.println("Run after");
    }

    @After
    public void after2() {
        System.out.println("Run after 2");
    }

    @Test
    private void test1() {
        System.out.println("Run test 1");
    }

    @Test
    private void test2() {
        throw new IllegalArgumentException("test 2 exception");
    }

    @Test
    private void test3() {
        System.out.println("Run test 3");
    }
}
