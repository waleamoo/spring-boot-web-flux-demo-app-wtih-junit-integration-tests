package com.tech.springbootwebfluxdemoapp.service.impl;

import org.springframework.stereotype.Service;

import com.tech.springbootwebfluxdemoapp.dto.EmployeeDto;
import com.tech.springbootwebfluxdemoapp.entity.Employee;
import com.tech.springbootwebfluxdemoapp.mapper.EmployeeMapper;
import com.tech.springbootwebfluxdemoapp.repository.EmployeeRepository;
import com.tech.springbootwebfluxdemoapp.service.EmployeeService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    
    @Override
    public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Mono<Employee> savedEmployee = employeeRepository.save(employee);
        return savedEmployee.map((employeeEntity) -> EmployeeMapper.mapToEmployeeDto(employeeEntity));
    }

    @Override
    public Mono<EmployeeDto> getEmployee(String employeeId) {
        Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);
        return savedEmployee.map((employeeEntity) -> EmployeeMapper.mapToEmployeeDto(employeeEntity));
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        Flux<Employee> savedEmployees = employeeRepository.findAll();
        return savedEmployees.map((employee) -> EmployeeMapper.mapToEmployeeDto(employee)).switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId) {
        Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);
        Mono<Employee> updatedEmployee = savedEmployee.flatMap((existingEmployee) -> {
            existingEmployee.setFirstName(employeeDto.getFirstName());
            existingEmployee.setLastName(employeeDto.getLastName());
            existingEmployee.setEmail(employeeDto.getEmail());
            return employeeRepository.save(existingEmployee);
        });
        return updatedEmployee.map((employeeEntity) -> EmployeeMapper.mapToEmployeeDto(employeeEntity));

    }

    @Override
    public Mono<Void> deleteEmployee(String employeeId) {
        return employeeRepository.deleteById(employeeId);
    }
    
}
