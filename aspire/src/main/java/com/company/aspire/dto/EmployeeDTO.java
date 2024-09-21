package com.company.aspire.dto;

import com.company.aspire.model.Account;
import com.company.aspire.model.Stream;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class EmployeeDTO {

    private String name;

    private String designation;

    private Long account;

    private Long stream;

    private Long managerId;

}
