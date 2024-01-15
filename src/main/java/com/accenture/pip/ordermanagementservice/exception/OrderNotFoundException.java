package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.OrderConstant;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends AbstractApiException {
    public OrderNotFoundException(final Throwable cause){

        super(cause, HttpStatus.NOT_FOUND, OrderConstant.ORDER_NOT_FOUND);
    }
    public OrderNotFoundException( ){

        super(null, HttpStatus.NOT_FOUND, OrderConstant.ORDER_NOT_FOUND);
    }
}
