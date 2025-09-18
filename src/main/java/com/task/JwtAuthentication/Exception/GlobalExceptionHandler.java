package com.task.JwtAuthentication.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handlerException(ProductNotFoundException exc)
    {
        CustomErrorResponse productErrorResponse = new CustomErrorResponse();

        productErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        productErrorResponse.setMessage(exc.getMessage());
        productErrorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(productErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handlerException(UserNotFoundException exc)
    {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse();

        customErrorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        customErrorResponse.setMessage(exc.getMessage());
        customErrorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(customErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handlerException(Exception exc)
    {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse();

        customErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        customErrorResponse.setMessage("Something Went Wrong "+exc.getMessage());
        customErrorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(customErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
