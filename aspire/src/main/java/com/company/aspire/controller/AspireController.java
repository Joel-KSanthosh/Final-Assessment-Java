package com.company.aspire.controller;

import com.company.aspire.service.AspireService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AspireController {

    private AspireService aspireService;

    public AspireController(AspireService aspireService){
        this.aspireService = aspireService;
    }
}
