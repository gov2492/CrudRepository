package com.project.crud.crud.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Data
@Document("employee")
public class Employee extends BaseModel {

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	@NotNull
	@Valid
	private Address address;

	private String password;
}
