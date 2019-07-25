package ru.otus.hw03;

public class TestRunner {
    public static void main(String[] args) throws Exception{

        for (String arg : args) {
            new TestExecutor(arg).run();
        }
    }
}