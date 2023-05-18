/*******************************************************************************
 * Copyright (c) 2022 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.bbox.bug577992;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
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
 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.Bug577992Factory
 * @model kind="package"
 * @generated
 */
public interface Bug577992Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "bug577992";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://bug577992";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.m2m.tests.qvt.oml";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Bug577992Package eINSTANCE = org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.impl.Bug577992PackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.impl.TestClassImpl <em>Test Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.impl.TestClassImpl
	 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.impl.Bug577992PackageImpl#getTestClass()
	 * @generated
	 */
	int TEST_CLASS = 0;

	/**
	 * The feature id for the '<em><b>Attr</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CLASS__ATTR = 0;

	/**
	 * The number of structural features of the '<em>Test Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CLASS_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CLASS___OP__TESTCLASS = 0;

	/**
	 * The number of operations of the '<em>Test Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_CLASS_OPERATION_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass <em>Test Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Class</em>'.
	 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass
	 * @generated
	 */
	EClass getTestClass();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass#getAttr <em>Attr</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attr</em>'.
	 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass#getAttr()
	 * @see #getTestClass()
	 * @generated
	 */
	EAttribute getTestClass_Attr();

	/**
	 * Returns the meta object for the '{@link org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass#op(org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass) <em>Op</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Op</em>' operation.
	 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass#op(org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass)
	 * @generated
	 */
	EOperation getTestClass__Op__TestClass();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Bug577992Factory getBug577992Factory();

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
		 * The meta object literal for the '{@link org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.impl.TestClassImpl <em>Test Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.impl.TestClassImpl
		 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.impl.Bug577992PackageImpl#getTestClass()
		 * @generated
		 */
		EClass TEST_CLASS = eINSTANCE.getTestClass();

		/**
		 * The meta object literal for the '<em><b>Attr</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_CLASS__ATTR = eINSTANCE.getTestClass_Attr();

		/**
		 * The meta object literal for the '<em><b>Op</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TEST_CLASS___OP__TESTCLASS = eINSTANCE.getTestClass__Op__TestClass();

	}

} //Bug577992Package
