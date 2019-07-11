package ru.otus.hw02;

import java.util.Collections;

public class Main {

    public static void main(String... args) {
        DIYarrayList<Integer> list = new DIYarrayList<>();
        DIYarrayList<Integer> copiedList = new DIYarrayList<>();

        for (int i = 1; i <= 500; i++) {
            list.add(i);
            copiedList.add(null);
        }

        copiedList.add(null);
        copiedList.add(null);
        copiedList.add(null);
        copiedList.add(null);

        Collections.addAll(list, 501, 502, 503, 504);
        Collections.copy(copiedList, list);
        Collections.sort(list, Collections.reverseOrder());

        for (Integer el : list) {
            System.out.println(el);
        }

        System.out.println("-------------------");

        for (Integer el : copiedList) {
            System.out.println(el);
        }
    }
}