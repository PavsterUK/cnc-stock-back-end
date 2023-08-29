package com.cncstock.exception;

public class SubCategoryAlreadyExistsException extends RuntimeException{

    public SubCategoryAlreadyExistsException(String subCategoryName) {
        super("Category '" + subCategoryName + "' already exists.");
    }
}
