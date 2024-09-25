package com.company.aspire;

import com.company.aspire.dto.EmployeeGet;
import com.company.aspire.dto.StreamGet;
import com.company.aspire.repository.AspireRepository;

import com.company.aspire.service.impl.AspireServiceImpl;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.RuntimeMBeanException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    void testGetEmployeeByIdAndWord_Failure(){
        when(aspireRepository.findEmployeeByIdStartingWith(anyLong(),anyString())).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception=assertThrows(RuntimeException.class,()->{
            aspireService.getEmployee(anyLong(),anyString());
        });
        assertEquals("Database error",exception.getMessage());
    }

    @Test
    void testGetEmployeeById(){
        EmployeeGet employeeGet = new EmployeeGet();
        when(aspireRepository.findEmployeeById(anyLong())).thenReturn(employeeGet);
        List<EmployeeGet>  result=aspireService.getEmployee(1L,null);
        assertEquals(List.of(employeeGet),result);

    }

    @Test
    void testGetEmployeeById_Failure(){
        when(aspireRepository.findEmployeeById(anyLong())).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception=assertThrows(RuntimeException.class,()->{
            aspireService.getEmployee(anyLong(),null);

        });
        assertEquals("Database error",exception.getMessage());
    }

    @Test
    void testGetEmployeeByWord(){
        EmployeeGet employeeGet=new EmployeeGet();
        when(aspireRepository.findEmployeeStartsWith(anyString())).thenReturn(List.of(employeeGet));
        List<EmployeeGet> result =aspireService.getEmployee(null,"s");
        assertEquals(List.of(employeeGet),result);

    }

    @Test
    void testGetEmployeeByWord_Failure(){
        when(aspireRepository.findEmployeeStartsWith(anyString())).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception=assertThrows(RuntimeException.class,()->{
            aspireService.getEmployee(null,"s");

        });
        assertEquals("Database error",exception.getMessage());
    }
    @Test
    void testGetAllEmployee(){
          EmployeeGet employeeGet=new EmployeeGet();
          when(aspireRepository.findAllEmployee()).thenReturn(List.of(employeeGet));
          List<EmployeeGet> result =aspireService.getEmployee(null,null);
          assertEquals(List.of(employeeGet),result);
    }
    @Test
    void testGetAllEmployee_Failure(){
        when(aspireRepository.findAllEmployee()).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception = assertThrows(RuntimeException.class,()-> {
            aspireService.getEmployee(null,null);

        });
        assertEquals("Database error",exception.getMessage());

    }


    @Test
    void testGetAllStreams(){
        StreamGet streamGet=new StreamGet();
        when(aspireRepository.fetchAllStreams()).thenReturn(List.of(String.valueOf(streamGet)));
        List<String> result =aspireService.getStreams();
        assertEquals(List.of(String.valueOf(streamGet)),result);
    }



    @Test
    void testGetAllStreams_Failure() {
        // Arrange
        when(aspireRepository.fetchAllStreams()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            aspireService.getStreams();
        });

        assertEquals("Database error", exception.getMessage());
    }




    @Test
    void testUpdateManagerId_Success() {
        // Arrange
        Long employeeId = 1L;
        Long newManagerId = 2L;

        EmployeeGet employee = new EmployeeGet();
        employee.setManagerId(1L); // Current manager ID
        EmployeeGet manager = new EmployeeGet();
        manager.setManagerId(newManagerId);
        // Mock repository responses
        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(true);
        when(aspireRepository.existsManagerWithId(newManagerId)).thenReturn(true);
        when(aspireRepository.findEmployeeById(employeeId)).thenReturn(employee);
        when(aspireRepository.findEmployeeById(newManagerId)).thenReturn(manager);
        // Act
        aspireService.updateManagerId(employeeId, newManagerId);
        // Assert
        verify(aspireRepository).updateManagerId(eq(manager),eq(employeeId));
    }

    @Test
    void testUpdateManagerId_ManagerAlreadyAssigned() {
        // Arrange
        Long employeeId = 1L;
        Long currentManagerId = 2L;

        EmployeeGet employee = new EmployeeGet();
        employee.setManagerId(currentManagerId);  // Same as the new managerId


        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(true);
        when(aspireRepository.existsManagerWithId(currentManagerId)).thenReturn(true);
        when(aspireRepository.findEmployeeById(employeeId)).thenReturn(employee);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                aspireService.updateManagerId(employeeId, currentManagerId)
        );
        assertEquals("Given manager is already the current manager for this employee Id " + employeeId, exception.getMessage());


        verify(aspireRepository, never()).updateManagerId(any(EmployeeGet.class), anyLong());
    }

    @Test
    void testUpdateManagerId_EmployeeNotFound(){
        Long employeeId=1L;
        Long newManagerId=2L;

        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(false);
        NoResultException exception =assertThrows(NoResultException.class,()->aspireService.updateManagerId(employeeId,newManagerId));
        assertEquals("Employee id"+employeeId+" doesn't exist",exception.getMessage());
        verify(aspireRepository,never()).updateManagerId(any(EmployeeGet.class),anyLong());
    }

    @Test
    void testUpdateManagerId_ManagerNotFound() {
        // Arrange
        Long employeeId = 1L;
        Long newManagerId = 2L;

        EmployeeGet employee = new EmployeeGet();
        employee.setManagerId(1L); // Current manager ID

        // Mock repository responses
        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(true);
        when(aspireRepository.existsManagerWithId(newManagerId)).thenReturn(false);

        // Act & Assert
        NoResultException exception = assertThrows(NoResultException.class, () ->
                aspireService.updateManagerId(employeeId, newManagerId)
        );
        assertEquals("Manager id " + employeeId + " doesn't exist", exception.getMessage());

        // Verify that the updateManagerId method was not called
        verify(aspireRepository, never()).updateManagerId(any(EmployeeGet.class), anyLong());
    }

    @Test
    void testEmployeeToManager_Success(){
        StreamGet streamGet=new StreamGet();
        Long employeeId=1L;
        Long streamId=2L;
        String designation="manager";
        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(true);
        when(aspireRepository.existsStreamWithId(streamId)).thenReturn(true);
        when(aspireRepository.existsManagerWithStreamId(streamId)).thenReturn(false);
        when(aspireRepository.findStreamAndAccountId(streamId)).thenReturn(streamGet);
        aspireService.employeeToManager(employeeId,streamId,designation);
        verify(aspireRepository).changeEmployeeToManager(eq(employeeId),eq(designation),any(StreamGet.class));

    }

    @Test
    void testEmployeeToManager_Failure_EmployeeDoesNotExist() {
        Long employeeId=1L;
        Long streamId=2L;
        String designation="manager";
        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(false);
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->{
            aspireService.employeeToManager(employeeId,streamId,designation);
        });
        assertEquals("EmployeeId doesn't exist or EmployeeId is already a Manager", exception.getMessage());
    }

    @Test
    void testEmployeeToManager_Failure_StreamDoesNotExist() {
        Long employeeId=1L;
        Long streamId=2L;
        String designation="manager";
        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(true);
        when(aspireRepository.existsStreamWithId(streamId)).thenReturn(false);

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->{
            aspireService.employeeToManager(employeeId,streamId,designation);
        });
        assertEquals("Stream Id doesn't " + streamId + " exist", exception.getMessage());
    }

    @Test
    void testEmployeeToManager_Failure_InvalidDesignation() {
        Long employeeId=1L;
        Long streamId=2L;
        String designation="employee";
        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(true);
        when(aspireRepository.existsStreamWithId(streamId)).thenReturn(true);
        IllegalArgumentException exception =assertThrows(IllegalArgumentException.class,()->{
            aspireService.employeeToManager(employeeId, streamId, designation);
        });
        assertEquals("Designation should be given as manager", exception.getMessage());
    }
    @Test
    void testEmployeeToManager_Failure_ManagerAlreadyExists() {
        Long employeeId=1L;
        Long streamId=2L;
        String designation="manager";
        when(aspireRepository.existsEmployeeWithId(employeeId)).thenReturn(true);
        when(aspireRepository.existsStreamWithId(streamId)).thenReturn(true);
        when(aspireRepository.existsManagerWithStreamId(streamId)).thenReturn(true);

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class,()->{
            aspireService.employeeToManager(employeeId,streamId,designation);
        });
        assertEquals("Manager already exists for this streamId", exception.getMessage());
    }

    @Test
    void testManagerToEmployee_Success() {

        Long managerId = 1L;
        Long newManagerId = 2L;
        String designation = "employee";

        EmployeeGet employee = new EmployeeGet();
        employee.setManagerId(newManagerId); // Setting new manager

        when(aspireRepository.existsManagerWithId(managerId)).thenReturn(true);
        when(aspireRepository.existsManagerWithId(newManagerId)).thenReturn(true);
        when(aspireRepository.existsMangerWithSubOrdinates(managerId)).thenReturn(false);
        when(aspireRepository.findEmployeeById(newManagerId)).thenReturn(employee);

        // Act
        aspireService.managerToEmployee(managerId, newManagerId, designation);

        // Assert
        verify(aspireRepository).changeManagerToEmployee(eq(managerId), eq(designation), eq(employee));
    }
    @Test
    void testManagerToEmployee_Failure_ManagerIdDoesNotExist() {
        // Arrange
        Long managerId = 1L;
        Long newManagerId = 2L;
        String designation = "employee";

        when(aspireRepository.existsManagerWithId(managerId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            aspireService.managerToEmployee(managerId, newManagerId, designation);
        });

        assertEquals("Employee with id :" + managerId + " doesn't exist or  Designation of  id : " + managerId + " is not manager", exception.getMessage());
    }

    @Test
    void testManagerToEmployee_Failure_NewManagerIdDoesNotExist() {
        // Arrange
        Long managerId = 1L;
        Long newManagerId = 2L;
        String designation = "employee";

        when(aspireRepository.existsManagerWithId(managerId)).thenReturn(true);
        when(aspireRepository.existsManagerWithId(newManagerId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            aspireService.managerToEmployee(managerId, newManagerId, designation);
        });

        assertEquals("Manager Id doesn't exist or Designation of  managerId : " + newManagerId + " is not manager", exception.getMessage());
    }

    @Test
    void testManagerToEmployee_Failure_InvalidDesignation() {
        // Arrange
        Long managerId = 1L;
        Long newManagerId = 2L;
        String designation = "manager";  // Invalid designation (should be "employee")

        when(aspireRepository.existsManagerWithId(managerId)).thenReturn(true);
        when(aspireRepository.existsManagerWithId(newManagerId)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            aspireService.managerToEmployee(managerId, newManagerId, designation);
        });

        assertEquals("Designation should be employee", exception.getMessage());
    }
    @Test
    void testManagerToEmployee_Failure_ManagerHasSubordinates() {
        // Arrange
        Long managerId = 1L;
        Long newManagerId = 2L;
        String designation = "employee";

        when(aspireRepository.existsManagerWithId(managerId)).thenReturn(true);
        when(aspireRepository.existsManagerWithId(newManagerId)).thenReturn(true);
        when(aspireRepository.existsMangerWithSubOrdinates(managerId)).thenReturn(true);  // Manager has subordinates

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            aspireService.managerToEmployee(managerId, newManagerId, designation);
        });

        assertEquals("Manager has SubOrdinates", exception.getMessage());
    }










//    @Test
//    void testUpdateEmployeeSuccess() {
//        EmployeeGet employeeGet = new EmployeeGet();
//        when(aspireRepository.existsEmployeeWithId(anyLong())).thenReturn(true);
//        when(aspireRepository.existsManagerWithId(anyLong())).thenReturn(true);
//        when(aspireRepository.findEmployeeById(anyLong()).getManagerId().equals(anyLong())).thenReturn(false);
//        when(aspireRepository.findEmployeeById(anyLong())).thenReturn(employeeGet);
//        doAnswer(invocation ->{
//            EmployeeGet updatedEmployee=invocation.getArgument(0);
//            Long newManagerId =invocation.getArgument(1);
//            assertEquals(2L,newManagerId);
//            assertEquals("mgr",updatedEmployee.getDesignation());
//            return null;
//
//        }).when(aspireRepository).updateManagerId(any(EmployeeGet.class),anyLong());
////        aspireService.updateManagerId(any(EmployeeGet.class),2L);
//    }
//        when(aspireRepository.updateManagerId( any(EmployeeGet.class),anyLong())).thenAnswer(
//                invocation -> {
//                    EmployeeGet e = invocation.getArgument(0);
//                    assertEquals("mgr",e.getDesignation());
//                    return null;
//                }
//        );}
}











