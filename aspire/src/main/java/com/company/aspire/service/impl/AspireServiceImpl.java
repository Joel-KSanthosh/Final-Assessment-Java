package com.company.aspire.service.impl;

import com.company.aspire.repository.AspireRepository;
import com.company.aspire.service.AspireService;
import org.springframework.stereotype.Service;

@Service
public class AspireServiceImpl implements AspireService {

    private AspireRepository aspireRepository;

    public AspireServiceImpl(AspireRepository aspireRepository){
        this.aspireRepository = aspireRepository;
    }
}
