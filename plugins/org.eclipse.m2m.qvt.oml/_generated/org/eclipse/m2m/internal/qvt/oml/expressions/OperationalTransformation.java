/**
 * Copyright (c) 2008, 2019 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *
 * $Id: OperationalTransformation.java,v 1.3 2008/12/18 15:18:10 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operational Transformation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation#getIntermediateClass <em>Intermediate Class</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation#getIntermediateProperty <em>Intermediate Property</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation#getModelParameter <em>Model Parameter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationalTransformation()
 * @model
 * @generated
 */
public interface OperationalTransformation extends org.eclipse.m2m.internal.qvt.oml.expressions.Module {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Intermediate Class</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Intermediate Class</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Intermediate Class</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationalTransformation_IntermediateClass()
	 * @model
	 * @generated
	 */
	EList<EClass> getIntermediateClass();

	/**
	 * Returns the value of the '<em><b>Intermediate Property</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EStructuralFeature}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Intermediate Property</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Intermediate Property</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationalTransformation_IntermediateProperty()
	 * @model
	 * @generated
	 */
	EList<EStructuralFeature> getIntermediateProperty();

	/**
	 * Returns the value of the '<em><b>Model Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Parameter</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Parameter</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getOperationalTransformation_ModelParameter()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModelParameter> getModelParameter();

} // OperationalTransformation
