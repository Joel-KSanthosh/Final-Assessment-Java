package com.inventory.shopcart.repository;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.dto.ProductGET;

import java.util.List;

public interface ShopcartRepository {

    public void insertCategory(CategoryDTO categoryDTO);
    public void insertProduct(ProductDTO productDTO);
    public CategoryDetails findCategoryDetailsById(Long id);
    public boolean existsCategoryById(Long id);
    public List<CategoryDetails> findAllCategoryDetails();
    public ProductGET findProductById(Long id);
    public void buyProduct(ProductGET product, int quantity);
    public void createOrder(Long productId, Long userId);
    public boolean existsBuyerWithId(Long id);
}
