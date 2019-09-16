package ru.otus.hw08.serializer;

import org.apache.commons.lang3.ClassUtils;
import ru.otus.hw08.serializer.base.Visitor;
import ru.otus.hw08.serializer.exception.UnsupportedObjectType;
import ru.otus.hw08.serializer.types.TraversedArray;
import ru.otus.hw08.serializer.types.TraversedCollection;
import ru.otus.hw08.serializer.types.TraversedObject;
import ru.otus.hw08.serializer.types.TraversedPrimitive;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class JsonVisitor implements Visitor {

    @Override
    public void visit(TraversedObject traversed, JsonObjectBuilder jsonObjectBuilder) throws IllegalAccessException, UnsupportedObjectType {
        Object object = traversed.getObject();
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Class<?> fieldClass = declaredField.getType();
            Object declaredObject = declaredField.get(object);

            if (Modifier.isStatic(declaredField.getModifiers())) {
                continue;
            }

            if (declaredObject == null) {
                jsonObjectBuilder.addNull(declaredField.getName());
            } else if (fieldClass.equals(String.class)) {
                jsonObjectBuilder.add(declaredField.getName(), (String) declaredObject);
            } else if (ClassUtils.isPrimitiveOrWrapper(fieldClass)) {
                new TraversedPrimitive(declaredField, declaredObject).accept(new JsonVisitor(), jsonObjectBuilder);
            } else if (fieldClass.isArray()) {
                new TraversedArray(declaredField, declaredObject).accept(new JsonVisitor(), jsonObjectBuilder);
            } else if (Collection.class.isAssignableFrom(fieldClass)) {
                new TraversedCollection(declaredField, declaredObject).accept(new JsonVisitor(), jsonObjectBuilder);
            } else {
                JsonObjectBuilder subObjectBuilder = Json.createObjectBuilder();

                new TraversedObject(null, declaredObject).accept(new JsonVisitor(), subObjectBuilder);
                jsonObjectBuilder.add(declaredField.getName(), subObjectBuilder);
            }
        }
    }

    @Override
    public void visit(TraversedArray traversed, JsonObjectBuilder jsonObjectBuilder) throws UnsupportedObjectType, IllegalAccessException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        Field field = traversed.getField();
        Object object = traversed.getObject();

        int length = Array.getLength(object);

        for (int i = 0; i < length; i++) {
            Object item = Array.get(object, i);
            if (item.getClass().equals(String.class)) {
                arrayBuilder.add((String) item);
            } else if (ClassUtils.isPrimitiveOrWrapper(item.getClass())) {
                arrayBuilder.add(objectToJsonValue(Array.get(object, i)));
            } else {
                JsonObjectBuilder subObjectBuilder = Json.createObjectBuilder();
                new TraversedObject(null, Array.get(object, i)).accept(new JsonVisitor(), subObjectBuilder);
                arrayBuilder.add(subObjectBuilder);
            }
        }

        jsonObjectBuilder.add(field.getName(), arrayBuilder);
    }

    @Override
    public void visit(TraversedPrimitive traversed, JsonObjectBuilder jsonObjectBuilder) throws UnsupportedObjectType {
        String name = traversed.getField().getName();
        Object object = traversed.getObject();

        jsonObjectBuilder.add(name, objectToJsonValue(object));
    }

    @Override
    public void visit(TraversedCollection traversed, JsonObjectBuilder jsonObjectBuilder) throws IllegalAccessException, UnsupportedObjectType {
        Field field = traversed.getField();
        Collection collection = (Collection) traversed.getObject();

        new TraversedArray(field, collection.toArray()).accept(new JsonVisitor(), jsonObjectBuilder);
    }

    private JsonValue objectToJsonValue(Object object) throws UnsupportedObjectType {
        Class<?> aClass = object.getClass();

        if (aClass.equals(Byte.class)) {
            return Json.createValue((Byte) object);
        } else if (aClass.equals(Short.class)) {
            return Json.createValue((Short) object);
        } else if (aClass.equals(Integer.class)) {
            return Json.createValue((Integer) object);
        } else if (aClass.equals(Long.class)) {
            return Json.createValue((Long) object);
        } else if (aClass.equals(Float.class)) {
            return Json.createValue((Float) object);
        } else if (aClass.equals(Double.class)) {
            return Json.createValue((Double) object);
        } else if (aClass.equals(Boolean.class)) {
            return (boolean) object ? JsonValue.TRUE : JsonValue.FALSE;
        } else if (aClass.equals(String.class) || aClass.equals(Character.class)) {
            return Json.createValue(object.toString());
        }

        throw new UnsupportedObjectType();
    }
}
