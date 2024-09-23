package com.inventory.shopcart.service.impl;

import com.inventory.shopcart.dto.*;
import com.inventory.shopcart.repository.ShopcartRepository;

import com.inventory.shopcart.service.ShopcartService;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopcartServiceImpl implements ShopcartService {

    private final ShopcartRepository shopcartRepository;

    public ShopcartServiceImpl(ShopcartRepository shopcartRepository){
        this.shopcartRepository = shopcartRepository;
    }

    @Override
    public String insertCategory(CategoryDTO categoryDTO) {
        if(shopcartRepository.existsCategoryWithName(categoryDTO.getCategoryName())){
            throw new DuplicateKeyException("Category with name "+categoryDTO.getCategoryName()+" already exists!");
        }
       return shopcartRepository.insertCategory(categoryDTO);
    }

   @Override
    public String insertProduct(ProductDTO productDTO){
        if(shopcartRepository.existsCategoryById(productDTO.getCategory_Id())){
            if(shopcartRepository.existsProductWithName(productDTO.getProductName())){
                throw new DuplicateKeyException("Product with name "+productDTO.getProductName()+" already exists!");
            }
            return shopcartRepository.insertProduct(productDTO);
        }
        else{
             throw new NoResultException("Category with given id doesn't exist!");
        }
    }

    @Override
    public String restockProduct(Long id, Long user_id, Long quantity) {
        if(shopcartRepository.existsSellerWithId(user_id)){
            if(shopcartRepository.existsProductWithId(id)){
                return shopcartRepository.restockProduct(id,quantity);
            }
            throw new NoResultException("Product with given id doesn't exist!");
        }
        throw new NoResultException("Seller with given id doesn't exist!");
    }

    @Override
    public String insertBuyer(UserDTO buyer) {
        return shopcartRepository.insertBuyer(buyer);
    }

    @Override
    public String insertSeller(UserDTO seller) {
        return shopcartRepository.insertSeller(seller);
    }

    @Override
    public Object getProducts(Long id, Long category_id){
        if(id!=null && category_id != null){
            if(shopcartRepository.existsProductWithId(id)){
                if(shopcartRepository.existsCategoryById(category_id)){
                    return List.of(shopcartRepository.getProductByIdCategoryId(id,category_id));
                }
                throw new NoResultException("Category with given id doesn't exist!");
            }
            throw new NoResultException("Product with given id doesn't exist!");
        } else if (id != null) {
            if(shopcartRepository.existsProductWithId(id)) {
                return List.of(shopcartRepository.getProductById(id));
            }
            throw new NoResultException("Product with given id doesn't exist!");
        } else if (category_id != null) {
            if(shopcartRepository.existsCategoryById(category_id)){
                return shopcartRepository.getProductByCategoryId(category_id);
            }
            throw new NoResultException("Category with given id doesn't exist!");
        } else{
            return shopcartRepository.getAllProducts();
        }

    }

    @Override
    public void deleteCategory(Long id){
        if(shopcartRepository.existsCategoryById(id)) {
            if (!shopcartRepository.existsCategoryHasProductWithId(id)) {
                shopcartRepository.deleteCategory(id);
            } else {
                throw new DataIntegrityViolationException("Product exists in the given category!");
            }
        }else{
            throw new NoResultException("Category with given id doesn't exist!");
        }
    }

    @Override
    public void deleteProduct(Long id) {
     if(shopcartRepository.existsProductWithId(id)){
         shopcartRepository.deleteProduct(id);
     }else{
         throw new NoResultException("Product with given id doesn't exist!");
     }
    }

    @Override
    public CategoryDetails findCategoryDetailsById(Long id) {
        if(shopcartRepository.existsCategoryById(id)){
            return shopcartRepository.findCategoryDetailsById(id);
        }
        else {
            throw new NoResultException("Category with given id doesn't exist!");
        }
    }

    @Override
    public List<CategoryDetails> findAllCategoryDetails() {
        return shopcartRepository.findAllCategoryDetails();
    }

    @Override
    public ProductGET updateProduct(Long id, String name, Float price, Integer quantity, Long categoryId) {
        if(shopcartRepository.existsProductWithId(id)){
            ProductGET productGET = shopcartRepository.findProductById(id);
            if(name != null){
                if(productGET.getName().equals(name)){
                    throw new IllegalArgumentException("Product's current name is also "+ name);
                }
                if(shopcartRepository.existsProductWithName(name)){
                    throw new DuplicateKeyException("Product with "+name+" already exists");
                }
                productGET.setName(name);
            }
            if(price != null){
                if(price < 1){
                    throw new IllegalArgumentException("Enter a valid price!");
                }
                productGET.setPrice(price);
            }
            if(quantity != null){
                if(quantity < 0){
                    throw new IllegalArgumentException("Enter a valid quantity!");
                }
                productGET.setQuantity(quantity);
            }
            if(categoryId != null){
                if(productGET.getCategoryId().equals(categoryId)){
                    throw new IllegalArgumentException("Product currently exists in the same category");
                }
                if(!shopcartRepository.existsCategoryById(categoryId)){
                    throw new NoResultException("Category with given id doesn't exist!");
                }
                productGET.setCategoryId(categoryId);
            }
            shopcartRepository.updateProduct(productGET);
            return productGET;
        }
        throw new NoResultException("Product with given id doesn't exist!");
    }

    @Override
    @Transactional
    public void orderProduct(Long productId, Long userId, Long quantity) {
        if(shopcartRepository.existsBuyerWithId(userId)){
            ProductGET product = shopcartRepository.findProductById(productId);
            shopcartRepository.buyProduct(product,quantity);
            shopcartRepository.createOrder(productId,userId,quantity);
        }
        else {
            throw new NoResultException("Buyer with given id doesn't exist!");
        }
    }

    @Override
    public List<String> updateCategory(Long id,String name){
        if(shopcartRepository.existsCategoryById(id)){
            if (shopcartRepository.existsCategoryWithName(name)){
                throw new DuplicateKeyException("Category with name "+name+" already exists!");
            }
            return shopcartRepository.updateCategory(id,name);
        }
        throw new NoResultException("Category with given id doesn't exist!");
    }



}
