package com.battleships.server.api.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class GameNotFoundExeption extends RuntimeException{

    public GameNotFoundExeption(String string) {
        super(string);
    }
    
}
