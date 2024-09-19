package com.inventory.shopcart.model;

import jakarta.persistence.Column;
// import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
// import jakarta.persistence.CascadeType;
// import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="category")
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name",nullable = false,unique = true)
    private String name;

}
