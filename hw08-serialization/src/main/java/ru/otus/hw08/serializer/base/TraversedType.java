package ru.otus.hw08.serializer.base;

import ru.otus.hw08.serializer.exception.UnsupportedObjectType;

import javax.json.JsonObjectBuilder;

public interface TraversedType {

    void accept(Visitor visitor, JsonObjectBuilder jsonObjectBuilder) throws UnsupportedObjectType, IllegalAccessException;
}
