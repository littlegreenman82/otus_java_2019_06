package ru.otus.jdbc.reflectiondata;

import java.lang.reflect.Field;

public class ReflectionData {

    private boolean hasIdAnnotation = false;

    private Field idField;

    private Field[] fields;


    public boolean isHasIdAnnotation() {
        return hasIdAnnotation;
    }

    public void setHasIdAnnotation(boolean hasIdAnnotation) {
        this.hasIdAnnotation = hasIdAnnotation;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }
}
