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
package org.eclipse.core.databinding.validation.jsr303.samples.rcp.editor;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeanValidationSupport.StrategyValidatorFactoryResolver;
import org.eclipse.core.databinding.validation.jsr303.Jsr303BeansUpdateValueStrategyFactory;
import org.eclipse.core.databinding.validation.jsr303.samples.rcp.Activator;
import org.eclipse.core.databinding.validation.jsr303.samples.rcp.model.Person;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class OverviewPage extends FormPage {

	private static final String ID = "overview";
	protected static final String HIBERNATE_VALIDATOR = "javax.validation.osgi.config.bundle.hibernatevalidator";
	protected static final String APACHE_BVAL = "javax.validation.osgi.config.bundle.apachebval";
	private Text personNameText;
	private Text personEmailText;
	private Composite parent;
	private Button jsr303Hibernate;
	private Button jsr303Apache;
	private Label jsr303ImplLabel;

	public OverviewPage(PersonEditor editor) {
		super(editor, ID, "Overview");
	}

	@Override
	public PersonEditor getEditor() {
		return (PersonEditor) super.getEditor();
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		displayState();
		createUI(managedForm);
		createBindings();
	}

	private void displayState() {
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
	}

	private void createUI(IManagedForm managedForm) {
		FormToolkit toolkit = managedForm.getToolkit();
		Composite body = managedForm.getForm().getBody();
		TableWrapLayout tableWrapLayout = new TableWrapLayout();
		tableWrapLayout.numColumns = 1;
		body.setLayout(tableWrapLayout);

		parent = toolkit.createComposite(body);
		parent.setLayout(new GridLayout(2, false));
		parent.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		toolkit.createLabel(parent, "Name:");
		personNameText = toolkit.createText(parent, " ", SWT.NONE);
		personNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolkit.createLabel(parent, "Email:");
		personEmailText = toolkit.createText(parent, " ", SWT.NONE);
		personEmailText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		toolkit.createLabel(parent,
				"JSR-303 Impl:");
		jsr303ImplLabel = toolkit
				.createLabel(
						parent,
						Jsr303BeanValidationSupport
								.getValidatorFactoryClassName() != null ? Jsr303BeanValidationSupport
								.getValidatorFactoryClassName() : "");
		jsr303ImplLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		if (Jsr303BeanValidationSupport.getStrategy() == StrategyValidatorFactoryResolver.Bundle) {
			Composite composite=toolkit.createComposite(parent, SWT.NONE);
			GridData gridData=new GridData(GridData.FILL_VERTICAL);
			gridData.horizontalSpan=2;
			composite.setLayoutData(gridData);
			composite.setLayout(new GridLayout(2, true));
			
			// Display button to change JSR-303 implementation
			jsr303Hibernate = toolkit.createButton(composite,
					"Active Hibernate Validator", SWT.BORDER);
			jsr303Hibernate.setEnabled(false);
			jsr303Hibernate.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					changeJSR303Implementation(HIBERNATE_VALIDATOR, APACHE_BVAL);
				}
			});

			jsr303Apache = toolkit.createButton(composite, "Active Apache bval",
					SWT.BORDER);
			jsr303Apache.setEnabled(true);
			jsr303Apache.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					changeJSR303Implementation(APACHE_BVAL, HIBERNATE_VALIDATOR);
				}
			});
		}

		toolkit.paintBordersFor(parent);
	}

	private void createBindings() {

		Person person = getEditor().getPerson();
		DataBindingContext dataBindingContext = new DataBindingContext();

		// Binding name
		IObservableValue textObserveTextObserveWidget = SWTObservables
				.observeText(personNameText, SWT.Modify);
		IObservableValue modelNameObserveValue = PojoObservables.observeValue(
				person, "name");
		Binding binding = dataBindingContext.bindValue(
				textObserveTextObserveWidget, modelNameObserveValue,
				Jsr303BeansUpdateValueStrategyFactory
						.create(modelNameObserveValue), null);
		ControlDecorationSupport.create(binding, SWT.LEFT, parent);

		// Binding email
		IObservableValue emailTextObserveTextObserveWidget = SWTObservables
				.observeText(personEmailText, SWT.Modify);
		IObservableValue modelEmailObserveValue = PojoObservables.observeValue(
				person, "email");
		binding = dataBindingContext.bindValue(
				emailTextObserveTextObserveWidget, modelEmailObserveValue,
				Jsr303BeansUpdateValueStrategyFactory
						.create(modelEmailObserveValue), null);
		ControlDecorationSupport.create(binding, SWT.LEFT, parent);
	}

	protected void changeJSR303Implementation(String bundleIdToStart,
			String bundleIdToStop) {
		Activator.getDefault().changeJSR303Implementation(bundleIdToStart,
				bundleIdToStop);
		if (HIBERNATE_VALIDATOR.equals(bundleIdToStart)) {
			jsr303Hibernate.setEnabled(false);
			jsr303Apache.setEnabled(true);
		} else {
			jsr303Hibernate.setEnabled(true);
			jsr303Apache.setEnabled(false);
		}
		jsr303ImplLabel
				.setText(Jsr303BeanValidationSupport
						.getValidatorFactoryClassName() != null ? Jsr303BeanValidationSupport
						.getValidatorFactoryClassName() : "");
		displayState();
	}
}
