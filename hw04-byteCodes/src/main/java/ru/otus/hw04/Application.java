package ru.otus.hw04;

class Application {
    public static void main(String[] args) {
        TestLogging testLogging = new TestLogging();

        testLogging.testWrapperTypedParams(1, "2");
        testLogging.testPrimitiveTypedParams(1, 2f);
        testLogging.testMixedTypedParams("1", 2);
        testLogging.testEmptyParam();
        testLogging.testNotLoggedMethod();
    }
}