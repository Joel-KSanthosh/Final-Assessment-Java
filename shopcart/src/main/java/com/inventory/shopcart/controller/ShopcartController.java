package com.inventory.shopcart.controller;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CustomResponse;
import com.inventory.shopcart.repository.ShopcartRepository;
import com.inventory.shopcart.repository.impl.ShopcartRepositoryImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ShopcartController {

     ShopcartRepositoryImpl shopcartRepository;

    // public ShopcartController(ShopcartRepositoryImpl shopcartRepositoryImpl){
    //     this.shopcartRepository = shopcartRepositoryImpl;
    // }

    @PostMapping("category/add")
    public void insertCategory(@RequestBody CategoryDTO categoryDTO){
        System.out.println(categoryDTO);
        shopcartRepository.insertCategory(categoryDTO);
    }
   
   

    
}
