package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.RestaurantManagementConstant;
import org.springframework.http.HttpStatus;

public class RestaurantNotFoundException extends AbstractApiException {
    public RestaurantNotFoundException(final Throwable cause){

        super(cause, HttpStatus.NOT_FOUND, RestaurantManagementConstant.RESTAURANT_NOT_FOUND);
    }
    public RestaurantNotFoundException( ){

        super(null, HttpStatus.NOT_FOUND, RestaurantManagementConstant.RESTAURANT_NOT_FOUND);
    }
}
