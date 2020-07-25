package com.project.crud.crud.Repository;

import com.project.crud.crud.model.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

	public Mono<Employee> findByEmail(String email);
}
