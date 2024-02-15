package com.tech.springbootwebfluxdemoapp.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.tech.springbootwebfluxdemoapp.entity.Employee;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, String>{
    
}
