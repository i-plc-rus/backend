package com.bank.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class
    })
    ResponseEntity<ExceptionMessage> handleValidationExceptions(final Exception e) {
        ExceptionMessage exceptionMessage = new ExceptionMessage(
                e.getClass().getSimpleName(),
                e.getMessage());
        return new ResponseEntity<>(exceptionMessage, HttpStatus.BAD_REQUEST);
    }
}
