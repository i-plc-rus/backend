package com.bank.transactions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public String handleDefault(Exception e) {
        log.error(e.getMessage());
        return e.getMessage();
    }
}
