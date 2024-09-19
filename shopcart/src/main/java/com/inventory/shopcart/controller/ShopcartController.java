package com.inventory.shopcart.controller;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CustomResponse;
import com.inventory.shopcart.dto.ProductDTO;

import com.inventory.shopcart.service.impl.ShopcartServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ShopcartController {
    ShopcartServiceImpl shopServiceImpl;
    @PostMapping("category/add")
    public void insertCategory(@RequestBody CategoryDTO categoryDTO){
        shopServiceImpl.insertCategory(categoryDTO);
    }

    @PostMapping("product/add")
    public CustomResponse insertProduct(@RequestBody ProductDTO productDTO){
        return null;
    }

}
