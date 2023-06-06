package com.battleships.server.api.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("PASSWORD INVALID");
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
