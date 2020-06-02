/**
 * Copyright (c) 2007, 2019 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *
 * $Id: ContextualProperty.java,v 1.5 2009/01/28 11:47:26 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contextual Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty#getContext <em>Context</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty#getInitExpression <em>Init Expression</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty#getOverridden <em>Overridden</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getContextualProperty()
 * @model
 * @generated
 */
public interface ContextualProperty extends EStructuralFeature, VisitableASTNode {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Context</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' reference.
	 * @see #setContext(EClass)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getContextualProperty_Context()
	 * @model required="true"
	 * @generated
	 */
	EClass getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty#getContext <em>Context</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' reference.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(EClass value);

	/**
	 * Returns the value of the '<em><b>Overridden</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Overridden</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Overridden</em>' reference.
	 * @see #setOverridden(EStructuralFeature)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getContextualProperty_Overridden()
	 * @model
	 * @generated
	 */
	EStructuralFeature getOverridden();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty#getOverridden <em>Overridden</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Overridden</em>' reference.
	 * @see #getOverridden()
	 * @generated
	 */
	void setOverridden(EStructuralFeature value);

	/**
	 * Returns the value of the '<em><b>Init Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Init Expression</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Init Expression</em>' containment reference.
	 * @see #setInitExpression(OCLExpression)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getContextualProperty_InitExpression()
	 * @model containment="true"
	 * @generated
	 */
	OCLExpression getInitExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty#getInitExpression <em>Init Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init Expression</em>' containment reference.
	 * @see #getInitExpression()
	 * @generated
	 */
	void setInitExpression(OCLExpression value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // ContextualProperty
