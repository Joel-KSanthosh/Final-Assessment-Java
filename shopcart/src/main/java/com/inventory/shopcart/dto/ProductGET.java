package com.inventory.shopcart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductGET {

    private Long id;

    private String name;

    private float price;

    private long quantity;

    private Long CategoryId;

}
