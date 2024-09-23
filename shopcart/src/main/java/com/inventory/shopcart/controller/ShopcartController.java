package com.inventory.shopcart.controller;

import com.inventory.shopcart.dto.*;
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
    public CustomResponse insertCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        String insertedCategoryName = shopcartService.insertCategory(categoryDTO);
        return new CustomResponse("Successfully Inserted Category : "+insertedCategoryName);
    }

    @PostMapping("product/add")
    public CustomResponse insertProduct(@Valid @RequestBody ProductDTO productDTO){
        String insertedProductName = shopcartService.insertProduct(productDTO);
        return new CustomResponse("Successfully Inserted Product : "+insertedProductName);
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

    @GetMapping(path = {"product" ,"product/{id}"})
    public CustomResponse getProduct(
            @PathVariable(required = false) Long id,
            @RequestParam(required = false) Long category_id
            ){
        return new CustomResponse("Successfully Fetched Product",(List<?>) shopcartService.getProducts(id,category_id));
//        if (id != null) {
//            if (result != null) {
//                return new CustomResponse("Product found with ID: " + id, List.of(result));
//            } else {
//                return new CustomResponse("No product found with ID: " + id);
//            }
//        } else {
//            return new CustomResponse("All products retrieved successfully", (List<?>) result);
//        }
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

    @PutMapping("product/{id}/order")
    public CustomResponse orderProduct(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam int quantity
    ){
        shopcartService.orderProduct(id,userId,quantity);
        return new CustomResponse("Order placed Successfully.");
    }

    @PutMapping("category/{id}/update")
    public CustomResponse updateCategoryDetails(@PathVariable Long id, @RequestParam String name){
        List<String> names = shopcartService.updateCategory(id,name);
        return new CustomResponse("Category name has been changed from "+names.get(0)+" to "+names.get(1));
    }

    @PutMapping("/product/{id}/restock")
    public CustomResponse restockProduct(@PathVariable Long id,@RequestParam Long user_id,@RequestParam int quantity){
        if(quantity>0){
            String productName = shopcartService.restockProduct(id,user_id , quantity);
            return new CustomResponse(productName+" restocked successfully");
        }
        throw new IllegalArgumentException("Quantity must be greater than 0");
    }

    @PutMapping("product/{id}/update")
    public CustomResponse updateProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false,name = "category_id") Long categoryId
    ){
        if(name == null && price == null && quantity == null && categoryId == null){
            throw new IllegalArgumentException("No arguments given for update.");
        }
        else {
            ProductGET product = shopcartService.updateProduct(id,name,price,quantity,categoryId);
            return new CustomResponse("Successfully Updated product",List.of(product));
        }
    }

    @PostMapping("buyer/add")
    public CustomResponse insertBuyer(@Valid @RequestBody UserDTO buyer){
        String user = shopcartService.insertBuyer(buyer);
        return new CustomResponse("Successfully Inserted Buyer : "+user);
    }

    @PostMapping("seller/add")
    public CustomResponse insertSeller(@Valid @RequestBody UserDTO seller){
        String user = shopcartService.insertSeller(seller);
        return new CustomResponse("Successfully Inserted Seller : "+user);
    }
}
