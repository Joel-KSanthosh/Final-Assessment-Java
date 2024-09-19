package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.dto.ProductGET;
import com.inventory.shopcart.repository.ShopcartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String query_category = "SELECT name FROM category WHERE id = ?";
        String query_product_list = "SELECT name FROM product WHERE category_id = ?";

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

    @Override
    public List<CategoryDetails> findAllCategoryDetails() {
        String query = "SELECT c.id,c.name,p.name as product_name FROM category as c LEFT JOIN product as p ON p.category_id = c.id";

        Map<Long, CategoryDetails> categoryMap = new HashMap<>();
        jdbcTemplate.query(query, new RowMapper<CategoryDetails>() {
            @Override
            public CategoryDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long categoryId = rs.getLong("id");
                CategoryDetails categoryDetails = categoryMap.get(categoryId);
                if (categoryDetails == null) {
                    categoryDetails = new CategoryDetails();
                    categoryDetails.setCategoryId(categoryId);
                    categoryDetails.setCategoryName(rs.getString("name"));
                    categoryMap.put(categoryId, categoryDetails);
                }
                List<String> products = categoryDetails.getProducts();
                if (products == null) {
                    products = new ArrayList<>();
                    categoryDetails.setProducts(products);
                }
                products.add(rs.getString("product_name"));
                return categoryDetails;
            }
        });
        return new ArrayList<>(categoryMap.values());
    }

    @Override
    public ProductGET findProductById(Long id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query, new RowMapper<ProductGET>() {
                @Override
                public ProductGET mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ProductGET product = new ProductGET();
                    product.setId(id);
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getFloat("price"));
                    product.setQuantity(rs.getLong("quantity"));
                    product.setCategoryId(rs.getLong("category_id"));
                    return product;
                }
            },id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Product with given id doesn't exist!",1);
        }
    }

    @Override
    public void buyProduct(ProductGET product, int quantity) {
        if(product.getQuantity()>=quantity){
            String query = "UPDATE product SET quantity = quantity - ? WHERE id = ?";
            jdbcTemplate.update(query, quantity, product.getId());
        }
        else {
            throw new DataIntegrityViolationException("Unable to process order to limited stock." +
                    " Item left = "+product.getQuantity());
        }
    }

    @Override
    public void createOrder(Long productId, Long userId) {
        String query = "INSERT INTO Orders(product_id,user_id) VALUES(?,?)";
        jdbcTemplate.update(query,productId,userId);
    }

    @Override
    public boolean existsBuyerWithId(Long id) {
        String query = "SELECT COUNT(*) FROM user WHERE id = ? AND role = 'buyer'";
        Long count = jdbcTemplate.queryForObject(query, Long.class,id);
        return count != null && count > 0;
    }
}
