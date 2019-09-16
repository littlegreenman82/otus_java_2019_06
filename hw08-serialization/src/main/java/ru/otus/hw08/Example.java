package ru.otus.hw08;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Example {

    private String nullableString;

    private String string;

    private Integer integer;

    private Float aFloat;

    private Boolean aBoolean;

    private ArrayList<String> arrayList = new ArrayList<>();

    private Example2 example2;

    private ArrayList<Example2> example2ArrayList = new ArrayList<>();

    public Example() {
        string = "Test";
        integer = 17;
        aFloat = 17f;
        aBoolean = false;
        arrayList.add("test1");
        arrayList.add("test2");

        example2 = new Example2();

        example2ArrayList.add(example2);
        example2ArrayList.add(example2);
    }

    public String getNullableString() {
        return nullableString;
    }

    public String getString() {
        return string;
    }

    public Integer getInteger() {
        return integer;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public Boolean getaBoolean() {
        return aBoolean;
    }

    public List<String> getArrayList() {
        return arrayList;
    }

    public Example2 getExample2() {
        return example2;
    }

    public List<Example2> getExample2ArrayList() {
        return example2ArrayList;
    }
}
