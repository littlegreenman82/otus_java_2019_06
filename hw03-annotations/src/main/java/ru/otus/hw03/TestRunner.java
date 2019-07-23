package ru.otus.hw03;

import java.lang.reflect.InvocationTargetException;

public class TestRunner {
    public static void main(String[] args) throws
            ClassNotFoundException,
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException {

        for (String arg : args) {
            new TestExecutor(arg).run();
        }
    }
}