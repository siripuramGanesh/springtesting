package com.ganeshsiripuram.springtesting.springtesting.controllers;

import com.ganeshsiripuram.springtesting.springtesting.TestContainerConfiguration;
import com.ganeshsiripuram.springtesting.springtesting.dto.EmployeeDto;
import com.ganeshsiripuram.springtesting.springtesting.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout="10000")
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {


    @Autowired
     WebTestClient webTestClient;

    Employee testEmployee=Employee.builder()
            .id(1L)
                .name("ganesh")
                .email("ganesh@gmail.com")
                .salary(200L)
                .build();

   EmployeeDto testEmployeeDto=EmployeeDto.builder()
            .id(1L)
                .name("ganesh")
                .email("ganesh@gmail.com")
                .salary(200L)
                .build();


}
