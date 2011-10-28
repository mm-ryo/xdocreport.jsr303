package org.eclipse.core.databinding.validation.jsr303;

import javax.validation.ValidatorFactory;

public interface IValidatorFactoryProvider {

	ValidatorFactory getValidatorFactory();
	
}
