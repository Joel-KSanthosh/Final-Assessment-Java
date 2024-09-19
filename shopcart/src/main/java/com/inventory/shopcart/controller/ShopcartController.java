package com.inventory.shopcart.controller;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CustomResponse;
import com.inventory.shopcart.dto.ProductDTO;

import com.inventory.shopcart.service.ShopcartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class ShopcartController {
    private ShopcartService shopcartService;


   @Autowired
    public ShopcartController(ShopcartService shopcartService) {
        this.shopcartService = shopcartService;
    }
    @PostMapping("category/add")
    public CustomResponse insertCategory(@RequestBody CategoryDTO categoryDTO){
        System.out.println("entering contoller");
        String insertedCategoryName = shopcartService.insertCategory(categoryDTO);
        return new CustomResponse("Successfully Inserted Category "+insertedCategoryName);
    }

    @PostMapping("product/add")
    public CustomResponse insertProduct(@RequestBody ProductDTO productDTO){
        String insertedProductName = shopcartService.insertProduct(productDTO);
        return new CustomResponse("Successfully Inserted Product"+insertedProductName);
    }

    @GetMapping(path = {"products" ,"products/{id}"})
    public CustomResponse getProduct(@PathVariable(required = false) Long id){
        Object result = shopcartService.getProducts(id);
        if (id != null) {
            if (result != null) {
                return new CustomResponse("Product found with ID: " + id, List.of(result));
            } else {
                return new CustomResponse("No product found with ID: " + id);
            }
        } else {
            return new CustomResponse("All products retrieved successfully", (List<?>) result);
        }
    }

    @DeleteMapping("category/{id}/delete")
    public CustomResponse deleteCategory(@PathVariable Long id){
        shopcartService.deleteCategory(id);
        return new CustomResponse("Successfully Deleted Category");
    }

    @DeleteMapping("product/{id}/delete")
    public CustomResponse deleteProduct(@PathVariable Long id){
        shopcartService.deleteProduct(id);
        return new CustomResponse("Successfully Deleted Product");

    }





}
