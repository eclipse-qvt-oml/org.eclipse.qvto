/*******************************************************************************
 * Copyright (c) 2007 Borland Software Corporation
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.runtime.ui.trace.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompiler;
import org.eclipse.m2m.internal.qvt.oml.runtime.ui.trace.presentation.EObjectNode;
import org.eclipse.m2m.qvt.oml.common.emf.ExtendedEmfUtil;
import org.eclipse.m2m.qvt.oml.common.io.eclipse.EclipseFile;
import org.eclipse.m2m.qvt.oml.runtime.ui.trace.common.TraceWorkbenchPart;
import org.eclipse.m2m.qvt.oml.trace.Trace;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.EditorPart;

/**
 * @author aigdalov
 */
public class TraceEditorPart extends EditorPart implements IGotoMarker {
    private final TraceWorkbenchPart myTraceWorkbenchPart = new TraceWorkbenchPart(this);
	private Trace myTrace;
    
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        if (!(input instanceof IFileEditorInput)) {
            throw new PartInitException(NLS.bind(Messages.TraceEditorPart_InvalidInput, input));
        }
        final IFileEditorInput inp = (IFileEditorInput) input;
        setSite(site);
        setPartName(input.getName());
        setInput(input);
        myTrace = initTrace(inp.getFile());
	}
	
	@Override
	public void createPartControl(Composite parent) {
        myTraceWorkbenchPart.createPartControl(parent);
        myTraceWorkbenchPart.setInput(myTrace);
	}

	@Override
	public void setFocus() {
        if (!getViewer().getControl().isDisposed()) {
            getViewer().getControl().setFocus();
        }
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	public TreeViewer getViewer() {
		return myTraceWorkbenchPart.getViewer();
	}

	public void gotoMarker(IMarker marker) {
		try {
			if (marker.getType().equals(QvtCompiler.PROBLEM_MARKER)) {
				String uriAttribute = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
				if (uriAttribute != null) {
					EObject eObject = myTrace.eResource().getEObject(uriAttribute);
					if (eObject != null) {
						myTraceWorkbenchPart.setSelection(new EObjectNode(eObject.eContainer(), eObject));
					}
				}
			}
		} catch (CoreException e) {
		}
	}
	
    private Trace initTrace(IFile file) throws PartInitException {
        EObject eObject = ExtendedEmfUtil.loadModel(new EclipseFile(file));
        if(!(eObject instanceof Trace)) {
            throw new PartInitException(NLS.bind(Messages.TraceEditorPart_LoadingFailed, file.getFullPath()));
        }
        return (Trace) eObject;
    }

}