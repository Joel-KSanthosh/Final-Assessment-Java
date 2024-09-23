package com.company.aspire.repository;

import com.company.aspire.dto.EmployeeGet;

import java.util.List;

public interface AspireRepository {

    EmployeeGet findEmployeeById(Long id);
    EmployeeGet findEmployeeByIdStartingWith(Long id, String word);

    List<EmployeeGet> findEmployeeStartsWith(String word);
    List<EmployeeGet> findAllEmployee();

    List<String> fetchAllStreams();
    void updateManagerId(Long id,Long manager_id);

    boolean existsWithEmployeeId(Long id);
}
