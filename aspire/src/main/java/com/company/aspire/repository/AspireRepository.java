package com.company.aspire.repository;

import com.company.aspire.dto.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface AspireRepository {

    EmployeeGet findEmployeeById(Long id);
    EmployeeGet findEmployeeByIdStartingWith(Long id, String word);

    List<EmployeeGet> findEmployeeStartsWith(String word);
    List<EmployeeGet> findAllEmployee();
    List<String> fetchAllStreams();

    StreamGet findStreamAndAccountId(Long streamId);

    boolean existsEmployeeWithId(Long id);
    boolean existsManagerWithStreamId(Long StreamId);
    boolean existsManagerWithId(Long id);
    boolean existsStreamWithId(Long id);
    boolean existsMangerWithSubOrdinates(Long manager_id);
    boolean existsStreamWithIdAndAccountId(Long streamId,Long accountId);
    boolean existsManagerWithIdAndStreamId(Long id,Long streamId);
    boolean existsAccountWithId(Long accountId);

    void updateManagerId(EmployeeGet manager,Long id);
    void changeManagerToEmployee(Long id,  String designation,EmployeeGet manager);
    void changeEmployeeToManager(Long id,  String designation, StreamGet stream);
    void insertAccount(AccountDTO account);
    void insertStream(StreamDTO stream);
    void insertEmployee(EmployeeDTO employee);
}
