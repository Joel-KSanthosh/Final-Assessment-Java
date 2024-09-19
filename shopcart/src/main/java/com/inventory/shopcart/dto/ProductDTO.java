package com.inventory.shopcart.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    @JsonProperty
    @NotBlank(message = "product name is mandatory")
    private String productName;

    @JsonProperty
    @NotNull(message = "price is mandatory")
    private float price;

    @JsonProperty
    @NotNull(message="quantity is mandatory")
    private long quantity;

    @JsonProperty
    @NotNull(message="category_Id is mandatory")
    private Long category_Id;
    // private 
    
}
