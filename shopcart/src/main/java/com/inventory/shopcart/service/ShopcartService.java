package com.inventory.shopcart.service;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;

import java.util.List;

public interface ShopcartService {

    public void insertCategory(CategoryDTO categoryDTO);
    public CategoryDetails findCategoryDetailsById(Long id);
    public List<CategoryDetails> findAllCategoryDetails();

}
