package com.inventory.shopcart.service;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;

import java.util.List;
import com.inventory.shopcart.dto.ProductDTO;

public interface ShopcartService {
    void deleteProduct(Long id);
    void orderProduct(Long productId,Long userId,int quantity);
    void deleteCategory(Long id);

    String insertCategory(CategoryDTO categoryDTO);
    String insertProduct(ProductDTO productDTO);

    Object getProducts(Long id);

    CategoryDetails findCategoryDetailsById(Long id);

    List<CategoryDetails> findAllCategoryDetails();
}
