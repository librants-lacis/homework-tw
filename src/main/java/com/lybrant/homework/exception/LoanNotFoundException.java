package com.lybrant.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigInteger;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(BigInteger id) {
        super(String.format("Loan not found with id: '%s'", id));
    }
}
