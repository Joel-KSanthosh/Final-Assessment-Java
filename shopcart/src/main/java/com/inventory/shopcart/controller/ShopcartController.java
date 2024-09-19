package com.inventory.shopcart.controller;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.CustomResponse;
import com.inventory.shopcart.dto.ProductDTO;

import com.inventory.shopcart.service.ShopcartService;
import jakarta.validation.Valid;
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
    public void insertCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        System.out.println(categoryDTO);
    }

    @PostMapping("product/add")
    public CustomResponse insertProduct(@Valid @RequestBody ProductDTO productDTO){
        return null;
    }

    @GetMapping(path = {"category","category/{id}"})
    public CustomResponse getCategory(@PathVariable(required = false) Long id){
        if(id == null){
            return new CustomResponse("Successfully Fetched",shopcartService.findAllCategoryDetails());
        }
        else {
            CategoryDetails categoryDetails = shopcartService.findCategoryDetailsById(id);
            return new CustomResponse("Successfully Fetched", List.of(categoryDetails));
        }
    }

    @PutMapping("product/{id}/order")
    public CustomResponse orderProduct(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam int quantity
    ){
        shopcartService.orderProduct(id,userId,quantity);
        return new CustomResponse("Order placed Successfully.");
    }
}
