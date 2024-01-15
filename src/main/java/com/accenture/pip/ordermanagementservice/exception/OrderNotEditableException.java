package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.constants.OrderConstant;
import org.springframework.http.HttpStatus;

public class OrderNotEditableException extends AbstractApiException {
    public OrderNotEditableException(final Throwable cause){

        super(cause, HttpStatus.PRECONDITION_FAILED, OrderConstant.ORDER_NOT_EDITABLE);
    }
    public OrderNotEditableException( ){

        super(null, HttpStatus.PRECONDITION_FAILED, OrderConstant.ORDER_NOT_EDITABLE);
    }
}
