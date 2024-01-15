package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.ApplicationConstant;
import org.springframework.http.HttpStatus;

public class AuthenticationException extends AbstractApiException{
    public AuthenticationException(final Throwable cause){

        super(cause, HttpStatus.FORBIDDEN, ApplicationConstant.AUTHENTICATION_FAILED);
    }
    public AuthenticationException(){

        super(null, HttpStatus.FORBIDDEN, ApplicationConstant.AUTHENTICATION_FAILED);
    }
}
