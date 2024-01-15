package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.ApplicationConstant;
import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends AbstractApiException {
    public CustomerNotFoundException(final Throwable cause){

        super(cause, HttpStatus.NOT_FOUND, ApplicationConstant.CUSTOMER_NOT_FOUND);
    }
    public CustomerNotFoundException( ){

        super(null, HttpStatus.NOT_FOUND, ApplicationConstant.CUSTOMER_NOT_FOUND);
    }
}
