/*******************************************************************************
 * Copyright (c) 2007, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.IntermediateClassFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalTypesUtil;
import org.eclipse.m2m.internal.qvt.oml.common.MdaException;
import org.eclipse.m2m.internal.qvt.oml.common.emf.ExtendedEmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.Logger;
import org.eclipse.m2m.internal.qvt.oml.emf.util.URIUtils;
import org.eclipse.m2m.internal.qvt.oml.evaluator.TraceUtil;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.trace.EDirectionKind;
import org.eclipse.m2m.internal.qvt.oml.trace.EValue;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;
import org.eclipse.m2m.internal.qvt.oml.trace.TraceRecord;
import org.eclipse.m2m.internal.qvt.oml.trace.VarParameterValue;
import org.eclipse.osgi.util.NLS;


public class TraceSerializer {

	public TraceSerializer(Trace trace) {
		myTrace = trace;
		myUnboundObjects = new ArrayList<EObject>(1);

		patch(myTrace);
	}

    public void saveTraceModel(URI traceUri) throws MdaException {
        Map<Object, Object> options = new HashMap<Object, Object>();
        options.put(XMLResource.OPTION_PROCESS_DANGLING_HREF, XMLResource.OPTION_PROCESS_DANGLING_HREF_DISCARD);
        ExtendedEmfUtil.saveModel(myTrace, traceUri, options);
    }
    
    public void markUnboundObjects(URI traceUri) throws CoreException {
    	IFile ifile = URIUtils.getFile(traceUri);
	    if (ifile == null) {
	    	return;
	    }
    	ifile.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		for (EObject eObj : myUnboundObjects) {
    		createQvtMarker(ifile, eObj);
    	}
    }

	private void createQvtMarker(IResource iresource, EObject eObj) {
	    Map<String, Object> attributes = new HashMap<String, Object>();
	    attributes.put(IMarker.MESSAGE, NLS.bind("Trace contains unbounded object of type ''{0}''", //$NON-NLS-1$
	    		QvtOperationalTypesUtil.getTypeFullName(eObj.eClass())));
	    attributes.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
	    URI uri = EcoreUtil.getURI(eObj);
	    if (uri != null) {
	    	attributes.put(EValidator.URI_ATTRIBUTE, uri.fragment());
	    }
	    try {
	        IMarker marker = iresource.createMarker(IMarker.PROBLEM);
	        marker.setAttributes(attributes);
	    }
	    catch (CoreException e) {
	        Logger.getLogger().log(Logger.WARNING, "TraceSerializer: failed to create marker", e);//$NON-NLS-1$
	    }                   
	}

	private Trace patch(Trace trace) {
        for (TraceRecord traceRecord : trace.getTraceRecords()) {
            VarParameterValue context = traceRecord.getContext().getContext();
            if (context != null) {
    			patch(context.getValue());
            }
            for (VarParameterValue param : traceRecord.getParameters().getParameters()) {
                patch(param.getValue());
            }
            for (VarParameterValue result : traceRecord.getResult().getResult()) {
            	updateResult(result);
                patch(result.getValue());
            }
        }
        return trace;
    }

    private void updateResult(VarParameterValue varParamValue) {
		if (varParamValue.getValue() != null) {
			varParamValue.setValue(TraceUtil.createEValue(varParamValue.getValue().getOclObject(), DirectionKind.getByName(varParamValue.getKind().getName())));
		}
	}

	private void patch(EValue eValue) {
        if (eValue != null) {
            EObject modelElement = eValue.getModelElement();
            if (modelElement != null) {
                if (modelElement.eResource() == null) {
                    if (modelElement.eContainer() == null 
                    		&& !IntermediateClassFactory.isIntermediateClass(modelElement.eClass())) {
                    	eValue.setIntermediateElement(modelElement);
                    	myUnboundObjects.add(modelElement);
                    }
                }
            } else {
        		for (EValue collectionElement : eValue.getCollection()) {
					patch(collectionElement);
				}
            }
        }
    }
    
    private final Trace myTrace;
    private final List<EObject> myUnboundObjects;
}
