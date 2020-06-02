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
package org.eclipse.m2m.internal.qvt.oml.expressions.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.ConstructorBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsFactory;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImportKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.Library;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport;
import org.eclipse.m2m.internal.qvt.oml.expressions.ObjectExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveInExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExpressionsFactoryImpl extends EFactoryImpl implements ExpressionsFactory {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ExpressionsFactory init() {
		try {
			ExpressionsFactory theExpressionsFactory = (ExpressionsFactory)EPackage.Registry.INSTANCE.getEFactory(ExpressionsPackage.eNS_URI);
			if (theExpressionsFactory != null) {
				return theExpressionsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ExpressionsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExpressionsFactoryImpl() {
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
			case ExpressionsPackage.CONSTRUCTOR: return createConstructor();
			case ExpressionsPackage.CONSTRUCTOR_BODY: return createConstructorBody();
			case ExpressionsPackage.CONTEXTUAL_PROPERTY: return createContextualProperty();
			case ExpressionsPackage.ENTRY_OPERATION: return createEntryOperation();
			case ExpressionsPackage.HELPER: return createHelper();
			case ExpressionsPackage.IMPERATIVE_CALL_EXP: return createImperativeCallExp();
			case ExpressionsPackage.IMPERATIVE_OPERATION: return createImperativeOperation();
			case ExpressionsPackage.LIBRARY: return createLibrary();
			case ExpressionsPackage.MAPPING_BODY: return createMappingBody();
			case ExpressionsPackage.MAPPING_CALL_EXP: return createMappingCallExp();
			case ExpressionsPackage.MAPPING_OPERATION: return createMappingOperation();
			case ExpressionsPackage.MAPPING_PARAMETER: return createMappingParameter();
			case ExpressionsPackage.MODEL_PARAMETER: return createModelParameter();
			case ExpressionsPackage.MODEL_TYPE: return createModelType();
			case ExpressionsPackage.MODULE: return createModule();
			case ExpressionsPackage.MODULE_IMPORT: return createModuleImport();
			case ExpressionsPackage.OBJECT_EXP: return createObjectExp();
			case ExpressionsPackage.OPERATION_BODY: return createOperationBody();
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION: return createOperationalTransformation();
			case ExpressionsPackage.RESOLVE_EXP: return createResolveExp();
			case ExpressionsPackage.RESOLVE_IN_EXP: return createResolveInExp();
			case ExpressionsPackage.VAR_PARAMETER: return createVarParameter();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ExpressionsPackage.DIRECTION_KIND:
				return createDirectionKindFromString(eDataType, initialValue);
			case ExpressionsPackage.IMPORT_KIND:
				return createImportKindFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ExpressionsPackage.DIRECTION_KIND:
				return convertDirectionKindToString(eDataType, instanceValue);
			case ExpressionsPackage.IMPORT_KIND:
				return convertImportKindToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Constructor createConstructor() {
		ConstructorImpl constructor = new ConstructorImpl();
		return constructor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public org.eclipse.m2m.internal.qvt.oml.expressions.Module createModule() {
		ModuleImpl module = new ModuleImpl();
		return module;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Library createLibrary() {
		LibraryImpl library = new LibraryImpl();
		return library;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModuleImport createModuleImport() {
		ModuleImportImpl moduleImport = new ModuleImportImpl();
		return moduleImport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContextualProperty createContextualProperty() {
		ContextualPropertyImpl contextualProperty = new ContextualPropertyImpl();
		return contextualProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImperativeCallExp createImperativeCallExp() {
		ImperativeCallExpImpl imperativeCallExp = new ImperativeCallExpImpl();
		return imperativeCallExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImperativeOperation createImperativeOperation() {
		ImperativeOperationImpl imperativeOperation = new ImperativeOperationImpl();
		return imperativeOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelParameter createModelParameter() {
		ModelParameterImpl modelParameter = new ModelParameterImpl();
		return modelParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VarParameter createVarParameter() {
		VarParameterImpl varParameter = new VarParameterImpl();
		return varParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationBody createOperationBody() {
		OperationBodyImpl operationBody = new OperationBodyImpl();
		return operationBody;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstructorBody createConstructorBody() {
		ConstructorBodyImpl constructorBody = new ConstructorBodyImpl();
		return constructorBody;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingOperation createMappingOperation() {
		MappingOperationImpl mappingOperation = new MappingOperationImpl();
		return mappingOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Helper createHelper() {
		HelperImpl helper = new HelperImpl();
		return helper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingBody createMappingBody() {
		MappingBodyImpl mappingBody = new MappingBodyImpl();
		return mappingBody;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingCallExp createMappingCallExp() {
		MappingCallExpImpl mappingCallExp = new MappingCallExpImpl();
		return mappingCallExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ObjectExp createObjectExp() {
		ObjectExpImpl objectExp = new ObjectExpImpl();
		return objectExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResolveExp createResolveExp() {
		ResolveExpImpl resolveExp = new ResolveExpImpl();
		return resolveExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResolveInExp createResolveInExp() {
		ResolveInExpImpl resolveInExp = new ResolveInExpImpl();
		return resolveInExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelType createModelType() {
		ModelTypeImpl modelType = new ModelTypeImpl();
		return modelType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingParameter createMappingParameter() {
		MappingParameterImpl mappingParameter = new MappingParameterImpl();
		return mappingParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationalTransformation createOperationalTransformation() {
		OperationalTransformationImpl operationalTransformation = new OperationalTransformationImpl();
		return operationalTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntryOperation createEntryOperation() {
		EntryOperationImpl entryOperation = new EntryOperationImpl();
		return entryOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImportKind createImportKindFromString(EDataType eDataType, String initialValue) {
		ImportKind result = ImportKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertImportKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DirectionKind createDirectionKindFromString(EDataType eDataType, String initialValue) {
		DirectionKind result = DirectionKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDirectionKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExpressionsPackage getExpressionsPackage() {
		return (ExpressionsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ExpressionsPackage getPackage() {
		return ExpressionsPackage.eINSTANCE;
	}

} //ExpressionsFactoryImpl
