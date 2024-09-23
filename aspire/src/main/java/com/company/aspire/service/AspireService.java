package com.company.aspire.service;

import com.company.aspire.dto.EmployeeGet;

import java.util.List;

public interface AspireService {
    List<EmployeeGet> getEmployee(Long id,String word);

    List<String> getStreams();
}
