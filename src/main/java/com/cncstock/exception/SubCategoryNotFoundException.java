package com.cncstock.exception;

public class SubCategoryNotFoundException extends RuntimeException {
    public SubCategoryNotFoundException(Long id) {
        super("Sub Category with specified id "+ id +" doesent exist");
    }
}
