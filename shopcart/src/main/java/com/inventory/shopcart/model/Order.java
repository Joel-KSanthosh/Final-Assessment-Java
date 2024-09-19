package com.inventory.shopcart.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="orders")
public class Order implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @CreatedDate
    @Column(name = "order_date",updatable = false,nullable = false)
    private Date orderDate;

    @Override
    public boolean isNew() {
        return orderDate == null;
    }
}

