package org.eclipse.core.databinding.validation.jsr303.samples.rcp.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person {

	private long id;

	@Size(min = 1)
	private String name;

	@Size(min = 1)
	@Pattern(regexp = ".+@.+\\.[a-z]+")
	private String email;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
