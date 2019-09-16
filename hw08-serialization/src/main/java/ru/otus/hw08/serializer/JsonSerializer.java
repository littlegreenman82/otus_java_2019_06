package ru.otus.hw08.serializer;

import ru.otus.hw08.serializer.exception.UnsupportedObjectType;
import ru.otus.hw08.serializer.types.TraversedObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonSerializer {

    public JsonObject serialize(Object object) throws UnsupportedObjectType, IllegalAccessException {
        if (object.getClass().isArray()) {
            throw new UnsupportedObjectType("Input type cannot be Array");
        }

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        new TraversedObject(null, object).accept(new JsonVisitor(), jsonObjectBuilder);

        return jsonObjectBuilder.build();
    }
}
