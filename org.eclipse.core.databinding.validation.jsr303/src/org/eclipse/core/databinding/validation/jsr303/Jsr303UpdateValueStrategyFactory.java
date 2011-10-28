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
import org.eclipse.core.runtime.MultiStatus;

/**
 * {@link UpdateValueStrategy} factory to create instance of
 * {@link UpdateValueStrategy} configured with {@link Jsr303BeanValidator} to
 * validate a property of a class beanType.
 * 
 */
public class Jsr303UpdateValueStrategyFactory {

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the given propertyName of the
	 * given class beanType.
	 * 
	 * @param beanType
	 *            the class type of the Pojo to validate (ex: Person.class).
	 * @param propertyName
	 *            the property name to validate (ex: "email" if there is
	 *            Person#getEmail()).
	 * @return
	 */
	public static UpdateValueStrategy create(final Class beanType,
			final String propertyName) {
		return create(beanType, propertyName, false);
	}

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the given propertyName of the
	 * given class beanType.
	 * 
	 * @param beanType
	 *            the class type of the Pojo to validate (ex: Person.class).
	 * @param propertyName
	 *            the property name to validate (ex: "email" if there is
	 *            Person#getEmail()).
	 * @param multiStatus
	 *            true if validator must return a {@link MultiStatus} with the
	 *            whole errors and false otherwise.
	 * @return
	 */
	public static UpdateValueStrategy create(final Class beanType,
			final String propertyName, boolean multiStatus) {
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterConvertValidator(new Jsr303BeanValidator(beanType,
				propertyName, multiStatus));
		return strategy;
	}

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the given propertyName of the
	 * given class beanType.
	 * 
	 * @param beanType
	 *            the class type of the Pojo to validate (ex: Person.class).
	 * @param propertyName
	 *            the property name to validate (ex: "email" if there is
	 *            Person#getEmail()).
	 * @param provideDefaults
	 *            if <code>true</code>, default validators and a default
	 *            converter will be provided based on the observable value's
	 *            type.
	 * @param updatePolicy
	 *            one of {@link #POLICY_NEVER}, {@link #POLICY_ON_REQUEST},
	 *            {@link #POLICY_CONVERT}, or {@link #POLICY_UPDATE}
	 * @return
	 */
	public static UpdateValueStrategy create(final Class beanType,
			final String propertyName, boolean provideDefaults, int updatePolicy) {
		return create(beanType, propertyName, false, provideDefaults,
				updatePolicy);
	}

	/**
	 * Create {@link UpdateValueStrategy} configured with
	 * {@link Jsr303BeanValidator} to validate the given propertyName of the
	 * given class beanType.
	 * 
	 * @param beanType
	 *            the class type of the Pojo to validate (ex: Person.class).
	 * @param propertyName
	 *            the property name to validate (ex: "email" if there is
	 *            Person#getEmail()).
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
	public static UpdateValueStrategy create(final Class beanType,
			final String propertyName, boolean multiStatus,
			boolean provideDefaults, int updatePolicy) {
		UpdateValueStrategy strategy = new UpdateValueStrategy(provideDefaults,
				updatePolicy);
		strategy.setAfterConvertValidator(new Jsr303BeanValidator(beanType,
				propertyName, multiStatus));
		return strategy;
	}
}
