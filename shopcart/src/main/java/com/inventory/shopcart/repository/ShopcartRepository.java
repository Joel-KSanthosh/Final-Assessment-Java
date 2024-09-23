package com.inventory.shopcart.repository;

import com.inventory.shopcart.dto.*;

import java.util.List;


public interface ShopcartRepository {

    void deleteCategory(Long categoryId);
    void deleteProduct(Long productId);
    void buyProduct(ProductGET product, Long quantity);
    void createOrder(Long productId,Long userId,Long quantity);

    boolean existsCategoryHasProductWithId(Long categoryId);
    boolean existsCategoryById(Long id);
    boolean existsProductWithId(Long productId);
    boolean existsBuyerWithId(Long id);
    boolean existsSellerWithId(Long id);
    boolean existsProductWithName(String name);
    boolean existsCategoryWithName(String name);

    String insertCategory(CategoryDTO categoryDTO);
    String insertProduct(ProductDTO productDTO);
    String findCategoryNameWithId(Long id);
    String findProductNameWithId(Long id);
    String insertBuyer(UserDTO buyer);
    String insertSeller(UserDTO seller);

    Object getProductById(Long id);
    Object getAllProducts();
    Object getProductByIdCategoryId(Long id,Long category_id);
    Object getProductByCategoryId(Long category_id);

    CategoryDetails findCategoryDetailsById(Long id);
    ProductGET findProductById(Long id);

    List<CategoryDetails> findAllCategoryDetails();
    List<String> updateCategory(Long id,String name);
    String restockProduct(Long id,Long quantity);
    void updateProduct(ProductGET productGET);

}
