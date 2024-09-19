package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.repository.ShopcartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.NoArgsConstructor;

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
}
