package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.OrderConstant;
import org.springframework.http.HttpStatus;

public class InvalidOrderStatusException extends AbstractApiException {
    public InvalidOrderStatusException(final Throwable cause){

        super(cause, HttpStatus.NOT_FOUND, OrderConstant.INVALID_FOOD_ITEM_ID);
    }
    public InvalidOrderStatusException( ){

        super(null, HttpStatus.NOT_FOUND, OrderConstant.INVALID_FOOD_ITEM_ID);
    }

}
