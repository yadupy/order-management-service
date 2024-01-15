package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.RestaurantManagementConstant;
import org.springframework.http.HttpStatus;

public class EmailAlreadyInUseException extends AbstractApiException {

    public EmailAlreadyInUseException(final Throwable cause){

        super(cause, HttpStatus.BAD_REQUEST, RestaurantManagementConstant.EMAIL_ALREADY_IN_USE_EXCEPTION);
    }
    public EmailAlreadyInUseException( ){

        super(null, HttpStatus.BAD_REQUEST, RestaurantManagementConstant.EMAIL_ALREADY_IN_USE_EXCEPTION);
    }

}
