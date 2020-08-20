/*******************************************************************************
 * Copyright (c) 2018 Eclipse Modeling Project and others.
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

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see generics.GenericsPackage
 * @generated
 */
public interface GenericsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GenericsFactory eINSTANCE = generics.impl.GenericsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Generic Cls</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Generic Cls</em>'.
	 * @generated
	 */
	<E extends Number> GenericCls<E> createGenericCls();

	/**
	 * Returns a new object of class '<em>My Iterable</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>My Iterable</em>'.
	 * @generated
	 */
	<C extends EClassifier> Iterable<C> createMyIterable();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	GenericsPackage getGenericsPackage();

} //GenericsFactory
