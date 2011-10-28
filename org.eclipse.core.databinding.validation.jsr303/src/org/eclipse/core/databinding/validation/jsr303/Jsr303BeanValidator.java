/*******************************************************************************
 * Copyright (c) 2011 Angelo Zerr and Pascal Leclercq.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:      
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *     Pascal Leclercq <pascal.leclercq@gmail.com> - Initial API and implementation 
 *******************************************************************************/
package org.eclipse.core.databinding.validation.jsr303;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.databinding.validation.jsr303.internal.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

/**
 * JFace Databinding Validator {@link IValidator} implementation to validate
 * value with JSR-303 Bean Validation.
 * 
 * Example to validate Person#getEmail() which defines JSR-303 annotation :
 * 
 * <pre>
 * dataBindingContext.bindValue(swtObservableValue, pojoObservableValue,
 * 		new UpdateValueStrategy()
 * 				.setAfterConvertValidator(new Jsr303BeanValidator(Person.class,
 * 						&quot;email&quot;)), null);
 * </pre>
 * 
 * For simplify this code you can use {@link Jsr303UpdateValueStrategyFactory}
 * or {@link Jsr303BeansUpdateValueStrategyFactory}.
 * 
 */
public class Jsr303BeanValidator implements IValidator {

	private static final String VALIDATION_ERRORS = "Validation errors";

	// The class type of the Pojo to validate (ex: Person.class).
	private final Class beanType;
	// The property name to validate (ex: "email" if there is Person#getEmail())
	private final String propertyName;

	private final boolean multiStatus;

	/**
	 * Constructor to create Jsr303BeanValidator.
	 * 
	 * @param beanType
	 *            the class type of the Pojo to validate (ex: Person.class).
	 * @param propertyName
	 *            the property name to validate (ex: "email" if there is
	 *            Person#getEmail()).
	 */
	public Jsr303BeanValidator(Class beanType, String propertyName) {
		this(beanType, propertyName, false);
	}

	/**
	 * Constructor to create Jsr303BeanValidator.
	 * 
	 * @param beanType
	 *            the class type of the Pojo to validate (ex: Person.class).
	 * @param propertyName
	 *            the property name to validate (ex: "email" if there is
	 *            Person#getEmail()).
	 */
	public Jsr303BeanValidator(Class beanType, String propertyName,
			boolean multiStatus) {
		this.beanType = beanType;
		this.propertyName = propertyName;
		this.multiStatus = multiStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.databinding.validation.IValidator#validate(java.lang
	 * .Object)
	 */
	public IStatus validate(Object value) {
		// Validate value for the propertyName of the beanType.
		Set<ConstraintViolation> violations = validateConstraints(value);
		if (violations.size() > 0) {
			if (multiStatus) {
				// Create a multi status with list of errors
				List<IStatus> statusList = new ArrayList<IStatus>();
				for (ConstraintViolation<Object> cv : violations) {
					statusList.add(ValidationStatus.error(cv.getMessage()));

				}
				return new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR,
						statusList.toArray(new IStatus[statusList.size()]),
						VALIDATION_ERRORS, null);

			} else {
				// Create a simple status with the first error.
				for (ConstraintViolation<Object> cv : violations) {
					return ValidationStatus.error(cv.getMessage());
				}

			}
		}
		return Status.OK_STATUS;
	}

	/**
	 * Validate the value for the propertyName of the beanType.
	 * 
	 * @param value
	 * @return
	 */
	protected Set<ConstraintViolation> validateConstraints(Object value) {
		return (Set<ConstraintViolation>) getValidatorFactory().getValidator()
				.validateValue(beanType, propertyName, value);
	}

	/**
	 * Returns the JSR-303 {@link ValidatorFactory}to use to execute the
	 * validate.
	 * 
	 * @return
	 */
	protected ValidatorFactory getValidatorFactory() {
		return Jsr303BeanValidationSupport.getValidatorFactory();
	}

}
