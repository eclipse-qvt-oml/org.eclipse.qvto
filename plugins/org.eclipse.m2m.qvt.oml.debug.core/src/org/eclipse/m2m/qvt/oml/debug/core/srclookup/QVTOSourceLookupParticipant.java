/*******************************************************************************
 * Copyright (c) 2009 Eclipse Modeling Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Radek Dvorak - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.qvt.oml.debug.core.srclookup;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.emf.util.URIUtils;
import org.eclipse.m2m.qvt.oml.debug.core.QVTODebugUtil;
import org.eclipse.m2m.qvt.oml.debug.core.QVTOStackFrame;


public class QVTOSourceLookupParticipant extends AbstractSourceLookupParticipant {
		
	public QVTOSourceLookupParticipant() {
		super();
	}
	
    public String getSourceName(Object object) throws CoreException {
    	
    	if (object instanceof QVTOStackFrame) {
			QVTOStackFrame frame = (QVTOStackFrame) object;
			URI sourceURI = frame.getUnitURI();

			IFile findSourceFile = findSourceFile(sourceURI);
			IFile sourceFile = findSourceFile;
			if(sourceFile != null) {
				return sourceFile.getProjectRelativePath().toString();
			}
        } 
        
        return null;
    }

	private IFile findSourceFile(URI sourceURI) {
		IFile sourceFile = URIUtils.getFile(sourceURI);
		if(sourceFile == null && sourceURI.isPlatformPlugin()) {
			// FIXME
			URI workspaceURI = QVTODebugUtil.toPlatformResourceURI(sourceURI.toString());
			sourceFile = URIUtils.getFile(workspaceURI);
		}
		return sourceFile;
	}
}
