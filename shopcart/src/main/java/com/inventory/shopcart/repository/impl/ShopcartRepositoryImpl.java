package com.inventory.shopcart.repository.impl;

import com.inventory.shopcart.repository.ShopcartRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ShopcartRepositoryImpl implements ShopcartRepository {

    private final JdbcTemplate jdbcTemplate;

    public ShopcartRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


//    public long getId(){
//
//    }

}
