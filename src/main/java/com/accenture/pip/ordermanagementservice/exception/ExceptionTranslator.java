package com.accenture.pip.ordermanagementservice.exception;

import com.accenture.pip.ordermanagementservice.exception.response.ErrorAdvice;
import com.accenture.pip.ordermanagementservice.exception.response.RestaurantManagementErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionTranslator {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleValidationExceptions(
            MethodArgumentNotValidException ex,
            @NotNull NativeWebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        final Object target = bindingResult.getTarget();
        final String className = (target!=null)?target.getClass().getSimpleName():"(unknown)";
        final String violatedConstrains = bindingResult.getFieldErrors()
                .stream()
                .map(fe->String.format("[%s.%s] did not confirm to [%s]",className,fe.getField(),fe.getDefaultMessage()))
                .collect(Collectors.joining(","));
        log.error("An illegal payload was encountered {}, following constraints were violated {}"
                ,bindingResult.getFieldErrorCount(),violatedConstrains);
        final String errorMessage = bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));
        return new ApiError(0,"Attempting the api with an invalid payload".concat(errorMessage));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiError handleConstraintViolationException(
            ConstraintViolationException ex,
            @NotNull NativeWebRequest request) {

        final String violatedConstrains = ex.getConstraintViolations()
                .stream()
                .map(v->String.format("[%s.%s] did not confirm to [%s]",
                                v.getRootBeanClass().getSimpleName(),v.getPropertyPath(),v.getMessage()))
                .collect(Collectors.joining(","));
        log.error("An illegal payload was encountered {}, following constraints were violated {}",
                ex.getConstraintViolations().size(),
                violatedConstrains);
        final String errorMessage = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return new ApiError(0,"Attempting the api with an invalid payload".concat(errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<RestaurantManagementErrorResponse> handleCustomerException(AbstractApiException exception, NativeWebRequest webRequest){

         final ErrorAdvice errorAdvice = ErrorAdvice
                .builder()
                .errorCode(String.valueOf(exception.getHttpStatus().value()))
                .errorMsg(exception.getErrorMsg())
                .errorReason(exception.getErrorReason())
                .build();

         RestaurantManagementErrorResponse errorResponse =  new RestaurantManagementErrorResponse(errorAdvice);
         return ResponseEntity
                 .status(exception.getHttpStatus())
                 .body(errorResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFound(Exception ex) throws Exception {
        throw ex;
    }
}
