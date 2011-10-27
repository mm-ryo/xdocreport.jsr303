package javax.validation.osgi.config.bundle.hibernatevalidator.internal;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationProviderResolver;
import javax.validation.spi.ValidationProvider;

import org.apache.bval.jsr303.ApacheValidationProvider;

/**
 * OSGi classpath aware {@link javax.validation.ValidationProviderResolver
 * ValidationProviderResolver}.
 * 
 */
public class ApacheValidationProviderResolver implements
		ValidationProviderResolver {

	public List<ValidationProvider<?>> getValidationProviders() {
		List<ValidationProvider<?>> providers = new ArrayList<ValidationProvider<?>>(
				1);
		providers.add(new ApacheValidationProvider());
		return providers;
	}
}