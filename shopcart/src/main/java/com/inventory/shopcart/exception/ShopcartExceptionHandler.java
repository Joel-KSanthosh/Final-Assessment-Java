package com.inventory.shopcart.exception;

import com.inventory.shopcart.dto.CustomResponse;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ShopcartExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, List<String>> error = new HashMap<>();
        List<String> errorList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(errors -> errorList.add(errors.getDefaultMessage()));
        error.put("message",errorList);
        return error;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResourceFoundException.class)
    public CustomResponse handleNoResourceFoundException(NoResourceFoundException ex){
        return new CustomResponse(ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoResultException.class)
    public CustomResponse handleNoResultException(NoResultException ex){
        return new CustomResponse(ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CustomResponse handleGenericException(Exception ex){
        return new CustomResponse(ex.getMessage());
    }
}
