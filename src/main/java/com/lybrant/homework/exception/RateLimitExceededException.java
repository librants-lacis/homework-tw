package com.lybrant.homework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String countryCode) {
        super(String.format("Rate Limit Exceeded for country code: '%s'", countryCode));
    }
}
