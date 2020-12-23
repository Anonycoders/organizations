package com.hospitad.organization.error;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
    public UserAlreadyExistException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }

}
