package com.company.aspire.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse {

    @JsonProperty
    private String message;

    @JsonProperty
    private List<?> details;

    public CustomResponse(String message){
        this.message = message;
    }

}
