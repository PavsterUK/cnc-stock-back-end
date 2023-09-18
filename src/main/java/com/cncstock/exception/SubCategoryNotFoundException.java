package com.cncstock.exception;

public class SubCategoryNotFoundException extends RuntimeException {
    public SubCategoryNotFoundException(String subCategory) {
        super("Category '" + subCategory + "' does NOT exists.");
    }
}
