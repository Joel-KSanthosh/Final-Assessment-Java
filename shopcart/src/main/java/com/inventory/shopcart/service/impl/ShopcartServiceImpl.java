package com.inventory.shopcart.service.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.repository.ShopcartRepository;
import com.inventory.shopcart.service.ShopcartService;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

@Service
public class ShopcartServiceImpl implements ShopcartService {

    private final ShopcartRepository shopcartRepository;

    public ShopcartServiceImpl(ShopcartRepository shopcartRepository){
        this.shopcartRepository = shopcartRepository;
    }

    @Override
    public void insertCategory(CategoryDTO categoryDTO) {

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

}
