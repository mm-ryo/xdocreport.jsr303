package javax.validation.osgi.config.bundle.hibernatevalidator.internal;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.ProviderSpecificBootstrap;

import org.apache.bval.jsr303.ApacheValidationProvider;
import org.apache.bval.jsr303.ApacheValidatorConfiguration;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

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
		super.start(context);
		plugin = this;
		
		// configure and build an instance of ValidatorFactory
	    ProviderSpecificBootstrap<ApacheValidatorConfiguration> validationBootStrap = Validation
	        .byProvider(ApacheValidationProvider.class);
	 
	    // bootstrap to properly resolve in an OSGi environment
	    validationBootStrap.providerResolver(new ApacheValidationProviderResolver());
	 
	    ApacheValidatorConfiguration configure = validationBootStrap.configure();
	 
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
		super.stop(context);
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
