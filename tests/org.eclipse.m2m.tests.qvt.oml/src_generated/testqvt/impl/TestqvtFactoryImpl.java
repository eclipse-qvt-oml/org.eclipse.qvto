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
package testqvt.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import testqvt.BooleanElement;
import testqvt.Element;
import testqvt.Model;
import testqvt.Numbers;
import testqvt.TestqvtFactory;
import testqvt.TestqvtPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TestqvtFactoryImpl extends EFactoryImpl implements TestqvtFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TestqvtFactory init() {
		try {
			TestqvtFactory theTestqvtFactory = (TestqvtFactory)EPackage.Registry.INSTANCE.getEFactory(TestqvtPackage.eNS_URI);
			if (theTestqvtFactory != null) {
				return theTestqvtFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TestqvtFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestqvtFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case TestqvtPackage.MODEL: return createModel();
			case TestqvtPackage.ELEMENT: return createElement();
			case TestqvtPackage.BOOLEAN_ELEMENT: return createBooleanElement();
			case TestqvtPackage.NUMBERS: return createNumbers();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Model createModel() {
		ModelImpl model = new ModelImpl();
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Element createElement() {
		ElementImpl element = new ElementImpl();
		return element;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BooleanElement createBooleanElement() {
		BooleanElementImpl booleanElement = new BooleanElementImpl();
		return booleanElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Numbers createNumbers() {
		NumbersImpl numbers = new NumbersImpl();
		return numbers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestqvtPackage getTestqvtPackage() {
		return (TestqvtPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TestqvtPackage getPackage() {
		return TestqvtPackage.eINSTANCE;
	}

} //TestqvtFactoryImpl
