package ru.otus.hw05;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakProcess implements MemoryLeakProcessMBean {

    private List<Object> list = new ArrayList<>();

    private long addIterationCount;

    @Override
    public void run() throws OutOfMemoryError {
        while (true) {
//            int size = 1000000;
            int size = 10000;

            for (int i = 0; i < size; i++) {
                list.add(new Object());
                addIterationCount++;
            }

            list.subList(0, size /2).clear();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {

            }


        }
    }

    @Override
    public int getSize() {
        return list.size();
    }

    public long getAddIterationCount() {
        return addIterationCount;
    }
}
