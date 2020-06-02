/*******************************************************************************
 * Copyright (c) 2018, 2019 Eclipse Modeling Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     S.Boyko - initial API and implementation
 *******************************************************************************/
/**
 */
package generics;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Cls</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link generics.GenericCls#getValues <em>Values</em>}</li>
 * </ul>
 *
 * @see generics.GenericsPackage#getGenericCls()
 * @model EBounds="generics.MyNumber"
 * @generated
 */
public interface GenericCls<E extends Number> extends EObject {
	/**
	 * Returns the value of the '<em><b>Values</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Values</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Values</em>' attribute list.
	 * @see generics.GenericsPackage#getGenericCls_Values()
	 * @model
	 * @generated
	 */
	EList<E> getValues();

} // GenericCls
