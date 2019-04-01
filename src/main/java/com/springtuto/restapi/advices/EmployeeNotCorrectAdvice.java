package com.springtuto.restapi.advices;

import com.springtuto.restapi.exceptions.EmployeeNotCorrectException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeNotCorrectAdvice {

    @ResponseBody
    @ExceptionHandler(EmployeeNotCorrectException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String employeeNotCorrectHandler(EmployeeNotCorrectException ex) {
        return ex.getMessage();
    }
}
