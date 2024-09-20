package com.inventory.shopcart.repository;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.model.Product;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import java.util.List;

public interface ShopcartRepository {

    public String insertCategory(CategoryDTO categoryDTO);
    public String insertProduct(ProductDTO productDTO);
    public void deleteCategory(Long categoryId);
    public void deleteProduct(Long productId);
    public Object getProductById( Long id);

    public Object getAllProducts();
    public CategoryDetails findCategoryDetailsById(Long id);
    public boolean existsCategoryById(Long id);
    public List<CategoryDetails> findAllCategoryDetails();

}
