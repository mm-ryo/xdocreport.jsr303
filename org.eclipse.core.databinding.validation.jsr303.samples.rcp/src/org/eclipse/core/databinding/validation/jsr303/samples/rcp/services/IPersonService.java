package org.eclipse.core.databinding.validation.jsr303.samples.rcp.services;

import java.util.Collection;

import org.eclipse.core.databinding.validation.jsr303.samples.rcp.model.Person;

public interface IPersonService {

	Person getPerson(long id);

	Person savePerson(Person person);
	
	Collection<Person> getPersons();
}
