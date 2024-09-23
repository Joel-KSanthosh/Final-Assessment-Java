package com.company.aspire.repository.impl;

import com.company.aspire.dto.EmployeeGet;
import com.company.aspire.repository.AspireRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AspireRepositoryImpl implements AspireRepository {

    private JdbcTemplate jdbcTemplate;

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
    public List<EmployeeGet> findEmployeeStartsWith(String word) {
        return List.of();
    }

    @Override
    public List<String> fetchAllStreams() {
        return List.of();
    }
}
