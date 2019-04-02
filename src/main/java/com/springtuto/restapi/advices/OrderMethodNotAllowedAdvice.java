package com.springtuto.restapi.advices;

import com.springtuto.restapi.exceptions.OrderMethodNotAllowedException;
import com.springtuto.restapi.exceptions.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderMethodNotAllowedAdvice {
    @ResponseBody
    @ExceptionHandler(OrderMethodNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String employeeNotFoundHandler(OrderMethodNotAllowedException ex) {
        return ex.getMessage();
    }
}
