package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.ApplicationConstant;
import org.springframework.http.HttpStatus;

public class JwtTokenException extends AbstractApiException{
    public JwtTokenException(final Throwable cause){

        super(cause, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationConstant.JWT_TOKEN_FAILED);
    }
    public JwtTokenException( ){

        super(null, HttpStatus.INTERNAL_SERVER_ERROR, ApplicationConstant.JWT_TOKEN_FAILED);
    }
}
