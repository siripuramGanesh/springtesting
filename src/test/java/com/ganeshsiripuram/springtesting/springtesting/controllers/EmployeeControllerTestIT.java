package com.ganeshsiripuram.springtesting.springtesting.controllers;

import com.ganeshsiripuram.springtesting.springtesting.TestContainerConfiguration;
import com.ganeshsiripuram.springtesting.springtesting.dto.EmployeeDto;
import com.ganeshsiripuram.springtesting.springtesting.entities.Employee;
import com.ganeshsiripuram.springtesting.springtesting.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeControllerTestIT extends AbstractIntegrationTest{


    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp(){

        employeeRepository.deleteAll();
    }

    @Test
    void testEmployeeById_Success(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employees/{id}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedEmployee.getId())
                .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());
            //.isEqualTo(testEmployeeDto);
//                .value(employeeDto -> {
//                   assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//                   assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());


    }

    @Test
    void testEmployeeById_Failure(){
        webTestClient.get()
                .uri("/employees/{id}")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeAlreadyExists_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testCreateNewEmployee_whenEmployeeDoesNotExists_thenCreateEmployee(){
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
                .jsonPath("$.name").isEqualTo(testEmployeeDto.getName())
                .jsonPath("$.salary").isEqualTo(testEmployeeDto.getSalary());
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        webTestClient.put()
                .uri("/employees/777")
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateTheEmail_thenThrowException(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Venkata");
        testEmployeeDto.setEmail("venkata@gmail.com");
        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testUpdateEmployee_WhenEmployeeIsValid_thenUpdateEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        testEmployeeDto.setName("Venkata");
        testEmployeeDto.setSalary(337L);
        webTestClient.put()
                .uri("/employees/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testEmployeeDto);
    }

    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException(){
        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee(){
        Employee savedEmployee=employeeRepository.save(testEmployee);
        webTestClient.delete()
                .uri("/employees/{}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(void.class);

        webTestClient.delete()
                .uri("/employees/{}",savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

}