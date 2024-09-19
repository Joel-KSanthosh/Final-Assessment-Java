package com.inventory.shopcart.repository;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.ProductDTO;

public interface ShopcartRepository {

    public void insertCategory(CategoryDTO categoryDTO);
    public void insertProduct(ProductDTO productDTO);
}
