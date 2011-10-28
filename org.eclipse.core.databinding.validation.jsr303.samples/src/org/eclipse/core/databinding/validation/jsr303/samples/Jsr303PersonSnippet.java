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
package org.eclipse.core.databinding.validation.jsr303.samples;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeansUpdateValueStrategyFactory;
import org.eclipse.core.databinding.validation.jsr303.samples.model.Person;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Jsr303PersonSnippet {

	public static void main(String[] args) {
		// Display state of JSR-303 Bean Support
		System.out.println("JSR-303 Bean Support available?: "
				+ Jsr303BeanValidationSupport.isAvailable());
		System.out.println("OSGi context?: "
				+ Jsr303BeanValidationSupport.isOSGi());
		System.out
				.println("JSR-303 Bean Support ValidatorFactory Implementation: "
						+ Jsr303BeanValidationSupport
								.getValidatorFactoryClassName());
		System.out.println("JSR-303 Bean Support strategy resolver?: "
				+ Jsr303BeanValidationSupport.getStrategy());

		// Create UI+Binding
		final Display display = new Display();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				final Shell shell = new Shell(display);

				shell.setLayout(new FillLayout());

				Composite parent = new Composite(shell, SWT.NONE);
				parent.setLayout(new GridLayout(2, false));
				parent.setLayoutData(new GridData(GridData.FILL_BOTH));
				Person model = new Person();

				// UI Name
				Label nameLabel = new Label(parent, SWT.NONE);
				nameLabel.setText("Name:");
				Text nameText = new Text(parent, SWT.BORDER);
				nameText.setLayoutData(new GridData(GridData.FILL_BOTH));

				// UI Email
				Label emailLabel = new Label(parent, SWT.NONE);
				emailLabel.setText("Email:");
				Text emailText = new Text(parent, SWT.BORDER);
				emailText.setLayoutData(new GridData(GridData.FILL_BOTH));

				DataBindingContext dataBindingContext = new DataBindingContext(
						SWTObservables.getRealm(display));

				// Binding Name
				IObservableValue nameTextObserveTextObserveWidget = SWTObservables
						.observeText(nameText, SWT.Modify);
				IObservableValue modelNameObserveValue = PojoObservables
						.observeValue(model, "name");

				Binding binding = dataBindingContext.bindValue(
						nameTextObserveTextObserveWidget,
						modelNameObserveValue,
						Jsr303BeansUpdateValueStrategyFactory
								.create(modelNameObserveValue), null);
				ControlDecorationSupport.create(binding, SWT.LEFT, parent);

				// Binding Email
				IObservableValue emailTextObserveTextObserveWidget = SWTObservables
						.observeText(emailText, SWT.Modify);
				IObservableValue modelEmailObserveValue = PojoObservables
						.observeValue(model, "email");

				binding = dataBindingContext.bindValue(
						emailTextObserveTextObserveWidget,
						modelEmailObserveValue,
						Jsr303BeansUpdateValueStrategyFactory
								.create(modelEmailObserveValue), null);
				ControlDecorationSupport.create(binding, SWT.LEFT, parent);

				shell.setSize(200, 100);
				shell.open();

				// The SWT event loop
				Display display = Display.getCurrent();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			}
		});
	}
}
