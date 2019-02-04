package com.daveace.salesdiary.interfaces;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public interface ObjectMapper {

    default<T> Map<String, Object> map(T object) {

        Map<String, Object> map = new HashMap<>();
        Class theClass = object.getClass();
        for (Field field : theClass.getDeclaredFields()) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
