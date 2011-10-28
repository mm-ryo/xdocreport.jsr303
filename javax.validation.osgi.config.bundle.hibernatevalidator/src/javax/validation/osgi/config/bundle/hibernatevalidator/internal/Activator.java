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
package javax.validation.osgi.config.bundle.hibernatevalidator.internal;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.ProviderSpecificBootstrap;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle. It register an instance
 * of {@link ValidatorFactory} configured for Hibernate Validator in the OSGi
 * services registry.
 */
public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "javax.validation.osgi.config.bundle.hibernatevalidator"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceRegistration<?> serviceRegistration;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		plugin = this;
		
		// configure and build an instance of ValidatorFactory
	    ProviderSpecificBootstrap<HibernateValidatorConfiguration> validationBootStrap = Validation
	        .byProvider(HibernateValidator.class);
	 
	    // bootstrap to properly resolve in an OSGi environment
	    validationBootStrap.providerResolver(new HibernateValidationProviderResolver());
	 
	    HibernateValidatorConfiguration configure = validationBootStrap.configure();
	 
	    // now that we've done configuring the ValidatorFactory, let's build it
	    ValidatorFactory validatorFactory = configure.buildValidatorFactory();
		
		serviceRegistration = context.registerService(
				ValidatorFactory.class.getName(),
				validatorFactory, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		if (serviceRegistration != null) {
			serviceRegistration.unregister();
			serviceRegistration = null;
		}
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
