package com.lybrant.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PersonalIdNotFoundException extends RuntimeException {
    public PersonalIdNotFoundException(String id) {
        super(String.format("PersonalId '%s' not found", id));
    }
}
