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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class PersonEditorInput implements IEditorInput {

	private long personId;

	public PersonEditorInput(long personId) {
		this.personId = personId;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "XX";
	}

	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "tt";
	}

	public long getPersonId() {
		return personId;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

}
