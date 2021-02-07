package com.tokmeninov.spring.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id){
        super("Could not find object with id = "+id);
    }
}
