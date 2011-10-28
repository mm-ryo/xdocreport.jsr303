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

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.IBeanObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.MultiStatus;

/**
 * * {@link UpdateValueStrategy} factory to create instance of
 * {@link UpdateValueStrategy} configured with {@link Jsr303BeanValidator} to
 * validate a property of a class beanType coming from the
 * {@link IBeanObservable}.
 * 
 */
public class Jsr303BeansUpdateValueStrategyFactory extends
		Jsr303UpdateValueStrategyFactory {

	private static final String MESSAGE = "value must be an instance of "
			+ IBeanObservable.class.getName();

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the propertyName of the class
	 * beanType declared in the given {@link IBeanObservable}.
	 * 
	 * @param value
	 *            the {@link IBeanObservable}.
	 * @return
	 */
	public static UpdateValueStrategy create(IObservableValue value) {
		return create(value, false);
	}

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the propertyName of the class
	 * beanType declared in the given {@link IBeanObservable}.
	 * 
	 * @param value
	 *            the {@link IBeanObservable}.
	 * @param multiStatus
	 *            true if validator must return a {@link MultiStatus} with the
	 *            whole errors and false otherwise.
	 * @return
	 */
	public static UpdateValueStrategy create(IObservableValue value,
			boolean multiStatus) {
		Assert.isTrue(value instanceof IBeanObservable, MESSAGE);
		IBeanObservable beanObservable = (IBeanObservable) value;
		Class beanType = beanObservable.getObserved().getClass();
		String propertyName = beanObservable.getPropertyDescriptor().getName();
		return Jsr303UpdateValueStrategyFactory.create(beanType, propertyName,
				multiStatus);
	}

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the propertyName of the class
	 * beanType declared in the given {@link IBeanObservable}.
	 * 
	 * @param value
	 *            the {@link IBeanObservable}.
	 * @param provideDefaults
	 *            if <code>true</code>, default validators and a default
	 *            converter will be provided based on the observable value's
	 *            type.
	 * @param updatePolicy
	 *            one of {@link #POLICY_NEVER}, {@link #POLICY_ON_REQUEST},
	 *            {@link #POLICY_CONVERT}, or {@link #POLICY_UPDATE}
	 * @return
	 */
	public static UpdateValueStrategy create(IObservableValue value,
			boolean provideDefaults, int updatePolicy) {
		return create(value, false, provideDefaults, updatePolicy);
	}

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the propertyName of the class
	 * beanType declared in the given {@link IBeanObservable}.
	 * 
	 * @param value
	 *            the {@link IBeanObservable}.
	 * @param multiStatus
	 *            true if validator must return a {@link MultiStatus} with the
	 *            whole errors and false otherwise.
	 * @param provideDefaults
	 *            if <code>true</code>, default validators and a default
	 *            converter will be provided based on the observable value's
	 *            type.
	 * @param updatePolicy
	 *            one of {@link #POLICY_NEVER}, {@link #POLICY_ON_REQUEST},
	 *            {@link #POLICY_CONVERT}, or {@link #POLICY_UPDATE}
	 * @return
	 */
	public static UpdateValueStrategy create(IObservableValue value,
			boolean multiStatus, boolean provideDefaults, int updatePolicy) {
		Assert.isTrue(value instanceof IBeanObservable, MESSAGE);
		IBeanObservable beanObservable = (IBeanObservable) value;
		Class beanType = beanObservable.getObserved().getClass();
		String propertyName = beanObservable.getPropertyDescriptor().getName();
		return Jsr303UpdateValueStrategyFactory.create(beanType, propertyName,
				multiStatus, provideDefaults, updatePolicy);
	}
}
