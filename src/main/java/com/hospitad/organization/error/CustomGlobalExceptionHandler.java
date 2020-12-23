package com.hospitad.organization.error;

import com.hospitad.organization.responses.ExceptionResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object>
    handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                 HttpHeaders headers,
                                 HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());


        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    @ExceptionHandler({UserAlreadyExistException.class, OrganizationAlreadyExistException.class})
    public ResponseEntity<ExceptionResponse> handleException(final Exception exception,
                                                             final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();

        error.setErrorMessage(exception.getMessage());
        error.setRequestedURI(request.getRequestURI());
        error.setStatus(400);
        error.setTimestamp(new Date());

        return ResponseEntity.status(400).body(error);
    }


    @ExceptionHandler({UsernameNotFoundException.class, OrganizationNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final Exception exception,
                                                                     final HttpServletRequest request) {

        ExceptionResponse error = new ExceptionResponse();

        error.setErrorMessage(exception.getMessage());
        error.setRequestedURI(request.getRequestURI());
        error.setStatus(404);
        error.setTimestamp(new Date());

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(final Exception exception,
                                                                  final HttpServletRequest request) {
        return ResponseEntity.status(401).body(new ExceptionResponse("Username or password is wrong"));
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ExceptionResponse> otherErrors(final Exception e) {
        return ResponseEntity.status(400).body(new ExceptionResponse(e.getMessage()));
    }
}
