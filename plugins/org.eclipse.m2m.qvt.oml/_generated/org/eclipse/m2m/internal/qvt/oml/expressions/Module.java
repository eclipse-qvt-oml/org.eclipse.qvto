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
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getConfigProperty <em>Config Property</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getEntry <em>Entry</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#isIsBlackbox <em>Is Blackbox</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getModuleImport <em>Module Import</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getOwnedTag <em>Owned Tag</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getOwnedVariable <em>Owned Variable</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getUsedModelType <em>Used Model Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule()
 * @model
 * @generated
 */
public interface Module extends EClass, EPackage, VisitableASTNode {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * Returns the value of the '<em><b>Owned Variable</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.ocl.ecore.Variable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Variable</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Variable</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule_OwnedVariable()
	 * @model containment="true" ordered="false"
	 * @generated
	 */
	EList<Variable> getOwnedVariable();

				/**
	 * Returns the value of the '<em><b>Entry</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entry</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entry</em>' reference.
	 * @see #setEntry(EntryOperation)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule_Entry()
	 * @model
	 * @generated
	 */
	EntryOperation getEntry();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getEntry <em>Entry</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entry</em>' reference.
	 * @see #getEntry()
	 * @generated
	 */
	void setEntry(EntryOperation value);

	/**
	 * Returns the value of the '<em><b>Is Blackbox</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Blackbox</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Blackbox</em>' attribute.
	 * @see #isSetIsBlackbox()
	 * @see #unsetIsBlackbox()
	 * @see #setIsBlackbox(boolean)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule_IsBlackbox()
	 * @model unsettable="true"
	 * @generated
	 */
	boolean isIsBlackbox();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#isIsBlackbox <em>Is Blackbox</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Blackbox</em>' attribute.
	 * @see #isSetIsBlackbox()
	 * @see #unsetIsBlackbox()
	 * @see #isIsBlackbox()
	 * @generated
	 */
	void setIsBlackbox(boolean value);

	/**
	 * Unsets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#isIsBlackbox <em>Is Blackbox</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIsBlackbox()
	 * @see #isIsBlackbox()
	 * @see #setIsBlackbox(boolean)
	 * @generated
	 */
	void unsetIsBlackbox();

	/**
	 * Returns whether the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#isIsBlackbox <em>Is Blackbox</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Is Blackbox</em>' attribute is set.
	 * @see #unsetIsBlackbox()
	 * @see #isIsBlackbox()
	 * @see #setIsBlackbox(boolean)
	 * @generated
	 */
	boolean isSetIsBlackbox();

	/**
	 * Returns the value of the '<em><b>Config Property</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EStructuralFeature}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Config Property</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Config Property</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule_ConfigProperty()
	 * @model
	 * @generated
	 */
	EList<EStructuralFeature> getConfigProperty();

	/**
	 * Returns the value of the '<em><b>Module Import</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Module Import</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module Import</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule_ModuleImport()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getModule
	 * @model opposite="module" containment="true"
	 * @generated
	 */
	EList<ModuleImport> getModuleImport();

	/**
	 * Returns the value of the '<em><b>Owned Tag</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EAnnotation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Owned Tag</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Tag</em>' containment reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule_OwnedTag()
	 * @model containment="true"
	 *        annotation="http://schema.omg.org/spec/MOF/2.0/emof.xml#Property.oppositeRoleName body='owner'"
	 * @generated
	 */
	EList<EAnnotation> getOwnedTag();

	/**
	 * Returns the value of the '<em><b>Used Model Type</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.ModelType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Used Model Type</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Used Model Type</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModule_UsedModelType()
	 * @model
	 * @generated
	 */
	EList<ModelType> getUsedModelType();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // Module
