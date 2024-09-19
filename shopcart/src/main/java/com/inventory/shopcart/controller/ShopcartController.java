package com.inventory.shopcart.controller;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.CustomResponse;
import com.inventory.shopcart.dto.ProductDTO;

import com.inventory.shopcart.service.ShopcartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShopcartController {

    private final ShopcartService shopcartService;

    public ShopcartController(ShopcartService shopcartService){
        this.shopcartService = shopcartService;
    }

    @PostMapping("category/add")
    public void insertCategory(@RequestBody CategoryDTO categoryDTO){
        System.out.println(categoryDTO);
    }

    @PostMapping("product/add")
    public CustomResponse insertProduct(@RequestBody ProductDTO productDTO){
        return null;
    }

    @GetMapping("category/{id}")
    public CustomResponse getCategory(@PathVariable Long id){
        CategoryDetails categoryDetails = shopcartService.findCategoryDetailsById(id);
        return new CustomResponse("Successfully Fetched", List.of(categoryDetails));
    }
}
