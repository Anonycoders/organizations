package com.hospitad.organization.error;

public class OrganizationNotFoundException extends Exception {
    public OrganizationNotFoundException(String message) {
        super(message);
    }

    public OrganizationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
