package com.inventory.shopcart.exception;

import com.inventory.shopcart.dto.CustomResponse;

import jakarta.persistence.NoResultException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.http.HttpStatus;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CustomResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex){
        return new CustomResponse(ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public CustomResponse handleEmptyResultDataAccessException(EmptyResultDataAccessException ex){
        return new CustomResponse(ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public CustomResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex){
        return new CustomResponse(ex.getLocalizedMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public CustomResponse handleNullPointerException(Exception ex){
        return new CustomResponse(ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateKeyException.class)
    public CustomResponse handleDuplicateKeyException(DuplicateKeyException ex){
        return new CustomResponse(ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CustomResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        return new CustomResponse("Enter a valid input for "+ ex.getName());
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public CustomResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        return new CustomResponse("Request Body is invalid");
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public CustomResponse handleIllegalArgumentException(IllegalArgumentException ex){
        return new CustomResponse(ex.getMessage());
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CustomResponse handleGenericException(Exception ex){
        return new CustomResponse(ex.getMessage());
    }
}
