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

package org.eclipse.m2m.qvt.oml.internal.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.DOMHandler;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.m2m.qvt.oml.QvtEngine;
import org.eclipse.m2m.qvt.oml.QvtMessage;
import org.eclipse.m2m.qvt.oml.common.MdaException;
import org.eclipse.m2m.qvt.oml.common.io.eclipse.EclipseFile;
import org.eclipse.m2m.qvt.oml.compiler.CompilerMessages;
import org.eclipse.m2m.qvt.oml.compiler.QvtCompilationResult;
import org.eclipse.m2m.qvt.oml.emf.util.WorkspaceUtils;
import org.eclipse.osgi.util.NLS;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * @author sboyko
 *
 */
public class QvtOperationalResourceImpl extends XMIResourceImpl {

    public QvtOperationalResourceImpl(URI uri) {
    	super(uri);
    }
	
    @Override
    public void load(Map<?, ?> options) throws IOException {
    	if (!isLoaded()) {
    		Notification notification = null;
    		isLoading = true;

			if (errors != null) {
				errors.clear();
			}
			if (warnings != null) {
				warnings.clear();
			}
    		
            try {
            	URI normalizedUri = getURIConverter().normalize(getURI());
            	String uriPath = normalizedUri.isFile() ? normalizedUri.toFileString() :
            			(normalizedUri.isPlatform() ? normalizedUri.toPlatformString(true) :
            					normalizedUri.toString());
            	IFile file = WorkspaceUtils.getWorkspaceFile(uriPath);
    			QvtCompilationResult compilationResult = QvtEngine.getInstance(file).compile(new EclipseFile(file), false, null);
				fillCompilationDiagnostic(compilationResult, uriPath);

				if (compilationResult.getModule().getModule() == null) {
    				throw new IOException(NLS.bind(CompilerMessages.moduleCompilationErrors, 
    						file, Arrays.asList(compilationResult.getErrors())));
    			}

        		notification = setLoaded(true);
    			getContents().add(compilationResult.getModule().getModule());
            }
            catch (MdaException e) {
				throw new IOWrappedException(e);
			}
            finally {
				isLoading = false;
				
				if (notification != null) {
					eNotify(notification);
				}
				
				setModified(false);
            }
    	}
    }
    
    private void fillCompilationDiagnostic(QvtCompilationResult compilationResult, String uriPath) {
    	warnings = getWarnings();
		for (QvtMessage msg : compilationResult.getWarnings()) {
			warnings.add(new QvtCompilationErrorException(msg, uriPath));
		}
		errors = getErrors();
		for (QvtMessage msg : compilationResult.getErrors()) {
			errors.add(new QvtCompilationErrorException(msg, uriPath));
		}
	}

	@Override
    public void doLoad(InputStream inputStream, Map<?, ?> options)
    		throws IOException {
    	throw new UnsupportedOperationException();
    }

    @Override
    public void doLoad(InputSource inputSource, Map<?, ?> options) throws IOException {
    	throw new UnsupportedOperationException();
    }

    @Override
    public void doLoad(Node node, Map<?, ?> options) throws IOException {
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public Document save(Document doc, Map<?, ?> options, DOMHandler handler) {
    	throw new UnsupportedOperationException();
    }
    
    @Override
    public void save(Map<?, ?> options) throws IOException {
    	// resource operates as read-only
    }
    
    @Override
    public boolean isModified() {
    	return false;
    }
    
    @Override
    public boolean isTrackingModification() {
    	return false;
    }
    
}
