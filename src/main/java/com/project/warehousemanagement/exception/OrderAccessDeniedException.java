package com.project.warehousemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OrderAccessDeniedException extends RuntimeException {

    public OrderAccessDeniedException(Long orderId, String username) {
        super("Order " + orderId + " does not belong to user '" + username + "'");
    }
}
