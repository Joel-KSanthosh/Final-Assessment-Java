package com.company.aspire.dto;

import com.company.aspire.model.Designation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {

    @NotBlank(message = "Employee name is mandatory!")
    private String name;

    @NotBlank(message = "Designation is mandatory!")
    private String designation;

    @NotNull(message = "Account Id is mandatory!")
    private Long accountId;

    @NotNull(message = "Stream Id is mandatory!")
    private Long streamId;

    @NotNull(message = "Manager Id is mandatory!")
    @Min(value = 0,message = "Enter a valid manager id!")
    private Long managerId;

    @JsonIgnore
    public boolean isEmployee(){
        return managerId != 0 && Designation.employee.equalsIgnoreCase(designation);
    }

    @JsonIgnore
    public boolean isManager(){
        return managerId == 0 && Designation.manager.equalsIgnoreCase(designation);
    }

}

