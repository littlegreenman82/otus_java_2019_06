package ru.otus.hw08.serializer.base;

import java.lang.reflect.Field;

public abstract class TraversedField implements TraversedType {

    private Field field;

    private Object object;

    public TraversedField(Field field, Object object) {
        this.field = field;
        this.object = object;
    }

    public Field getField() {
        return field;
    }

    public Object getObject() {
        return object;
    }
}
