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
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation#getDisjunct <em>Disjunct</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation#getInherited <em>Inherited</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation#getMerged <em>Merged</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation#getWhen <em>When</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation#getWhere <em>Where</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getMappingOperation()
 * @model
 * @generated
 */
public interface MappingOperation extends ImperativeOperation {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * Returns the value of the '<em><b>When</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ocl.ecore.OCLExpression}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>When</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>When</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getMappingOperation_When()
	 * @model containment="true"
	 * @generated
	 */
	EList<OCLExpression> getWhen();

	/**
	 * Returns the value of the '<em><b>Where</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Where</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Where</em>' containment reference.
	 * @see #setWhere(OCLExpression)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getMappingOperation_Where()
	 * @model containment="true"
	 *        annotation="http://schema.omg.org/spec/MOF/2.0/emof.xml#Property.oppositeRoleName body='whereOwner'"
	 * @generated
	 */
	OCLExpression getWhere();

				/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation#getWhere <em>Where</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Where</em>' containment reference.
	 * @see #getWhere()
	 * @generated
	 */
	void setWhere(OCLExpression value);

	/**
	 * Returns the value of the '<em><b>Inherited</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inherited</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inherited</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getMappingOperation_Inherited()
	 * @model
	 * @generated
	 */
	EList<MappingOperation> getInherited();

				/**
	 * Returns the value of the '<em><b>Merged</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Merged</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Merged</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getMappingOperation_Merged()
	 * @model
	 * @generated
	 */
	EList<MappingOperation> getMerged();

				/**
	 * Returns the value of the '<em><b>Disjunct</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Disjunct</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Disjunct</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getMappingOperation_Disjunct()
	 * @model
	 * @generated
	 */
	EList<MappingOperation> getDisjunct();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // MappingOperation
