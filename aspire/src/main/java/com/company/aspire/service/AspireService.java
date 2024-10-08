package com.company.aspire.service;

import com.company.aspire.dto.AccountDTO;
import com.company.aspire.dto.EmployeeDTO;
import com.company.aspire.dto.EmployeeGet;
import com.company.aspire.dto.StreamDTO;

import java.util.List;

public interface AspireService {
    List<EmployeeGet> getEmployee(Long id,String word);

    List<String> getStreams();

    void updateManagerId(Long id,Long manager_id);

    void managerToEmployee(Long id, Long managerId, String designation);

    void employeeToManager(Long id, Long streamId, String designation);

    String insertAccount(AccountDTO account);

    String insertStream(StreamDTO stream);

    String insertEmployee(EmployeeDTO employee);
}
