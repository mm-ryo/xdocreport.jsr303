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

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

/**
 * 
 * Helper class which retrieves the instance of {@link ValidatorFactory} to use
 * to validate and gives state of the JSR-303 Bean Validator implementation
 * used.
 */
public class Jsr303BeanValidationSupport {

	// Enum for strategy to resolve the validator factory
	public enum StrategyValidatorFactoryResolver {
		NotInitialized, Unavailable, NoOSgi, Bundle, Fragment
	}

	// Sstrategy to resolve the validator factory
	private static StrategyValidatorFactoryResolver strategy = StrategyValidatorFactoryResolver.NotInitialized;

	// ValidatorFactory provider
	private static IValidatorFactoryProvider validatorFactoryProvider = null;

	// Is OSGi context?
	private static boolean osgi;

	/**
	 * Returns true if there is an implementation of JSR-303 Bean Validator and
	 * false otherwise.
	 * 
	 * @return
	 */
	public static boolean isAvailable() {
		initializeIfNeeded();
		return strategy != StrategyValidatorFactoryResolver.Unavailable;
	}

	/**
	 * Returns the JSR-303 Bean {@link ValidatorFactory} used and throw an
	 * exception if none JSR-303 Bean Validator Implementation was configured.
	 * 
	 * @return
	 */
	public static ValidatorFactory getValidatorFactory() {
		if (validatorFactoryProvider != null) {
			// ValidatorFactory provider setted, use it
			return validatorFactoryProvider.getValidatorFactory();
		}
		// Call default validator factory
		return getDefaultValidatorFactory();
	}

	/**
	 * Returns the ValidatorFactory by using
	 * Validation.buildDefaultValidatorFactory().
	 * 
	 * @return
	 */
	public static ValidatorFactory getDefaultValidatorFactory() {
		boolean osgiContext = isOSGi();
		try {
			// Build default ValidatorFactory
			ValidatorFactory factory = Validation
					.buildDefaultValidatorFactory();
			// The factory was build, set the flag strategy (fragment if OSGi
			// and NoOSgi otherwise).
			Jsr303BeanValidationSupport
					.setStrategy(osgiContext ? StrategyValidatorFactoryResolver.Fragment
							: StrategyValidatorFactoryResolver.NoOSgi);
			return factory;
		} catch (ValidationException e) {
			// Error, set strategy as Unavailable
			Jsr303BeanValidationSupport
					.setStrategy(StrategyValidatorFactoryResolver.Unavailable);
			throw e;
		}
	}

	/**
	 * Set the provider to retrieves the {@link ValidatorFactory}.
	 * 
	 * @param validatorFactoryProvider
	 */
	public static void setValidatorFactoryProvider(
			IValidatorFactoryProvider validatorFactoryProvider) {
		Jsr303BeanValidationSupport.validatorFactoryProvider = validatorFactoryProvider;
	}

	/**
	 * Returns the class name of the JSR-303 Bean Validator Implementation used
	 * and null otherwise.
	 * 
	 * @return
	 */
	public static String getValidatorFactoryClassName() {
		if (!isAvailable()) {
			return null;
		}
		return getValidatorFactory().getClass().getName();
	}

	/**
	 * Returns true if JSR-303 bean validation support is used in an OSGi
	 * context and false otherwise.
	 * 
	 * @return
	 */
	public static boolean isOSGi() {
		return osgi;
	}

	/**
	 * Set true if JSR-303 bean validation support is used in an OSGi context
	 * and false otherwise. This method must never called.
	 * 
	 * @param osgi
	 */
	public static void setOsgi(boolean osgi) {
		Jsr303BeanValidationSupport.osgi = osgi;
	}

	/**
	 * Returns the strategy to resolve the validator factory :
	 * 
	 * <ul>
	 * <li>NotInitialized: not initialized.</li>
	 * <li>Unavailable: JSR-303 bean validation ValidatorFactory cannot be
	 * retrived (none implementation of JSR-303 bean validation available).</li>
	 * <li>NoOSgi: JSR-303 bean validation ValidatorFactory is well configured
	 * in NO OSGi context.</li>
	 * <li>Bundle: JSR-303 bean validation ValidatorFactory is well configured
	 * in OSGi context with BUNDLE strategy (See Activator for more
	 * explanation).</li>
	 * <li>Fragment: JSR-303 bean validation ValidatorFactory is well configured
	 * in OSGi context with FRAGMENT strategy (See Activator for more
	 * explanation).</li>
	 * </ul>
	 * 
	 * @return
	 */
	public static StrategyValidatorFactoryResolver getStrategy() {
		return strategy;
	}

	/**
	 * Set the strategy to resolve the validator factory. This method must never
	 * called.
	 * 
	 * @param strategy
	 */
	public static void setStrategy(StrategyValidatorFactoryResolver strategy) {
		Jsr303BeanValidationSupport.strategy = strategy;
	}

	/**
	 * Call getValidatorFactory to initialize strategy field if it's not
	 * initialized.
	 */
	private static void initializeIfNeeded() {
		if (strategy == StrategyValidatorFactoryResolver.NotInitialized) {
			try {
				getValidatorFactory();
			} catch (Throwable e) {

			}
		}
	}
}
