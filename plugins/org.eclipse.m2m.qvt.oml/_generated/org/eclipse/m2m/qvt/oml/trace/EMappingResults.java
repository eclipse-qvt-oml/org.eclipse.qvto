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
package org.eclipse.m2m.qvt.oml.trace;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMapping Results</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.m2m.qvt.oml.trace.EMappingResults#getResult <em>Result</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.m2m.qvt.oml.trace.TracePackage#getEMappingResults()
 * @model
 * @generated
 */
public interface EMappingResults extends EObject {
    /**
     * Returns the value of the '<em><b>Result</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.m2m.qvt.oml.trace.VarParameterValue}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Result</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Result</em>' containment reference list.
     * @see org.eclipse.m2m.qvt.oml.trace.TracePackage#getEMappingResults_Result()
     * @model type="org.eclipse.m2m.qvt.oml.trace.VarParameterValue" containment="true"
     * @generated
     */
    EList<VarParameterValue> getResult();

} // EMappingResults
