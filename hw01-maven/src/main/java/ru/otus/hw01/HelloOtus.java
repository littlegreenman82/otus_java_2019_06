package ru.otus.hw01;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class HelloOtus {

    private static final String WHITESPACES_REGEX = "\\s+";

    public static void main(String... args) {
        System.out.println("Введите слова через пробел:");
        String in = (new Scanner(System.in)).nextLine();

        List<String> inputList = Arrays.asList(in.split(HelloOtus.WHITESPACES_REGEX));
        Iterable<Integer> weights = Lists.transform(inputList, input->input.length());

        System.out.println(weights);
    }
}
