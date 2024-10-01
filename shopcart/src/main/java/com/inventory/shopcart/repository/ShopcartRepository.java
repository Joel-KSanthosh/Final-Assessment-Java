package com.inventory.shopcart.repository;

import com.inventory.shopcart.dto.*;

import java.util.List;


public interface ShopcartRepository {

    void deleteCategory(Long categoryId);
    void deleteProduct(Long productId);
    void buyProduct(ProductGET product, Long quantity);
    void updateProduct(ProductGET productGET);

    void createOrder(Long productId,Long userId,Long quantity);
    boolean existsCategoryHasProductWithId(Long categoryId);
    boolean existsCategoryById(Long id);
    boolean existsCategoryWithName(String name);
    boolean existsProductWithId(Long productId);
    boolean existsBuyerWithId(Long id);
    boolean existsProductWithName(String name);
    boolean existsSellerWithId(Long id);

    String insertCategory(CategoryDTO categoryDTO);
    String insertProduct(ProductDTO productDTO);
    String findCategoryNameWithId(Long id);
    String insertSeller(UserDTO seller);
    String insertBuyer(UserDTO buyer);
    String findProductNameWithId(Long id);
    String restockProduct(Long id,Long quantity);

    Object getAllProducts();
    Object getProductByCategoryId(Long category_id);
    Object getProductById(Long id);
    Object getProductByIdCategoryId(Long id,Long category_id);

    CategoryDetails findCategoryDetailsById(Long id);
    ProductGET findProductById(Long id);
    List<CategoryDetails> findAllCategoryDetails();
    List<String> updateCategory(Long id,String name);

}
