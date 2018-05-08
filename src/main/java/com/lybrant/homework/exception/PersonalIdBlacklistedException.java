package com.lybrant.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class PersonalIdBlacklistedException extends RuntimeException {
    public PersonalIdBlacklistedException(String personalId) {
        super(String.format("PersonalId blacklisted with id: '%s'", personalId));
    }
}
