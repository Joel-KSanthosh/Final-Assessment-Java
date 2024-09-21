package com.company.aspire.model;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "designation",nullable = false)
    private String designation;

    @Column(name = "manager_id",nullable = false)
    private Long managerId;

    @ManyToOne
    @JoinColumn(name = "stream_id",nullable = false)
    private Stream stream;

    @ManyToOne
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;

}
