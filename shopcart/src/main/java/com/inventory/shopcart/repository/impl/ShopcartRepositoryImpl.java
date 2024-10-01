package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.*;
import com.inventory.shopcart.model.Category;
import com.inventory.shopcart.model.Product;
import com.inventory.shopcart.repository.ShopcartRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ShopcartRepositoryImpl implements ShopcartRepository {

    private final JdbcTemplate jdbcTemplate;

    private Map<Long,String> categoryCache=new HashMap<>();
    private Map<Long,ProductGET> productCache=new HashMap<>();
    private Long maxCategoryKey;
    private Long maxProductKey;

    public ShopcartRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
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
        return productDTO.getProductName();
    }

    @Override
    public String findCategoryNameWithId(Long id) {
        return categoryCache.get(id);
    }

    @Override
    public String findProductNameWithId(Long id) {
        return productCache.get(id).getName();
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
        if(productCache.containsKey(id)){
            return productCache.get(id);
        }
        String query = "SELECT * FROM product WHERE id = ?";
       ProductGET product=jdbcTemplate.queryForObject(query, this::mapProductGet,id);
       productCache.put(id,product);
       return product;

    }

    @Override
    public Object getAllProducts() {
        String query = "SELECT * FROM product";
        return jdbcTemplate.query(query, this::mapProductGet);

    }

    @Override
    public Object getProductByIdCategoryId(Long id, Long category_id) {
        if(productCache.containsKey(id) && productCache.get(id).getCategoryId().equals(category_id)){
            System.out.println(productCache);
            return productCache.get(id);
        }

        try{
            String query = "SELECT * FROM product WHERE id = ? AND category_id = ?";
            ProductGET product= jdbcTemplate.queryForObject(query,this::mapProductGet,id,category_id);
            productCache.put(id,product);
            return product;
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("No products found!",1);
        }

    }

    @Override
    public Object getProductByCategoryId(Long category_id) {
        String query = "SELECT * FROM product WHERE category_id = ?";
        return jdbcTemplate.query(query,this::mapProductGet,category_id);
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
        product.setCategory(category);

        return product;
    }

    private ProductGET mapProductGet(ResultSet rs,int rowNum) throws SQLException{
        ProductGET product = new ProductGET();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getFloat("price"));
        product.setQuantity(rs.getLong("quantity"));
        product.setCategoryId(rs.getLong("category_id"));
        return product;
    }

    @Override
    public CategoryDetails findCategoryDetailsById(Long id) {
        String query_category = "SELECT name FROM category WHERE id = ?";
        String query_product_list = "SELECT name FROM product WHERE category_id = ?";

        CategoryDetails categoryDetails = jdbcTemplate.queryForObject(query_category,(rs, rowNum) -> {
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
        String sql="SELECT COUNT(*) FROM category WHERE id = ? ";
        Integer count=jdbcTemplate.queryForObject(sql,Integer.class,id);
        return count!=null && count>0;

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
    public String restockProduct(Long id, Long quantity) {
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
        productCache.replace(productGET.getId(),productGET);
    }

    @Override
    public ProductGET findProductById(Long id) {
        String query = "SELECT * FROM product WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(query,this::mapProductGet,id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Product with given id doesn't exist!",1);
        }
    }

    @Override
    public void buyProduct(ProductGET product, Long quantity) {
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
    public void createOrder(Long productId, Long userId, Long quantity) {
        String query = "INSERT INTO Orders(product_id,user_id,quantity) VALUES(?,?,?)";
        jdbcTemplate.update(query,productId,userId,quantity);
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
        String sql="SELECT COUNT(*) FROM product WHERE id = ?";
        Integer count=jdbcTemplate.queryForObject(sql,Integer.class,productId);
        return count!=null && count>0;

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




