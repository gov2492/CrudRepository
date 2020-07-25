package com.project.crud.crud.service.impl;

import com.project.crud.crud.Repository.EmployeeRepository;
import com.project.crud.crud.model.Employee;
import com.project.crud.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository repository;

	@Override
	public Mono<Object> save(Employee e) {
		return repository.findByEmail(e.getEmail()).flatMap(x -> {
			throw new IllegalArgumentException("Email is already exists");
		}).switchIfEmpty(repository.insert(e));
	}

	@Override
	public Flux<?> getAll() {
		return repository.findAll().switchIfEmpty(Mono.error(new NullPointerException("No employee found")));
	}
}
