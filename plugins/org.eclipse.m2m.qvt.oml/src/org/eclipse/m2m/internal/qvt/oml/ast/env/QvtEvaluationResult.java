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

package org.eclipse.m2m.internal.qvt.oml.ast.env;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.m2m.internal.qvt.oml.ExecutionDiagnosticImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;

/**
 * @author sboyko
 *
 */
public class QvtEvaluationResult {
	
	@Deprecated
	public QvtEvaluationResult(List<ModelExtentContents> modelExtents, List<EObject> unboundedObjects, List<Object> outParamValues) {
		this(modelExtents, unboundedObjects, outParamValues, ExecutionDiagnosticImpl.createOkInstance());
	}

	public QvtEvaluationResult(List<ModelExtentContents> modelExtents, List<EObject> unboundedObjects, List<Object> outParamValues, ExecutionDiagnostic executionDiagnostic) {
		myModelExtents = modelExtents;
		myUnboundedObjects = unboundedObjects;
		myOutParamValues = outParamValues;
		myExecutionDiagnostic = executionDiagnostic;
	}
	
	public List<ModelExtentContents> getModelExtents() {
		return myModelExtents;
	}
	
	public List<EObject> getUnboundedObjects() {
		return myUnboundedObjects;
	}
	
	public List<Object> getOutParamValues() {
		return myOutParamValues;
	}
	
	public ExecutionDiagnostic getExecutionDiagnostic() {
		return myExecutionDiagnostic;
	}
	
	private final List<ModelExtentContents> myModelExtents;
	private final List<EObject> myUnboundedObjects;
	private final List<Object> myOutParamValues;
	private final ExecutionDiagnostic myExecutionDiagnostic;

}
