package com.inventory.shopcart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false,unique = true)
    private String name;

    @Column(name = "price",nullable = false)
    private float price;

    @Column(name = "quantity",nullable = false)
    private long quantity;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}