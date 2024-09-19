package com.inventory.shopcart.service;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.ProductDTO;

public interface ShopcartService {
    public String insertCategory(CategoryDTO categoryDTO);
    public String insertProduct(ProductDTO productDTO);
    public Object getProducts(Long id);
    public void deleteCategory(Long id);
    public void deleteProduct(Long id);

}
