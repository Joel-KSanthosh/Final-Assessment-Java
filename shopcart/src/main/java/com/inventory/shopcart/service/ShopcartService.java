package com.inventory.shopcart.service;

import com.inventory.shopcart.dto.*;

import java.util.List;

public interface ShopcartService {
    void deleteProduct(Long id);
    void orderProduct(Long productId,Long userId,Long quantity);
    void deleteCategory(Long id);

    String insertCategory(CategoryDTO categoryDTO);
    String insertProduct(ProductDTO productDTO);
    String restockProduct(Long id,Long user_id,Long quantity);
    String insertBuyer(UserDTO buyer);
    String insertSeller(UserDTO seller);

    Object getProducts(Long id, Long category_id);

    CategoryDetails findCategoryDetailsById(Long id);

    List<String> updateCategory(Long id,String name);
    List<CategoryDetails> findAllCategoryDetails();
    ProductGET updateProduct(Long id, String name, Float price, Integer quantity, Long categoryId);
}
