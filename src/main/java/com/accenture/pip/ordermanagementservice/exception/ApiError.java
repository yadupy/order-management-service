package com.accenture.pip.ordermanagementservice.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ApiError implements Serializable {
    private final int code;
    private final String message;
    private Map<String,Object> additions;

    public ApiError addAdditions(String key, Object value){
        if(additions==null){
            additions = new HashMap<>();
        }
        additions.put(key,value);
        return this;
    }
    public static ApiError fromErrorCode(int errorCode, String message){
        return new ApiError(errorCode,message);
    }
}
