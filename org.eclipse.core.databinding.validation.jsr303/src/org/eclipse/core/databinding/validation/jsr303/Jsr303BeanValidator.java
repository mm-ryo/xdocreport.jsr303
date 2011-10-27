package org.eclipse.core.databinding.validation.jsr303;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class Jsr303BeanValidator implements IValidator {

	private final Class beanType;
	private final String propertyName;

	public Jsr303BeanValidator(Class beanType, String propertyName) {
		this.beanType = beanType;
		this.propertyName = propertyName;
	}

	public IStatus validate(Object value) {
		Set<ConstraintViolation> violations = validateConstraints(value);
		if (violations.size() > 0) {

			// List<IStatus> statusList = new ArrayList<IStatus>();
			for (ConstraintViolation<Object> cv : violations) {
				return ValidationStatus.error(cv.getMessage());
				// statusList.add(ValidationStatus.error(cv.getMessage()));

			}
			// return new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR,
			// statusList.toArray(new IStatus[statusList.size()]),
			// "Validation errors", null);
		}
		return Status.OK_STATUS;
	}

	protected Set<ConstraintViolation> validateConstraints(Object value) {
		return (Set<ConstraintViolation>) getValidatorFactory().getValidator()
				.validateValue(beanType, propertyName, value);
	}

	protected ValidatorFactory getValidatorFactory() {
		return Jsr303BeanValidationSupport.getValidatorFactory();
	}

}
