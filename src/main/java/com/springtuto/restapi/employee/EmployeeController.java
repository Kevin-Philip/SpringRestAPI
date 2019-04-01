package com.springtuto.restapi.employee;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.springtuto.restapi.exceptions.EmployeeNotCorrectException;
import com.springtuto.restapi.exceptions.EmployeeNotFoundException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeResourceAssembler assembler;

    EmployeeController(EmployeeRepository repository,
                       EmployeeResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Aggregate root

    @GetMapping("/employees")
    Resources<Resource<Employee>> all() {

        List<Resource<Employee>> employees = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    // Single item

    @GetMapping("/employees/{id}")
    Resource<Employee> one(@PathVariable Long id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toResource(employee);
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
        if (verifyEmployee(newEmployee)) {
            Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));

            return ResponseEntity
                    .created(new URI(resource.getId().expand().getHref()))
                    .body(resource);
        } else {
            throw new EmployeeNotCorrectException(listeAttribute(newEmployee));
        }
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {

        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    if (verifyEmployee(newEmployee)) {
                        newEmployee.setId(id);
                        return repository.save(newEmployee);
                    } else {
                        throw new EmployeeNotCorrectException(listeAttribute(newEmployee));
                    }
                });

        Resource<Employee> resource = assembler.toResource(updatedEmployee);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private List<String> listeAttribute(Employee newEmployee) {
        List<String> listNullAttribute = new ArrayList<>();
        if (newEmployee.getName() == null) {
            listNullAttribute.add("Name");
        }
        if (newEmployee.getRole() == null) {
            listNullAttribute.add("Role");
        }
        return listNullAttribute;
    }

    private boolean verifyEmployee(Employee newEmployee) {
        return (newEmployee.getName() != null && newEmployee.getRole() != null);
    }
}