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
 * $Id: ResolveExp.java,v 1.5 2009/01/28 11:47:27 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions;

import org.eclipse.ocl.ecore.CallExp;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resolve Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#getCondition <em>Condition</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#isIsDeferred <em>Is Deferred</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#isIsInverse <em>Is Inverse</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#isOne <em>One</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getResolveExp()
 * @model
 * @generated
 */
public interface ResolveExp extends CallExp {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>One</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>One</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>One</em>' attribute.
	 * @see #setOne(boolean)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getResolveExp_One()
	 * @model required="true"
	 * @generated
	 */
	boolean isOne();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#isOne <em>One</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>One</em>' attribute.
	 * @see #isOne()
	 * @generated
	 */
	void setOne(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Inverse</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Inverse</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Inverse</em>' attribute.
	 * @see #setIsInverse(boolean)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getResolveExp_IsInverse()
	 * @model required="true"
	 * @generated
	 */
	boolean isIsInverse();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#isIsInverse <em>Is Inverse</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Inverse</em>' attribute.
	 * @see #isIsInverse()
	 * @generated
	 */
	void setIsInverse(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Deferred</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Deferred</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Deferred</em>' attribute.
	 * @see #setIsDeferred(boolean)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getResolveExp_IsDeferred()
	 * @model required="true"
	 * @generated
	 */
	boolean isIsDeferred();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#isIsDeferred <em>Is Deferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Deferred</em>' attribute.
	 * @see #isIsDeferred()
	 * @generated
	 */
	void setIsDeferred(boolean value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(Variable)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getResolveExp_Target()
	 * @model containment="true"
	 * @generated
	 */
	Variable getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Variable value);

	/**
	 * Returns the value of the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Condition</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Condition</em>' containment reference.
	 * @see #setCondition(OCLExpression)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getResolveExp_Condition()
	 * @model containment="true"
	 * @generated
	 */
	OCLExpression getCondition();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp#getCondition <em>Condition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Condition</em>' containment reference.
	 * @see #getCondition()
	 * @generated
	 */
	void setCondition(OCLExpression value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // ResolveExp
