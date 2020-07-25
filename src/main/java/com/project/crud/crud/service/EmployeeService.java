package com.project.crud.crud.service;

import com.project.crud.crud.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

	public Mono<Object> save(Employee e);

	public Flux<?> getAll();
}
