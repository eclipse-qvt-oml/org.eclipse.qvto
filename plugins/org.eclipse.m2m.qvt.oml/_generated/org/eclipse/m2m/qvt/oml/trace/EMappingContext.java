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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMapping Context</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.m2m.qvt.oml.trace.EMappingContext#getContext <em>Context</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.m2m.qvt.oml.trace.TracePackage#getEMappingContext()
 * @model
 * @generated
 */
public interface EMappingContext extends EObject {
    /**
     * Returns the value of the '<em><b>Context</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Context</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Context</em>' containment reference.
     * @see #setContext(VarParameterValue)
     * @see org.eclipse.m2m.qvt.oml.trace.TracePackage#getEMappingContext_Context()
     * @model containment="true"
     * @generated
     */
    VarParameterValue getContext();

    /**
     * Sets the value of the '{@link org.eclipse.m2m.qvt.oml.trace.EMappingContext#getContext <em>Context</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Context</em>' containment reference.
     * @see #getContext()
     * @generated
     */
    void setContext(VarParameterValue value);

} // EMappingContext
