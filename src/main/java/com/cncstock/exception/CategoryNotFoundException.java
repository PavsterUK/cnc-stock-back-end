package com.cncstock.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String categoryName) {
        super("Category '" + categoryName + "' does NOT exists.");
    }
}
