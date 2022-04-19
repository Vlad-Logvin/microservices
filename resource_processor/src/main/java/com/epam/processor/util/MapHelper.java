package com.epam.processor.util;

import com.epam.processor.exception.impl.ObjectParsingException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class MapHelper {
    public Map<String, Object> convertToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Field[] allFields = obj.getClass().getDeclaredFields();
        try {
            fillMapWithFields(obj, map, allFields);
            return map;
        } catch (IllegalAccessException e) {
            throw new ObjectParsingException(e, "Object " + obj + " doesn't parse to map", 500);
        }
    }

    private void fillMapWithFields(Object obj, Map<String, Object> map, Field[] allFields) throws IllegalAccessException {
        for (Field field : allFields) {
            putFieldValueToMap(obj, map, field);
        }
    }

    private void putFieldValueToMap(Object obj, Map<String, Object> map, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(obj);
        map.put(field.getName(), value);
    }
}
