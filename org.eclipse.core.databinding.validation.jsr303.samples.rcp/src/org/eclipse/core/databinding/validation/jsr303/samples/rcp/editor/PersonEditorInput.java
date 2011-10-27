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
