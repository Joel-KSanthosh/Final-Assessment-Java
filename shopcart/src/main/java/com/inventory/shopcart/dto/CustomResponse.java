package com.inventory.shopcart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class CustomResponse {

    private String message;
    private List<?> details;

    public CustomResponse(String message){
        this.message = message;
    }

}
