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
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Body</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody#getContent <em>Content</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody#getOperation <em>Operation</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody#getVariable <em>Variable</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationBody()
 * @model
 * @generated
 */
public interface OperationBody extends VisitableASTNode, EModelElement {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * Returns the value of the '<em><b>Operation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operation</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operation</em>' container reference.
	 * @see #setOperation(ImperativeOperation)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationBody_Operation()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation#getBody
	 * @model opposite="body"
	 * @generated
	 */
	ImperativeOperation getOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody#getOperation <em>Operation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Operation</em>' container reference.
	 * @see #getOperation()
	 * @generated
	 */
	void setOperation(ImperativeOperation value);

	/**
	 * Returns the value of the '<em><b>Content</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ocl.ecore.OCLExpression}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Content</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Content</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationBody_Content()
	 * @model containment="true"
	 * @generated
	 */
	EList<OCLExpression> getContent();

	/**
	 * Returns the value of the '<em><b>Variable</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ocl.ecore.Variable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variable</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variable</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationBody_Variable()
	 * @model containment="true"
	 * @generated
	 */
	EList<Variable> getVariable();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // OperationBody
