package com.company.aspire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse {

    private String message;
    private List<?> details;

    public CustomResponse(String message){
        this.message = message;
    }

}
