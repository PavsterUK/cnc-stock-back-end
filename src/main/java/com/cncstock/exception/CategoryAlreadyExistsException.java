package com.cncstock.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String categoryName) {
        super("Category '" + categoryName + "' already exists.");
    }
}
