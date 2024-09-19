package com.inventory.shopcart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDetails {

    private Long categoryId;
    private String categoryName;
    private List<String> products;

}
