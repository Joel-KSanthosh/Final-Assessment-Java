package com.inventory.shopcart.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private String productName;
    private float price;
    private long quantity;
    private Long category_Id;
    // private 
    
}
