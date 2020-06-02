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
 * $Id: ModelType.java,v 1.5 2009/01/28 11:47:27 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModelType#getAdditionalCondition <em>Additional Condition</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModelType#getConformanceKind <em>Conformance Kind</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModelType#getMetamodel <em>Metamodel</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModelType()
 * @model
 * @generated
 */
public interface ModelType extends EClass, VisitableASTNode {
	
	/**
	 * @generated NOT
	 */
	String SINGLETON_NAME = "ModelType"; //$NON-NLS-1$
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Metamodel</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EPackage}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Metamodel</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Metamodel</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModelType_Metamodel()
	 * @model required="true"
	 * @generated
	 */
	EList<EPackage> getMetamodel();

	/**
	 * Returns the value of the '<em><b>Additional Condition</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ocl.ecore.OCLExpression}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Additional Condition</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Additional Condition</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModelType_AdditionalCondition()
	 * @model containment="true"
	 * @generated
	 */
	EList<OCLExpression> getAdditionalCondition();

	/**
	 * Returns the value of the '<em><b>Conformance Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Conformance Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Conformance Kind</em>' attribute.
	 * @see #setConformanceKind(String)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModelType_ConformanceKind()
	 * @model
	 * @generated
	 */
	String getConformanceKind();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModelType#getConformanceKind <em>Conformance Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Conformance Kind</em>' attribute.
	 * @see #getConformanceKind()
	 * @generated
	 */
	void setConformanceKind(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // ModelType
