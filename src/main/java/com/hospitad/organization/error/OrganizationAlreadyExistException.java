package com.hospitad.organization.error;

public class OrganizationAlreadyExistException extends Exception {

    public OrganizationAlreadyExistException(String message) {
        super(message);
    }

    public OrganizationAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
