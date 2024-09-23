package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.*;
import com.inventory.shopcart.model.Category;
import com.inventory.shopcart.model.Product;
import com.inventory.shopcart.repository.ShopcartRepository;

import jakarta.annotation.PostConstruct;

import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ShopcartRepositoryImpl implements ShopcartRepository {

    private final JdbcTemplate jdbcTemplate;

    private Map<Long,String> categoryCache;
    private Map<Long,String> productCache;
    private Long maxCategoryKey;
    private Long maxProductKey;

    public ShopcartRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init(){
        String productNameQuery = "SELECT id,name FROM product";
        String categoryNameQuery = "SELECT id,name FROM category";

        categoryCache = new HashMap<>();
        productCache = new HashMap<>();

        try {
                jdbcTemplate.query(categoryNameQuery, new RowMapper<Void>() {
                    @Override
                    public Void mapRow(ResultSet rs, int rowNum) throws SQLException {
                        categoryCache.put(rs.getLong("id"), rs.getString("name"));
                        return null;
                    }
                });

                jdbcTemplate.query(productNameQuery, new RowMapper<Void>() {
                    @Override
                    public Void mapRow(ResultSet rs, int rowNum) throws SQLException {
                        productCache.put(rs.getLong("id"),rs.getString("name") );
                        return null;
                    }
                });
        }
        catch (EmptyResultDataAccessException ex){
            System.out.println("No data");
        }

        maxCategoryKey = categoryCache.keySet().stream().max(Long::compare).orElse(0L);
        maxProductKey = productCache.keySet().stream().max(Long::compare).orElse(0L);
    }

    @Override
    public String insertCategory(CategoryDTO categoryDTO){
        String query = "INSERT INTO Category(name) VALUES(?)";

        jdbcTemplate.update(query,categoryDTO.getCategoryName());
        maxCategoryKey += 1;
        categoryCache.put(maxCategoryKey,categoryDTO.getCategoryName());
        return categoryDTO.getCategoryName();
    }

    @Override
    public String insertProduct(ProductDTO productDTO) {
        String query="INSERT INTO Product(name,price,quantity,category_id) VALUES(?,?,?,?)";

        jdbcTemplate.update(query,productDTO.getProductName(),
                productDTO.getPrice(),productDTO.getQuantity(),
                productDTO.getCategory_Id());
        maxProductKey += 1;
        productCache.put(maxProductKey,productDTO.getProductName());
        return productDTO.getProductName();
    }

    @Override
    public String findCategoryNameWithId(Long id) {
        return categoryCache.get(id);
    }

    @Override
    public String findProductNameWithId(Long id) {
        return productCache.get(id);
    }

    @Override
    public String insertBuyer(UserDTO buyer) {
        String query = "INSERT INTO User(name,role) VALUES(?,'buyer')";
        jdbcTemplate.update(query,buyer.getName());
        return buyer.getName();
    }

    @Override
    public String insertSeller(UserDTO seller) {
        String query = "INSERT INTO User(name,role) VALUES(?,'seller')";
        jdbcTemplate.update(query,seller.getName());
        return seller.getName();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        String query="DELETE  FROM Category where id = ?";
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

        Product product = jdbcTemplate.queryForObject(productSql, this::mapProduct,id);
        if (product != null) {
            Category category = jdbcTemplate.queryForObject(categorySql, this::mapCategory,product.getCategory().getId());
            product.setCategory(category);
        }
        return product;

    }


    @Override
    public Object getAllProducts() {
        String productSql = "SELECT * FROM product";
        String categorySql = "SELECT id, name FROM category WHERE id = ?";

        List<Product> products = jdbcTemplate.query(productSql, this::mapProduct);

        for (Product product : products) {
            Category category = jdbcTemplate.queryForObject(categorySql, this::mapCategory,product.getCategory().getId());
            product.setCategory(category);
        }

        return products;
    }

    @Override
    public Object getProductByIdCategoryId(Long id, Long category_id) {
        String productSql = "SELECT * FROM product WHERE id = ? AND category_id = ?";
        String categorySql = "SELECT * FROM category WHERE id = ?";
        try{
            Product product = jdbcTemplate.queryForObject(productSql,this::mapProduct,id,category_id);
            if(product != null){
                Category category = jdbcTemplate.queryForObject(categorySql,this::mapCategory,category_id);
                product.setCategory(category);
            }
            return product;
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("No products found!",1);
        }

    }

    @Override
    public Object getProductByCategoryId(Long category_id) {
        String productSql = "SELECT * FROM product WHERE category_id = ?";
        String categorySql = "SELECT * FROM category WHERE id = ?";
        List<Product> products = jdbcTemplate.query(productSql,this::mapProduct,category_id);
        Category category = jdbcTemplate.queryForObject(categorySql,this::mapCategory,category_id);
        for(Product product : products){
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
        return categoryCache.containsKey(id);
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
                if (rs.getString("product_name") != null) {
                    products.add(rs.getString("product_name"));
                }
                return categoryDetails;
            }
        });
        return new ArrayList<>(categoryMap.values());
    }

    @Override
    public List<String> updateCategory(Long id, String name) {
        String prevCategoryName = findCategoryNameWithId(id);

        String query = "UPDATE category SET name = ? WHERE id = ?";
        jdbcTemplate.update(query,name,id);
        categoryCache.replace(id,name);
        return List.of(prevCategoryName,name);
    }

    @Override
    public String restockProduct(Long id, int quantity) {
        String query = "UPDATE product SET quantity = quantity + ? WHERE id = ?";
        jdbcTemplate.update(query,quantity,id);
        return findProductNameWithId(id);
    }

    @Override
    public void updateProduct(ProductGET productGET) {
        String query = "UPDATE product SET name = ?,price = ?,quantity = ?,category_id = ? WHERE id =?";
        jdbcTemplate.update(
                query,productGET.getName(),
                productGET.getPrice(),
                productGET.getQuantity(),
                productGET.getCategoryId(),
                productGET.getId());
        productCache.replace(productGET.getId(),productGET.getName());
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

    @Override
    public boolean existsSellerWithId(Long id) {
        String query = "SELECT COUNT(*) FROM user WHERE id = ? AND role = 'seller'";
        Long count = jdbcTemplate.queryForObject(query,Long.class,id);
        return count != null && count>0;
    }

    private Category mapCategory(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        return category;
    }

    public boolean existsProductWithId(Long productId){
        return productCache.containsKey(productId);
    }

    public boolean existsCategoryHasProductWithId(Long categoryId){
        String sql="SELECT COUNT(*) FROM product WHERE category_id = ?";
        Integer count=jdbcTemplate.queryForObject(sql,Integer.class,categoryId);
        return count!=null && count>0;
    }

    @Override
    public boolean existsCategoryWithName(String name){
        return categoryCache.containsValue(name);
    }

    @Override
    public boolean existsProductWithName(String name){
        return productCache.containsValue(name);
    }

}




