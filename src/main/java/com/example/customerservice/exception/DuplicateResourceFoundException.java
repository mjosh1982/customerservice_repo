package com.example.customerservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = org.springframework.http.HttpStatus.CONFLICT)
public class DuplicateResourceFoundException extends RuntimeException{
    public DuplicateResourceFoundException(String message) {
        super(message);
    }
}
