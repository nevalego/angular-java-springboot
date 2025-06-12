package com.nveapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
public class InvalidSsoCallbackException extends RuntimeException {

    public InvalidSsoCallbackException(String message) {
        super(message);
    }
}