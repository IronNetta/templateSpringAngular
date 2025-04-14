package org.seba.exceptions.user;

import org.seba.exceptions.GlobalException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GlobalException {

  public UserNotFoundException(HttpStatus status, Object error) {
    super(status, error);
  }
}
