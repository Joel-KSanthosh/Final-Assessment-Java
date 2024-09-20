package com.inventory.shopcart.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "role",nullable = false)
    private String role;

}
