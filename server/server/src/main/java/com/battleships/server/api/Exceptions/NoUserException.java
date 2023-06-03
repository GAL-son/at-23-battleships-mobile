package com.battleships.server.api.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoUserException extends RuntimeException {
    public NoUserException() {
        super();
    }

    public NoUserException(String message) {
        super(message);
    }
}
