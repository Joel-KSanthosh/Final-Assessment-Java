package com.inventory.shopcart.service;

import com.inventory.shopcart.dto.CategoryDetails;
import com.inventory.shopcart.dto.ProductGET;
import com.inventory.shopcart.repository.ShopcartRepository;
import com.inventory.shopcart.service.impl.ShopcartServiceImpl;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

public class ShopcartServiceTests {

    @InjectMocks
    private ShopcartServiceImpl mockService;

    @Mock
    private ShopcartRepository mockRepository;

    CategoryDetails categoryDetails;
    ProductGET product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryDetails = new CategoryDetails();
        categoryDetails.setCategoryId(1L);
        categoryDetails.setCategoryName("Stationary");
        categoryDetails.setProducts(List.of("Pen","Pencil"));

        product = new ProductGET();
        product.setId(1L);
        product.setName("Pen");
        product.setPrice(10);
        product.setQuantity(10);
        product.setCategoryId(1L);

    }

    @Test
    public void findCategoryDetailsByIdExists(){
        when(mockRepository.existsCategoryById(anyLong())).thenReturn(true);
        when(mockRepository.findCategoryDetailsById(anyLong())).thenReturn(categoryDetails);

        CategoryDetails response = mockService.findCategoryDetailsById(1L);
        assertEquals(response,categoryDetails);
    }

    @Test
    public void findCategoryDetailsByIdNotExists(){
        when(mockRepository.existsCategoryById(anyLong())).thenReturn(false);

        Exception ex = assertThrows(NoResultException.class,
                () -> mockService.findCategoryDetailsById(100L));
        assertThat(ex.getMessage()).isEqualTo("Category with given id doesn't exist!");

    }

    @Test
    public void findAllCategoryDetailsTest(){

        List<CategoryDetails> categoryDetailsList = List.of(categoryDetails);
        when(mockRepository.findAllCategoryDetails()).thenReturn(categoryDetailsList);
        List<CategoryDetails> details = mockService.findAllCategoryDetails();
        assertEquals(details,categoryDetailsList);
    }

    @Test
    public void updateProductWithSameNameTest(){
        when(mockRepository.existsProductWithId(anyLong())).thenReturn(true);
        when(mockRepository.findProductById(anyLong())).thenReturn(product);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> mockService.updateProduct(1L,"Pen",20f,100,1L));

        assertTrue(ex.getMessage().contains("Product's current name is also"));
    }

    @Test
    public void updateProductWithNameAlreadyExistsTest(){
        when(mockRepository.existsProductWithId(anyLong())).thenReturn(true);
        when(mockRepository.findProductById(anyLong())).thenReturn(product);
        when(mockRepository.existsProductWithName(anyString())).thenReturn(true);

        Exception ex = assertThrows(DuplicateKeyException.class,
                () -> mockService.updateProduct(1L,"Bag",200f,100,1L));

        assertTrue(ex.getMessage().contains("Product with"));
    }

    @Test
    public void updateProductWithNotValidPrice(){
        when(mockRepository.existsProductWithId(anyLong())).thenReturn(true);
        when(mockRepository.findProductById(anyLong())).thenReturn(product);
        when(mockRepository.existsProductWithName(anyString())).thenReturn(false);

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> mockService.updateProduct(1L,"Bag",0f,100,1L));

        assertThat(ex.getMessage()).isEqualTo("Enter a valid price!");
    }
}
