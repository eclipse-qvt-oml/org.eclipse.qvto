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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see generics.GenericsFactory
 * @model kind="package"
 * @generated
 */
public interface GenericsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "generics";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/m2m/qvt/oml/generics";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "generics";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GenericsPackage eINSTANCE = generics.impl.GenericsPackageImpl.init();

	/**
	 * The meta object id for the '{@link generics.impl.GenericClsImpl <em>Generic Cls</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see generics.impl.GenericClsImpl
	 * @see generics.impl.GenericsPackageImpl#getGenericCls()
	 * @generated
	 */
	int GENERIC_CLS = 0;

	/**
	 * The feature id for the '<em><b>Values</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CLS__VALUES = 0;

	/**
	 * The number of structural features of the '<em>Generic Cls</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CLS_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Generic Cls</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_CLS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link generics.impl.MyIterableImpl <em>My Iterable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see generics.impl.MyIterableImpl
	 * @see generics.impl.GenericsPackageImpl#getMyIterable()
	 * @generated
	 */
	int MY_ITERABLE = 1;

	/**
	 * The number of structural features of the '<em>My Iterable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_ITERABLE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>My Iterable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_ITERABLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>My Number</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.Number
	 * @see generics.impl.GenericsPackageImpl#getMyNumber()
	 * @generated
	 */
	int MY_NUMBER = 2;


	/**
	 * Returns the meta object for class '{@link generics.GenericCls <em>Generic Cls</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generic Cls</em>'.
	 * @see generics.GenericCls
	 * @generated
	 */
	EClass getGenericCls();

	/**
	 * Returns the meta object for the attribute list '{@link generics.GenericCls#getValues <em>Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Values</em>'.
	 * @see generics.GenericCls#getValues()
	 * @see #getGenericCls()
	 * @generated
	 */
	EAttribute getGenericCls_Values();

	/**
	 * Returns the meta object for class '{@link java.lang.Iterable <em>My Iterable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>My Iterable</em>'.
	 * @see java.lang.Iterable
	 * @model instanceClass="java.lang.Iterable" typeParameters="C"
	 * @generated
	 */
	EClass getMyIterable();

	/**
	 * Returns the meta object for data type '{@link java.lang.Number <em>My Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>My Number</em>'.
	 * @see java.lang.Number
	 * @model instanceClass="java.lang.Number"
	 * @generated
	 */
	EDataType getMyNumber();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GenericsFactory getGenericsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link generics.impl.GenericClsImpl <em>Generic Cls</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see generics.impl.GenericClsImpl
		 * @see generics.impl.GenericsPackageImpl#getGenericCls()
		 * @generated
		 */
		EClass GENERIC_CLS = eINSTANCE.getGenericCls();

		/**
		 * The meta object literal for the '<em><b>Values</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERIC_CLS__VALUES = eINSTANCE.getGenericCls_Values();

		/**
		 * The meta object literal for the '{@link generics.impl.MyIterableImpl <em>My Iterable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see generics.impl.MyIterableImpl
		 * @see generics.impl.GenericsPackageImpl#getMyIterable()
		 * @generated
		 */
		EClass MY_ITERABLE = eINSTANCE.getMyIterable();

		/**
		 * The meta object literal for the '<em>My Number</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.Number
		 * @see generics.impl.GenericsPackageImpl#getMyNumber()
		 * @generated
		 */
		EDataType MY_NUMBER = eINSTANCE.getMyNumber();

	}

} //GenericsPackage
