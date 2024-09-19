package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.CategoryDTO;
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


    public void insertCategory(CategoryDTO categoryDTO){
        String query="INSERT INTO Category(name) VALUES(?)";
        System.out.println(categoryDTO.getCategoryName());
        System.out.println("HEllo");
        jdbcTemplate.update(query,categoryDTO.getCategoryName());
        
    }
}
// public void insertUser(User user) {
//     String sql = "INSERT INTO users (id, name, address, email) VALUES (?, ?, ?, ?)";
//     jdbcTemplate.update(sql, user.getId(), user.getName(), user.getAddress(), user.getEmail());
// }

// public void insertUser(User user) {
//     String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
//     jdbcTemplate.update(sql, user.getName(), user.getEmail());
// }