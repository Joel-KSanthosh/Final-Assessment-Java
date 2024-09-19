package com.inventory.shopcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @JsonProperty
    @NotBlank(message = "Category name is mandatory")
    private String categoryName;

}
