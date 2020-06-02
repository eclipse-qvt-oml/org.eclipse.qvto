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
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getBinding <em>Binding</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getImportedModule <em>Imported Module</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getModule <em>Module</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModuleImport()
 * @model
 * @generated
 */
public interface ModuleImport extends VisitableASTNode, EModelElement {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * Returns the value of the '<em><b>Binding</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.m2m.internal.qvt.oml.expressions.ModelType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Binding</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Binding</em>' reference list.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModuleImport_Binding()
	 * @model
	 * @generated
	 */
	EList<ModelType> getBinding();

				/**
	 * Returns the value of the '<em><b>Module</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.m2m.internal.qvt.oml.expressions.Module#getModuleImport <em>Module Import</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Module</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module</em>' container reference.
	 * @see #setModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModuleImport_Module()
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.Module#getModuleImport
	 * @model opposite="moduleImport"
	 * @generated
	 */
	org.eclipse.m2m.internal.qvt.oml.expressions.Module getModule();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getModule <em>Module</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module</em>' container reference.
	 * @see #getModule()
	 * @generated
	 */
	void setModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module value);

	/**
	 * Returns the value of the '<em><b>Imported Module</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Imported Module</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Imported Module</em>' reference.
	 * @see #setImportedModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModuleImport_ImportedModule()
	 * @model required="true"
	 * @generated
	 */
	org.eclipse.m2m.internal.qvt.oml.expressions.Module getImportedModule();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getImportedModule <em>Imported Module</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Imported Module</em>' reference.
	 * @see #getImportedModule()
	 * @generated
	 */
	void setImportedModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module value);

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.m2m.internal.qvt.oml.expressions.ImportKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ImportKind
	 * @see #setKind(ImportKind)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getModuleImport_Kind()
	 * @model
	 * @generated
	 */
	ImportKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ImportKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ImportKind value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // ModuleImport
