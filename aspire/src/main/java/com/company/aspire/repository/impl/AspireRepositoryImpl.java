package com.company.aspire.repository.impl;

import com.company.aspire.dto.EmployeeGet;
import com.company.aspire.dto.StreamGet;
import com.company.aspire.repository.AspireRepository;

import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AspireRepositoryImpl implements AspireRepository {

    private final JdbcTemplate jdbcTemplate;

    public AspireRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public EmployeeGet findEmployeeById(Long id) {
        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee WHERE id = ?";
        try{
            return jdbcTemplate.queryForObject(query, new RowMapper<EmployeeGet>() {
                @Override
                public EmployeeGet mapRow(ResultSet rs, int rowNum) throws SQLException {
                    EmployeeGet employee = new EmployeeGet();
                    employee.setId(id);
                    employee.setName(rs.getString("name"));
                    employee.setDesignation(rs.getString("designation"));
                    employee.setManagerId(rs.getLong("manager_id"));
                    employee.setAccount(rs.getLong("account_id"));
                    employee.setStream(rs.getLong("stream_id"));
                    return employee;
                }
            },id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Employee with given id doesn't exist!",1);
        }

    }

    @Override
    public EmployeeGet findEmployeeByIdStartingWith(Long id, String word) {
        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee WHERE id = ? AND LIKE ?%";
        try {
            return jdbcTemplate.queryForObject(query, new RowMapper<EmployeeGet>() {
                @Override
                public EmployeeGet mapRow(ResultSet rs, int rowNum) throws SQLException {
                    EmployeeGet employee = new EmployeeGet();
                    employee.setId(id);
                    employee.setName(rs.getString("name"));
                    employee.setDesignation(rs.getString("designation"));
                    employee.setManagerId(rs.getLong("manager_id"));
                    employee.setAccount(rs.getLong("account_id"));
                    employee.setStream(rs.getLong("stream_id"));
                    return employee;
                }
            },id,word);
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Employee with id = "+id+" and starts_with = "+word+" doesn't exist!",1);
        }
    }

    @Override
    public List<EmployeeGet> findEmployeeStartsWith(String word) {
        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee WHERE name LIKE ?";
        try {
            System.out.println("Executing query: " + query + " with parameter: " + word + "%");
            return jdbcTemplate.query(query, new RowMapper<EmployeeGet>() {
                @Override
                public EmployeeGet mapRow(ResultSet rs, int rowNum) throws SQLException {
                    EmployeeGet employee = new EmployeeGet();
                    employee.setId(rs.getLong("id"));
                    employee.setName(rs.getString("name"));
                    employee.setDesignation(rs.getString("designation"));
                    employee.setManagerId(rs.getLong("manager_id"));
                    employee.setAccount(rs.getLong("account_id"));
                    employee.setStream(rs.getLong("stream_id"));
                    return employee;
                }
            },word + "%");
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Employee whose name starts with "+word+" doesn't exist!",1);
        }

    }

    @Override
    public List<String> fetchAllStreams() {
        List<String> streams =new ArrayList<>();
        String query="SELECT name from stream";
        streams= jdbcTemplate.queryForList(query,String.class);
        return streams;

    }

    @Override
    public void updateManagerId(Long id, Long manager_id) {
        if(existsWithEmployeeId(id)){

        }

    }

    @Override
    public boolean existsWithEmployeeId(Long id) {
        String query ="SELECT COUNT(*) FROM employee WHERE ID = ?";
        Long count=jdbcTemplate.queryForObject(query,Long.class,id);
        return count!=null && count>0;
    }

    @Override
    public List<EmployeeGet> findAllEmployee() {
        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee";
        try {
            return jdbcTemplate.query(query, new RowMapper<EmployeeGet>() {
                @Override
                public EmployeeGet mapRow(ResultSet rs, int rowNum) throws SQLException {
                    EmployeeGet employee = new EmployeeGet();
                    employee.setId(rs.getLong("id"));
                    employee.setName(rs.getString("name"));
                    employee.setDesignation(rs.getString("designation"));
                    employee.setManagerId(rs.getLong("manager_id"));
                    employee.setAccount(rs.getLong("account_id"));
                    employee.setStream(rs.getLong("stream_id"));
                    return employee;
                }
            });
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("No Employees Found!",1);
        }
    }




}
