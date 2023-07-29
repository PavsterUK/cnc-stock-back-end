package com.cncstock;

import java.lang.reflect.Field;
import java.util.List;

public class ExceptionGenerator {
    public static void checkForEmptyFields(Object obj, List<String> fieldNames) throws EmptyFieldException {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null.");
        }

        Class<?> clazz = obj.getClass();

        for (String fieldName : fieldNames) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value == null || value.equals(-1) ||  value.toString().isEmpty()) {
                    throw new EmptyFieldException("Field '" + fieldName + "' is empty.");
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Handle any exception that might occur during field access or if the field doesn't exist.
                e.printStackTrace();
            }
        }
    }

    public static class EmptyFieldException extends Exception {
        public EmptyFieldException(String message) {
            super(message);
        }
    }
}
