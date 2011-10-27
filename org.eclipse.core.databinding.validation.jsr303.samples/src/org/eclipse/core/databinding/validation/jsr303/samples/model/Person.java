package org.eclipse.core.databinding.validation.jsr303.samples.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person {

	@Size(min = 1)
	private String name;

	@Size(min = 1)
	@Pattern(regexp = ".+@.+\\.[a-z]+")
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
