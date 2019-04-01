package com.springtuto.restapi.exceptions;

import java.util.ArrayList;
import java.util.List;

public class EmployeeNotCorrectException extends RuntimeException {

    public EmployeeNotCorrectException(List<String> listNullAttribute) {
        super("Could not find employee attribute" + listNullAttribute.toString());
    }


}
