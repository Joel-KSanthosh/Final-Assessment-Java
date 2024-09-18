package com.inventory.shopcart.controller;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CustomResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ShopcartController {

    @PostMapping("category/add")
    public CustomResponse insertCategory(@RequestBody CategoryDTO categoryDTO){
        return null;
    }

    
}
