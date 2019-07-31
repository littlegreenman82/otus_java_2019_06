package ru.otus.hw04;

import ru.otus.hw04.annotations.Log;

@SuppressWarnings("WeakerAccess")
public class TestLogging {
    @Log
    public static void testStaticMethodSupport(String p1) {
    }

    @Log
    public void testWrapperTypedParams(Integer p1, String p2) {
    }

    @Log
    public void testPrimitiveTypedParams(int p1, float p2) {
    }

    @Log
    public void testMixedTypedParams(String p1, int p2) {
    }

    @Log
    public void testEmptyParam(){}


    public void testNotLoggedMethod(){}
}
