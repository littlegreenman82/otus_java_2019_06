package ru.otus.hw08.serializer.base;

import ru.otus.hw08.serializer.exception.UnsupportedObjectType;
import ru.otus.hw08.serializer.types.TraversedArray;
import ru.otus.hw08.serializer.types.TraversedCollection;
import ru.otus.hw08.serializer.types.TraversedObject;
import ru.otus.hw08.serializer.types.TraversedPrimitive;

import javax.json.JsonObjectBuilder;

public interface Visitor {

    void visit(TraversedObject traversed, JsonObjectBuilder jsonObjectBuilder) throws IllegalAccessException, UnsupportedObjectType;

    void visit(TraversedArray traversed, JsonObjectBuilder jsonObjectBuilder) throws UnsupportedObjectType, IllegalAccessException;

    void visit(TraversedPrimitive traversed, JsonObjectBuilder jsonObjectBuilder) throws UnsupportedObjectType;

    void visit(TraversedCollection traversed, JsonObjectBuilder jsonObjectBuilder) throws IllegalAccessException, UnsupportedObjectType;
}
