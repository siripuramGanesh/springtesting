package com.ganeshsiripuram.springtesting.springtesting.repositories;

import com.ganeshsiripuram.springtesting.springtesting.TestContainerConfiguration;
import com.ganeshsiripuram.springtesting.springtesting.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@Import(TestContainerConfiguration.class )
//@SpringBootTest this loads the whole application
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp(){
        employee=Employee.builder()
                .id(1L)
                .name("Ganesh")
                .email("ganesh@gmail.com")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        //Arrange
            employeeRepository.save(employee);
        //Act
        List<Employee> employeeList=employeeRepository.findByEmail(employee.getEmail());
        //Assert
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());
    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList(){
        String email="notPresent.123@gmail.com";

        List<Employee> employeeList=employeeRepository.findByEmail(email);

        assertThat(employeeList).isNotNull();
         assertThat(employeeList).isEmpty();
    }
}