package com.company.aspire.controller;

import com.company.aspire.dto.CustomResponse;
import com.company.aspire.service.AspireService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class AspireController {

    private final AspireService aspireService;

    public AspireController(AspireService aspireService){
        this.aspireService = aspireService;
    }

    @GetMapping(path = {"/employee","/employee/{id}"})
    public CustomResponse getEmployee(
            @PathVariable(required = false) Long id,
            @RequestParam(required = false) String starts_with
            ){
        return new CustomResponse("Successfully Fetched Employees",aspireService.getEmployee(id,starts_with));
    }

}
