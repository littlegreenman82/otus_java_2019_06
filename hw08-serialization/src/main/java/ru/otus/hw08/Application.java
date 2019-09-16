package ru.otus.hw08;

import ru.otus.hw08.serializer.JsonSerializer;
import ru.otus.hw08.serializer.exception.UnsupportedObjectType;

import javax.json.JsonObject;

public class Application {
    public static void main(String[] args) {

        try {
            JsonObject jsonObject = new JsonSerializer().serialize(new Example());

            System.out.println(jsonObject.toString());
        } catch (UnsupportedObjectType | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
