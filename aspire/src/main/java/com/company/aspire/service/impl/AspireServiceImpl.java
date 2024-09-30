package com.company.aspire.service.impl;

import com.company.aspire.dto.AccountDTO;
import com.company.aspire.dto.EmployeeDTO;
import com.company.aspire.dto.EmployeeGet;
import com.company.aspire.dto.StreamDTO;
import com.company.aspire.repository.AspireRepository;
import com.company.aspire.service.AspireService;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AspireServiceImpl implements AspireService {

    private final AspireRepository aspireRepository;

    public AspireServiceImpl(AspireRepository aspireRepository){
        this.aspireRepository = aspireRepository;
    }


    @Override
    public List<EmployeeGet> getEmployee(Long id, String word) {
         if (id != null && word != null) {
            return List.of(aspireRepository.findEmployeeByIdStartingWith(id,word));
        } else if (id != null) {
             return List.of(aspireRepository.findEmployeeById(id));
        } else if (word != null) {
             return aspireRepository.findEmployeeStartsWith(word);
         }
         return aspireRepository.findAllEmployee();
    }

    @Override
    public List<String> getStreams() {
        return aspireRepository.fetchAllStreams();
    }

    @Override
    public void updateManagerId(Long id,Long manager_id) {
        if(aspireRepository.existsEmployeeWithId(id)){
            if (aspireRepository.existsManagerWithId(manager_id)){
                if(!aspireRepository.findEmployeeById(id).getManagerId().equals(manager_id)){
                aspireRepository.updateManagerId(aspireRepository.findEmployeeById(manager_id),id);
                 }
                else{
                    throw new IllegalArgumentException("Given manager is already the current manager for this employee Id "+id);
                }
            }else{
                throw new NoResultException("Manager id "+id+" doesn't exist");
            }
        }
        else{
             throw new NoResultException("Employee id"+id+" doesn't exist");
        }


    }

    @Override
    public void managerToEmployee(Long id, Long managerId, String designation) {
           if(aspireRepository.existsManagerWithId(id)){
               if(aspireRepository.existsManagerWithId(managerId)){
                  System.out.println(designation);
                  if(designation.equals("employee")){
                      if(!aspireRepository.existsMangerWithSubOrdinates(id)){
                          aspireRepository.changeManagerToEmployee(id,designation,aspireRepository.findEmployeeById(managerId));}
                      else{
                          throw new IllegalArgumentException("Manager has Subordinates");
                      }
                 }else{
                     throw new IllegalArgumentException("Designation should be employee");}
               }else{
                   throw new IllegalArgumentException("Manager Id doesn't exist or Designation of  managerId : "+managerId+" is not manager");
               }
           }else{
               throw new IllegalArgumentException("Employee with id :"+id+" doesn't exist or  Designation of  id : "+id+" is not manager");
           }
    }

    @Override
    public void employeeToManager(Long id, Long streamId, String designation) {
        if(aspireRepository.existsEmployeeWithId(id)){
            if(aspireRepository.existsStreamWithId(streamId)){
                if(designation.equals("manager")){
                   if(!aspireRepository.existsManagerWithStreamId(streamId)){
                       aspireRepository.changeEmployeeToManager(id,designation,aspireRepository.findStreamAndAccountId(streamId));
                   }else{
                       throw new IllegalArgumentException("Manager already exists for this streamId");
                   }

                }else{
                    throw new IllegalArgumentException("Designation should be given as manager");
                }


            }else{
                throw new IllegalArgumentException("Stream Id doesn't "+streamId+" exist");
            }
        }else{
            throw new IllegalArgumentException("EmployeeId doesn't exist or EmployeeId is already a Manager");
        }
    }

    @Override
    public String insertAccount(AccountDTO account) {
        aspireRepository.insertAccount(account);
        return account.getName();
    }

    @Override
    public String insertStream(StreamDTO stream){
        if(aspireRepository.existsAccountWithId(stream.getAccountId())){
            aspireRepository.insertStream(stream);
            return stream.getName();
        }
        throw new IllegalArgumentException("Account with given id doesn't exist!");
    }

    @Override
    public String insertEmployee(EmployeeDTO employee) {
        if(aspireRepository.existsAccountWithId(employee.getAccountId())){
            if(aspireRepository.existsStreamWithIdAndAccountId(employee.getStreamId(), employee.getAccountId())){
                if(employee.isManager()){
                    employee.setDesignation("Manager");
                    if(aspireRepository.existsManagerWithStreamId(employee.getStreamId())){
                        throw new IllegalArgumentException("Manager already exists for stream with id : "+employee.getStreamId());
                    }
                    aspireRepository.insertEmployee(employee);
                    return employee.getName();
                } else if (employee.isEmployee()) {
                    employee.setDesignation("Employee");
                    if(aspireRepository.existsManagerWithIdAndStreamId(employee.getManagerId(), employee.getStreamId())){
                        aspireRepository.insertEmployee(employee);
                        return employee.getName();
                    }
                    throw new IllegalArgumentException("Manager doesn't exist for stream with Id : "+employee.getStreamId());
                }
                else {
                    throw new IllegalArgumentException("Enter a valid Employee!");
                }
            }
            throw new IllegalArgumentException("Entered Stream doesn't exist in given account!");
        }
        throw new IllegalArgumentException("Account with given id doesn't exist!");
    }

}
