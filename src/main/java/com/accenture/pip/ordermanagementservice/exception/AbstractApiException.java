package com.accenture.pip.ordermanagementservice.exception;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class AbstractApiException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String errorMsg;
    private final String errorReason;

    public AbstractApiException(final Throwable cause, final HttpStatus httpStatus, final String errorMsg){
        this.httpStatus = httpStatus;
        this.errorMsg = errorMsg;
        this.errorReason = ExceptionUtils.getRootCauseMessage(cause);
    }
    public AbstractApiException(final Throwable cause, final HttpStatus httpStatus){
        this.httpStatus = httpStatus;
        this.errorMsg = null;
        this.errorReason = ExceptionUtils.getRootCauseMessage(cause);
    }
}
