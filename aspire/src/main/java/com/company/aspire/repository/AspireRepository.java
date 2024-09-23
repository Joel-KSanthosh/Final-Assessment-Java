package com.company.aspire.repository;

import com.company.aspire.dto.EmployeeGet;

import java.util.List;

public interface AspireRepository {

    EmployeeGet findEmployeeById(Long id);

    List<EmployeeGet> findEmployeeStartsWith(String word);

    List<String> fetchAllStreams();

}
