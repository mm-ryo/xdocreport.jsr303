package org.eclipse.core.databinding.validation.jsr303.internal;

import javax.validation.ValidatorFactory;

import org.eclipse.core.databinding.validation.jsr303.IValidatorFactoryProvider;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport.StrategyValidatorFactoryResolver;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, IValidatorFactoryProvider {

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
		Jsr303BeanValidationSupport.setOsgi(true);
		Jsr303BeanValidationSupport.setValidatorFactoryProvider(this);
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
		Jsr303BeanValidationSupport.setOsgi(false);
		Jsr303BeanValidationSupport.setValidatorFactoryProvider(null);
		if (validatorFactoryServiceTracker != null) {
			validatorFactoryServiceTracker.close();
			validatorFactoryServiceTracker = null;
		}
		Activator.plugin = null;

	}

	public ValidatorFactory getValidatorFactory() {

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
		return Jsr303BeanValidationSupport.getDefaultValidatorFactory();

	}

}
