package com.tech.springbootwebfluxdemoapp;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.tech.springbootwebfluxdemoapp.dto.EmployeeDto;
import com.tech.springbootwebfluxdemoapp.repository.EmployeeRepository;
import com.tech.springbootwebfluxdemoapp.service.EmployeeService;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTests {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void before(){
        System.out.println("Before Each Test");
        employeeRepository.deleteAll().subscribe();
    }

    @Test
    public void testSaveEmployee(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Wale");
        employeeDto.setLastName("Amoo");
        employeeDto.setEmail("amoowale@gmail.com");

        webTestClient.post().uri("/api/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(employeeDto), EmployeeDto.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .consumeWith(System.out::println)
            .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
            .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
            .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void testGetSingleEmployee(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ruka");
        employeeDto.setLastName("Amoo");
        employeeDto.setEmail("ruka11@gmail.com");
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployee.getId()))
            .exchange()
            .expectStatus().isOk()
            .consumeWith(System.out::println)
            .jsonPath("$.id").isEqualTo(employeeDto.getId())
            .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
            .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
            .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

    @Test
    public void testGetAllEmployees(){
        
        webTestClient.get().uri("/api/employees")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(EmployeeDto.class)
            .consumeWith(System.out::println);
    }


    @Test
    public void testUpdatedEmployee(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ruka");
        employeeDto.setLastName("Amoo");
        employeeDto.setEmail("ruka11@gmail.com");
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();

        // update employee
        EmployeeDto updatedEmployee = new EmployeeDto();
        updatedEmployee.setFirstName("Rukayat");
        updatedEmployee.setLastName("Amoo");
        updatedEmployee.setEmail("rukky11@gmail.com");

        
        webTestClient.put().uri("/api/employees{id}", Collections.singletonMap("id", savedEmployee.getId()))
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(EmployeeDto.class)
            .consumeWith(System.out::println)
            .jsonPath("$.firstName").isEqualTo(updatedEmployee.getFirstName())
            .jsonPath("$.lastName").isEqualTo(updatedEmployee.getLastName())
            .jsonPath("$.email").isEqualTo(updatedEmployee.getEmail());
    }

    @Test
    public void testDeletedEmployee(){

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("Ruka");
        employeeDto.setLastName("Amoo");
        employeeDto.setEmail("ruka11@gmail.com");
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto).block();
        
        webTestClient.delete().uri("/api/employees{id}", Collections.singletonMap("id", savedEmployee.getId()))
            .exchange()
            .expectStatus().isNoContent()
            .expectBody()
            .consumeWith(System.out::println);
    }

}
