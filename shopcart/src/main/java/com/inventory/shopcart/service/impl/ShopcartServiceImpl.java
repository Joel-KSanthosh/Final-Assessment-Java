package com.inventory.shopcart.service.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductGET;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.repository.ShopcartRepository;

import com.inventory.shopcart.service.ShopcartService;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopcartServiceImpl implements ShopcartService {

    private final ShopcartRepository shopcartRepository;

    public ShopcartServiceImpl(ShopcartRepository shopcartRepository){
        this.shopcartRepository = shopcartRepository;
    }

    @Override
    @Transactional
    public String insertCategory(CategoryDTO categoryDTO) {
       return shopcartRepository.insertCategory(categoryDTO);
    }

   @Override
    public String insertProduct(ProductDTO productDTO){
       return shopcartRepository.insertProduct(productDTO);
    }

    @Override
    public Object getProducts(Long id){
        if(id!=null){
           return shopcartRepository.getProductById(id);
        }else{
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
    @Transactional
    public void orderProduct(Long productId, Long userId, int quantity) {
        if(shopcartRepository.existsBuyerWithId(userId)){
            ProductGET product = shopcartRepository.findProductById(productId);
            shopcartRepository.buyProduct(product,quantity);
            shopcartRepository.createOrder(productId,userId);
        }
        else {
            throw new NoResultException("Buyer with given id doesn't exist!");
        }
    }

}
