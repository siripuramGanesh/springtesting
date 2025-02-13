package com.ganeshsiripuram.springtesting.springtesting.repositories;

import com.ganeshsiripuram.springtesting.springtesting.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmail(String email);
}
