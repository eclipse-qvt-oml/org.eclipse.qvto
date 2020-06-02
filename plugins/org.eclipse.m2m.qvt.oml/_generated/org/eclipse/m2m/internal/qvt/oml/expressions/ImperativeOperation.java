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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Imperative Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getContext <em>Context</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#isIsBlackbox <em>Is Blackbox</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getOverridden <em>Overridden</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeOperation()
 * @model
 * @generated
 */
public interface ImperativeOperation extends EOperation, VisitableASTNode {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * Returns the value of the '<em><b>Context</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getCtxOwner <em>Ctx Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' containment reference.
	 * @see #setContext(VarParameter)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeOperation_Context()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getCtxOwner
	 * @model opposite="ctxOwner" containment="true"
	 * @generated
	 */
	VarParameter getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getContext <em>Context</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' containment reference.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(VarParameter value);

	/**
	 * Returns the value of the '<em><b>Result</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getResOwner <em>Res Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Result</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeOperation_Result()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter#getResOwner
	 * @model opposite="resOwner" containment="true"
	 * @generated
	 */
	EList<VarParameter> getResult();

	/**
	 * Returns the value of the '<em><b>Body</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Body</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Body</em>' containment reference.
	 * @see #setBody(OperationBody)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeOperation_Body()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody#getOperation
	 * @model opposite="operation" containment="true"
	 * @generated
	 */
	OperationBody getBody();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getBody <em>Body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Body</em>' containment reference.
	 * @see #getBody()
	 * @generated
	 */
	void setBody(OperationBody value);

	/**
	 * Returns the value of the '<em><b>Is Blackbox</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Blackbox</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Blackbox</em>' attribute.
	 * @see #setIsBlackbox(boolean)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeOperation_IsBlackbox()
	 * @model
	 * @generated
	 */
	boolean isIsBlackbox();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#isIsBlackbox <em>Is Blackbox</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Blackbox</em>' attribute.
	 * @see #isIsBlackbox()
	 * @generated
	 */
	void setIsBlackbox(boolean value);

	/**
	 * Returns the value of the '<em><b>Overridden</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Overridden</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Overridden</em>' reference.
	 * @see #setOverridden(ImperativeOperation)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeOperation_Overridden()
	 * @model annotation="http://schema.omg.org/spec/MOF/2.0/emof.xml#Property.oppositeRoleName body='overriding'"
	 * @generated
	 */
	ImperativeOperation getOverridden();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getOverridden <em>Overridden</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Overridden</em>' reference.
	 * @see #getOverridden()
	 * @generated
	 */
	void setOverridden(ImperativeOperation value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // ImperativeOperation
