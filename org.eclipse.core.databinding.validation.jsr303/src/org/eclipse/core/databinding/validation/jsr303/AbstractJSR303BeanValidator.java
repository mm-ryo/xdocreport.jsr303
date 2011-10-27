/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.eclipse.core.databinding.validation.jsr303;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.databinding.validation.jsr303.internal.Activator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public abstract class AbstractJSR303BeanValidator implements IValidator {

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

	protected ValidatorFactory getValidatorFactory() {
		return Activator.getValidatorFactory();
	}

	protected abstract Set<ConstraintViolation> validateConstraints(Object value);

}
