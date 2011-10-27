package org.eclipse.core.databinding.validation.jsr303;

import org.eclipse.core.databinding.UpdateValueStrategy;

public class Jsr303UpdateValueStrategyFactory {

	public static UpdateValueStrategy create(final Class beanType,
			final String propertyName) {
		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setAfterConvertValidator(new Jsr303BeanValidator(beanType,
				propertyName));
		return strategy;
	}

	public static UpdateValueStrategy create(final Class beanType,
			final String propertyName, boolean provideDefaults, int updatePolicy) {
		UpdateValueStrategy strategy = new UpdateValueStrategy(provideDefaults,
				updatePolicy);
		strategy.setAfterConvertValidator(new Jsr303BeanValidator(beanType,
				propertyName));
		return strategy;
	}
}
