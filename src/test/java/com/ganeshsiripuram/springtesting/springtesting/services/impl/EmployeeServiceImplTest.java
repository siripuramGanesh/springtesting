package com.ganeshsiripuram.springtesting.springtesting.services.impl;

import com.ganeshsiripuram.springtesting.springtesting.TestContainerConfiguration;
import com.ganeshsiripuram.springtesting.springtesting.dto.EmployeeDto;
import com.ganeshsiripuram.springtesting.springtesting.entities.Employee;
import com.ganeshsiripuram.springtesting.springtesting.exceptions.ResourceNotFoundException;
import com.ganeshsiripuram.springtesting.springtesting.repositories.EmployeeRepository;
import com.ganeshsiripuram.springtesting.springtesting.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;
    @BeforeEach
    void setUp(){
        mockEmployee=Employee.builder()
                .id(1L)
                .name("ganesh")
                .email("ganesh@gmail.com")
                .salary(200L)
                .build();
        mockEmployeeDto=modelMapper.map(mockEmployee, EmployeeDto.class);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
        //assign
            Long id=1L;
            when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee));

        //act
        EmployeeDto employeeDto=employeeService.getEmployeeById(id);
        //assert
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployee.getEmail());
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIsNotPresent_thenThrowException(){
        //arrange
            when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        //act and act
            assertThatThrownBy(()->employeeService.getEmployeeById(1L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Employee not found with id: 1");
            verify(employeeRepository).findById(1L);
    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee(){
        //assign
            when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
            when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
          //act
            EmployeeDto employeeDto=employeeService.createNewEmployee(mockEmployeeDto);
        //assert


            assertThat(employeeDto).isNotNull();
            assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee capturedEmployee=employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());
    }

    @Test
    void testCreateNewEmployee_WhenAttemptingToCreateEmployeeWithExistingEmail_ThenThrowException(){
        //arange
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));
        //act
        //assert
        assertThatThrownBy(()->employeeService.createNewEmployee(mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployee.getEmail());

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenEmployeeDoesNotExists_ThenThrowException(){
        //Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        //act
        //assert
        assertThatThrownBy(()->employeeService.updateEmployee(1L,mockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenAttemptingToUpdateEmail_ThenTHrowException(){
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setEmail("random@gmail.com");
        assertThatThrownBy(()->employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());
    }

    @Test
    void testUpdateEmployee_WhenValidEmployee_ThenUpdateEmployee(){
        when(employeeRepository.findById(mockEmployeeDto.getId())).thenReturn(Optional.of(mockEmployee));
        mockEmployeeDto.setName("VENKATA");
        mockEmployeeDto.setSalary(337L);

        Employee newEmployee=modelMapper.map(mockEmployeeDto, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        EmployeeDto newEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);
        assertThat(newEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());

    }

    @Test
    void testDeleteEmployee_EmployeeNotFound_ThenThrowException(){
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(()->employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

        verify(employeeRepository,never()).deleteById( anyLong());
    }

    @Test
    void testDeleteEmployee_EmployeeFound_ThenDeleteIt(){
        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertThatCode(()->employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);
    }



}