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
package generics.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import generics.GenericCls;
import generics.GenericsFactory;
import generics.GenericsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GenericsPackageImpl extends EPackageImpl implements GenericsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass genericClsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType myNumberEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see generics.GenericsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private GenericsPackageImpl() {
		super(eNS_URI, GenericsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link GenericsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static GenericsPackage init() {
		if (isInited) return (GenericsPackage)EPackage.Registry.INSTANCE.getEPackage(GenericsPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredGenericsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		GenericsPackageImpl theGenericsPackage = registeredGenericsPackage instanceof GenericsPackageImpl ? (GenericsPackageImpl)registeredGenericsPackage : new GenericsPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theGenericsPackage.createPackageContents();

		// Initialize created meta-data
		theGenericsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theGenericsPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(GenericsPackage.eNS_URI, theGenericsPackage);
		return theGenericsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGenericCls() {
		return genericClsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGenericCls_Values() {
		return (EAttribute)genericClsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getMyNumber() {
		return myNumberEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenericsFactory getGenericsFactory() {
		return (GenericsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		genericClsEClass = createEClass(GENERIC_CLS);
		createEAttribute(genericClsEClass, GENERIC_CLS__VALUES);

		// Create data types
		myNumberEDataType = createEDataType(MY_NUMBER);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters
		ETypeParameter genericClsEClass_E = addETypeParameter(genericClsEClass, "E");

		// Set bounds for type parameters
		EGenericType g1 = createEGenericType(this.getMyNumber());
		genericClsEClass_E.getEBounds().add(g1);

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(genericClsEClass, GenericCls.class, "GenericCls", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(genericClsEClass_E);
		initEAttribute(getGenericCls_Values(), g1, "values", null, 0, -1, GenericCls.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(myNumberEDataType, Number.class, "MyNumber", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //GenericsPackageImpl
