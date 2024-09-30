package com.company.aspire.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StreamDTO {

    @NotBlank(message = "Stream name is mandatory!")
    private String name;

    @NotNull(message = "Account id is mandatory!")
    private Long accountId;
}
