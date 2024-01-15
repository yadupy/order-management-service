package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.RestaurantManagementConstant;
import org.springframework.http.HttpStatus;

public class ContactNumberAlreadyInUseException  extends AbstractApiException {

    public ContactNumberAlreadyInUseException(final Throwable cause){

        super(cause, HttpStatus.BAD_REQUEST, RestaurantManagementConstant.CONTACT_NUMBER_ALREADY_IN_USE_EXCEPTION);
    }
    public ContactNumberAlreadyInUseException( ){

        super(null, HttpStatus.BAD_REQUEST, RestaurantManagementConstant.CONTACT_NUMBER_ALREADY_IN_USE_EXCEPTION);
    }
}
