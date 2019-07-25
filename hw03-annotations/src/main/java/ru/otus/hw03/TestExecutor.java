package ru.otus.hw03;

import ru.otus.hw03.annotations.After;
import ru.otus.hw03.annotations.Before;
import ru.otus.hw03.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class TestExecutor {

    private Object     instance;
    private Class<?>   aClass;
    private Statistics statistics = new Statistics();

    private List<Method> beforeMethods = new ArrayList<>();
    private List<Method> afterMethods  = new ArrayList<>();
    private List<Method> testMethods   = new ArrayList<>();

    public TestExecutor(String className) throws ClassNotFoundException, IllegalAccessException {
        aClass = Class.forName(className);

        scanAnnotations(aClass);
    }

    private void scanAnnotations(Class<?> aClass) throws IllegalAccessException {
        for (Method declaredMethod : aClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Before.class)) {
                checkAnnotation(declaredMethod);
                beforeMethods.add(declaredMethod);
            }

            if (declaredMethod.isAnnotationPresent(Test.class)) {
                checkAnnotation(declaredMethod);
                testMethods.add(declaredMethod);
            }

            if (declaredMethod.isAnnotationPresent(After.class)) {
                checkAnnotation(declaredMethod);
                afterMethods.add(declaredMethod);
            }
        }
    }

    private void checkAnnotation(Method declaredMethod) throws IllegalAccessException {
        if (Modifier.isStatic(declaredMethod.getModifiers())) {
            throw new IllegalAccessException("This annotation only for non-static method");
        }
    }


    private void runBefore() throws InvocationTargetException, IllegalAccessException {
        for (Method method : beforeMethods) {
            runMethod(method);
        }
    }

    private void runAfter() throws InvocationTargetException, IllegalAccessException {
        for (Method method : afterMethods) {
            runMethod(method);
        }
    }

    private void runTests() {
        for (Method method : testMethods) {
            try {
                instance = aClass.getConstructors()[0].newInstance();

                runBefore();
                runMethod(method);
                runAfter();

                statistics.incSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                statistics.incFailed();
            } finally {
                statistics.incAll();
            }
        }
    }

    private void runMethod(Method method) throws InvocationTargetException, IllegalAccessException {
        if (!method.canAccess(instance))
            method.setAccessible(true);

        method.invoke(instance);
    }

    public void run() {
        runTests();
        System.out.println(statistics);
    }
}
