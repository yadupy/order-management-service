package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.ApplicationConstant;
import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends AbstractApiException {
    public AddressNotFoundException(final Throwable cause){

        super(cause, HttpStatus.NOT_FOUND, ApplicationConstant.ADDRESS_NOT_FOUND);
    }
    public AddressNotFoundException( ){

        super(null, HttpStatus.NOT_FOUND, ApplicationConstant.ADDRESS_NOT_FOUND);
    }
}
