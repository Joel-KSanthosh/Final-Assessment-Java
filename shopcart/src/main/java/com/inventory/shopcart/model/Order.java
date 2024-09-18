package com.inventory.shopcart.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
// import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="order")
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @NotEmpty(message = "product id is mandatory")
    Long Product_id;
    @NotEmpty(message = "user id  is mandatory")
    Long user_id;

}

