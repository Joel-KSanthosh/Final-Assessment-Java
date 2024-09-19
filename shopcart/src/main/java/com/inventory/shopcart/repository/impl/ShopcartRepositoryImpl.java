package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.model.Category;
import com.inventory.shopcart.model.Product;
import com.inventory.shopcart.repository.ShopcartRepository;

import com.inventory.shopcart.service.impl.ShopcartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    private Category mapCategory(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        return category;
    }

    public boolean isProductPresent(Long productId){
    String sql="SELECT COUNT(*) FROM Product WHERE id = ? ";
    Integer count=jdbcTemplate.queryForObject(sql,Integer.class,productId);
    return count!=null && count>0;}

    public boolean isCategoryIdHasProduct(Long  categoryId){

    String sql="SELECT COUNT(*) FROM Product WHERE category_id = ?";
    Integer count=jdbcTemplate.queryForObject(sql,Integer.class,categoryId);
    return count!=null && count>0;
     }

    public boolean iscategoryPresent(Long CategoryId){
    String sql="SELECT COUNT(*) FROM Category WHERE ID = ?";
    Integer count=jdbcTemplate.queryForObject(sql,Integer.class,CategoryId);
    return count!=null && count>0;
}

}

//    @Override
//    public Object getAllProducts() {
//        return Object;
//    }


//    @Override
//    public List<Product> getProduct(@PathVariable(required = false) Long id) {
//        List<Product> products = new ArrayList<Product>();
//        if(id==null){
//
//
//        }else{
//          String query="SELECT * FROM Product WHERE id = ? ";
//            String categorySql = "SELECT * FROM Category WHERE id = ?";
//          return jdbcTemplate.query(query, new RowMapper<Product>() {
//              @Override
//              public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
//                  Product product = new Product();
//                  product.setId(rs.getLong("id"));
//                  product.setName(rs.getString("name"));
//                  product.setPrice(rs.getFloat("price"));
//                  product.setQuantity(rs.getLong("quantity"));
//
//
////                  Long categoryId=rs.getLong("category_id");
////                  Category category =jdbcTemplate.queryForObject(categorySql, new Object[]{categoryId}, new RowMapper<Category>() {
////                      @Override
////                      public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
////                          Category category = new Category();
////                          category.setId(rs.getLong("id"));
////                          category.setName(rs.getString("name"));
////                          return category;
////                      }
////                  });
////                  product.setCategory(category);
////                  return product;
//
//
//
//
//
//
//        return List.of();
//    }

//public boolean isProductPresent(Long productId){
//    String sql="SELECT COUNT(*) FROM Product WHERE id = ? ";
//    Integer count=jdbcTemplate.queryForObject(sql,Integer.class,productId);
//    return count!=null && count>0;
//}
//
//
//public boolean isCategoryIdHasProduct(Long  categoryId){
//
//    String sql="SELECT COUNT(*) FROM Products WHERE id = ?";
//    Integer count=jdbcTemplate.queryForObject(sql,Integer.class,categoryId);
//    return count!=null && count>0;
//}
//
//public boolean iscategoryPresent(Long CategoryId){
//    String sql="SELECT COUNT(*) FROM Category WHERE ID = ?";
//    Integer count=jdbcTemplate.queryForObject(sql,Integer.class,CategoryId);
//    return count!=null && count>0;
//}
//




