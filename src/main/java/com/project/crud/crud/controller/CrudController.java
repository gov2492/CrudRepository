package com.project.crud.crud.controller;

import com.project.crud.crud.model.Employee;
import com.project.crud.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class CrudController {


	@Autowired
	EmployeeService employeeService;

	@GetMapping("/employees")
	public Flux<?> get() {
		return employeeService.getAll();
	}

	@PostMapping("/employee")
	public Mono<?> add(@Valid @RequestBody Employee employee) {
		return employeeService.save(employee);
	}
}
