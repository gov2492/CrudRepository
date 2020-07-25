package com.project.crud.crud.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class Address {

	@NotBlank
	private String city;

	@NotBlank
	private String pincode;
}
