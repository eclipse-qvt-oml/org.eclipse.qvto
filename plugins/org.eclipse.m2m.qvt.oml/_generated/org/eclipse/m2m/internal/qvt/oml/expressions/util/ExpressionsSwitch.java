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
package org.eclipse.m2m.internal.qvt.oml.expressions.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.util.Switch;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.ConstructorBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty;
import org.eclipse.m2m.internal.qvt.oml.expressions.EntryOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeCallExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
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
import org.eclipse.m2m.internal.qvt.oml.expressions.VisitableASTNode;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ImperativeExpression;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.InstantiationExp;
import org.eclipse.ocl.expressions.CallExp;
import org.eclipse.ocl.expressions.FeatureCallExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.utilities.ASTNode;
import org.eclipse.ocl.utilities.CallingASTNode;
import org.eclipse.ocl.utilities.TypedASTNode;
import org.eclipse.ocl.utilities.TypedElement;
import org.eclipse.ocl.utilities.Visitable;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage
 * @generated
 */
public class ExpressionsSwitch<T> extends Switch<T> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ExpressionsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExpressionsSwitch() {
		if (modelPackage == null) {
			modelPackage = ExpressionsPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case ExpressionsPackage.CONSTRUCTOR: {
				Constructor constructor = (Constructor)theEObject;
				T result = caseConstructor(constructor);
				if (result == null) result = caseImperativeOperation(constructor);
				if (result == null) result = caseEOperation(constructor);
				if (result == null) result = caseVisitableASTNode(constructor);
				if (result == null) result = caseETypedElement(constructor);
				if (result == null) result = caseVisitable(constructor);
				if (result == null) result = caseASTNode(constructor);
				if (result == null) result = caseENamedElement(constructor);
				if (result == null) result = caseEModelElement(constructor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.CONSTRUCTOR_BODY: {
				ConstructorBody constructorBody = (ConstructorBody)theEObject;
				T result = caseConstructorBody(constructorBody);
				if (result == null) result = caseOperationBody(constructorBody);
				if (result == null) result = caseVisitableASTNode(constructorBody);
				if (result == null) result = caseEModelElement(constructorBody);
				if (result == null) result = caseVisitable(constructorBody);
				if (result == null) result = caseASTNode(constructorBody);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.CONTEXTUAL_PROPERTY: {
				ContextualProperty contextualProperty = (ContextualProperty)theEObject;
				T result = caseContextualProperty(contextualProperty);
				if (result == null) result = caseEStructuralFeature(contextualProperty);
				if (result == null) result = caseVisitableASTNode(contextualProperty);
				if (result == null) result = caseETypedElement(contextualProperty);
				if (result == null) result = caseVisitable(contextualProperty);
				if (result == null) result = caseASTNode(contextualProperty);
				if (result == null) result = caseENamedElement(contextualProperty);
				if (result == null) result = caseEModelElement(contextualProperty);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.ENTRY_OPERATION: {
				EntryOperation entryOperation = (EntryOperation)theEObject;
				T result = caseEntryOperation(entryOperation);
				if (result == null) result = caseImperativeOperation(entryOperation);
				if (result == null) result = caseEOperation(entryOperation);
				if (result == null) result = caseVisitableASTNode(entryOperation);
				if (result == null) result = caseETypedElement(entryOperation);
				if (result == null) result = caseVisitable(entryOperation);
				if (result == null) result = caseASTNode(entryOperation);
				if (result == null) result = caseENamedElement(entryOperation);
				if (result == null) result = caseEModelElement(entryOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.HELPER: {
				Helper helper = (Helper)theEObject;
				T result = caseHelper(helper);
				if (result == null) result = caseImperativeOperation(helper);
				if (result == null) result = caseEOperation(helper);
				if (result == null) result = caseVisitableASTNode(helper);
				if (result == null) result = caseETypedElement(helper);
				if (result == null) result = caseVisitable(helper);
				if (result == null) result = caseASTNode(helper);
				if (result == null) result = caseENamedElement(helper);
				if (result == null) result = caseEModelElement(helper);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.IMPERATIVE_CALL_EXP: {
				ImperativeCallExp imperativeCallExp = (ImperativeCallExp)theEObject;
				T result = caseImperativeCallExp(imperativeCallExp);
				if (result == null) result = caseEcore_OperationCallExp(imperativeCallExp);
				if (result == null) result = caseImperativeExpression(imperativeCallExp);
				if (result == null) result = caseEcore_FeatureCallExp(imperativeCallExp);
				if (result == null) result = caseOperationCallExp(imperativeCallExp);
				if (result == null) result = caseEcore_CallExp(imperativeCallExp);
				if (result == null) result = caseFeatureCallExp(imperativeCallExp);
				if (result == null) result = caseEcore_OCLExpression(imperativeCallExp);
				if (result == null) result = caseCallExp(imperativeCallExp);
				if (result == null) result = caseETypedElement(imperativeCallExp);
				if (result == null) result = caseOCLExpression(imperativeCallExp);
				if (result == null) result = caseCallingASTNode(imperativeCallExp);
				if (result == null) result = caseENamedElement(imperativeCallExp);
				if (result == null) result = caseTypedElement(imperativeCallExp);
				if (result == null) result = caseVisitable(imperativeCallExp);
				if (result == null) result = caseASTNode(imperativeCallExp);
				if (result == null) result = caseEModelElement(imperativeCallExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.IMPERATIVE_OPERATION: {
				ImperativeOperation imperativeOperation = (ImperativeOperation)theEObject;
				T result = caseImperativeOperation(imperativeOperation);
				if (result == null) result = caseEOperation(imperativeOperation);
				if (result == null) result = caseVisitableASTNode(imperativeOperation);
				if (result == null) result = caseETypedElement(imperativeOperation);
				if (result == null) result = caseVisitable(imperativeOperation);
				if (result == null) result = caseASTNode(imperativeOperation);
				if (result == null) result = caseENamedElement(imperativeOperation);
				if (result == null) result = caseEModelElement(imperativeOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.LIBRARY: {
				Library library = (Library)theEObject;
				T result = caseLibrary(library);
				if (result == null) result = caseModule(library);
				if (result == null) result = caseEClass(library);
				if (result == null) result = caseEPackage(library);
				if (result == null) result = caseVisitableASTNode(library);
				if (result == null) result = caseEClassifier(library);
				if (result == null) result = caseVisitable(library);
				if (result == null) result = caseASTNode(library);
				if (result == null) result = caseENamedElement(library);
				if (result == null) result = caseEModelElement(library);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MAPPING_BODY: {
				MappingBody mappingBody = (MappingBody)theEObject;
				T result = caseMappingBody(mappingBody);
				if (result == null) result = caseOperationBody(mappingBody);
				if (result == null) result = caseVisitableASTNode(mappingBody);
				if (result == null) result = caseEModelElement(mappingBody);
				if (result == null) result = caseVisitable(mappingBody);
				if (result == null) result = caseASTNode(mappingBody);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MAPPING_CALL_EXP: {
				MappingCallExp mappingCallExp = (MappingCallExp)theEObject;
				T result = caseMappingCallExp(mappingCallExp);
				if (result == null) result = caseImperativeCallExp(mappingCallExp);
				if (result == null) result = caseEcore_OperationCallExp(mappingCallExp);
				if (result == null) result = caseImperativeExpression(mappingCallExp);
				if (result == null) result = caseEcore_FeatureCallExp(mappingCallExp);
				if (result == null) result = caseOperationCallExp(mappingCallExp);
				if (result == null) result = caseEcore_CallExp(mappingCallExp);
				if (result == null) result = caseFeatureCallExp(mappingCallExp);
				if (result == null) result = caseEcore_OCLExpression(mappingCallExp);
				if (result == null) result = caseCallExp(mappingCallExp);
				if (result == null) result = caseETypedElement(mappingCallExp);
				if (result == null) result = caseOCLExpression(mappingCallExp);
				if (result == null) result = caseCallingASTNode(mappingCallExp);
				if (result == null) result = caseENamedElement(mappingCallExp);
				if (result == null) result = caseTypedElement(mappingCallExp);
				if (result == null) result = caseVisitable(mappingCallExp);
				if (result == null) result = caseASTNode(mappingCallExp);
				if (result == null) result = caseEModelElement(mappingCallExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MAPPING_OPERATION: {
				MappingOperation mappingOperation = (MappingOperation)theEObject;
				T result = caseMappingOperation(mappingOperation);
				if (result == null) result = caseImperativeOperation(mappingOperation);
				if (result == null) result = caseEOperation(mappingOperation);
				if (result == null) result = caseVisitableASTNode(mappingOperation);
				if (result == null) result = caseETypedElement(mappingOperation);
				if (result == null) result = caseVisitable(mappingOperation);
				if (result == null) result = caseASTNode(mappingOperation);
				if (result == null) result = caseENamedElement(mappingOperation);
				if (result == null) result = caseEModelElement(mappingOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MAPPING_PARAMETER: {
				MappingParameter mappingParameter = (MappingParameter)theEObject;
				T result = caseMappingParameter(mappingParameter);
				if (result == null) result = caseVarParameter(mappingParameter);
				if (result == null) result = caseEcore_Variable(mappingParameter);
				if (result == null) result = caseEParameter(mappingParameter);
				if (result == null) result = caseETypedElement(mappingParameter);
				if (result == null) result = caseVariable(mappingParameter);
				if (result == null) result = caseENamedElement(mappingParameter);
				if (result == null) result = caseTypedElement(mappingParameter);
				if (result == null) result = caseVisitable(mappingParameter);
				if (result == null) result = caseTypedASTNode(mappingParameter);
				if (result == null) result = caseEModelElement(mappingParameter);
				if (result == null) result = caseASTNode(mappingParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MODEL_PARAMETER: {
				ModelParameter modelParameter = (ModelParameter)theEObject;
				T result = caseModelParameter(modelParameter);
				if (result == null) result = caseVarParameter(modelParameter);
				if (result == null) result = caseEcore_Variable(modelParameter);
				if (result == null) result = caseEParameter(modelParameter);
				if (result == null) result = caseETypedElement(modelParameter);
				if (result == null) result = caseVariable(modelParameter);
				if (result == null) result = caseENamedElement(modelParameter);
				if (result == null) result = caseTypedElement(modelParameter);
				if (result == null) result = caseVisitable(modelParameter);
				if (result == null) result = caseTypedASTNode(modelParameter);
				if (result == null) result = caseEModelElement(modelParameter);
				if (result == null) result = caseASTNode(modelParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MODEL_TYPE: {
				ModelType modelType = (ModelType)theEObject;
				T result = caseModelType(modelType);
				if (result == null) result = caseEClass(modelType);
				if (result == null) result = caseVisitableASTNode(modelType);
				if (result == null) result = caseEClassifier(modelType);
				if (result == null) result = caseVisitable(modelType);
				if (result == null) result = caseASTNode(modelType);
				if (result == null) result = caseENamedElement(modelType);
				if (result == null) result = caseEModelElement(modelType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MODULE: {
				org.eclipse.m2m.internal.qvt.oml.expressions.Module module = (org.eclipse.m2m.internal.qvt.oml.expressions.Module)theEObject;
				T result = caseModule(module);
				if (result == null) result = caseEClass(module);
				if (result == null) result = caseEPackage(module);
				if (result == null) result = caseVisitableASTNode(module);
				if (result == null) result = caseEClassifier(module);
				if (result == null) result = caseVisitable(module);
				if (result == null) result = caseASTNode(module);
				if (result == null) result = caseENamedElement(module);
				if (result == null) result = caseEModelElement(module);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.MODULE_IMPORT: {
				ModuleImport moduleImport = (ModuleImport)theEObject;
				T result = caseModuleImport(moduleImport);
				if (result == null) result = caseVisitableASTNode(moduleImport);
				if (result == null) result = caseEModelElement(moduleImport);
				if (result == null) result = caseVisitable(moduleImport);
				if (result == null) result = caseASTNode(moduleImport);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.OBJECT_EXP: {
				ObjectExp objectExp = (ObjectExp)theEObject;
				T result = caseObjectExp(objectExp);
				if (result == null) result = caseInstantiationExp(objectExp);
				if (result == null) result = caseImperativeExpression(objectExp);
				if (result == null) result = caseEcore_OCLExpression(objectExp);
				if (result == null) result = caseETypedElement(objectExp);
				if (result == null) result = caseOCLExpression(objectExp);
				if (result == null) result = caseENamedElement(objectExp);
				if (result == null) result = caseTypedElement(objectExp);
				if (result == null) result = caseVisitable(objectExp);
				if (result == null) result = caseASTNode(objectExp);
				if (result == null) result = caseEModelElement(objectExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.OPERATION_BODY: {
				OperationBody operationBody = (OperationBody)theEObject;
				T result = caseOperationBody(operationBody);
				if (result == null) result = caseVisitableASTNode(operationBody);
				if (result == null) result = caseEModelElement(operationBody);
				if (result == null) result = caseVisitable(operationBody);
				if (result == null) result = caseASTNode(operationBody);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION: {
				OperationalTransformation operationalTransformation = (OperationalTransformation)theEObject;
				T result = caseOperationalTransformation(operationalTransformation);
				if (result == null) result = caseModule(operationalTransformation);
				if (result == null) result = caseEClass(operationalTransformation);
				if (result == null) result = caseEPackage(operationalTransformation);
				if (result == null) result = caseVisitableASTNode(operationalTransformation);
				if (result == null) result = caseEClassifier(operationalTransformation);
				if (result == null) result = caseVisitable(operationalTransformation);
				if (result == null) result = caseASTNode(operationalTransformation);
				if (result == null) result = caseENamedElement(operationalTransformation);
				if (result == null) result = caseEModelElement(operationalTransformation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.RESOLVE_EXP: {
				ResolveExp resolveExp = (ResolveExp)theEObject;
				T result = caseResolveExp(resolveExp);
				if (result == null) result = caseEcore_CallExp(resolveExp);
				if (result == null) result = caseEcore_OCLExpression(resolveExp);
				if (result == null) result = caseCallExp(resolveExp);
				if (result == null) result = caseETypedElement(resolveExp);
				if (result == null) result = caseOCLExpression(resolveExp);
				if (result == null) result = caseCallingASTNode(resolveExp);
				if (result == null) result = caseENamedElement(resolveExp);
				if (result == null) result = caseTypedElement(resolveExp);
				if (result == null) result = caseVisitable(resolveExp);
				if (result == null) result = caseASTNode(resolveExp);
				if (result == null) result = caseEModelElement(resolveExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.RESOLVE_IN_EXP: {
				ResolveInExp resolveInExp = (ResolveInExp)theEObject;
				T result = caseResolveInExp(resolveInExp);
				if (result == null) result = caseResolveExp(resolveInExp);
				if (result == null) result = caseEcore_CallExp(resolveInExp);
				if (result == null) result = caseEcore_OCLExpression(resolveInExp);
				if (result == null) result = caseCallExp(resolveInExp);
				if (result == null) result = caseETypedElement(resolveInExp);
				if (result == null) result = caseOCLExpression(resolveInExp);
				if (result == null) result = caseCallingASTNode(resolveInExp);
				if (result == null) result = caseENamedElement(resolveInExp);
				if (result == null) result = caseTypedElement(resolveInExp);
				if (result == null) result = caseVisitable(resolveInExp);
				if (result == null) result = caseASTNode(resolveInExp);
				if (result == null) result = caseEModelElement(resolveInExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.VAR_PARAMETER: {
				VarParameter varParameter = (VarParameter)theEObject;
				T result = caseVarParameter(varParameter);
				if (result == null) result = caseEcore_Variable(varParameter);
				if (result == null) result = caseEParameter(varParameter);
				if (result == null) result = caseETypedElement(varParameter);
				if (result == null) result = caseVariable(varParameter);
				if (result == null) result = caseENamedElement(varParameter);
				if (result == null) result = caseTypedElement(varParameter);
				if (result == null) result = caseVisitable(varParameter);
				if (result == null) result = caseTypedASTNode(varParameter);
				if (result == null) result = caseEModelElement(varParameter);
				if (result == null) result = caseASTNode(varParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case ExpressionsPackage.VISITABLE_AST_NODE: {
				VisitableASTNode visitableASTNode = (VisitableASTNode)theEObject;
				T result = caseVisitableASTNode(visitableASTNode);
				if (result == null) result = caseVisitable(visitableASTNode);
				if (result == null) result = caseASTNode(visitableASTNode);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constructor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constructor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstructor(Constructor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Library</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Library</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLibrary(Library object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module Import</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module Import</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModuleImport(ModuleImport object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contextual Property</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contextual Property</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContextualProperty(ContextualProperty object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Imperative Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Imperative Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImperativeCallExp(ImperativeCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Imperative Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Imperative Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImperativeOperation(ImperativeOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelParameter(ModelParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Var Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Var Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVarParameter(VarParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Body</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Body</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationBody(OperationBody object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constructor Body</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constructor Body</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstructorBody(ConstructorBody object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingOperation(MappingOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Helper</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Helper</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHelper(Helper object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Body</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Body</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingBody(MappingBody object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingCallExp(MappingCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseObjectExp(ObjectExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Visitable AST Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Visitable AST Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVisitableASTNode(VisitableASTNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resolve Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resolve Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResolveExp(ResolveExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resolve In Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resolve In Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResolveInExp(ResolveInExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelType(ModelType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModule(org.eclipse.m2m.internal.qvt.oml.expressions.Module object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingParameter(MappingParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operational Transformation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operational Transformation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationalTransformation(OperationalTransformation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entry Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entry Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntryOperation(EntryOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Imperative Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Imperative Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImperativeExpression(ImperativeExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Instantiation Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Instantiation Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInstantiationExp(InstantiationExp object) {
		return null;
	}

																/**
	 * Returns the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEModelElement(EModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseENamedElement(ENamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEClassifier(EClassifier object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEClass(EClass object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPackage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPackage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEPackage(EPackage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Visitable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Visitable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVisitable(Visitable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>AST Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>AST Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseASTNode(ASTNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseETypedElement(ETypedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStructuralFeature(EStructuralFeature object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EOperation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EOperation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEOperation(EOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Typed Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Typed Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <C> T caseTypedElement(TypedElement<C> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Typed AST Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Typed AST Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTypedASTNode(TypedASTNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <C, PM> T caseVariable(Variable<C, PM> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Variable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Variable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEcore_Variable(org.eclipse.ocl.ecore.Variable object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EParameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EParameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEParameter(EParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>OCL Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>OCL Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <C> T caseOCLExpression(OCLExpression<C> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>OCL Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>OCL Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEcore_OCLExpression(org.eclipse.ocl.ecore.OCLExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Calling AST Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Calling AST Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCallingASTNode(CallingASTNode object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <C> T caseCallExp(CallExp<C> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEcore_CallExp(org.eclipse.ocl.ecore.CallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Feature Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Feature Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <C> T caseFeatureCallExp(FeatureCallExp<C> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Feature Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Feature Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEcore_FeatureCallExp(org.eclipse.ocl.ecore.FeatureCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public <C, O> T caseOperationCallExp(OperationCallExp<C, O> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEcore_OperationCallExp(org.eclipse.ocl.ecore.OperationCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //ExpressionsSwitch
