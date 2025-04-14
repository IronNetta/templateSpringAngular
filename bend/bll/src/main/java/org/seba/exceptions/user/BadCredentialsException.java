package org.seba.exceptions.user;

import org.seba.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class BadCredentialsException extends GlobalException {
    public BadCredentialsException(HttpStatus status, Object error) {
        super(status, error);
    }
}

