package ru.otus.jdbc.reflectiondata;

import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.exception.MissingIdAnnotationException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectionDataCacheHolder {

    private Map<Class, ReflectionData> reflectionCache = new HashMap<>();

    public ReflectionData getOrSetIfEmpty(Class<?> aClass) {
        ReflectionData reflectionData;

        if (reflectionCache.containsKey(aClass)) {
            reflectionData = reflectionCache.get(aClass);
        } else {
            Field[] declaredFields = aClass.getDeclaredFields();

            reflectionData = new ReflectionData();
            reflectionData.setFields(declaredFields);
            reflectionData.setClassName(aClass.getSimpleName());

            for (Field field : declaredFields) {
                Id declaredAnnotation = field.getDeclaredAnnotation(Id.class);

                if (declaredAnnotation != null) {
                    reflectionData.setHasIdAnnotation(true);
                    reflectionData.setIdField(field);
                }
            }

            reflectionCache.put(aClass, reflectionData);
        }

        if (!reflectionData.isHasIdAnnotation()) throw new MissingIdAnnotationException();

        return reflectionData;
    }
}
