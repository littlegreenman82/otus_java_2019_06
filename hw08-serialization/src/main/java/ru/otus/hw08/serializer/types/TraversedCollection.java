package ru.otus.hw08.serializer.types;

import ru.otus.hw08.serializer.base.TraversedField;
import ru.otus.hw08.serializer.base.Visitor;
import ru.otus.hw08.serializer.exception.UnsupportedObjectType;

import javax.json.JsonObjectBuilder;
import java.lang.reflect.Field;

public class TraversedCollection extends TraversedField {
    public TraversedCollection(Field field, Object object) {
        super(field, object);
    }

    @Override
    public void accept(Visitor visitor, JsonObjectBuilder jsonObjectBuilder) throws UnsupportedObjectType, IllegalAccessException {
        visitor.visit(this, jsonObjectBuilder);
    }
}
