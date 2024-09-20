package com.inventory.shopcart.repository;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductDTO;

import java.util.List;
import com.inventory.shopcart.dto.ProductGET;


public interface ShopcartRepository {

    void deleteCategory(Long categoryId);
    void deleteProduct(Long productId);
    void buyProduct(ProductGET product, int quantity);
    void createOrder(Long productId, Long userId);

    boolean existsCategoryHasProductWithId(Long categoryId);
    boolean existsCategoryById(Long id);
    boolean existsProductWithId(Long productId);
    boolean existsBuyerWithId(Long id);

    String insertCategory(CategoryDTO categoryDTO);
    String insertProduct(ProductDTO productDTO);

    Object getProductById( Long id);
    Object getAllProducts();

    CategoryDetails findCategoryDetailsById(Long id);
    ProductGET findProductById(Long id);

    List<CategoryDetails> findAllCategoryDetails();
}
