package ru.otus.hw05;

public interface MemoryLeakProcessMBean {
    public void run() throws OutOfMemoryError;

    public int getSize();
}
