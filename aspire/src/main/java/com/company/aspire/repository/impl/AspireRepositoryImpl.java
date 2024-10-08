package com.company.aspire.repository.impl;

import com.company.aspire.dto.*;
import com.company.aspire.repository.AspireRepository;

import jakarta.persistence.NoResultException;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AspireRepositoryImpl implements AspireRepository {

    private final JdbcTemplate jdbcTemplate;

    public AspireRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    Map<Long,EmployeeGet> employeeCache = new HashMap<>();
    List<Long> streamCache = new ArrayList<>();
    List<Long> accountCache = new ArrayList<>();

    private EmployeeGet mapEmployeeGet(ResultSet rs, int rowNum) throws SQLException {
        EmployeeGet employee = new EmployeeGet();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setDesignation(rs.getString("designation"));
        employee.setManagerId(rs.getLong("manager_id"));
        employee.setAccount(rs.getLong("account_id"));
        employee.setStream(rs.getLong("stream_id"));
        return employee;
    }

    @Override
    public EmployeeGet findEmployeeById(Long id) {
        if(employeeCache.containsKey(id)){
            return employeeCache.get(id);
        }
        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee WHERE id = ?";
        try{
            EmployeeGet employee = jdbcTemplate.queryForObject(query, this::mapEmployeeGet, id);
            employeeCache.put(id,employee);
            return employee;
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Employee with given id doesn't exist!",1);
        }

    }

    @Override
    public EmployeeGet findEmployeeByIdStartingWith(Long id, String word) {
        if(employeeCache.containsKey(id)){
            if(employeeCache.get(id).getName().startsWith(word.toUpperCase())){
                return employeeCache.get(id);
            }
            throw new NoResultException("No employees found!");
        }
        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee WHERE id = ? AND name LIKE ?";
        try {
            EmployeeGet employeeGet = jdbcTemplate.queryForObject(query,this::mapEmployeeGet, id, word + "%");
            employeeCache.put(id,employeeGet);
            return employeeGet;
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Employee with id = "+id+" and starts_with = "+word+" doesn't exist!",1);
        }
    }

    @Override
    public List<EmployeeGet> findEmployeeStartsWith(String word) {
        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee WHERE name LIKE ?";
        try {
            return jdbcTemplate.query(query, this::mapEmployeeGet, word + "%");
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Employee whose name starts with "+word+" doesn't exist!",1);
        }

    }

    public StreamGet findStreamAndAccountId(Long streamId){
        String query="SELECT name,account_id from stream WHERE id = ?";
        try{
            return jdbcTemplate.queryForObject(query, new RowMapper<StreamGet>() {
                @Override
                public StreamGet mapRow(ResultSet rs, int rowNum) throws SQLException {
                    StreamGet stream =new StreamGet();
                    stream.setId(streamId);
                    stream.setAccount_Id(rs.getLong("account_id"));
                    stream.setName(rs.getString("name"));

                    return stream;
                }
            },streamId);

        }catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("Stream Id doesn't exist "+streamId,1);
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
    public void updateManagerId(EmployeeGet manager, Long id) {
        String query="UPDATE employee SET manager_id = ? ,stream_id = ?,account_id =? WHERE id = ?";
        jdbcTemplate.update(query,manager.getId(),manager.getStream(),manager.getAccount(),id);
        employeeCache.get(id).setManagerId(manager.getId());
        employeeCache.get(id).setAccount(manager.getAccount());
        employeeCache.get(id).setStream(manager.getStream());

    }

    @Override
    public boolean existsManagerWithStreamId(Long streamId) {
        String query="SELECT COUNT(*) FROM employee WHERE stream_id = ? AND designation = 'manager'";
        Long count=jdbcTemplate.queryForObject(query,Long.class,streamId);
        return count!=null && count>0;
    }


    @Override
    public boolean existsEmployeeWithId(Long id) {
        if(employeeCache.containsKey(id)){
            return employeeCache.get(id).getDesignation().equals("employee");
        }
        String query ="SELECT COUNT(*) FROM employee WHERE id = ? AND designation = 'employee' ";
        Long count = jdbcTemplate.queryForObject(query,Long.class,id);
        return count!=null && count>0;
    }

    @Override
    public boolean existsManagerWithId(Long id) {
        if(employeeCache.containsKey(id)){
            return employeeCache.get(id).getManagerId() == 0;
        }
        String query="SELECT COUNT(*) FROM employee WHERE id = ? AND designation = 'manager' ";
        Long count=jdbcTemplate.queryForObject(query,Long.class,id);
        return count!=null && count>0;
    }

    @Override
    public boolean existsStreamWithId(Long id) {
        String query="SELECT COUNT(*) FROM stream WHERE id = ?";
        Long count=jdbcTemplate.queryForObject(query,Long.class,id);
        return count!=null && count>0;
    }

    @Override
    public boolean existsMangerWithSubOrdinates(Long manager_id) {
        String query="SELECT COUNT(*) FROM employee where manager_id = ? ";
        Long count=jdbcTemplate.queryForObject(query, Long.class,manager_id);
        return count!=null && count>0;
    }


    @Override
    public void changeManagerToEmployee(Long id,String designation,EmployeeGet manager) {
        String query ="UPDATE employee SET manager_id = ?,designation = ? ,stream_id= ?, account_id = ?  WHERE id = ?" ;
        jdbcTemplate.update(query,manager.getId(),designation,manager.getStream(),manager.getAccount(),id);
    }

    @Override
    public void changeEmployeeToManager(Long id,  String designation, StreamGet stream) {
        String query="UPDATE employee SET stream_id = ? ,designation = ? , account_id = ? ,manager_id = ?  WHERE id = ? ";
        jdbcTemplate.update(query,stream.getId(),designation,stream.getAccount_Id(),0,id);

    }

    @Override
    public void insertAccount(AccountDTO account) {
        String query = "INSERT INTO Account(name) VALUES(?)";
        jdbcTemplate.update(query,account.getName());
    }

    @Override
    public void insertStream(StreamDTO stream) {
        String query = "INSERT INTO Stream(name,account_id) VALUES(?,?)";
        jdbcTemplate.update(query,stream.getName(),stream.getAccountId());
    }

    @Override
    public boolean existsAccountWithId(Long accountId) {
        String query = "SELECT COUNT(*) FROM account WHERE id = ?";
        Long count = jdbcTemplate.queryForObject(query, Long.class,accountId);
        return count != null && count>0;
    }

    @Override
    public boolean existsAccountWithName(String name) {
        String query="SELECT COUNT(*) FROM account WHERE name = ?";
        Long count=jdbcTemplate.queryForObject(query,Long.class,name);
        return count!=null && count>0;
    }

    @Override
    public boolean existsStreamWithNameAndAccountId(StreamDTO stream) {
        String query="SELECT COUNT(*) FROM stream WHERE name = ? AND account_id = ?";
        Long count=jdbcTemplate.queryForObject(query,Long.class,stream.getName(),stream.getAccountId());

        return count!=null && count>0;
    }

    @Override
    public boolean existsStreamWithIdAndAccountId(Long streamId, Long accountId) {
        String query = "SELECT COUNT(*) FROM stream WHERE id = ? AND account_id = ?";
        Long count = jdbcTemplate.queryForObject(query,Long.class,streamId,accountId);
        return count != null && count>0;
    }

    @Override
    public void insertEmployee(EmployeeDTO employee) {
        String query = "INSERT INTO Employee(name,designation,account_id,stream_id,manager_id) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(query,employee.getName(),employee.getDesignation(),employee.getAccountId(),employee.getStreamId(),employee.getManagerId());
    }

    @Override
    public boolean existsManagerWithIdAndStreamId(Long id, Long streamId) {
        if(employeeCache.containsKey(id)){
            return employeeCache.get(id).getManagerId() == 0 && employeeCache.get(id).getStream().equals(streamId);
        }
        String query = "SELECT COUNT(*) FROM employee where id = ? AND stream_id = ? AND manager_id = ?";
        Long count = jdbcTemplate.queryForObject(query, Long.class,id,streamId,0);
        return count != null && count>0;
    }

    @Override
    public List<EmployeeGet> findAllEmployee() {

        String query = "SELECT id,name,designation,manager_id,account_id,stream_id FROM employee";
        try {
            return jdbcTemplate.query(query, this::mapEmployeeGet);
        }
        catch (EmptyResultDataAccessException ex){
            throw new EmptyResultDataAccessException("No Employees Found!",1);
        }
    }




}
