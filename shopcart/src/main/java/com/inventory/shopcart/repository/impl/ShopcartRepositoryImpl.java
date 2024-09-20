package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.dto.ProductGET;
import com.inventory.shopcart.model.Category;
import com.inventory.shopcart.model.Product;
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
    public String insertCategory(CategoryDTO categoryDTO){

        String query = "INSERT INTO Category(name) VALUES(?)";
        jdbcTemplate.update(query,categoryDTO.getCategoryName());
        return categoryDTO.getCategoryName();
    }

    @Override
    public String  insertProduct(ProductDTO productDTO) {
        System.out.println( productDTO.getProductName());

        String query="INSERT INTO Product(name,price,quantity,category_id) VALUES(?,?,?,?)";
        jdbcTemplate.update(query,productDTO.getProductName(),
                productDTO.getPrice(),productDTO.getQuantity(),
                productDTO.getCategory_Id());
        return productDTO.getProductName();
    }

    @Override
    public void deleteCategory(Long categoryId) {

        String query="DELETE FROM Category where id = ?";
        jdbcTemplate.update(query,categoryId);
    }

    @Override
    public void deleteProduct(Long productId) {
        String sql="DELETE FROM Product where id = ?";
        jdbcTemplate.update(sql,productId);

    }

    @Override
    public Object getProductById(Long id) {
        String productSql = "SELECT * FROM product WHERE id = ?";
        String categorySql = "SELECT id, name FROM category WHERE id = ?";

        // Fetching product by ID
        Product product = jdbcTemplate.queryForObject(productSql, new Object[]{id}, this::mapProduct);

        // Fetching associated category
        if (product != null) {

            Category category = jdbcTemplate.queryForObject(categorySql, new Object[]{product.getCategory().getId()}, this::mapCategory);
            product.setCategory(category);
        }

        return product;
    }


    @Override
    public Object getAllProducts() {
        String productSql = "SELECT * FROM product";
        String categorySql = "SELECT id, name FROM category WHERE id = ?";

        // Fetching all products
        List<Product> products = jdbcTemplate.query(productSql, this::mapProduct);

        // Fetching associated categories
        for (Product product : products) {
            Category category = jdbcTemplate.queryForObject(categorySql, new Object[]{product.getCategory().getId()}, this::mapCategory);
            product.setCategory(category);
        }

        return products;
    }

    private Product mapProduct(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getFloat("price"));
        product.setQuantity(rs.getLong("quantity"));

        // Assuming `category_id` exists in the product table
        Long categoryId = rs.getLong("category_id");
        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);  // Only setting category ID for now

        return product;
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
    private Category mapCategory(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        return category;
    }

    public boolean existsProductWithId(Long productId){
        String sql="SELECT COUNT(*) FROM product WHERE id = ? ";
        Integer count=jdbcTemplate.queryForObject(sql,Integer.class,productId);
        return count!=null && count>0;}

    public boolean existsCategoryHasProductWithId(Long categoryId){
        String sql="SELECT COUNT(*) FROM product WHERE category_id = ?";
        Integer count=jdbcTemplate.queryForObject(sql,Integer.class,categoryId);
        return count!=null && count>0;
    }

}




