package com.inventory.shopcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.jdbc.core.PreparedStatementSetter;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @JsonProperty
    @NotNull(message = "ERROR")
    private String categoryName;

    
}
