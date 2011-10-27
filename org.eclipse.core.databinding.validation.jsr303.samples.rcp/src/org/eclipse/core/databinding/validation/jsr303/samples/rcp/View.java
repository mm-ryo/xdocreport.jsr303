package org.eclipse.core.databinding.validation.jsr303.samples.rcp;

import org.eclipse.core.databinding.validation.jsr303.samples.rcp.editor.PersonEditor;
import org.eclipse.core.databinding.validation.jsr303.samples.rcp.editor.PersonEditorInput;
import org.eclipse.core.databinding.validation.jsr303.samples.rcp.model.Person;
import org.eclipse.core.databinding.validation.jsr303.samples.rcp.services.PersonService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart implements IDoubleClickListener {
	public static final String ID = "org.eclipse.core.databinding.validation.jsr303.samples.rcp.view";

	private TableViewer viewer;

//	/**
//	 * The content provider class is responsible for providing objects to the
//	 * view. It can wrap existing objects in adapters or simply return objects
//	 * as-is. These objects may be sensitive to the current input of the view,
//	 * or ignore it and always show the same content (like Task List, for
//	 * example).
//	 */
//	class ViewContentProvider implements IStructuredContentProvider {
//		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
//		}
//
//		public void dispose() {
//		}
//
//		public Object[] getElements(Object parent) {
//			if (parent instanceof Object[]) {
//				return (Object[]) parent;
//			}
//			return new Object[0];
//		}
//	}
//
	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if (obj instanceof Person) {
				return ((Person) obj).getName();
			}
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new ViewLabelProvider());
		// Provide the input to the ContentProvider
		viewer.setInput(PersonService.getInstance().getPersons());
		viewer.addDoubleClickListener(this);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void doubleClick(final DoubleClickEvent event) {
		ISelection selection = event.getSelection();
		if (selection != null && selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object element = structuredSelection.getFirstElement();
			if (element instanceof Person) {
				try {
					long personId = ((Person) element).getId();
					PersonEditorInput input = new PersonEditorInput(personId);
					getSite().getPage().openEditor(input, PersonEditor.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}

	}

}