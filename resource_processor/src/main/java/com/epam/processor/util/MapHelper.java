package com.epam.processor.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class MapHelper {
    public Map<String, Object> convertToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] allFields = obj.getClass().getDeclaredFields();
        for (Field field : allFields) {
            putFieldValueToMap(obj, map, field);
        }
        return map;
    }

    private void putFieldValueToMap(Object obj, Map<String, Object> map, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(obj);
        map.put(field.getName(), value);
    }
}
