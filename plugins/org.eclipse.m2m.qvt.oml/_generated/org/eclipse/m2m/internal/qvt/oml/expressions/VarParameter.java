/*******************************************************************************
 * Copyright (c) 2007, 2019 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.expressions;

import org.eclipse.emf.ecore.EParameter;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getCtxOwner <em>Ctx Owner</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getResOwner <em>Res Owner</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getVarParameter()
 * @model
 * @generated
 */
public interface VarParameter extends Variable, EParameter {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * Returns the value of the '<em><b>Ctx Owner</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ctx Owner</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ctx Owner</em>' container reference.
	 * @see #setCtxOwner(ImperativeOperation)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getVarParameter_CtxOwner()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getContext
	 * @model opposite="context"
	 * @generated
	 */
	ImperativeOperation getCtxOwner();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getCtxOwner <em>Ctx Owner</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ctx Owner</em>' container reference.
	 * @see #getCtxOwner()
	 * @generated
	 */
	void setCtxOwner(ImperativeOperation value);

	/**
	 * Returns the value of the '<em><b>Res Owner</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Res Owner</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Res Owner</em>' container reference.
	 * @see #setResOwner(ImperativeOperation)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getVarParameter_ResOwner()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getResult
	 * @model opposite="result"
	 * @generated
	 */
	ImperativeOperation getResOwner();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getResOwner <em>Res Owner</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Res Owner</em>' container reference.
	 * @see #getResOwner()
	 * @generated
	 */
	void setResOwner(ImperativeOperation value);

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind
	 * @see #setKind(DirectionKind)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getVarParameter_Kind()
	 * @model
	 * @generated
	 */
	DirectionKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(DirectionKind value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // VarParameter
