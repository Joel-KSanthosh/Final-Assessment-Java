package com.company.aspire.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    @NotBlank(message = "Account name is mandatory!")
    private String name;

}
