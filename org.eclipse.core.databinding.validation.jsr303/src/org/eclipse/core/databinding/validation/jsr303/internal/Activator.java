package org.eclipse.core.databinding.validation.jsr303.internal;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport.StrategyValidatorFactoryResolver;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.core.databinding.validation.jsr303";

	// The shared instance
	private static Activator plugin;

	public static Activator getDefault() {
		return Activator.plugin;
	}

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
		// Create and open the ValidatorFactory ServiceTracker
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
		if (validatorFactoryServiceTracker != null) {
			validatorFactoryServiceTracker.close();
			validatorFactoryServiceTracker = null;
		}
		Activator.plugin = null;

	}

	public static ValidatorFactory getValidatorFactory() {
		boolean osgiContext = Jsr303BeanValidationSupport.isOSGi();
		if (osgiContext) {
			// OSGi context
			// There are 2 means to get ValidatorFactory in a OSGi context :
			// 1) an another bundle register an instance of ValidatorFactory in
			// the OSGi registry
			// 2) a fragment linked to the javax.validation configure with SPI
			// the provider
			// META-INF/services/javax.validation.spi.ValidationProvider

			// 1) Try to find an instance of ValidatorFactory registered in the
			// OSGi bundle context
			ValidatorFactory validatorFactory = (ValidatorFactory) Activator
					.getDefault().validatorFactoryServiceTracker.getService();
			if (validatorFactory != null) {
				// Validator is discovered from the OSGi context
				Jsr303BeanValidationSupport
						.setStrategy(StrategyValidatorFactoryResolver.Bundle);
				return validatorFactory;
			}
			return getDefaultValidatorFactory(osgiContext);
		} else {
			// NO OSGi context
			// Use classique mean to retrieves the ValidatorFactory (use SPI
			// provider
			// META-INF/services/javax.validation.spi.ValidationProvider) in teh
			// global classpath.
			return getDefaultValidatorFactory(osgiContext);
		}
	}

	private static ValidatorFactory getDefaultValidatorFactory(
			boolean osgiContext) {
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

}
