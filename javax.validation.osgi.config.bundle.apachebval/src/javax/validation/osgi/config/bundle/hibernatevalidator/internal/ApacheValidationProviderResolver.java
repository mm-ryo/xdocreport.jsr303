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