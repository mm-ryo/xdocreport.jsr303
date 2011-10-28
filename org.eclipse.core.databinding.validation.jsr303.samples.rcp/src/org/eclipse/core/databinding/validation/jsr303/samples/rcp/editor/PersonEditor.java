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

import org.eclipse.core.databinding.validation.jsr303.samples.rcp.model.Person;
import org.eclipse.core.databinding.validation.jsr303.samples.rcp.services.PersonService;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

public class PersonEditor extends FormEditor {

	public static final String ID = "org.eclipse.core.databinding.validation.jsr303.samples.rcp.editor.PersonEditor";

	private Person person;

	@Override
	protected void addPages() {
		try {
			super.addPage(new OverviewPage(this));
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	public Person getPerson() {
		if (person == null) {
			long personId = ((PersonEditorInput) super.getEditorInput())
					.getPersonId();
			person = PersonService.getInstance().getPerson(personId);
		}
		return person;
	}

	@Override
	public void dispose() {
		super.dispose();
		person = null;
	}
}
