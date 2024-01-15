package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.OrderConstant;
import org.springframework.http.HttpStatus;

public class InvalidOrderFoodItemIdException extends AbstractApiException {
    public InvalidOrderFoodItemIdException(final Throwable cause){

        super(cause, HttpStatus.BAD_REQUEST, OrderConstant.INVALID_ORDER_STATUS);
    }
    public InvalidOrderFoodItemIdException( ){

        super(null, HttpStatus.BAD_REQUEST, OrderConstant.INVALID_ORDER_STATUS);
    }
}
