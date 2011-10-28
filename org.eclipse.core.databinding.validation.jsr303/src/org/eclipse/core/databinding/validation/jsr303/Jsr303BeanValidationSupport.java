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

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

/**
 * 
 * Helper class which gives state of the JSR-303 Bean Validator implementation
 * used.
 */
public class Jsr303BeanValidationSupport {

	public enum StrategyValidatorFactoryResolver {
		NotInitialized, Unavailable, NoOSgi, Bundle, Fragment
	}

	private static StrategyValidatorFactoryResolver strategy = StrategyValidatorFactoryResolver.NotInitialized;

	private static IValidatorFactoryProvider validatorFactoryProvider = null;

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

	private static void initializeIfNeeded() {
		if (strategy == StrategyValidatorFactoryResolver.NotInitialized) {
			try {
				getValidatorFactory();
			} catch (Throwable e) {

			}
		}
	}

	/**
	 * Returns the JSR-303 Bean Validator factory used and throw an exception if
	 * none JSR-303 Bean Validator Implementation was configured.
	 * 
	 * @return
	 */
	public static ValidatorFactory getValidatorFactory() {
		if (validatorFactoryProvider != null) {
			return validatorFactoryProvider.getValidatorFactory();
		}
		return getDefaultValidatorFactory();
	}

	public static ValidatorFactory getDefaultValidatorFactory() {
		boolean osgiContext = isOSGi();
		try {
			ValidatorFactory factory = Validation
					.buildDefaultValidatorFactory();
			Jsr303BeanValidationSupport
					.setStrategy(osgiContext ? StrategyValidatorFactoryResolver.Fragment
							: StrategyValidatorFactoryResolver.NoOSgi);
			return factory;
		} catch (ValidationException e) {
			Jsr303BeanValidationSupport
					.setStrategy(StrategyValidatorFactoryResolver.Unavailable);
			throw e;
		}
	}

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

	public static void setOsgi(boolean osgi) {
		Jsr303BeanValidationSupport.osgi = osgi;
	}

	public static StrategyValidatorFactoryResolver getStrategy() {
		return strategy;
	}

	public static void setStrategy(StrategyValidatorFactoryResolver strategy) {
		Jsr303BeanValidationSupport.strategy = strategy;
	}
}
