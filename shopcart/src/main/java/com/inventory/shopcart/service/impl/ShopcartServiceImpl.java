package com.inventory.shopcart.service.impl;

import com.inventory.shopcart.dto.CategoryDTO;
import com.inventory.shopcart.dto.ProductDTO;
import com.inventory.shopcart.repository.ShopcartRepository;
import com.inventory.shopcart.repository.impl.ShopcartRepositoryImpl;
import com.inventory.shopcart.service.ShopcartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ShopcartServiceImpl implements ShopcartService {
    @Autowired
    private ShopcartRepository shopcartRepository;
    @Autowired
    private ShopcartRepositoryImpl shopcartRepositoryImpl;

    public ShopcartServiceImpl(ShopcartRepository shopcartRepository){
        this.shopcartRepository = shopcartRepository;
    }


    @Override
    public String insertCategory(CategoryDTO categoryDTO) {
        System.out.println("entering service of insert category");
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
        if(shopcartRepositoryImpl.iscategoryPresent(id)) {
            System.out.println("chk is categoryPresent"+shopcartRepositoryImpl.iscategoryPresent(id));
            if (!shopcartRepositoryImpl.isCategoryIdHasProduct(id)) {
             System.out.println("chk it has products"+!shopcartRepositoryImpl.isCategoryIdHasProduct(id))   ;
                shopcartRepository.deleteCategory(id);
            } else {
                System.out.println("Category Id has products ,so It cant be deleted");
            }
        }else{
            System.out.println("There is no category id"+id);
        }
    }

    @Override
    public void deleteProduct(Long id) {
     if(shopcartRepositoryImpl.isProductPresent(id)){
         shopcartRepository.deleteProduct(id);
         System.out.println("Product id is deleetd");
     }else{
         System.out.println("Product Id doesn't exists");
     }
    }


}
