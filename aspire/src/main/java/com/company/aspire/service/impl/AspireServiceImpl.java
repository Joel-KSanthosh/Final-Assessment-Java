package com.company.aspire.service.impl;

import com.company.aspire.dto.EmployeeGet;
import com.company.aspire.repository.AspireRepository;
import com.company.aspire.service.AspireService;

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
}
