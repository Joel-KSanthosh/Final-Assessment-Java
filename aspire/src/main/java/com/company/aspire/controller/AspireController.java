package com.company.aspire.controller;

import com.company.aspire.dto.AccountDTO;
import com.company.aspire.dto.CustomResponse;
import com.company.aspire.dto.EmployeeDTO;
import com.company.aspire.dto.StreamDTO;
import com.company.aspire.service.AspireService;

import jakarta.validation.Valid;
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

    @GetMapping("/streams")
    public CustomResponse getStreams(){
    return new CustomResponse("Successfully Fetched",aspireService.getStreams());
    }

    @PutMapping("/employee/{id}/update")
    public CustomResponse updateEmployee(
            @PathVariable Long id ,
            @RequestParam Long manager_id
            ){
        aspireService.updateManagerId(id,manager_id);
        return new CustomResponse("Successfully Updated manager of "+id+" to "+manager_id);
    }

    @PutMapping("/manager-to-employee")
    public CustomResponse managerToEmployee
            (@RequestParam Long id,
             @RequestParam Long manager_id,
             @RequestParam String designation) {
            aspireService.managerToEmployee(id,manager_id,designation);
            return new CustomResponse("Successfully Changed");
           }
    @PutMapping("/employee-to-manager")
    public CustomResponse employeeToManager(
            @RequestParam Long id,@RequestParam Long stream_id,@RequestParam String designation){
            aspireService.employeeToManager(id,stream_id,designation);
            return new CustomResponse("Successfully Updated");
    }

    @PostMapping("/account/add")
    public CustomResponse insertAccount(@Valid @RequestBody AccountDTO account){
        return new CustomResponse("Successfully Inserted account : "+aspireService.insertAccount(account));
    }

    @PostMapping("/stream/add")
    public CustomResponse insertStream(@Valid @RequestBody StreamDTO stream){
        return new CustomResponse("Successfully Inserted account : "+aspireService.insertStream(stream));
    }

    @PostMapping("/employee/add")
    public CustomResponse insertEmployee(@Valid @RequestBody EmployeeDTO employee){
        return new CustomResponse("Successfully Inserted employee : "+aspireService.insertEmployee(employee));
    }







}
