package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.repository.ShopcartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShopcartRepositoryImpl implements ShopcartRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public ShopcartRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertCategory(CategoryDTO categoryDTO){
        String query="INSERT INTO Category(name) VALUES(?)";
        jdbcTemplate.update(query,categoryDTO.getCategoryName());
    }

    @Override
    public void insertProduct(ProductDTO productDTO) {
        System.out.println( productDTO.getProductName());
        String query="INSERT INTO Product(name,price,quantity,category_id) VALUES(?,?,?,?)";
        jdbcTemplate.update(query,productDTO.getProductName(),
                productDTO.getPrice(),productDTO.getQuantity(),
                productDTO.getCategory_Id());
    }

    @Override
    public CategoryDetails findCategoryDetailsById(Long id) {
        String query_category = "SELECT name from category as c WHERE c.id = ?";
        String query_product_list = "SELECT name from product as p WHERE p.category_id = ?";

        CategoryDetails categoryDetails = jdbcTemplate.queryForObject(query_category, (rs, rowNum) -> {
            CategoryDetails details = new CategoryDetails();
            details.setCategoryId(id);
            details.setCategoryName(rs.getString("name"));
            return details;
        }, id);

        List<String> products = jdbcTemplate.queryForList(query_product_list,String.class,id);

        if (categoryDetails != null) {
            categoryDetails.setProducts(products);
        }
        return categoryDetails;
    }

    @Override
    public boolean existsCategoryById(Long id) {
        String query = "SELECT COUNT(*) FROM category WHERE id = ?";
        Long count = jdbcTemplate.queryForObject(query,Long.class,id);
        return count!=null && count > 0;
    }
}
