package com.inventory.shopcart.model;

import jakarta.persistence.*;

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
    private Float price;

    @Column(name = "quantity",nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}