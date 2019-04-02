package com.springtuto.restapi.exceptions;

import com.springtuto.restapi.order.Status;

public class OrderMethodNotAllowedException extends RuntimeException {
    public OrderMethodNotAllowedException(Status status) {
        super("Method not allowed on status " + status);
    }
}
