package com.hospitad.organization.responses;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponse {
    private String errorMessage;
    private String requestedURI;
    private Integer status;
    private Date timestamp;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
