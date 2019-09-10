package ru.otus.hw08;

@SuppressWarnings("WeakerAccess")
public class Example2 {

    private int anInt;

    private char[] chars;

    private boolean aBoolean;

    public Example2() {
        this.anInt = 92;
        this.chars = new char[]{'a', 'b', 'c'};
        this.aBoolean = true;
    }

    public int getAnInt() {
        return anInt;
    }

    public char[] getChars() {
        return chars;
    }

    public boolean isaBoolean() {
        return aBoolean;
    }
}
