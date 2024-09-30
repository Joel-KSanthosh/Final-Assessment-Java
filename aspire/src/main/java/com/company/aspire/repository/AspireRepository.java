package com.company.aspire.repository;

import com.company.aspire.dto.*;

import java.util.List;

public interface AspireRepository {

    EmployeeGet findEmployeeById(Long id);
    EmployeeGet findEmployeeByIdStartingWith(Long id, String word);

    List<EmployeeGet> findEmployeeStartsWith(String word);
    List<EmployeeGet> findAllEmployee();
    StreamGet findStreamAndAccountId(Long streamId);
    List<String> fetchAllStreams();
    void updateManagerId(EmployeeGet manager,Long id);

    boolean existsManagerWithStreamId(Long StreamId);
    boolean existsEmployeeWithId(Long id);
    boolean existsManagerWithId(Long id);
    boolean existsStreamWithId(Long id);
    boolean existsMangerWithSubOrdinates(Long manager_id);
    void changeManagerToEmployee(Long id,  String designation,EmployeeGet manager);
    void changeEmployeeToManager(Long id,  String designation, StreamGet stream);
    void insertAccount(AccountDTO account);
    void insertStream(StreamDTO stream);
    boolean existsAccountWithId(Long accountId);
    boolean existsStreamWithIdAndAccountId(Long streamId,Long accountId);
    void insertEmployee(EmployeeDTO employee);
    boolean existsManagerWithIdAndStreamId(Long id,Long streamId);
}
