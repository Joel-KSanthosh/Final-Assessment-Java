package com.company.aspire;

import com.company.aspire.dto.EmployeeGet;
import com.company.aspire.dto.StreamGet;
import com.company.aspire.repository.AspireRepository;

import com.company.aspire.service.impl.AspireServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ServiceTest {
    @Mock
    private AspireRepository aspireRepository;

    @InjectMocks
    private AspireServiceImpl aspireService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeByIdAndWord(){

      EmployeeGet employeeGet = new EmployeeGet();

      when(aspireRepository.findEmployeeByIdStartingWith(anyLong(),anyString())).thenReturn(employeeGet);
      List<EmployeeGet> result = aspireService.getEmployee(1L,"S");
      assertEquals(List.of(employeeGet),result);

    }

    @Test
    void testGetEmployeeById(){
        EmployeeGet employeeGet = new EmployeeGet();
        when(aspireRepository.findEmployeeById(anyLong())).thenReturn(employeeGet);
        List<EmployeeGet>  result=aspireService.getEmployee(1L,null);
        assertEquals(List.of(employeeGet),result);

    }

    @Test
    void testGetEmployeeByWord(){
        EmployeeGet employeeGet=new EmployeeGet();
        when(aspireRepository.findEmployeeStartsWith(anyString())).thenReturn(List.of(employeeGet));
        List<EmployeeGet> result =aspireService.getEmployee(null,"s");
        assertEquals(List.of(employeeGet),result);

    }
    @Test
    void testGetAllEmployee(){
          EmployeeGet employeeGet=new EmployeeGet();
          when(aspireRepository.findAllEmployee()).thenReturn(List.of(employeeGet));
          List<EmployeeGet> result =aspireService.getEmployee(null,null);
          assertEquals(List.of(employeeGet),result);
    }

    @Test
    void testGetAllStreams(){
        StreamGet streamGet=new StreamGet();
        when(aspireRepository.fetchAllStreams()).thenReturn(List.of(String.valueOf(streamGet)));
        List<String> result =aspireService.getStreams();
        assertEquals(List.of(String.valueOf(streamGet)),result);
    }

//    @Test
//    void testUpdateEmployeeSuccess(){
//        EmployeeGet employeeGet=new EmployeeGet();
//        when(aspireRepository.existsEmployeeWithId(anyLong())).thenReturn(true);
//        when(aspireRepository.existsManagerWithStreamId(anyLong())).thenReturn(true);
//        when(aspireRepository.findEmployeeById(anyLong()).getManagerId().equals(anyLong())).thenReturn(false);
//        when(aspireRepository.findEmployeeById(anyLong())).thenReturn(employeeGet);
//        when(aspireRepository.updateManagerId(anyLong(),anyLong())).thenAnswer(
//                invocation -> {
//                    EmployeeGet e = invocation.getArgument(0);
//                    assertEquals("mgr",e.getDesignation());
//                    return null;
//                }
//        );
////
}











