package org.eclipse.core.databinding.validation.jsr303;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.IBeanObservable;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;

public class Jsr303BeansUpdateValueStrategyFactory extends
		Jsr303UpdateValueStrategyFactory {

	private static final String MESSAGE = "value must be an instance of "
			+ IBeanObservable.class.getName();

	public static UpdateValueStrategy create(IObservableValue value) {
		Assert.isTrue(value instanceof IBeanObservable, MESSAGE);
		IBeanObservable beanObservable = (IBeanObservable) value;
		Class beanType = beanObservable.getObserved().getClass();
		String propertyName = beanObservable.getPropertyDescriptor().getName();
		return Jsr303UpdateValueStrategyFactory.create(beanType, propertyName);
	}

	public static UpdateValueStrategy create(IObservableValue value,
			boolean provideDefaults, int updatePolicy) {
		Assert.isTrue(value instanceof IBeanObservable, MESSAGE);
		IBeanObservable beanObservable = (IBeanObservable) value;
		Class beanType = beanObservable.getObserved().getClass();
		String propertyName = beanObservable.getPropertyDescriptor().getName();
		return Jsr303UpdateValueStrategyFactory.create(beanType, propertyName,
				provideDefaults, updatePolicy);
	}
}
