package org.seba.exceptions.user;

import org.seba.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistExeption extends GlobalException {
    public UserAlreadyExistExeption(HttpStatus status, Object error) {
        super(status, error);
    }
}
