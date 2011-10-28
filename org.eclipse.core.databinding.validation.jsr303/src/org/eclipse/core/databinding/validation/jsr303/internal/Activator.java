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
package org.eclipse.core.databinding.validation.jsr303.internal;

import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

import org.eclipse.core.databinding.validation.jsr303.IValidatorFactoryProvider;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport.StrategyValidatorFactoryResolver;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 * The goal of this OSGi {@link BundleActivator} is to configure
 * {@link Jsr303BeanValidator} with well JS3-303 {@link ValidatorFactory}. In
 * OSGi context, the {@link ValidatorFactory} can be retrieved with 2 means:
 * 
 * <ul>
 * <li>another bundles register an instance of ValidatorFactory in the OSGi
 * registry. This bundle retrieves the instance of ValidatorFactory in the OSGi
 * registry which was registered. See for instance bundles
 * javax.validation.osgi.config.bundle.apachebval and
 * javax.validation.osgi.config.bundle.hibernatevalidator.</li>
 * <li>a fragment linked to the javax.validation configure the
 * {@link ValidatorFactory} with SPI with the provider
 * META-INF/services/javax.validation.spi.ValidationProvider. See for instance
 * fragment javax.validation.osgi.config.fragment.apachebval and
 * javax.validation.osgi.config.fragment.hibernatevalidator.</li>
 * </ul>
 */
public class Activator implements BundleActivator, IValidatorFactoryProvider {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.core.databinding.validation.jsr303";

	// The shared instance
	private static Activator plugin;

	public static Activator getDefault() {
		return Activator.plugin;
	}

	// Service tracker used to track ValidatorFactory from the OSGi registry
	private ServiceTracker validatorFactoryServiceTracker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.plugin = this;
		// 1) Configure Jsr303BeanValidationSupport to work in an OSGi context
		Jsr303BeanValidationSupport.setOsgi(true);
		Jsr303BeanValidationSupport.setValidatorFactoryProvider(this);
		// 2) Create and open the ValidatorFactory ServiceTracker
		validatorFactoryServiceTracker = new ServiceTracker(bundleContext,
				ValidatorFactory.class.getName(), null);
		validatorFactoryServiceTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		// 1) UnConfigure Jsr303BeanValidationSupport
		Jsr303BeanValidationSupport.setOsgi(false);
		Jsr303BeanValidationSupport.setValidatorFactoryProvider(null);
		// 2) Close the ValidatorFactory ServiceTracker
		if (validatorFactoryServiceTracker != null) {
			validatorFactoryServiceTracker.close();
			validatorFactoryServiceTracker = null;
		}
		Activator.plugin = null;

	}

	/**
	 * Returns an instance of {@link ValidatorFactory} to use to execute the
	 * validation and throws {@link ValidationException} if none JSR-303
	 * implementation cannot be found. In OSGi context, the
	 * {@link ValidatorFactory} can be retrieved with 2 means:
	 * 
	 * <ul>
	 * <li>another bundles register an instance of ValidatorFactory in the OSGi
	 * registry. This bundle retrieves the instance of ValidatorFactory in the
	 * OSGi registry which was registered. See for instance bundles
	 * javax.validation.osgi.config.bundle.apachebval and
	 * javax.validation.osgi.config.bundle.hibernatevalidator.</li>
	 * <li>a fragment linked to the javax.validation configure the
	 * {@link ValidatorFactory} with SPI with the provider
	 * META-INF/services/javax.validation.spi.ValidationProvider. See for
	 * instance fragment javax.validation.osgi.config.fragment.apachebval and
	 * javax.validation.osgi.config.fragment.hibernatevalidator.</li>
	 * </ul>
	 */
	public ValidatorFactory getValidatorFactory() {
		// 1) BUNDLE strategy : Try to find an instance of ValidatorFactory
		// registered in the
		// OSGi bundle context
		ValidatorFactory validatorFactory = (ValidatorFactory) Activator
				.getDefault().validatorFactoryServiceTracker.getService();
		if (validatorFactory != null) {
			// Validator is discovered from the OSGi context
			Jsr303BeanValidationSupport
					.setStrategy(StrategyValidatorFactoryResolver.Bundle);
			return validatorFactory;
		}
		// 2) FRAGMENT strategy : Try to find an instance of ValidatorFactory
		// with SPI with the provider
		// META-INF/services/javax.validation.spi.ValidationProvider from a
		// fragment linked to javax.validation.
		return Jsr303BeanValidationSupport.getDefaultValidatorFactory();
	}

}
