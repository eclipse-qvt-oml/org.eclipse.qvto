/*******************************************************************************
 * Copyright (c) 2008, 2020 Borland Software Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 302594, 310991, 289982, 391289, 425634, 427237,
 *     						   433585, 433919, 438038, 472482, 536601
 *     Alex Paperno - bugs 272869, 268636, 404647, 414363, 414363, 401521,
 *                         419299, 414619, 403440, 415024, 420970, 413391,
 *                         424584, 424869
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ast.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.Monitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.internal.qvt.oml.Messages;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.ast.binding.ASTBindingHelper;
import org.eclipse.m2m.internal.qvt.oml.ast.binding.ASTSyntheticNode;
import org.eclipse.m2m.internal.qvt.oml.ast.binding.ASTSyntheticNodeAccess;
import org.eclipse.m2m.internal.qvt.oml.ast.binding.IModuleSourceInfo;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QVTOEnvironment;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtEnvironmentBase;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalFileEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalStdLibrary;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxRegistry;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompilerMessages;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompilerUtils;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompilerOptions;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.cst.AssertExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.AssignStatementCS;
import org.eclipse.m2m.internal.qvt.oml.cst.BlockExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.BreakExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.CatchExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ClassifierDefCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ClassifierPropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ComputeExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ConfigPropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ConstructorCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ContextualPropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ContinueExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.DictLiteralExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.DictLiteralPartCS;
import org.eclipse.m2m.internal.qvt.oml.cst.DictionaryTypeCS;
import org.eclipse.m2m.internal.qvt.oml.cst.DirectionKindEnum;
import org.eclipse.m2m.internal.qvt.oml.cst.ExceptionDefCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ExpressionStatementCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ForExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ImperativeIterateExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ImperativeOperationCallExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ImportCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ImportKindEnum;
import org.eclipse.m2m.internal.qvt.oml.cst.InstantiationExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.IntermediateClassDefCS;
import org.eclipse.m2m.internal.qvt.oml.cst.LibraryCS;
import org.eclipse.m2m.internal.qvt.oml.cst.LibraryImportCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ListLiteralExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ListTypeCS;
import org.eclipse.m2m.internal.qvt.oml.cst.LocalPropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.LogExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingBodyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingCallExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingDeclarationCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingExtensionCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingExtensionKindCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingInitCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingMethodCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingModuleCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingQueryCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingRuleCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingSectionCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingSectionsCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ModelTypeCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ModulePropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ModuleRefCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ModuleUsageCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MultiplicityDefCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ObjectExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.OppositePropertyCS;
import org.eclipse.m2m.internal.qvt.oml.cst.PackageRefCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ParameterDeclarationCS;
import org.eclipse.m2m.internal.qvt.oml.cst.QualifierKindCS;
import org.eclipse.m2m.internal.qvt.oml.cst.RaiseExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.RenameCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ResolveExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ResolveInExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ReturnExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.ScopedNameCS;
import org.eclipse.m2m.internal.qvt.oml.cst.SwitchAltExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.SwitchExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.TagCS;
import org.eclipse.m2m.internal.qvt.oml.cst.TransformationHeaderCS;
import org.eclipse.m2m.internal.qvt.oml.cst.TryExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.TypeSpecCS;
import org.eclipse.m2m.internal.qvt.oml.cst.UnitCS;
import org.eclipse.m2m.internal.qvt.oml.cst.VariableInitializationCS;
import org.eclipse.m2m.internal.qvt.oml.cst.WhileExpCS;
import org.eclipse.m2m.internal.qvt.oml.cst.parser.AbstractQVTParser;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.mmregistry.MetamodelRegistry;
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
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport;
import org.eclipse.m2m.internal.qvt.oml.expressions.ObjectExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationBody;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.ResolveInExp;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.internal.qvt.oml.library.QvtResolveUtil;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandler;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandlerAdapter;
import org.eclipse.m2m.internal.qvt.oml.stdlib.QVTUMLReflection;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AltExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssertExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.AssignExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BlockExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.BreakExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.CatchExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ComputeExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ContinueExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.DictLiteralExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.DictLiteralPart;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.DictionaryType;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ForExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ImperativeIterateExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ImperativeOCLFactory;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.InstantiationExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ListType;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.LogExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.RaiseExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ReturnExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.SeverityKind;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.SwitchExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.TryExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.VariableInitExp;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.WhileExp;
import org.eclipse.ocl.AmbiguousLookupException;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.Environment.Internal;
import org.eclipse.ocl.LookupException;
import org.eclipse.ocl.SemanticException;
import org.eclipse.ocl.cst.CSTFactory;
import org.eclipse.ocl.cst.CSTNode;
import org.eclipse.ocl.cst.CallExpCS;
import org.eclipse.ocl.cst.CollectionLiteralPartCS;
import org.eclipse.ocl.cst.CollectionTypeCS;
import org.eclipse.ocl.cst.DotOrArrowEnum;
import org.eclipse.ocl.cst.FeatureCallExpCS;
import org.eclipse.ocl.cst.IfExpCS;
import org.eclipse.ocl.cst.IteratorExpCS;
import org.eclipse.ocl.cst.LiteralExpCS;
import org.eclipse.ocl.cst.OCLExpressionCS;
import org.eclipse.ocl.cst.OperationCallExpCS;
import org.eclipse.ocl.cst.PathNameCS;
import org.eclipse.ocl.cst.PrimitiveTypeCS;
import org.eclipse.ocl.cst.SimpleNameCS;
import org.eclipse.ocl.cst.SimpleTypeEnum;
import org.eclipse.ocl.cst.StringLiteralExpCS;
import org.eclipse.ocl.cst.TupleTypeCS;
import org.eclipse.ocl.cst.TypeCS;
import org.eclipse.ocl.cst.VariableCS;
import org.eclipse.ocl.cst.VariableExpCS;
import org.eclipse.ocl.ecore.CallExp;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreFactory;
import org.eclipse.ocl.ecore.SendSignalAction;
import org.eclipse.ocl.ecore.TupleType;
import org.eclipse.ocl.expressions.CollectionKind;
import org.eclipse.ocl.expressions.CollectionLiteralExp;
import org.eclipse.ocl.expressions.CollectionLiteralPart;
import org.eclipse.ocl.expressions.FeatureCallExp;
import org.eclipse.ocl.expressions.IfExp;
import org.eclipse.ocl.expressions.InvalidLiteralExp;
import org.eclipse.ocl.expressions.IteratorExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.OperationCallExp;
import org.eclipse.ocl.expressions.PropertyCallExp;
import org.eclipse.ocl.expressions.StringLiteralExp;
import org.eclipse.ocl.expressions.TypeExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.expressions.VariableExp;
import org.eclipse.ocl.parser.AbstractOCLAnalyzer;
import org.eclipse.ocl.parser.AbstractOCLParser;
import org.eclipse.ocl.parser.OCLLexer;
import org.eclipse.ocl.parser.OCLParser;
import org.eclipse.ocl.types.CollectionType;
import org.eclipse.ocl.types.OCLStandardLibrary;
import org.eclipse.ocl.types.OrderedSetType;
import org.eclipse.ocl.types.SequenceType;
import org.eclipse.ocl.types.TypeType;
import org.eclipse.ocl.util.OCLStandardLibraryUtil;
import org.eclipse.ocl.util.OCLUtil;
import org.eclipse.ocl.util.TypeUtil;
import org.eclipse.ocl.utilities.ASTNode;
import org.eclipse.ocl.utilities.PredefinedType;
import org.eclipse.ocl.utilities.TypedElement;
import org.eclipse.ocl.utilities.UMLReflection;


public class QvtOperationalVisitorCS
extends AbstractOCLAnalyzer<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject,
CallOperationAction, SendSignalAction, Constraint, EClass, EObject> { 	// FIXME - changed in M3.4 migration

	private final QvtCompilerOptions myCompilerOptions;
	private Set<TypedElement<?>> myErrorNodes;

	/* TODO -
	 * Groups all late resolve expression encountered during CST analysis for later validation.
	 * At the moment when resolve expression is visited it has not its container connect yet, which
	 * required for validation. Should be replaced by introducing a validation visitor.
	 */
	private List<ResolveExp> myLateResolveExps;

	private final IProgressMonitor myMonitor;

	public QvtOperationalVisitorCS(OCLLexer lexStream, QvtCompilerOptions options) {
		this(new OCLParser(lexStream), options);
	}

	public QvtOperationalVisitorCS(AbstractOCLParser parser, QvtCompilerOptions options) {
		this(parser, options, new NullProgressMonitor());
	}

	/* obsolete method re-instated to avoid GMF Tooling breakage */
	/* @deprecated use AbstractOCLParser */
	@Deprecated
	public QvtOperationalVisitorCS(AbstractQVTParser parser, QvtCompilerOptions options) {
		this((AbstractOCLParser)parser, options);
	}

	public QvtOperationalVisitorCS(AbstractOCLParser parser, QvtCompilerOptions options, IProgressMonitor monitor) {
		super(parser);
		myCompilerOptions = options;
		myMonitor = monitor;
	}

	/* obsolete method re-instated to avoid GMF Tooling breakage */
	/* @deprecated use AbstractOCLParser and IProgressMonitor */
	@Deprecated
	public QvtOperationalVisitorCS(AbstractQVTParser parser, QvtCompilerOptions options, Monitor monitor) {
		this(parser, options, BasicMonitor.toIProgressMonitor(monitor));
	}

	public QvtCompilerOptions getCompilerOptions() {
		return myCompilerOptions;
	}

	@Override
	protected void initStartEndPositions(ASTNode astNode, CSTNode cstNode) {
		// FIXME - temp workaround after OCL 1.2 migration
		if ((astNode != null) && (cstNode != null)) {
			astNode.setStartPosition(cstNode.getStartOffset());
			astNode.setEndPosition(cstNode.getEndOffset());
		}
	}

	protected InstantiationExp instantiationExpCS(InstantiationExpCS newCallExp,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		InstantiationExp instantiationExp = ImperativeOCLFactory.eINSTANCE.createInstantiationExp();
		initStartEndPositions(instantiationExp, newCallExp);
		newCallExp.setAst(instantiationExp);

		if (env instanceof QvtOperationalEnv) {
			TypeSpecPair typeSpecCS = visitTypeSpecCS(newCallExp.getTypeSpecCS(), DirectionKind.OUT, (QvtOperationalEnv) env);
			instantiationExp.setType(typeSpecCS.myType);
			instantiationExp.setExtent(typeSpecCS.myExtent);
		}
		else {
			EClassifier instantiatedClass = typeCS(newCallExp.getTypeSpecCS().getTypeCS(), env);
			instantiationExp.setType(instantiatedClass);
		}

		boolean isTransformationInstantiation = false;
		EClassifier type = instantiationExp.getType();
		if (type instanceof EClass) {
			instantiationExp.setInstantiatedClass((EClass) type);
			instantiationExp.setName(type.getName());
			isTransformationInstantiation = QvtOperationalStdLibrary.INSTANCE.getTransformationClass()
					.isSuperTypeOf(instantiationExp.getInstantiatedClass());
		}

		for (OCLExpressionCS nextArgCS : newCallExp.getArguments()) {
			OCLExpression<EClassifier> nextArgAST = oclExpressionCS(nextArgCS, env);
			if(nextArgAST != null) {
				instantiationExp.getArgument().add((org.eclipse.ocl.ecore.OCLExpression)nextArgAST);
			}
		}

		if (!isTransformationInstantiation && env instanceof QvtOperationalEnv && type != null) {
			QvtOperationalEnv qvtEnv = (QvtOperationalEnv) env;
			EOperation lookupOperation;
			try {
				lookupOperation = qvtEnv.tryLookupConstructorOperation(type, instantiationExp.getName(), instantiationExp.getArgument());
			} catch (LookupException e) {
				lookupOperation = (EOperation) handleOperationLookupException(env, newCallExp, e.getAmbiguousMatches());
			}

			if (lookupOperation instanceof Constructor) {
				ASTBindingHelper.setConstructorOperation(instantiationExp, (Constructor) lookupOperation);
			} else if(lookupOperation == null && QvtOperationalUtil.isInstantiable(type)) {
				if(newCallExp.getArguments().isEmpty()) {
					// we try to call a default constructor defined explicitly
					QvtOperationalUtil.reportWarning(env, ValidationMessages.DefaultConstructorNotDefinedImplicitUsed, newCallExp);
				} else {
					String errMessage = NLS.bind(ValidationMessages.UnresolvedConstructor, getFormatter().formatQualifiedName(type));
					QvtOperationalUtil.reportError(env, errMessage, newCallExp);
				}
			}
		}

		return instantiationExp;
	}

	@Override
	protected EClassifier tupleTypeCS(TupleTypeCS tupleTypeCS, Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		EClassifier type = null;
		try {
			type = super.tupleTypeCS(tupleTypeCS, env);
		} catch (Exception e) {
			// catch MDT OCL exception related to unresolved types used in Tuples, and report error
			String message = NLS.bind(ValidationMessages.UnknownClassifierType, QvtOperationalParserUtil.getStringRepresentation(tupleTypeCS), tupleTypeCS);
			QvtOperationalUtil.reportError(env, message, tupleTypeCS);
		}

		return type;
	}

	@Override
	protected EClassifier typeCS(TypeCS typeCS, Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		EClassifier type = super.typeCS(typeCS, env);
		if (type == null) {
			if (typeCS instanceof ListTypeCS) {
				type = listTypeCS((ListTypeCS) typeCS, env);
			} else if(typeCS instanceof DictionaryTypeCS) {
				type = dictionaryTypeCS((DictionaryTypeCS) typeCS, env);
			}
		}
		if(type == getOclVoid() && typeCS instanceof PrimitiveTypeCS == false) {
			if(typeCS instanceof PathNameCS) {
				// check whether Void synonym was used
				PathNameCS pathNameCS = (PathNameCS) typeCS;
				if(QvtOperationalStdLibrary.INSTANCE.lookupClassifier(QvtOperationalParserUtil.getSequenceOfNames(pathNameCS.getSimpleNames())) == getOclVoid()) {
					return type;
				}
			}
			// FIXME - workaround for migration to OCL 1.2 => non primitive type but
			// resolved as OclVoid in super impl. indicates actually a type unresolved by the fEnv.
			return null;
		}

		// MDT OCL does not check for nested type whether they are resolved
		// do it here if element type is null
		if(type instanceof CollectionType<?,?> && typeCS instanceof CollectionTypeCS) {
			CollectionType<?, ?> collectionType = (CollectionType<?, ?>)type;

			if(collectionType.getElementType() == null) {

				CollectionTypeCS collectionTypeCS = (CollectionTypeCS)typeCS;
				QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.UnknownClassifierType, new Object[] {
						QvtOperationalParserUtil.getStringRepresentation(collectionTypeCS.getTypeCS())}),
						collectionTypeCS.getTypeCS());
			}
		}

		if(type != null) {
			if(myCompilerOptions.isGenerateCompletionData()) {
				// bind Module type only, for now
				if(type instanceof Module) {
					ASTBindingHelper.createCST2ASTBindingUnidirectional(typeCS, type);
				}
			}
		}

		if (type instanceof Library
				&& !(typeCS.eContainer() instanceof ScopedNameCS)
				&& !(typeCS.eContainer() instanceof ResolveInExpCS)) {
			QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.QvtOperationalVisitorCS_cantUseLibraryAsType, QvtOperationalTypesUtil.getTypeFullName(type)),
					typeCS.getStartOffset(), typeCS.getEndOffset());
			return null;
		}

		return type;
	}

	private EClassifier visitTypeCS(TypeCS typeCS, DirectionKind directionKind,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		EClassifier type = typeCS(typeCS, env);
		if (type == null) {
			// FIXME - M3.4 not needed, already reported above in #typeCS(typeCS, fEnv);
			/*fEnv.reportError(NLS.bind(ValidationMessages.UnknownClassifierType, new Object[] {
					QvtOperationalParserUtil.getStringRepresentation(typeCS)}),
					typeCS); */
		}

		return type;
	}

	@Override
	protected EOperation lookupOperation(CSTNode cstNode,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			EClassifier owner, String name, List<? extends TypedElement<EClassifier>> args) {

		if ((cstNode instanceof SimpleNameCS) && (cstNode.eContainer() instanceof ImperativeOperationCallExpCS)) {
			ImperativeOperationCallExpCS imperativeOperationCallExpCS = (ImperativeOperationCallExpCS) cstNode.eContainer();
			if(imperativeOperationCallExpCS.getModule() != null && env instanceof QvtEnvironmentBase) {
				return qualifiedOperationLookup(imperativeOperationCallExpCS, env, owner, name, args);
			}
		}

		// overrides super implementation by handling ambiguities at WARN level
		try {
			@SuppressWarnings("unchecked")
			Environment.Lookup<?, EClassifier, EOperation, ?> lookup = OCLUtil.getAdapter(env, Environment.Lookup.class);
			EOperation operation = lookup.tryLookupOperation(owner, name, args);
			if (cstNode != null) {
				cstNode.setAst(operation);
			}

			return operation;
		} catch (LookupException e) {
			return (EOperation)handleOperationLookupException(env, cstNode, e.getAmbiguousMatches());
		}
	}

	private EOperation qualifiedOperationLookup(ImperativeOperationCallExpCS imperativeOperationCallExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			EClassifier owner, String name, List<? extends TypedElement<EClassifier>> args) {

		SimpleNameCS moduleQualifier = imperativeOperationCallExpCS.getModule();
		CSTNode cstNode = imperativeOperationCallExpCS.getSimpleNameCS();

		QvtEnvironmentBase qvtEnvironmentBase = (QvtEnvironmentBase) env;
		EOperation resolvedOper = null;
		String targetModuleName = (moduleQualifier == null) ? null : moduleQualifier.getValue();

		if(targetModuleName != null) {
			List<QvtEnvironmentBase> allExtendedEnvs;

			Module currentModule = qvtEnvironmentBase.getModuleContextType();
			if(currentModule != null && targetModuleName.equals(currentModule.getName())) {
				// the call is qualified by the calling module
				allExtendedEnvs = Collections.singletonList(qvtEnvironmentBase);
			} else {
				// lookup in all modules within the extends hierarchy
				allExtendedEnvs = new ArrayList<QvtEnvironmentBase>(qvtEnvironmentBase.getAllExtendedModules());

				for (QvtEnvironmentBase accessedEnv: qvtEnvironmentBase.getImportsByAccess()) {
					if(accessedEnv.getModuleContextType() instanceof Library) {
						allExtendedEnvs.add(accessedEnv);
					}
				}
			}

			for (QvtEnvironmentBase nextExtenedModuleEnv : allExtendedEnvs) {
				Module extendedModule = nextExtenedModuleEnv.getModuleContextType();
				if(extendedModule != null && targetModuleName.equals(extendedModule.getName())) {
					moduleQualifier.setAst(extendedModule);

					EClassifier actualOwner = (owner instanceof Module) ? extendedModule : owner;
					EOperation operation = null;
					try {
						operation = nextExtenedModuleEnv.tryLookupOperation(actualOwner, name, args);
						if(operation != null) {
							Module owningModule = QvtOperationalParserUtil.getOwningModule(operation);
							if(extendedModule == owningModule) {
								resolvedOper = operation;
							}
						}
					} catch (LookupException e) {
						for(Object nextMatch : e.getAmbiguousMatches()) {
							EOperation operMatch = (EOperation) nextMatch;
							// search for matches that are ambiguous only in the target module
							if(extendedModule == QvtOperationalParserUtil.getOwningModule(operMatch)) {
								if(operation != null) {
									// report ambiguous matches in the target module type
									ArrayList<EOperation> ambiguous = new ArrayList<EOperation>(2);
									Collections.addAll(ambiguous, operation, operMatch);
									resolvedOper = (EOperation)handleOperationLookupException(env, cstNode, ambiguous);
									break;
								}

								operation = operMatch;
							}
						}

						resolvedOper = operation;
					}

					if(resolvedOper != null) {
						// we have found what we needed
						break;
					}
				}
			}
		}

		cstNode.setAst(resolvedOper);
		return resolvedOper;
	}

	private Object handleOperationLookupException(
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			CSTNode problemNode, List<?> matches) {

		List<Object> filteredMatches = new ArrayList<Object>(matches.size());
		try {
			NEXT_MATCH:
				for (Object nextMatchObj : matches) {
					if(nextMatchObj instanceof EOperation) {
						EOperation operation = (EOperation) nextMatchObj;
						if (isGenericT2(operation.getEType(), env)) {
							continue NEXT_MATCH;
						}
						for (EParameter eParameter : operation.getEParameters()) {
							if (isGenericT2(eParameter.getEType(), env)) {
								continue NEXT_MATCH;
							}
						}
					}

					filteredMatches.add(nextMatchObj);
				}
		} catch (RuntimeException e) {
		}

		if (filteredMatches.isEmpty()) {
			return matches.isEmpty() ? null : matches.get(0);
		}
		if (filteredMatches.size() == 1) {
			return filteredMatches.get(0);
		}

		String message = NLS.bind(ValidationMessages.AmbiguousOperationReference, formatMatches(env, filteredMatches));
		getEnvironment().analyzerWarning(message, "lookupOperation", problemNode); //$NON-NLS-1$"
		return filteredMatches.get(0);
	}

	private String formatMatches(
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			List<?> filteredMatches) {
		StringBuffer buf = new StringBuffer();
		try {
			int i = 0;
			for (Object nextMatchObj : filteredMatches) {
				if(i++ > 0) {
					buf.append(", "); //$NON-NLS-1$
				}
				buf.append('\'');
				if(nextMatchObj instanceof EOperation) {
					buf.append(FormattingHelperImpl.INSTANCE.formatOperationSignature((EOperation) nextMatchObj, env.getUMLReflection()));
				} else {
					buf.append(FormattingHelperImpl.INSTANCE.formatQualifiedName(nextMatchObj));
				}
				buf.append('\'');
			}
		} catch (RuntimeException e) {
			// Remark : safety measure, added in 2.0 RC1
			buf.append("<null>"); //$NON-NLS-1$
		}
		return buf.toString();
	}

	@SuppressWarnings("unchecked")
	private boolean isGenericT2(EClassifier eType,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		if (eType instanceof CollectionType) {
			return isGenericT2(((CollectionType<EClassifier, EOperation>) eType).getElementType(), env);
		}
		return eType == env.getOCLStandardLibrary().getT2();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Variable<EClassifier, EParameter> lookupImplicitSourceForOperation(
			CSTNode cstNode,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			List<OCLExpression<EClassifier>> args, String operationName) {

		Variable<EClassifier, EParameter> source = null;
		try {
			source = super.lookupImplicitSourceForOperation(cstNode, env, args, operationName);
		}
		catch (RuntimeException e) {
			if (e.getCause() instanceof AmbiguousLookupException) {
				List<?> ambiguousMatches = ((AmbiguousLookupException) e.getCause()).getAmbiguousMatches();

				String message = NLS.bind(ValidationMessages.AmbiguousImplicitSourceReference, formatMatches(env, ambiguousMatches));
				((QvtEnvironmentBase) env).analyzerWarning(message, "lookupImplicitSource", ((OperationCallExpCS) cstNode).getSimpleNameCS()); //$NON-NLS-1$"

				source = ambiguousMatches.isEmpty() ? null : (Variable<EClassifier, EParameter>) ambiguousMatches.get(0);
			}
			else {
				throw e;
			}
		}

		if(source == null) {
			// FIXME - why is this done?, looks like a workaround for MDT OCL,
			// it's a legal contract for the lookupXXX operation to return null
			source = EcoreFactory.eINSTANCE.createVariable();
			initASTMapping(env, source, cstNode);
		}
		return source;
	}

	private EClassifier visitTypeCSInModelType(TypeSpecCS typeSpecCS, ModelType modelType,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		EClassifier type = typeCS(typeSpecCS.getTypeCS(), env);
		if (type == null) {
			// FIXME - M3.4 not needed, already reported above in #typeCS(typeCS, fEnv);
			/*fEnv.reportError(NLS.bind(ValidationMessages.UnknownClassifierType, new Object[] {
					QvtOperationalParserUtil.getStringRepresentation(typeSpecCS.getTypeCS())}),
					typeSpecCS.getTypeCS());*/
		}
		return type;
	}

	private static class TypeSpecPair {
		public EClassifier myType;
		public ModelParameter myExtent;
	}

	private ModelParameter lookupModelParameter(SimpleNameCS nameCS, DirectionKind directionKind, QvtOperationalEnv env) {
		QvtOperationalModuleEnv moduleEnv = getModuleContextEnv(env);
		ModelParameter modelParam = moduleEnv.lookupModelParameter(nameCS.getValue(), directionKind);
		if(modelParam == null) {
			env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_extentWrongName,
					new Object[] { nameCS.getValue(), moduleEnv.getAllExtentNames(directionKind) }), nameCS);
		}

		return modelParam;
	}

	private TypeSpecPair visitTypeSpecCS(TypeSpecCS typeSpecCS, DirectionKind directionKind,
			QvtOperationalEnv env)  {
		TypeSpecPair result = new TypeSpecPair();

		if (typeSpecCS.getSimpleNameCS() != null) {
			result.myExtent = lookupModelParameter(typeSpecCS.getSimpleNameCS(), directionKind, env);

			typeSpecCS.getSimpleNameCS().setAst(result.myExtent);

			if (result.myExtent != null && result.myExtent.getEType() instanceof ModelType) {
				result.myType = visitTypeCSInModelType(typeSpecCS, (ModelType) result.myExtent.getEType(), env);
				return result;
			}
		}

		result.myType = visitTypeCS(typeSpecCS.getTypeCS(), directionKind, env);

		typeSpecCS.setAst(result.myType);
		return result;
	}

	private String visitLiteralExpCS(StringLiteralExpCS stringLiteralExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env)
					throws SemanticException {
		// stringLiteralExpCS() is not called directly for AST-CST binding creation done in literalExpCS()
		OCLExpression<EClassifier> literalExp = literalExpCS(stringLiteralExpCS, env);
		if (literalExp instanceof StringLiteralExp<?>) {
			return ((StringLiteralExp<EClassifier>) literalExp).getStringSymbol();
		}
		return null;
	}

	@Override
	protected IfExp<EClassifier> ifExpCS(
			IfExpCS ifExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		// FIXME -
		IfExp<EClassifier> ifExp = super.ifExpCS(ifExpCS, env);
		return ifExp;
	}

	/**
	 * Produces AST Node from the given CST and performs validation on it.
	 */
	public OCLExpression<EClassifier> analyzeExpressionCS(OCLExpressionCS oclExpressionCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		OCLExpression<EClassifier> result = oclExpressionCS(oclExpressionCS, env);
		validate(env);
		return result;
	}

	@Override
	protected org.eclipse.ocl.ecore.OCLExpression oclExpressionCS(OCLExpressionCS oclExpressionCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature,
			EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction,
			Constraint, EClass, EObject> env) {

		if (myMonitor.isCanceled()) {
			CompilerUtils.throwOperationCanceled();
		}

		try {
			if (oclExpressionCS instanceof BlockExpCS) {
				return visitBlockExpCS((BlockExpCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof ComputeExpCS) {
				return visitComputeExpCS((ComputeExpCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof WhileExpCS) {
				return visitWhileExpCS((WhileExpCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof SwitchExpCS) {
				return visitSwitchExpCS((SwitchExpCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof RaiseExpCS) {
				return visitRaiseExpCS((RaiseExpCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof TryExpCS) {
				return visitTryExpCS((TryExpCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof ObjectExpCS) {
				return visitObjectExpCS((ObjectExpCS) oclExpressionCS, toQVTOperationalEnv(env), true);
			}
			if (oclExpressionCS instanceof AssignStatementCS) {
				return visitAssignStatementCS((AssignStatementCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof VariableInitializationCS) {
				return variableInitializationCS((VariableInitializationCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof ExpressionStatementCS) {
				return visitExpressionStatementCS((ExpressionStatementCS) oclExpressionCS, env);
			}
			if (oclExpressionCS instanceof ResolveInExpCS) {
				return visitResolveInExpCS((ResolveInExpCS) oclExpressionCS, toQVTOperationalEnv(env));
			}
			if (oclExpressionCS instanceof ResolveExpCS) {
				return visitResolveExpCS((ResolveExpCS) oclExpressionCS, toQVTOperationalEnv(env));
			}
			if(oclExpressionCS instanceof LogExpCS) {
				return logExp((LogExpCS) oclExpressionCS, env);
			}
			if(oclExpressionCS instanceof AssertExpCS) {
				return assertExp((AssertExpCS) oclExpressionCS, env);
			}

			if (oclExpressionCS instanceof ForExpCS) {
				return visitForExp((ForExpCS) oclExpressionCS, env);
			}

			if (oclExpressionCS instanceof ImperativeIterateExpCS) {
				return visitImperativeIterateExp((ImperativeIterateExpCS) oclExpressionCS, env);
			}

			if (oclExpressionCS instanceof ReturnExpCS) {
				return visitReturnExpCS((ReturnExpCS) oclExpressionCS, env);
			}

			if (oclExpressionCS instanceof BreakExpCS) {
				return visitBreakExpCS((BreakExpCS) oclExpressionCS, env);
			}

			if (oclExpressionCS instanceof ContinueExpCS) {
				return visitContinueExpCS((ContinueExpCS) oclExpressionCS, env);
			}

			if(oclExpressionCS instanceof InstantiationExpCS) {
				return instantiationExpCS((InstantiationExpCS)oclExpressionCS, env);
			}

			if (oclExpressionCS instanceof TypeCS) {
				EClassifier type = typeCS((TypeCS) oclExpressionCS, env);
				if (type == null) {
					QvtOperationalUtil.reportError(env, NLS.bind(
							ValidationMessages.UnknownClassifierType,
							QvtOperationalUtil.getStringRepresentation((TypeCS) oclExpressionCS)), oclExpressionCS);
				}
				else {
					TypeExp<EClassifier> typeExp = env.getOCLFactory().createTypeExp();
					typeExp.setReferredType(type);
					typeExp.setType(TypeUtil.resolveTypeType(env, type));
					return (org.eclipse.ocl.ecore.TypeExp)typeExp;
				}
			}
			else {
				org.eclipse.ocl.ecore.OCLExpression result = (org.eclipse.ocl.ecore.OCLExpression)super.oclExpressionCS(oclExpressionCS, env);

				if (oclExpressionCS instanceof OperationCallExpCS) {
					if (result instanceof OperationCallExp<?,?>) {
						@SuppressWarnings("unchecked")
						OperationCallExp<EClassifier, EOperation> resultOpCall = (OperationCallExp<EClassifier, EOperation>) result;
						validateOperationCall((OperationCallExpCS) oclExpressionCS, resultOpCall, env);
					}
					if (result instanceof IteratorExp<?,?>) {
						@SuppressWarnings("unchecked")
						IteratorExp<EClassifier, EOperation> resultIter = (IteratorExp<EClassifier, EOperation>) result;

						if(resultIter.getBody() instanceof OperationCallExp<?, ?>) {
							@SuppressWarnings("unchecked")
							OperationCallExp<EClassifier, EOperation> opCallBody = (OperationCallExp<EClassifier, EOperation>) resultIter.getBody();
							validateOperationCall((OperationCallExpCS) oclExpressionCS, opCallBody, env);
						}
					}
				}
				return result;
			}
		}
		catch (NullPointerException ex) {
			QvtPlugin.error(ex);
			QvtOperationalUtil.reportError(env, ValidationMessages.QvtOperationalVisitorCS_oclParseNPE, oclExpressionCS);
		}
		catch (OperationCanceledException ex) {
			throw ex;
		}
		catch (RuntimeException ex) {
			//QvtPlugin.log(ex);
			QvtOperationalUtil.reportError(env, ValidationMessages.QvtOperationalVisitorCS_oclParseNPE, oclExpressionCS);
		}
		return (org.eclipse.ocl.ecore.OCLExpression)createDummyInvalidLiteralExp(env, oclExpressionCS);
	}

	private org.eclipse.ocl.ecore.OCLExpression visitRaiseExpCS(
			RaiseExpCS raiseExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		RaiseExp raiseExp = ImperativeOCLFactory.eINSTANCE.createRaiseExp();
		initStartEndPositions(raiseExp, raiseExpCS);

		TypeCS excTypeCS = raiseExpCS.getException();
		if(excTypeCS != null) {
			EClassifier excType = visitTypeCS(excTypeCS, null, env);
			if (!EmfUtil.isAssignableFrom(QvtOperationalStdLibrary.INSTANCE.getExceptionClass(), excType)) {
				String typeName = QvtOperationalParserUtil.safeGetQualifiedName(toQVTOperationalEnv(env), excType);
				QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.CannotRaiseNonException, typeName), excTypeCS);
			}

			if (raiseExpCS.getArgument() != null) {
				org.eclipse.ocl.ecore.OCLExpression value = oclExpressionCS(raiseExpCS.getArgument(), env);
				raiseExp.setArgument(value);
				if (value.getEType() != env.getOCLStandardLibrary().getString()) {
					QvtOperationalUtil.reportError(env, ValidationMessages.StringTypeMessageExcArgumentError, raiseExpCS.getArgument());
				}
			}
			if (excType != null) {
				raiseExp.setException(excType);
			}
		}

		return raiseExp;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitTryExpCS(
			TryExpCS tryExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		Set<EClassifier> handledExceptions = new HashSet<EClassifier>(2);

		TryExp tryExp = ImperativeOCLFactory.eINSTANCE.createTryExp();
		initStartEndPositions(tryExp, tryExpCS);
		tryExp.getTryBody().add(oclExpressionCS(tryExpCS.getTryBody(), env));
		for (CatchExpCS catchExpCS : tryExpCS.getExceptClauses()) {
			tryExp.getExceptClause().add((CatchExp)visitCatchExpCS(catchExpCS, handledExceptions, env));
		}

		return tryExp;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitCatchExpCS(
			CatchExpCS catchExpCS,
			Set<EClassifier> handledExceptions,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		CatchExp catchExp = ImperativeOCLFactory.eINSTANCE.createCatchExp();
		initStartEndPositions(catchExp, catchExpCS);
		EClass rootExcType = QvtOperationalStdLibrary.INSTANCE.getExceptionClass();

		EClassifier catchVarType = null;
		for (TypeCS typeCS : catchExpCS.getExceptions()) {
			EClassifier excType = visitTypeCS(typeCS, null, env);
			if (!EmfUtil.isAssignableFrom(rootExcType, excType)) {
				String typeName = QvtOperationalParserUtil.safeGetQualifiedName(toQVTOperationalEnv(env), excType);
				QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.CannotCatchNonException, typeName), typeCS);
			}
			for (EClassifier handledExc : handledExceptions) {
				if (EmfUtil.isAssignableFrom(handledExc, excType)) {
					QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.UncatchableCatchBlock, excType.getName(), handledExc.getName()), typeCS);
				}
			}
			catchExp.getException().add(excType);
			handledExceptions.add(excType);
			if (catchVarType == null) { // use exact type
				catchVarType = excType;
			} else { // use compatible type
				catchVarType = rootExcType;
			}
		}

		if (catchExpCS.getExceptions().size() == 0) {
			handledExceptions.add(rootExcType);
		}

		SimpleNameCS nameCS = catchExpCS.getSimpleNameCS();
		if (nameCS != null) {
			Variable<EClassifier, EParameter> catchVar = genVariableDeclaration(nameCS, "visitCatchExp", env,//$NON-NLS-1$
					nameCS.getValue(), catchVarType, null, true, true, false);
			ASTBindingHelper.setCatchVariable(catchExp, (org.eclipse.ocl.ecore.Variable)catchVar);
		}

		catchExp.getBody().add(oclExpressionCS(catchExpCS.getBody(), env));

		if (nameCS != null) {
			env.deleteElement(nameCS.getValue());
		}

		return catchExp;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitContinueExpCS(
			ContinueExpCS continueExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		ContinueExp result = ImperativeOCLFactory.eINSTANCE.createContinueExp();
		initStartEndPositions(result, continueExpCS);

		return result;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitBreakExpCS(
			BreakExpCS breakExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		BreakExp result = ImperativeOCLFactory.eINSTANCE.createBreakExp();
		initStartEndPositions(result, breakExpCS);

		return result;
	}

	@Override
	protected OCLExpression<EClassifier> literalExpCS(LiteralExpCS literalExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		OCLExpression<EClassifier> literalExp;
		if (literalExpCS instanceof ListLiteralExpCS) {
			literalExp = listLiteralExpCS((ListLiteralExpCS) literalExpCS, env);
		} else if (literalExpCS instanceof DictLiteralExpCS) {
			literalExp = dictionaryLiteralExp((DictLiteralExpCS)literalExpCS, env);
		} else {
			literalExp = super.literalExpCS(literalExpCS, env);
		}
		// AST binding
		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(literalExpCS, literalExp);
		}
		//
		return literalExp;
	}

	protected AssertExp assertExp(AssertExpCS assertExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		AssertExp assertAST = ImperativeOCLFactory.eINSTANCE.createAssertExp();
		// make assert return null
		assertAST.setEType(getOCLEnvironment().getOCLStandardLibrary().getOclVoid());
		assertAST.setStartPosition(assertExpCS.getStartOffset());
		assertAST.setEndPosition(assertExpCS.getEndOffset());

		if(assertExpCS.getAssertion() != null) {
			assertAST.setAssertion(oclExpressionCS(assertExpCS.getAssertion(), env));
		}

		if(assertExpCS.getSeverity() != null) {
			String severityCSVal = assertExpCS.getSeverity().getValue();
			if(severityCSVal != null) {
				SeverityKind actualSeverity = SeverityKind.get(severityCSVal);
				if(actualSeverity != null) {
					assertAST.setSeverity(actualSeverity);
				} else {
					CSTNode errorNode = assertExpCS.getSeverity();
					QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.UknownSeverityKindError, severityCSVal), errorNode);
				}
			}
		}

		if(assertExpCS.getLog() != null) {
			assertAST.setLog(logExp(assertExpCS.getLog(), env));
		}

		validateAssertExp(assertAST, env);
		return assertAST;
	}

	private void validateAssertExp(AssertExp assertExp,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		EClassifier boolType = env.getOCLStandardLibrary().getBoolean();
		if(assertExp.getAssertion() == null || assertExp.getAssertion().getType() != boolType) {
			ASTNode errNode = assertExp.getAssertion() != null ? assertExp.getAssertion() : assertExp;
			QvtOperationalUtil.reportError(env, ValidationMessages.BooleanTypeAssertConditionError,
					errNode.getStartPosition(), errNode.getEndPosition());
		}

		LogExp logExp = assertExp.getLog();
		if(logExp != null) {
			validateLogExp(logExp, env);
		}
	}

	private void validateLogExp(LogExp logExp,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		EList<OCLExpression<EClassifier>> allArgs = logExp.getArgument();
		if(allArgs.isEmpty()) {
			QvtOperationalUtil.reportError(env, ValidationMessages.MissingMessageLogExpArgumentError, logExp.getStartPosition(), logExp.getEndPosition());
		}

		int pos = 1;
		for (OCLExpression<EClassifier> arg : allArgs) {
			switch (pos) {
			case 1:
				EClassifier stringType = env.getOCLStandardLibrary().getString();
				if(stringType != arg.getType()) {
					QvtOperationalUtil.reportError(env, ValidationMessages.StringTypeMessageLogArgumentError, arg.getStartPosition(), arg.getEndPosition());
				}
				break;
			case 2:
				// anything accepted here
				break;
			case 3:
				EClassifier intType = env.getOCLStandardLibrary().getInteger();
				if(intType != arg.getType()) {
					QvtOperationalUtil.reportError(env, ValidationMessages.LogLevelNumberArgumentError, arg.getStartPosition(), arg.getEndPosition());
				}

				break;

			default:
				QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.UnsupportedLogExpArgumentError, pos), arg.getStartPosition(), arg.getEndPosition());
			}
			pos++;
		}

		OCLExpression<EClassifier> condition = logExp.getCondition();
		if(condition != null && condition.getType() != env.getOCLStandardLibrary().getBoolean()) {
			QvtOperationalUtil.reportError(env, ValidationMessages.LogExpBooleanTypeConditionError,
					condition.getStartPosition(), condition.getEndPosition());
		}
	}

	protected LogExp logExp(LogExpCS logExpCS, Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		LogExp logAST = ImperativeOCLFactory.eINSTANCE.createLogExp();
		logAST.setStartPosition(logExpCS.getStartOffset());
		logAST.setEndPosition(logExpCS.getEndOffset());

		logAST.setName("log"); //$NON-NLS-1$
		for (OCLExpressionCS argCS : logExpCS.getArguments() ) {
			OCLExpression<EClassifier> arg = this.oclExpressionCS(argCS, env);
			if(arg != null) {
				logAST.getArgument().add(arg);
			}
		}

		if(logExpCS.getCondition() != null) {
			logAST.setCondition(oclExpressionCS(logExpCS.getCondition(), env));
		}

		validateLogExp(logAST, env);
		return logAST;
	}

	@Override
	protected Variable<EClassifier, EParameter> variableDeclarationCS(VariableCS variableDeclarationCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			boolean addToEnvironment) {
		Variable<EClassifier, EParameter> varDecl = super.variableDeclarationCS(variableDeclarationCS, env, addToEnvironment);
		if ((varDecl.getType() == null) && (varDecl.getInitExpression() != null)) {
			varDecl.setType(varDecl.getInitExpression().getType());
		}

		// AST binding
		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(variableDeclarationCS, varDecl);
		}
		//
		return varDecl;
	}

	@Override
	protected OCLExpression<EClassifier> propertyCallExpCS(CallExpCS propertyCallExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env)
	{
		//        if (propertyCallExpCS instanceof ErrorCallExpCS) {
		//            OCLExpressionCS sourceExpCS = propertyCallExpCS.getSource();
		//            if (sourceExpCS != null) {
		//                if (propertyCallExpCS.getAccessor() == DotOrArrowEnum.ARROW_LITERAL) {
		//                    getCollectionSourceExpression(sourceExpCS, env);
		//                } else {
		//                    oclExpressionCS(sourceExpCS, env);
		//                }
		//            }
		//            return null;
		//        }
		OCLExpression<EClassifier> result = super.propertyCallExpCS(propertyCallExpCS, env);
		if(result != null) {
			// AST binding
			if(myCompilerOptions.isGenerateCompletionData()) {
				createPropertyCallASTBinding(propertyCallExpCS, result, env);
			}
		}
		return result;
	}

	@Override
	protected OCLExpression<EClassifier> simpleNameCS(
			SimpleNameCS simpleNameCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			OCLExpression<EClassifier> source) {

		OCLExpression<EClassifier> result = customSimpleNameCS(simpleNameCS, env, source);

		// AST binding
		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(simpleNameCS, result);
		}
		//

		return result;
	}

	private OperationCallExp<EClassifier, EOperation> genNonContextualQualifiedOperationCall(
			OperationCallExpCS operationCallExpCS, PathNameCS sourceCS, QvtEnvironmentBase env) {

		EClassifier sourceType = lookupClassifier(sourceCS, env, QvtOperationalParserUtil.getSequenceOfNames(sourceCS.getSimpleNames()));
		sourceCS.setAst(sourceType);

		if (sourceType instanceof Module) {
			Module sourceModule = (Module) sourceType;
			Variable<EClassifier, EParameter> sourceVar = QvtOperationalParserUtil.getThisVariable(sourceModule);
			if(sourceVar == null) {
				return null;
			}

			OCLExpression<EClassifier> sourceExpr = createVariableExp(env, sourceCS, sourceVar);
			EList<OCLExpressionCS> argsCS = operationCallExpCS.getArguments();
			List<OCLExpression<EClassifier>> args = new java.util.ArrayList<OCLExpression<EClassifier>>(argsCS.size());
			for (OCLExpressionCS arg : argsCS) {
				OCLExpression<EClassifier> argExpr = oclExpressionCS(arg, env);
				if (argExpr == null) {
					argExpr = createDummyInvalidLiteralExp(env, arg);
					initASTMapping(env, argExpr, arg);
				}
				args.add(argExpr);
			}

			String rule = "genNonContextualQualifiedOperationCall"; //$NON-NLS-1$
			String name = operationCallExpCS.getSimpleNameCS().getValue();
			OperationCallExp<EClassifier, EOperation> result = genOperationCallExp(env, operationCallExpCS, rule, name, sourceExpr, sourceModule, args);

			if(result.getReferredOperation() != null) {
				boolean isValidModule = sourceModule instanceof Library ||
						env.getModuleContextType() == sourceModule ||
						QvtOperationalParserUtil.isExtendingEnv(env, sourceModule);
				if(!isValidModule) {
					String errMessage = NLS.bind(ValidationMessages.NoImplicitSourceForQualifiedCall, name);
					ERROR(operationCallExpCS.getSimpleNameCS(), rule, errMessage);
				}
			}

			if(result instanceof ImperativeCallExp) {
				((ImperativeCallExp) result).setIsVirtual(false);
			}
			return result;
		}

		return null;
	}


	protected boolean isBlockNode(EObject node)
	{
		if (node instanceof ObjectExpCS
				|| node instanceof MappingSectionCS
				|| node instanceof MappingQueryCS
				|| node instanceof ConstructorCS
				|| node instanceof BlockExpCS) {
			return true;
		}
		return false;
	}


	protected boolean isPossibleEqualityInsteadOfAssignment(OperationCallExpCS operationCallExpCS) {
		if (!"=".equals(operationCallExpCS.getSimpleNameCS().getValue())) { //$NON-NLS-1$
			return false;
		}

		EObject container = operationCallExpCS.eContainer();
		if (isBlockNode(container)) {
			if (container instanceof BlockExpCS && container.eContainer() instanceof MappingQueryCS) {
				container = container.eContainer();
			}
			if (!(container instanceof MappingQueryCS)) {
				return true;
			}
			// it's ok to have a '=' equality in the last expression of a query
			EList<OCLExpressionCS> exprList = ((MappingQueryCS)container).getBody().getBodyExpressions();
			return (exprList.get(exprList.size()-1) != operationCallExpCS);
		}
		if (container instanceof MappingRuleCS) {
			return false;
		}
		if (container instanceof IfExpCS) {
			IfExpCS containerIf = (IfExpCS) container;
			if (containerIf.getThenExpression() == operationCallExpCS
					|| containerIf.getElseExpression() == operationCallExpCS) {
				if (isBlockNode(containerIf.eContainer())) {
					return true;
				}
			}
		}
		if (container instanceof SwitchAltExpCS) {
			SwitchAltExpCS containerSwitchAlt = (SwitchAltExpCS) container;
			if (containerSwitchAlt.getBody() == operationCallExpCS) {
				if (isBlockNode(containerSwitchAlt.eContainer().eContainer())) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	protected OCLExpression<EClassifier> operationCallExpCS(
			OperationCallExpCS operationCallExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		if (operationCallExpCS.getSource() instanceof PathNameCS) {
			OCLExpressionCS sourceCS = operationCallExpCS.getSource();
			if (sourceCS instanceof PathNameCS && env instanceof QvtEnvironmentBase) {
				OperationCallExp<EClassifier, EOperation> result = genNonContextualQualifiedOperationCall(operationCallExpCS, (PathNameCS)sourceCS,(QvtEnvironmentBase) env);
				if(result != null) {
					return result;
				}
			}
		}

		if (isPossibleEqualityInsteadOfAssignment(operationCallExpCS)) {
			QvtOperationalUtil.reportWarning(env, NLS.bind(ValidationMessages.QvtOperationalVisitorCS_possibleEqualityInsteadOfAssignment , new Object[] { }),
					operationCallExpCS.getSimpleNameCS());
		}

		OCLExpression<EClassifier> result = super.operationCallExpCS(operationCallExpCS, env);

		if(result instanceof OperationCallExp<?,?>) {
			org.eclipse.ocl.ecore.OperationCallExp opCallExp = (org.eclipse.ocl.ecore.OperationCallExp) result;
			if(opCallExp.getReferredOperation() != null) {
				EOperation referredOperation = opCallExp.getReferredOperation();
				if(QvtOperationalParserUtil.isDeprecated(referredOperation)) {
					String message;
					String deprecatedByRef = QvtOperationalParserUtil.getDeprecatedBy(referredOperation);

					if(deprecatedByRef != null) {
						message = NLS.bind(ValidationMessages.DeprecatedElementBy,
								getFormatter().formatName(referredOperation), deprecatedByRef);
					} else {
						message = NLS.bind(ValidationMessages.DeprecatedElement, getFormatter().formatName(referredOperation));
					}

					QvtOperationalUtil.reportWarning(env, message, operationCallExpCS);
				}

				if(QvtOperationalParserUtil.isUnsupported(referredOperation)) {
					String msg = QvtOperationalParserUtil.getUnsupportedReason(referredOperation);
					if (msg == null) {
						msg = ValidationMessages.UnsupportedElement;
					}
					QvtOperationalUtil.reportError(env, NLS.bind(msg, getFormatter().formatName(referredOperation)), operationCallExpCS);
				}
			}

			OCLExpression<EClassifier> source = opCallExp.getSource();
			if (source != null && opCallExp.getReferredOperation() != null) {
				if (isArrowAccessToCollection(operationCallExpCS, source)) {
					// FIXME - use OCL TypeChecker::isStdLibOperation()
					// Is it a standard operation like "select"?
					// In other words, did lookupOperation() in genOperationCallExp() return something?
					Set<EOperation> operations = new HashSet<EOperation>(TypeUtil.getOperations(env, source.getType()));
					if (!operations.contains(opCallExp.getReferredOperation())) {
						result = createImplicitXCollect(source, opCallExp, env, operationCallExpCS);
					}
				}
			}
			DeprecatedImplicitSourceCallHelper.validateCallExp(operationCallExpCS, opCallExp, env);
		}

		return result;
	}

	@Override
	protected OperationCallExp<EClassifier, EOperation> genOperationCallExp(
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			OperationCallExpCS operationCallExpCS, String rule,
			String operName, OCLExpression<EClassifier> source,
			EClassifier ownerType, List<OCLExpression<EClassifier>> args) {

		EClassifier operationSourceType = ownerType;
		if (isArrowAccessToCollection(operationCallExpCS, source)
				&& (lookupOperation(operationCallExpCS, env, ownerType, operName, args) == null)) {
			@SuppressWarnings("unchecked")
			CollectionType<EClassifier, EOperation> sourceType = (CollectionType<EClassifier, EOperation>) ownerType;
			operationSourceType = sourceType.getElementType();
		}

		OperationCallExp<EClassifier, EOperation> result;

		// Performs method signature checking
		EOperation oper = lookupOperation(operationCallExpCS.getSimpleNameCS(), env,
				operationSourceType, operName, args);

		if(oper instanceof MappingOperation) {
			result = ExpressionsFactory.eINSTANCE.createMappingCallExp();
		} else if(oper instanceof ImperativeOperation) {
			result = ExpressionsFactory.eINSTANCE.createImperativeCallExp();
		} else {
			result = oclFactory.createOperationCallExp();
		}

		initStartEndPositions(result, operationCallExpCS);
		initASTMapping(env, result, operationCallExpCS);
		result.setSource(source);


		// sometimes we use the resolved name in case the environment's look-up
		// supports aliasing
		String resolvedName = operName;

		if (oper == null) {
			String message;
			String operationString = operationString(env, operName, args);
			String operationSourceTypeString = uml.getName(operationSourceType);
			EClassifier srcType = source == null ? null : source.getType();
			if (srcType == operationSourceType) {
				message = NLS.bind(
						ValidationMessages.QvtOperationalVisitorCS_OperationNotFound_ERROR_,
						operationString,
						operationSourceTypeString);
			} else {
				message = NLS.bind(
						ValidationMessages.QvtOperationalVisitorCS_OperationNotFoundForCollectionAndItsElement,
						new Object[] {operationString, uml.getName(srcType), operationSourceTypeString});
			}
			ERROR(operationCallExpCS, rule, message);
			result.setType(env.getOCLStandardLibrary().getOclVoid());
		} else {
			resolvedName = uml.getName(oper);
			TRACE(rule, resolvedName);
			result.setReferredOperation(oper);
		}

		// Set up arguments
		List<OCLExpression<EClassifier>> callargs = result.getArgument();
		if (args != null) {
			for (OCLExpression<EClassifier> arg : args) {
				if (arg == null) {
					ERROR(operationCallExpCS, rule, ValidationMessages.QvtOperationalVisitorCS_BadArg_ERROR_);
				} else {
					callargs.add(arg);
				}
			}
		}

		// Compute the result type, and perform conformance checking.
		if (oper != null) {
			EClassifier resultType = null;

			int opcode = 0;
			if (TypeUtil.isStandardLibraryFeature(env, env.getUMLReflection().getOCLType(operationSourceType), oper)) {
				// the operations defined intrinsically by the standard library
				// are the only ones that may have opcodes
				opcode = OCLStandardLibraryUtil.getOperationCode(resolvedName);
			} else if (TypeUtil.isOclAnyOperation(env, oper)) {
				// source is a user class, enumeration, or data type and the
				// operation is defined by OclAny, not the source type
				opcode = OCLStandardLibraryUtil
						.getOclAnyOperationCode(resolvedName);
			}

			result.setOperationCode(opcode);
			resultType = TypeUtil.getResultType(operationCallExpCS, env,
					operationSourceType, oper, args);
			if (resultType == null) {
				resultType = getOCLType(env, oper);
			}

			// resolve collection or tuple type against the cache in the
			// environment
			resultType = TypeUtil.resolveType(env, resultType);

			result.setType(resultType);
		}

		// setup the virtual call flag
		if(result instanceof ImperativeCallExp) {
			ImperativeCallExp imperativeCall = (ImperativeCallExp) result;
			imperativeCall.setIsVirtual(true);
			// qualified call on a source object
			if(operationCallExpCS instanceof ImperativeOperationCallExpCS ) {
				ImperativeOperationCallExpCS imperativeCallCS = (ImperativeOperationCallExpCS) operationCallExpCS;
				if(imperativeCallCS.getModule() != null) {
					imperativeCall.setIsVirtual(false);
				}
			} else if(operationCallExpCS.getSource() instanceof PathNameCS) {
				// qualified call on by a module type (aka static call)
				imperativeCall.setIsVirtual(false);
			}
		}

		if (result instanceof MappingCallExp && operationCallExpCS instanceof MappingCallExpCS) {
			MappingCallExp mappingCall = (MappingCallExp) result;
			mappingCall.setIsStrict(((MappingCallExpCS) operationCallExpCS).isStrict());
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected IteratorExp<EClassifier, EParameter> iteratorExpCS(
			IteratorExpCS iteratorExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		IteratorExp<EClassifier, EParameter> iterExp = super.iteratorExpCS(iteratorExpCS, env);
		if (iterExp == null) {
			return iterExp;
		}
		OCLExpression<EClassifier> source = iterExp.getSource();
		if (source == null) {
			return iterExp;
		}

		String name = iterExp.getName();
		if (PredefinedType.CLOSURE_NAME.equals(name)) {
			if (source.getType() instanceof SequenceType<?, ?> || source.getType() instanceof OrderedSetType<?, ?>) {
				CollectionType<EClassifier, EOperation> ct = (CollectionType<EClassifier, EOperation>) source.getType();
				iterExp.setType(getOrderedSetType(iteratorExpCS.getBody(), env, ct.getElementType()));
			}
		}

		if (false == source.getType() instanceof ListType || false == iterExp.getType() instanceof CollectionType<?, ?>) {
			return iterExp;
		}

		EClassifier elementType = ((CollectionType<EClassifier, EOperation>) iterExp.getType()).getElementType();

		if (PredefinedType.SELECT_NAME.equals(name) || PredefinedType.REJECT_NAME.equals(name)) {
			iterExp.setType(resolveCollectionType(env, CollectionKind.SEQUENCE_LITERAL, elementType));
		} else if (PredefinedType.COLLECT_NAME.equals(name)) {
			iterExp.setType(resolveCollectionType(env, CollectionKind.SEQUENCE_LITERAL, elementType));
		} else if (PredefinedType.COLLECT_NESTED_NAME.equals(name)) {
			iterExp.setType(resolveCollectionType(env, CollectionKind.SEQUENCE_LITERAL, elementType));
		} else if (PredefinedType.SORTED_BY_NAME.equals(name)) {
			iterExp.setType(resolveCollectionType(env, CollectionKind.SEQUENCE_LITERAL, elementType));
		} else if (PredefinedType.CLOSURE_NAME.equals(name)) {
			iterExp.setType(resolveCollectionType(env, CollectionKind.ORDERED_SET_LITERAL, elementType));
		}

		return iterExp;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected IteratorExp<EClassifier, EParameter> createImplicitCollect(
			OCLExpression<EClassifier> source,
			FeatureCallExp<EClassifier> propertyCall,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			CSTNode cstNode) {
		IteratorExp<EClassifier, EParameter> implicitCollect = super.createImplicitCollect(source, propertyCall, env, cstNode);
		if (source.getType() instanceof ListType) {
			EClassifier elementType = ((CollectionType<EClassifier, EOperation>) implicitCollect.getType()).getElementType();
			implicitCollect.setType(resolveCollectionType(env, CollectionKind.SEQUENCE_LITERAL, elementType));
		}
		return implicitCollect;
	}

	/**
	 * Creates an implicit <code>xcollect</code> iterator expression for a
	 * property call on a collection-type source expression.
	 *
	 * @param source the property call source expression
	 * @param propertyCall the property call expression
	 * @param fEnv the current environment
	 *
	 * @return the xcollect expression
	 * @throws TerminateException
	 */
	protected ImperativeIterateExp createImplicitXCollect(
			OCLExpression<EClassifier> source,
			CallExp propertyCall,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			CSTNode cstNode) {

		@SuppressWarnings("unchecked")
		EClassifier sourceElementType = ((CollectionType<EClassifier, EOperation>) source.getType())
		.getElementType();

		ImperativeIterateExp result = ImperativeOCLFactory.eINSTANCE.createImperativeIterateExp();
		initASTMapping(env, result, cstNode);
		Variable<EClassifier, EParameter> itervar =
				genVariableDeclaration(cstNode, "modelPropertyCallCS", env,//$NON-NLS-1$
						null, sourceElementType, null, false, true, false);

		List<Variable<EClassifier, EParameter>> iters = result.getIterator();
		iters.add(itervar);
		result.setBody(propertyCall);
		result.setName("xcollect");//$NON-NLS-1$
		VariableExp<EClassifier, EParameter> vexp = oclFactory.createVariableExp();
		initASTMapping(env, vexp, cstNode);
		vexp.setType(itervar.getType());
		vexp.setReferredVariable(itervar);
		vexp.setName(itervar.getName());

		/* adjust the source variable for the body expression to be the
           newly generated implicit iterator variable */
		propertyCall.setSource(vexp);

		if (!(propertyCall instanceof OperationCallExp<?,?>)) {
			// the overall start and end positions are the property positions
			propertyCall.setStartPosition(propertyCall.getPropertyStartPosition());
			propertyCall.setEndPosition(propertyCall.getPropertyEndPosition());
		}

		result.setSource(source);

		EClassifier bodyType = propertyCall.getType();

		if (source.getType() instanceof SequenceType<?, ?>
		|| source.getType() instanceof OrderedSetType<?, ?>
		|| source.getType() instanceof ListType) {
			EClassifier c = resolveCollectionType(
					env,
					CollectionKind.SEQUENCE_LITERAL,
					bodyType);
			result.setType(c);
		} else {
			EClassifier c = resolveCollectionType(
					env,
					CollectionKind.BAG_LITERAL,
					bodyType);
			result.setType(c);
		}

		env.deleteElement(itervar.getName());

		return result;
	}

	private boolean isArrowAccessToCollection(CallExpCS callExpCS, OCLExpression<EClassifier> source) {
		if (source == null) {
			return false;
		}
		return (callExpCS.getAccessor().getValue() == DotOrArrowEnum.ARROW)
				&& (source.getType() instanceof CollectionType<?, ?>);
	}

	@Override
	protected OCLExpression<EClassifier> variableExpCS(
			VariableExpCS variableExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		OCLExpression<EClassifier> result = super.variableExpCS(variableExpCS, env);

		if(result instanceof PropertyCallExp<?, ?>) {
			@SuppressWarnings("unchecked")
			PropertyCallExp<EClassifier, ?> propCallExp = (PropertyCallExp<EClassifier, ?>)result;
			DeprecatedImplicitSourceCallHelper.validateCallExp(variableExpCS, propCallExp, env);
		}

		// AST binding
		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(variableExpCS, result);
		}
		//
		return result;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitOclExpressionCS(OCLExpressionCS expressionCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		org.eclipse.ocl.ecore.OCLExpression result = oclExpressionCS(expressionCS, env);
		if (expressionCS instanceof MappingCallExpCS) {
			if (result instanceof OperationCallExp<?, ?>) {
				// FIXME - review this, seems to be useless as we create callexp according to referred operation
				if(result instanceof MappingCallExp) {
					// keep 'virtual' attribute value
					return result;
				}
				MappingCallExp mappingCallExp = createMappingCallExp((MappingCallExpCS) expressionCS, result);
				if (mappingCallExp != null) {
					return mappingCallExp;
				}
			} else if (result instanceof IteratorExp<?, ?>) {
				@SuppressWarnings("unchecked")
				IteratorExp<EClassifier, EParameter> iteratorExp = (IteratorExp<EClassifier, EParameter>) result;
				MappingCallExp mappingCallExp = createMappingCallExp((MappingCallExpCS) expressionCS, iteratorExp.getBody());
				if (mappingCallExp != null) {
					iteratorExp.setBody(mappingCallExp);
					return (org.eclipse.ocl.ecore.OCLExpression)iteratorExp;
				}
			} else if (result instanceof ImperativeIterateExp) {
				ImperativeIterateExp imperativeIterateExp = (ImperativeIterateExp) result;
				MappingCallExp mappingCallExp = createMappingCallExp((MappingCallExpCS) expressionCS, imperativeIterateExp.getBody());
				if (mappingCallExp != null) {
					imperativeIterateExp.setBody(mappingCallExp);
					return imperativeIterateExp;
				}
			}
			// FIXME why this?
			// should be processed more specifically, also unresolved operation error for a mapping call
			// is sufficient, so we do not need yet another complaint
			QvtOperationalUtil.reportError(env, ValidationMessages.mappingOperationExpected, expressionCS);
			return null;
		}

		return result;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitBlockExpCS(BlockExpCS expressionCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		Collection<Variable<EClassifier, EParameter>> beginScopeVars = null;
		if(expressionCS.eContainer() instanceof ImperativeOperation == false) {
			// variables defined in the scope of operation handled by environment scope
			beginScopeVars = env.getVariables();
		}

		try {
			return doVisitBlockExpCS(expressionCS, env);
		}
		finally {
			if(beginScopeVars != null) {
				// remove variables of this scope when leaving it, only successfully added variables into fEnv
				// in this block scope will be removed, so we can not remove outer scope
				// existing variables.
				// Note: nested block scopes have done their clean-up already so we remove
				// only our stuff
				Collection<Variable<EClassifier, EParameter>> endScopeVars = env.getVariables();
				for (Variable<EClassifier, EParameter> nextVar : endScopeVars) {
					// remove those new in the scope
					if(!beginScopeVars.contains(nextVar)) {
						env.deleteElement(nextVar.getName());
					}
				}
			}
		}
	}

	private org.eclipse.ocl.ecore.OCLExpression doVisitBlockExpCS(BlockExpCS expressionCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		BlockExp result = ImperativeOCLFactory.eINSTANCE.createBlockExp();
		result.setStartPosition(expressionCS.getStartOffset());
		result.setEndPosition(expressionCS.getEndOffset());
		result.setType(env.getOCLStandardLibrary().getOclVoid());

		for (OCLExpressionCS oclExpCS : expressionCS.getBodyExpressions()) {
			OCLExpression<EClassifier> bodyExp = visitOclExpressionCS(oclExpCS, env);
			if (bodyExp != null) {
				result.getBody().add((org.eclipse.ocl.ecore.OCLExpression)bodyExp);
			}
		}

		return result;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitComputeExpCS(ComputeExpCS computeExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		ComputeExp result = ImperativeOCLFactory.eINSTANCE.createComputeExp();
		result.setStartPosition(computeExpCS.getStartOffset());
		result.setEndPosition(computeExpCS.getEndOffset());

		Variable<EClassifier, EParameter> returnedElementExp = variableDeclarationCS(computeExpCS.getReturnedElement(), env, true);
		result.setReturnedElement((org.eclipse.ocl.ecore.Variable)returnedElementExp);
		result.setType(returnedElementExp.getType());

		OCLExpression<EClassifier> bodyExp = visitOclExpressionCS(computeExpCS.getBody(), env);
		result.setBody((org.eclipse.ocl.ecore.OCLExpression)bodyExp);

		env.deleteElement(returnedElementExp.getName());

		return result;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitSwitchExpCS(SwitchExpCS switchExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		SwitchExp switchExp = ImperativeOCLFactory.eINSTANCE.createSwitchExp();
		switchExp.setStartPosition(switchExpCS.getStartOffset());
		switchExp.setEndPosition(switchExpCS.getEndOffset());

		List<EClassifier> allPartTypes = new ArrayList<EClassifier>();
		if (switchExpCS.getAlternativePart() != null) {
			for (SwitchAltExpCS altExpCS : switchExpCS.getAlternativePart()) {
				AltExp altExp = visitSwitchAltExpCS(altExpCS, env);
				switchExp.getAlternativePart().add(altExp);
				allPartTypes.add(altExp.getBody() != null ? altExp.getBody().getType() : null);
			}
		}
		if (switchExpCS.getElsePart() != null) {
			org.eclipse.ocl.ecore.OCLExpression elsePart = visitOclExpressionCS(switchExpCS.getElsePart(), env);
			if (elsePart != null) {
				switchExp.setElsePart(elsePart);
				allPartTypes.add(elsePart.getType());
			}
		}

		if (allPartTypes.isEmpty()) {
			switchExp.setType(getOclVoid());
		}
		else if (allPartTypes.size() == 1) {
			switchExp.setType(allPartTypes.get(0));
		}
		else {
			EClassifier type = null;
			for (int i = 0; i+1 < allPartTypes.size(); ++i) {
				type = TypeUtil.commonSuperType(switchExpCS, env,
						allPartTypes.get(i), allPartTypes.get(i+1));
				if (type == null) {
					break;
				}
			}
			switchExp.setType(type == null ? getOclVoid() : type);
		}

		return switchExp;
	}

	private AltExp visitSwitchAltExpCS(SwitchAltExpCS altExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		AltExp altExp = ImperativeOCLFactory.eINSTANCE.createAltExp();
		altExp.setStartPosition(altExpCS.getStartOffset());
		altExp.setEndPosition(altExpCS.getEndOffset());

		OCLExpression<EClassifier> condition = visitOclExpressionCS(altExpCS.getCondition(), env);
		altExp.setCondition((org.eclipse.ocl.ecore.OCLExpression)condition);
		OCLExpression<EClassifier> body = visitOclExpressionCS(altExpCS.getBody(), env);
		altExp.setBody((org.eclipse.ocl.ecore.OCLExpression)body);
		return altExp;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitWhileExpCS(WhileExpCS whileCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		WhileExp whileExp = ImperativeOCLFactory.eINSTANCE.createWhileExp();
		org.eclipse.ocl.ecore.OCLExpression result = whileExp;

		whileExp.setStartPosition(whileCS.getStartOffset());
		whileExp.setEndPosition(whileCS.getEndOffset());

		String resultVarInEnv = null;
		// if result variable is declared, wrap WhileExp into ComputeExp
		if(whileCS.getResultVar() != null) {
			ComputeExp embeddedComputeExp = ImperativeOCLFactory.eINSTANCE.createComputeExp();
			result = embeddedComputeExp;
			embeddedComputeExp.setStartPosition(whileCS.getStartOffset());
			embeddedComputeExp.setEndPosition(whileCS.getEndOffset());
			// define result variable in environment
			org.eclipse.ocl.ecore.Variable resultVar = (org.eclipse.ocl.ecore.Variable)
					variableDeclarationCS(whileCS.getResultVar(), env, true);
			// check if the variable was successfully added into environment
			resultVarInEnv = (resultVar != null && env.lookup(resultVar.getName()) == resultVar) ? resultVar.getName() : null;

			embeddedComputeExp.setReturnedElement(resultVar);
			embeddedComputeExp.setType(resultVar.getType());
			// actual body to execute is while expression itself
			// we just define the result variable and its initial value
			embeddedComputeExp.setBody(whileExp);
			embeddedComputeExp.setReturnedElement(resultVar);
			// the wrapped while expression still need to return null, according to QVT specification
		}

		// while expression itself has
		whileExp.setType(env.getOCLStandardLibrary().getOclVoid());

		// analyze the condition of while
		org.eclipse.ocl.ecore.OCLExpression condExp = visitOclExpressionCS(whileCS.getCondition(), env);
		whileExp.setCondition(condExp);
		if(condExp != null) {
			EClassifier condType = condExp.getType();
			if(env.getOCLStandardLibrary().getBoolean() != condExp.getType()) {
				if(condType == null) {
					condType = env.getOCLStandardLibrary().getOclVoid();
				}
				String message = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_booleanTypeExpressionExpected, env.getUMLReflection().getName(condType));
				QvtOperationalUtil.reportError(env, message, whileCS.getCondition());
			}
		}

		// analyze the body of while
		whileExp.setBody(oclExpressionCS(whileCS.getBody(), env));

		// cleanup result variable in environment, if it was added
		if(resultVarInEnv != null) {
			env.deleteElement(resultVarInEnv);
		}

		return result;
	}

	private static MappingDeclarationCS findOwningMappingDeclarationCS(ObjectExpCS outExpCS) {
		EObject eContainer = outExpCS.eContainer();
		if (eContainer instanceof MappingBodyCS) {
			eContainer = eContainer.eContainer();
			if (eContainer instanceof MappingSectionsCS) {
				eContainer = eContainer.eContainer();
				if (eContainer instanceof MappingRuleCS) {
					MappingRuleCS mappingCS = (MappingRuleCS) eContainer;
					return mappingCS.getMappingDeclarationCS();
				}
			}
		}
		return null;
	}

	private ObjectExp visitObjectExpCS(ObjectExpCS outExpCS, QvtOperationalEnv env, boolean isValidationChecked) {
		MappingDeclarationCS topLevelInMapping = findOwningMappingDeclarationCS(outExpCS);
		if(topLevelInMapping != null && topLevelInMapping.getResult().isEmpty() && topLevelInMapping.getContextType() == null) {
			//return null; FIXME - review this, ommitted to get better error reporting for AST
		}

		TypeSpecCS typeSpecCS = null;
		if (outExpCS.getTypeSpecCS() != null) {
			typeSpecCS = outExpCS.getTypeSpecCS();
		}

		ObjectExp objectExp = ExpressionsFactory.eINSTANCE.createObjectExp();
		objectExp.setStartPosition(outExpCS.getStartOffset());
		objectExp.setEndPosition(outExpCS.getEndOffset());

		if(typeSpecCS != null) {
			TypeSpecPair objectTypeSpec = visitTypeSpecCS(typeSpecCS, DirectionKind.OUT, env);

			objectExp.setType(objectTypeSpec.myType);
			if(objectTypeSpec.myType instanceof EClass) {
				// skip DataTypes as the instantiatedClass property expects Class
				// let's make AST validation to complain on missing class.
				// Note: Still can be derived from the referred object if specified explicitly
				EClass derivedInstantiatedClass = (EClass) objectTypeSpec.myType;
				objectExp.setInstantiatedClass(derivedInstantiatedClass);
				if (outExpCS.getSimpleNameCS() == null && (derivedInstantiatedClass.isAbstract() || derivedInstantiatedClass.isInterface())) {
					// always creates a new instance, ensure non-abstract type.
					String typeName = QvtOperationalParserUtil.safeGetQualifiedName(env, derivedInstantiatedClass);
					env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_canNotInstantiateAbstractType, typeName), outExpCS);
				}
			}
			objectExp.setExtent(objectTypeSpec.myExtent);

		} else if(topLevelInMapping != null) {
			// TODO - support multiple result parameters, for now take the first as the grammar does not allow this yet
			boolean isContextInOut = topLevelInMapping.getDirectionKindCS() != null ?
					topLevelInMapping.getDirectionKindCS().getDirectionKind() == DirectionKindEnum.INOUT : false;

			assert env.getContextOperation() instanceof ImperativeOperation;
			ImperativeOperation owningOperation = (ImperativeOperation)env.getContextOperation();

			if(owningOperation.getResult().isEmpty()) {
				if(isContextInOut && owningOperation.getContext() != null) {
					objectExp.setType(owningOperation.getContext().getEType());
				}
			}
		}

		SimpleNameCS referredObjNameCS = outExpCS.getSimpleNameCS();
		if(referredObjNameCS != null) {
			// a referred object has been explicitly specified
			String varName = referredObjNameCS.getValue();
			org.eclipse.ocl.ecore.Variable referredObject  = varName != null ? (org.eclipse.ocl.ecore.Variable)env.lookup(varName) : null;
			if(referredObject == null) {
				// variable not resolved
				env.reportError(NLS.bind(ValidationMessages.unresolvedNameError, varName), referredObjNameCS);
			} else {
				referredObjNameCS.setAst(referredObject);

				// TODO - implicit variables should follow multiplicity [1] referredObject and should always be created
				// for now, only explicit variables are recorded
				objectExp.setName(varName);
				objectExp.setReferredObject(referredObject);
			}
		}

		if(objectExp.getReferredObject() != null && objectExp.getType() == null) {
			objectExp.setType(objectExp.getReferredObject().getType());
		}

		if(objectExp.getType() instanceof EClass) {
			objectExp.setInstantiatedClass((EClass)objectExp.getType());
		}

		// try to derive extent from referred variable, if not retrieved from explicit TypeSpec yet
		if (objectExp.getExtent() == null) {
			Variable<EClassifier, EParameter> referredObject  = objectExp.getReferredObject();
			if(referredObject != null) {
				if(referredObject.getRepresentedParameter() instanceof MappingParameter) {
					MappingParameter mappingPar = (MappingParameter) referredObject.getRepresentedParameter();
					objectExp.setExtent(mappingPar.getExtent());
				}
			}
			if (objectExp.getExtent() == null && objectExp.getType() != null) {
				QvtOperationalModuleEnv moduleEnv = getModuleContextEnv(env);
				objectExp.setExtent(moduleEnv.resolveModelParameter(objectExp.getType(), DirectionKind.OUT));
			}
		}


		Module module = env.getModuleContextType();

		if (objectExp.getExtent() == null && module != null && !getModelParameter(module).isEmpty()) {
			QvtOperationalModuleEnv moduleEnv = getModuleContextEnv(env);
			boolean isInvalidForExtentResolve = false;
			if (objectExp.getReferredObject() == null) {
				isInvalidForExtentResolve = (objectExp.getType() == null || !moduleEnv.isMayBelongToExtent(objectExp.getType()));
			}
			else {
				if (IntermediateClassFactory.isIntermediateClass(objectExp.getReferredObject().getType())) {
					isInvalidForExtentResolve = true;
				}
				else {
					isInvalidForExtentResolve = !QVTUMLReflection.isUserModelElement(objectExp.getReferredObject().getType());
				}
			}
			if(!isInvalidForExtentResolve) {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_extentFailToInfer,
						QvtOperationalTypesUtil.getTypeFullName(objectExp.getType())),
						typeSpecCS != null ? typeSpecCS : outExpCS);
			}
		}

		ConstructorBody body = ExpressionsFactory.eINSTANCE.createConstructorBody();
		body.setStartPosition(outExpCS.getStartOffset());
		body.setEndPosition(outExpCS.getEndOffset());
		objectExp.setBody(body);

		QvtOperationalEnv tempEnv = env.getFactory().createEnvironment(env);
		tempEnv.setParentLocal();
		org.eclipse.ocl.ecore.Variable elem = objectExp.getReferredObject();
		if (elem == null) { // new object creation
			elem = EcoreFactory.eINSTANCE.createVariable();
			elem.setType(objectExp.getType());
			objectExp.setReferredObject(elem);

			body.getVariable().add(elem);
		}
		String varName = (objectExp.getName() == null) ? env.generateTemporaryName() : objectExp.getName();
		objectExp.setName(varName);
		tempEnv.addImplicitVariableForProperties(varName, elem);

		for (OCLExpressionCS expCS : outExpCS.getExpressions()) {
			org.eclipse.ocl.ecore.OCLExpression exp = visitOclExpressionCS(expCS, tempEnv);
			if (exp != null) {
				body.getContent().add(exp);
			}
		}

		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(outExpCS, objectExp, env);
		}

        if (isValidationChecked) {
            validateObjectExp(objectExp, outExpCS, env);
        }

        return objectExp;
    }


    public List<QvtOperationalModuleEnv> visitUnitCS(UnitCS unitCS, UnitProxy unit, QvtOperationalFileEnv fileEnv, ExternalUnitElementsProvider importResolver, ResourceSet resSet) throws SemanticException {
    	SubMonitor subMonitor = SubMonitor.convert(myMonitor, NLS.bind(Messages.Visitor_Visit, unit.getQualifiedName()), 6 * unitCS.getModules().size());

    	List<QvtOperationalModuleEnv> moduleEnvs = new LinkedList<QvtOperationalModuleEnv>();
    	Map<MappingModuleCS, QvtOperationalModuleEnv> moduleEnvsMap = new HashMap<MappingModuleCS, QvtOperationalModuleEnv>(2);
    	Set<String> moduleNames = new HashSet<String>(unitCS.getModules().size());

    	// 1st pass: module headers
    	for(MappingModuleCS moduleCS : unitCS.getModules()) {
	        Module module = QvtOperationalParserUtil.createModule(moduleCS);

			QvtOperationalModuleEnv moduleEnv = fileEnv.getFactory().createModuleEnvironment(module, fileEnv);
			moduleEnvsMap.put(moduleCS, moduleEnv);
			moduleEnvs.add(moduleEnv);
			moduleEnv.setContextModule(module);
			visitModuleHeaders(moduleCS, importResolver.getImporter(), moduleEnv, importResolver, resSet);

			if (moduleNames.contains(module.getName())) {
				moduleEnv.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_transformationNameDuplicates,
						new Object[] { module.getName() }),
						moduleCS.getHeaderCS() == null ? moduleCS : moduleCS.getHeaderCS().getPathNameCS());

			}
			moduleNames.add(module.getName());
			subMonitor.worked(1);
		}

		// 2nd pass: imports and usages
		for(MappingModuleCS moduleCS : unitCS.getModules()) {
			Module module = (Module) moduleCS.getAst();
			importsCS(moduleCS, unit, module, moduleEnvsMap.get(moduleCS), importResolver);
			subMonitor.worked(1);
		}
		List<MappingModuleCS> sortedModuless = checkModuleLoops(unitCS, fileEnv);

		// 3nd pass: intermediate Classes, module tags & renamings
		for(MappingModuleCS moduleCS : sortedModuless) {
			Module module = (Module) moduleCS.getAst();
			QvtOperationalModuleEnv moduleEnv = moduleEnvsMap.get(moduleCS);
			visitIntermediateClassesCS(moduleEnv, moduleCS, module);
			for (TagCS tagCS : moduleCS.getTags()) {
				visitTagCS(moduleEnv, tagCS, module, moduleEnv.getModuleContextType());
			}
			for (RenameCS renameCS : moduleCS.getRenamings()) {
				legacyRenameCS(renameCS, moduleEnv);
			}
			subMonitor.worked(1);
		}

		// 4th pass: method headers
		HashMap<MappingModuleCS, HashMap<MappingMethodCS, ImperativeOperation>> methodMaps = new HashMap<MappingModuleCS, HashMap<MappingMethodCS, ImperativeOperation>>();
		for(MappingModuleCS moduleCS : sortedModuless) {
			HashMap<MappingMethodCS, ImperativeOperation> methodMap = visitMethodHeaders(moduleCS, moduleEnvsMap.get(moduleCS));
			methodMaps.put(moduleCS, methodMap);
			subMonitor.worked(1);
		}

		// 5rd pass: properties
		for(MappingModuleCS moduleCS : sortedModuless) {
			Module module = (Module) moduleCS.getAst();
			createModuleProperties(module, moduleCS, moduleEnvsMap.get(moduleCS));
			subMonitor.worked(1);
		}

		// 6th pass: method bodies
		for(MappingModuleCS moduleCS : sortedModuless) {
			visitMethodBodies(moduleCS, methodMaps.get(moduleCS), moduleEnvsMap.get(moduleCS));
			subMonitor.worked(1);
		}

		return moduleEnvs;
	}


	private String getModuleID(Module module)  {
		String moduleURIwPt = "local."; //$NON-NLS-1$
		IModuleSourceInfo sourceInfo = ASTBindingHelper.getModuleSourceBinding(module);
		if (sourceInfo != null) {
			moduleURIwPt = sourceInfo.getSourceURI() + "."; //$NON-NLS-1$
		}
		else {
			URI moduleURI = ASTBindingHelper.resolveModuleFile(module);
			if (moduleURI != null) {
				moduleURIwPt = moduleURI + "."; //$NON-NLS-1$
			}
		}
		return moduleURIwPt + module.getName();
	}

	private List<MappingModuleCS> checkModuleLoops(UnitCS unitCS, QvtOperationalModuleEnv env) {
		List<MappingModuleCS> sortedModulesCS = new ArrayList<MappingModuleCS>(2);
		// Init sorted list
		for (MappingModuleCS moduleCS : unitCS.getModules()) {
			sortedModulesCS.add(moduleCS);
		}

		// Check for loops
		for(MappingModuleCS moduleCS : unitCS.getModules()) {
			Module module = (Module) moduleCS.getAst();

			Map<String, Module> modulesToVisit = new HashMap<String, Module>(2);
			Map<String, Module> visitedModules = new HashMap<String, Module>(2);
			Map<String, ModuleImport> topModuleImports = new HashMap<String, ModuleImport>(2);
			List<ModuleImport> loopImports = new ArrayList<ModuleImport>(2);

			// Add current module as the starting node
			String thisModuleID = getModuleID(module);
			modulesToVisit.put(thisModuleID, module);

			while (!modulesToVisit.isEmpty()) {
				// Visit modules one by one
				String visitedModuleID = (String)modulesToVisit.keySet().toArray()[0];
				Module visitedModule = modulesToVisit.get(visitedModuleID);
				visitedModules.put(visitedModuleID, visitedModule);
				modulesToVisit.remove(visitedModuleID);
				for (ModuleImport moduleImport : visitedModule.getModuleImport()) {
					Module importedModule = moduleImport.getImportedModule();
					ModuleImport topModuleImport = moduleImport;
					if (visitedModule != module) {
						topModuleImport = topModuleImports.get(visitedModuleID);
					}
					String toVisitModuleID = getModuleID(importedModule);
					if (thisModuleID.equals(toVisitModuleID)) {
						String message = NLS.bind(CompilerMessages.cyclicImportError,
								module.getName(),
								visitedModule.getName());
						env.reportError(message, moduleCS.getHeaderCS());
						loopImports.add(topModuleImport);
					}
					if (!visitedModules.containsKey(toVisitModuleID)) {
						modulesToVisit.put(toVisitModuleID, importedModule);
						topModuleImports.put(toVisitModuleID, topModuleImport);
					}
				}
			}

			// Remove "bad" imports
			for (ModuleImport moduleImport : loopImports) {
				module.getModuleImport().remove(moduleImport);
			}

			// Update sorted list
			List<MappingModuleCS> headModulesCS = new ArrayList<MappingModuleCS>(2);
			List<MappingModuleCS> tailModulesCS = new ArrayList<MappingModuleCS>(2);
			for (MappingModuleCS sortedModuleCS : sortedModulesCS) {
				String sortedModuleID = getModuleID((Module)sortedModuleCS.getAst());
				if (visitedModules.containsKey(sortedModuleID) && sortedModuleCS != moduleCS) {
					headModulesCS.add(sortedModuleCS);
				} else if (sortedModuleCS != moduleCS) {
					tailModulesCS.add(sortedModuleCS);
				}
			}
			sortedModulesCS.clear();
			sortedModulesCS.addAll(headModulesCS);
			sortedModulesCS.add(moduleCS);
			sortedModulesCS.addAll(tailModulesCS);
		}
		return sortedModulesCS;
	}


	public void visitModuleHeaders(MappingModuleCS moduleCS, URI unitURI, QvtOperationalModuleEnv env, ExternalUnitElementsProvider importResolver, ResourceSet resSet) throws SemanticException {
		Module module = (Module)moduleCS.getAst();
		module.setStartPosition(moduleCS.getStartOffset());
		module.setEndPosition(moduleCS.getEndOffset());
		// AST binding
		// FIXME - workaround to make Environment available with the module
		ASTBindingHelper.createModuleBinding(moduleCS, module, env, unitURI);
		//

		for (ModelTypeCS modelTypeCS : moduleCS.getMetamodels()) {
			ModelType modelType = visitModelTypeCS(modelTypeCS, env, module, resSet);
			if (modelType == null) {
				continue;
			}
			module.getEClassifiers().add(modelType);
			module.getUsedModelType().add(modelType);
			if (modelType.getName().length() > 0) {
				ModelType existingModelType = env.getModelType(modelType.getName());
				if(existingModelType == null) {
					env.registerModelType(modelType);
				} else {
					env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_modeltypeAlreadyDefined,
							new Object[] { modelType.getName() }),
							modelTypeCS.getIdentifierCS());
				}
			}
			else {
				env.registerModelType(modelType);
				env.reportWarning(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_modeltypeDeprecatedSyntax, new Object[] { }),
						modelTypeCS);
			}

			checkModelTypeConstraints(modelTypeCS, env);
		}

		visitTransformationHeaderCS(moduleCS.getHeaderCS(), env, module);

		env.setContextModule(module); // update model's parameters registration
	}


	private void checkModelTypeConstraints(ModelTypeCS modelTypeCS, QvtOperationalModuleEnv env) {
		ModelType modelType = (ModelType)modelTypeCS.getAst();

		for (OCLExpressionCS conditionCS : modelTypeCS.getWhereStatements()) {
			org.eclipse.ocl.ecore.Variable referredVar = EcoreFactory.eINSTANCE.createVariable();
			referredVar.setName(Environment.SELF_VARIABLE_NAME);
			referredVar.setType(modelType);
			QvtOperationalEnv tempEnv = env.getFactory().createEnvironment(env);
			tempEnv.setParentLocal();
			tempEnv.addElement(referredVar.getName(), referredVar, false);
			org.eclipse.ocl.ecore.OCLExpression condition = visitOclExpressionCS(conditionCS, tempEnv);
			if (condition != null) {
				EClassifier conditionType = condition.getType();
				if (conditionType != env.getOCLStandardLibrary().getBoolean()) {
					env.reportError(NLS.bind(ValidationMessages.ModelTypeWhereNotBooleanError,
							new Object[] { QvtOperationalTypesUtil.getTypeFullName(conditionType) }), conditionCS);
				}
				else {
					modelType.getAdditionalCondition().add(condition);
				}
			}
		}
	}


	public HashMap<MappingMethodCS, ImperativeOperation> visitMethodHeaders(MappingModuleCS moduleCS, QvtOperationalModuleEnv env) throws SemanticException {
		Module module = (Module) moduleCS.getAst();
		env.setContextModule(module);

		HashMap<MappingMethodCS, ImperativeOperation> methodMap = new LinkedHashMap<MappingMethodCS, ImperativeOperation>(moduleCS.getMethods().size());

		// declare moduleAST operations as they are required to analyze rules' contents
		for (MappingMethodCS methodCS : moduleCS.getMethods()) {
			String name = ""; //$NON-NLS-1$
			if(methodCS.getMappingDeclarationCS() != null) {
				SimpleNameCS methodNameCS = methodCS.getMappingDeclarationCS().getSimpleNameCS();
				if(methodNameCS != null) {
					name = methodNameCS.getValue();
				}
			}

			ImperativeOperation operation = null;
			if (methodCS instanceof ConstructorCS) {
				operation = ExpressionsFactory.eINSTANCE.createConstructor();
			}
			else if (methodCS instanceof MappingRuleCS) {
				if (QvtOperationalEnv.MAIN.equals(name)) {
					env.reportWarning(NLS.bind(ValidationMessages.EntryOp_DisallowedDeclAsMapping, null),
							methodCS.getStartOffset(),
							methodCS.getMappingDeclarationCS() != null ? methodCS.getMappingDeclarationCS().getEndOffset() : methodCS.getEndOffset());
				}
				operation = ExpressionsFactory.eINSTANCE.createMappingOperation();
			}
			else if (QvtOperationalEnv.MAIN.equals(name)) {
				operation = ExpressionsFactory.eINSTANCE.createEntryOperation();
			}
			else {
				Helper helper = ExpressionsFactory.eINSTANCE.createHelper();
				if (methodCS.getMappingDeclarationCS() != null) {
					helper.setIsQuery(methodCS.getMappingDeclarationCS().isIsQuery());
				}
				operation = helper;
			}

			methodCS.setAst(operation);

			if (visitMappingDeclarationCS(methodCS, env, operation)) {
				ImperativeOperation imperativeOp = env.defineImperativeOperation(operation, methodCS instanceof MappingRuleCS, true);
				if (imperativeOp != null) {
					methodMap.put(methodCS, imperativeOp);
				}
			}
		}

		// Handle the case of legacy constructs which allows for signature-less
		// transformation definition
		DeprecatedSignaturelessTransf.patchModule(module);

		boolean moduleEntryFound = false;
		for (MappingMethodCS methodCS : methodMap.keySet()) {
			ImperativeOperation imperativeOp = methodMap.get(methodCS);
			if(imperativeOp == null) {
				continue;
			}

			// process operation qualifiers
			List<QualifierKindCS> qualifiersCS = (methodCS.getMappingDeclarationCS() == null) ? Collections.<QualifierKindCS>emptyList()
					: methodCS.getMappingDeclarationCS().getQualifiers();

			for (QualifierKindCS nextQualifierCS : qualifiersCS) {
				switch (nextQualifierCS) {
				case ABSTRACT:
					if(imperativeOp instanceof MappingOperation) {
						QvtOperationalParserUtil.markAsAbstractOperation(imperativeOp);
					}
					if(imperativeOp instanceof Constructor) {
						// only 'blackbox' qualifier for constructor is currently supported
						String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unsupportedQualifierOnConstructor, nextQualifierCS.getName(),
								QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
						env.reportWarning(errMessage, QvtOperationalParserUtil.getMethodNameProblemNodeCS(methodCS));
					}
					break;
				case BLACKBOX:
					if (QvtOperationalParserUtil.hasOperationBody(methodCS)) {
						if(imperativeOp instanceof Constructor) {
							String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_blackboxConstructorWithBodyNotAllowed,
									QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
							env.reportError(errMessage, QvtOperationalParserUtil.getMethodHeaderProblemNodeCS(methodCS));
						}
						else {
							String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_blackboxOperationWithBodyNotAllowed,
									QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
							env.reportError(errMessage, QvtOperationalParserUtil.getMethodHeaderProblemNodeCS(methodCS));
						}
					}
					if (QvtOperationalParserUtil.isDisjunctiveMappingOperation(methodCS)) {
						String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unsupportedBlackboxQualifierOnDisjunctiveMapping,
								QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
						env.reportError(errMessage, QvtOperationalParserUtil.getMethodNameProblemNodeCS(methodCS));
					}
					imperativeOp.setIsBlackbox(true);
					break;
				case STATIC:
					String msg = (imperativeOp instanceof Constructor) ? ValidationMessages.QvtOperationalVisitorCS_unsupportedQualifierOnConstructor
							: ValidationMessages.QvtOperationalVisitorCS_unsupportedQualifierOnOperation;
					String errMessage = NLS.bind(msg, nextQualifierCS.getName(), QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
					env.reportWarning(errMessage, QvtOperationalParserUtil.getMethodNameProblemNodeCS(methodCS));
					break;
				}
			}

			Collection<QualifierKindCS> qualifierDups = QvtOperationalParserUtil.selectDuplicateQualifiers(qualifiersCS);
			for(QualifierKindCS duplicate : qualifierDups) {
				String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_duplicateQualifierOnOperation,
						duplicate.getName(), QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
				env.reportWarning(errMessage, QvtOperationalParserUtil.getMethodNameProblemNodeCS(methodCS));
			}

			if ((!QvtOperationalEnv.MAIN.equals(imperativeOp.getName()) ||
					getModelParameter(env.getModuleContextType()).isEmpty()) == false) {
				checkMainMappingConformance(env, imperativeOp);
			}

			if (false == moduleCS instanceof LibraryCS
					&& QvtOperationalEnv.MAIN.equals(imperativeOp.getName())) {
				if (moduleEntryFound) {
					env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_entryPointShouldBeDeclOnce,
							QvtOperationalEnv.MAIN), methodCS);
				}
				moduleEntryFound = true;
			}
		}

		return methodMap;
	}


	public void visitMethodBodies(MappingModuleCS moduleCS, HashMap<MappingMethodCS,ImperativeOperation> methodMap, QvtOperationalModuleEnv env) throws SemanticException {
		Module module = (Module) moduleCS.getAst();
		env.setContextModule(module);

		for (MappingMethodCS methodCS : methodMap.keySet()) {
			ImperativeOperation imperativeOp = methodMap.get(methodCS);
			if(imperativeOp == null) {
				continue;
			}

			visitMappingMethodCS(methodCS, env, imperativeOp);
		}

		for (ModuleImport moduleImport : module.getModuleImport()) {
			for (EOperation operation : QvtOperationalParserUtil.getOwnedOperations(moduleImport.getImportedModule())) {
				if(operation instanceof MappingOperation) {
					MappingOperation mappingOperation = (MappingOperation) operation;
					env.registerMappingOperation(mappingOperation);
				}
			}
		}

		env.resolveResolveInExpInMappings();

		ImperativeOperation mainMethod = QvtOperationalParserUtil.getMainOperation(module);
		if(mainMethod instanceof EntryOperation) {
			module.setEntry((EntryOperation)mainMethod);
		}

		validate(env);
	}



	/**
	 * Clears the state of this QVT analyzer.
	 */
	public void clear() {
		if(myErrorNodes != null) {
			myErrorNodes.clear();
		}

		SubMonitor.done(myMonitor);
	}

	private void visitTagCS(QvtOperationalEnv env, TagCS ownedTagCS, Module module, EClassifier tagContextType) throws SemanticException {
		String tagId = visitLiteralExpCS(ownedTagCS.getName(), env);

		String value = null;
		if (ownedTagCS.getOclExpressionCS() != null) {
			org.eclipse.ocl.ecore.OCLExpression oclExpression = visitOclExpressionCS(ownedTagCS.getOclExpressionCS(), env);
			if (oclExpression instanceof StringLiteralExp<?>) {
				value = ((StringLiteralExp<?>) oclExpression).getStringSymbol();
			}
		}

		EModelElement element = null;
		ScopedNameCS scopedNameCS = ownedTagCS.getScopedNameCS();
		TypeCS typeCS = scopedNameCS.getTypeCS();
		if (typeCS != null) {
			EClassifier owningType = visitTypeCS(typeCS, null, env);
			if (owningType != null && scopedNameCS.getName() != null) {
				element = env.lookupProperty(TypeUtil.resolveType(env, owningType), scopedNameCS.getName());
			}
		}
		else if (scopedNameCS.getName() != null) {
			element = env.lookupProperty(tagContextType, scopedNameCS.getName());
			if(element == null) {
				element = env.lookupClassifier(Collections.singletonList(scopedNameCS.getName()));
			}
		}

		if (element == null) {
			QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.unresolvedNameError,
					new Object[] { QvtOperationalParserUtil.getStringRepresentation(scopedNameCS) }),
					scopedNameCS);
		}
		else {
			EAnnotation ownedTag = createTag(tagId, value, element);

			ASTSyntheticNode astNode = ASTSyntheticNodeAccess.createASTNode(ownedTag);
			astNode.setStartPosition(ownedTagCS.getStartOffset());
			astNode.setEndPosition(ownedTagCS.getEndOffset());

			module.getOwnedTag().add(ownedTag);

			scopedNameCS.setAst(element);
		}
	}

	private void visitIntermediateClassesCS(QvtOperationalModuleEnv env, MappingModuleCS moduleCS, Module module) throws SemanticException {
		IntermediateClassFactory intermediateClassFactory = null;
		Map<String, EClass> createdIntermClasses = new LinkedHashMap<String, EClass>(moduleCS.getClassifierDefCS().size());
		final Map<EClass, CSTNode> cstIntermClassesMap = new LinkedHashMap<EClass, CSTNode>();
		for (ClassifierDefCS classifierDefCS : moduleCS.getClassifierDefCS()) {
			if (false == module instanceof OperationalTransformation) {
				if (classifierDefCS instanceof IntermediateClassDefCS) {
					env.reportError(NLS.bind(ValidationMessages.IntermediateClassifierNotAllowed,
							new Object[] {}), classifierDefCS);
				}
				else {
					env.reportError(NLS.bind(ValidationMessages.ExceptionDefNotAllowed,
							new Object[] {}), classifierDefCS);
				}
			}
			if (createdIntermClasses.containsKey(classifierDefCS.getSimpleNameCS().getValue())) {
				env.reportError(NLS.bind(ValidationMessages.DuplicateClassifier,
						new Object[] { classifierDefCS.getSimpleNameCS().getValue() }), classifierDefCS.getSimpleNameCS());
				continue;
			}
			if (module.isIsBlackbox()) {
				String error = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_intermediateClassDefinitionInsideBlackboxModule, classifierDefCS.getSimpleNameCS().getValue());
				env.reportError(error, classifierDefCS);
			}

			if(intermediateClassFactory == null) {
				intermediateClassFactory = new IntermediateClassFactory(module);
			}
			EClass eClassifier = intermediateClassFactory.createIntermediateClassifier();

			ASTSyntheticNode astNode = ASTSyntheticNodeAccess.createASTNode(eClassifier);
			astNode.setStartPosition(classifierDefCS.getStartOffset());
			astNode.setEndPosition(classifierDefCS.getEndOffset());
			if (myCompilerOptions.isGenerateCompletionData()) {
				ASTSyntheticNodeAccess.setCST(astNode, classifierDefCS);
			}

			eClassifier.setName(classifierDefCS.getSimpleNameCS().getValue());

			createdIntermClasses.put(eClassifier.getName(), eClassifier);
			cstIntermClassesMap.put(eClassifier, classifierDefCS.getSimpleNameCS());
		}

		if (!createdIntermClasses.isEmpty()) {
			assert intermediateClassFactory != null;
			env.registerModelType(intermediateClassFactory.getIntermediateModelType());
		}

		for (ClassifierDefCS classifierDefCS : moduleCS.getClassifierDefCS()) {
			String className = classifierDefCS.getSimpleNameCS().getValue();
			EClass rootClass = createdIntermClasses.get(className);
			for (TypeCS typeCS : classifierDefCS.getExtends()) {

				if (typeCS instanceof PathNameCS && ((PathNameCS) typeCS).getSimpleNames().size() == 1) {
					EClass extClass = createdIntermClasses.get(((PathNameCS) typeCS).getSimpleNames().get(0));
					if (extClass != null) {
						typeCS.setAst(extClass);
						rootClass.getESuperTypes().add(extClass);
						continue;
					}
				}

				EClassifier extendType = visitTypeCS(typeCS, null, env);

				if (extendType == null) {
					// error reported by visitTypeCS(..)
					continue;
				}
				else if (extendType == QvtOperationalStdLibrary.INSTANCE.getExceptionClass()) {
					if (false == classifierDefCS instanceof ExceptionDefCS) {
						env.reportError(NLS.bind(ValidationMessages.CannotExtendExceptionExplicitly,
								new Object[] {}),
								typeCS);
					}
				}
				else if (!QVTUMLReflection.isUserModelElement(extendType)) {
					env.reportError(NLS.bind(ValidationMessages.InvalidClassifierForExtend,
							QvtOperationalTypesUtil.getTypeFullName(extendType)),
							typeCS);
				}
				else {
					rootClass.getESuperTypes().add((EClass) extendType);
				}
			}

			if (classifierDefCS instanceof ExceptionDefCS) {
				rootClass.getESuperTypes().add(QvtOperationalStdLibrary.INSTANCE.getExceptionClass());
			}
		}


		Map<ClassifierPropertyCS, EStructuralFeature> createdProperties = new IdentityHashMap<ClassifierPropertyCS, EStructuralFeature>();
		for (ClassifierDefCS classifierDefCS : moduleCS.getClassifierDefCS()) {
			String className = classifierDefCS.getSimpleNameCS().getValue();
			if (!createdIntermClasses.containsKey(className)) {
				continue;
			}
			visitClassifierDefCS(classifierDefCS, createdIntermClasses.get(className), module, createdProperties, env);
		}
		for (ClassifierDefCS classifierDefCS : moduleCS.getClassifierDefCS()) {
			String className = classifierDefCS.getSimpleNameCS().getValue();
			if (!createdIntermClasses.containsKey(className)) {
				continue;
			}
			initClassifierDefCS(createdIntermClasses.get(className), module, createdProperties, env);
		}

		class CycleChecker {
			boolean checkClass(EClass cls) {
				myVisitedClasses.clear();
				return checkClassImpl(cls);
			}

			private boolean checkClassImpl(EClass cls) {
				myVisitedClasses.add(cls);
				for (EClass superCls : cls.getESuperTypes()) {
					// check only intermediate hierarchy
					if (!cstIntermClassesMap.containsKey(superCls)) {
						continue;
					}
					if (myVisitedClasses.contains(superCls)) {
						return false;
					}
					if (!checkClassImpl(superCls)) {
						return false;
					}
				}
				return true;
			}

			final Set<EClass> myVisitedClasses = new HashSet<EClass>(2);
		}

		CycleChecker cycleChecker = new CycleChecker();
		for (EClass nextClass : cstIntermClassesMap.keySet()) {
			if (!cycleChecker.checkClass(nextClass)) {
				env.reportError(NLS.bind(ValidationMessages.CycleInIntermHierarchy,
						QvtOperationalTypesUtil.getTypeFullName(nextClass)),
						cstIntermClassesMap.get(nextClass));
			}

			//nextClass.getESuperTypes().add(EcorePackage.Literals.ECLASS);

			Map<String, EStructuralFeature> ownFeatures = new HashMap<String, EStructuralFeature>(nextClass.getEStructuralFeatures().size());
			for (EStructuralFeature nextFeature : nextClass.getEStructuralFeatures()) {
				ownFeatures.put(nextFeature.getName(), nextFeature);
			}
			for (EStructuralFeature nextFeature : nextClass.getEAllStructuralFeatures()) {
				EStructuralFeature ownFeature = ownFeatures.get(nextFeature.getName());
				if (ownFeature != null && ownFeature != nextFeature) {
					env.reportError(NLS.bind(ValidationMessages.HidingProperty,
							nextFeature.getName()),
							cstIntermClassesMap.get(nextClass));
				}
			}

		}

	}

	private EClass visitClassifierDefCS(ClassifierDefCS classifierDefCS, EClass eClassifier, Module module,
			Map<ClassifierPropertyCS, EStructuralFeature> createdProperties, QvtOperationalModuleEnv env) throws SemanticException {

		class PropertyPair {
			final EStructuralFeature myEFeature;
			final ClassifierPropertyCS myPropCS;

			PropertyPair(EStructuralFeature eFeature, ClassifierPropertyCS propCS) {
				myEFeature = eFeature;
				myPropCS = propCS;
			}
		}

		Map<String, List<PropertyPair>> classifierProperties = new LinkedHashMap<String, List<PropertyPair>>(classifierDefCS.getProperties().size());

		// first pass for creation
		for (ClassifierPropertyCS propCS : classifierDefCS.getProperties()) {
			EStructuralFeature eFeature = visitClassifierPropertyCS(propCS, env);
			if (eFeature == null) {
				continue;
			}

			eClassifier.getEStructuralFeatures().add(eFeature);

			List<PropertyPair> properties = classifierProperties.get(eFeature.getName());
			if (properties == null) {
				properties = new ArrayList<PropertyPair>(2);
				classifierProperties.put(eFeature.getName(), properties);
			}
			properties.add(new PropertyPair(eFeature, propCS));

			createdProperties.put(propCS, eFeature);
		}

		for (String propName : classifierProperties.keySet()) {
			List<PropertyPair> properties = classifierProperties.get(propName);
			if (properties.size() == 1) {
				continue;
			}
			for (PropertyPair propPair : properties) {
				HiddenElementAdapter.markAsHidden(propPair.myEFeature);
				env.reportError(NLS.bind(ValidationMessages.DuplicateProperty,
						new Object[] { eClassifier.getName() + '.' + propPair.myEFeature.getName() }), propPair.myPropCS.getSimpleNameCS());
			}
		}

		for (TagCS tagCS : classifierDefCS.getTags()) {
			visitTagCS(env, tagCS, module, eClassifier);
		}

		return eClassifier;
	}

	private void initClassifierDefCS(EClass eClassifier, Module module,
			Map<ClassifierPropertyCS, EStructuralFeature> createdProperties, QvtOperationalEnv env) throws SemanticException {

		// second pass for initialization parts and opposite properties
		for (ClassifierPropertyCS propCS : createdProperties.keySet()) {
			EStructuralFeature eFeature = createdProperties.get(propCS);
			if (eClassifier != eFeature.eContainer()) {
				continue;
			}

			initClassifierPropertyCS(propCS, eFeature, env);
		}
	}

	private void initClassifierPropertyCS(ClassifierPropertyCS propCS, EStructuralFeature eFeature, QvtOperationalEnv env) {
		// handle initialization expression
		OCLExpression<EClassifier> initExpression = null;
		if (propCS.getOclExpressionCS() != null) {
			initExpression = visitOclExpressionCS(propCS.getOclExpressionCS(), env);
			QvtOperationalParserUtil.setInitExpression(eFeature, initExpression);
		}

		if (eFeature.getEType() == null && initExpression != null) {
			eFeature.setEType(initExpression.getType());
		}

		if (initExpression != null) {
			EClassifier realType = initExpression.getType();
			EClassifier declaredType = env.getUMLReflection().getOCLType(eFeature);
			if (!QvtOperationalParserUtil.isAssignableToFrom(env, declaredType, realType)) {
				env.reportError(NLS.bind(ValidationMessages.TypeMismatchError,
						new Object[] { QvtOperationalTypesUtil.getTypeFullName(declaredType), QvtOperationalTypesUtil.getTypeFullName(realType) }),
						propCS.getStartOffset(), propCS.getEndOffset());
			}
		}

		// handle opposite property
		OppositePropertyCS oppositeCS = propCS.getOpposite();
		if (oppositeCS != null) {
			if (eFeature instanceof EReference) {

				if (oppositeCS.getMultiplicity() != null) {
					visitMultiplicityDefCS(oppositeCS.getMultiplicity(), env);
				}

				if (oppositeCS.getSimpleNameCS() != null) {
					String oppositeName = oppositeCS.getSimpleNameCS().getValue();
					EReference oppositeRef = null;
					EClassifier eFeatureType = eFeature.getEType();
					if (eFeatureType instanceof EClass) {
						for (EReference nextRef : ((EClass) eFeatureType).getEAllReferences()) {
							if (oppositeName.equals(nextRef.getName())) {
								oppositeRef = nextRef;
								break;
							}
						}

						if (oppositeRef != null) {
							((EReference) eFeature).setEOpposite(oppositeRef);
						}
						else {
							env.reportError(NLS.bind(ValidationMessages.IntermClassifier_invalideOppositeName,
									new Object[] { oppositeName, QvtOperationalTypesUtil.getTypeFullName(eFeatureType) }),
									oppositeCS.getSimpleNameCS());
						}
					}
					else {
						env.reportError(NLS.bind(ValidationMessages.IntermClassifier_invalideOppositeType,
								new Object[] { QvtOperationalTypesUtil.getTypeFullName(eFeatureType) }), oppositeCS);
					}
				}
			}
			else {
				env.reportError(NLS.bind(ValidationMessages.IntermClassifier_oppositeOnlyForReferences,
						new Object[] { }), oppositeCS);
			}
		}
	}

	private EStructuralFeature visitClassifierPropertyCS(ClassifierPropertyCS propCS, QvtOperationalEnv env) {
		EClassifier propertyEType = null;
		if (propCS.getTypeCS() != null) {
			propertyEType = visitTypeCS(propCS.getTypeCS(), null, env);
		}

		EStructuralFeature eFeature = createESFeature(propertyEType);
		eFeature.setName(propCS.getSimpleNameCS().getValue());
		eFeature.setEType(propertyEType);

		ASTSyntheticNode astNode = ASTSyntheticNodeAccess.createASTNode(eFeature);
		astNode.setStartPosition(propCS.getStartOffset());
		astNode.setEndPosition(propCS.getEndOffset());

		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTSyntheticNodeAccess.setCST(astNode, propCS);
		}

		// handle stereotype qualifiers
		Set<String> handledStereotypes = new HashSet<String>(2);
		for (SimpleNameCS nameCS : propCS.getStereotypeQualifiers()) {
			String qualifName = nameCS.getValue();
			if ("id".equals(qualifName)) { //$NON-NLS-1$
				if (eFeature instanceof EAttribute) {
					((EAttribute) eFeature).setID(true);
				}
				else {
					env.reportError(NLS.bind(ValidationMessages.IntermClassifier_unapplicableStereotypeQualifier,
							new Object[] { qualifName }), nameCS);
				}
			}
			else {
				env.reportWarning(NLS.bind(ValidationMessages.IntermClassifier_unknownStereotypeQualifier,
						new Object[] { qualifName }), nameCS);
			}

			if (handledStereotypes.contains(qualifName)) {
				env.reportWarning(NLS.bind(ValidationMessages.IntermClassifier_duplicatedStereotypeQualifier,
						new Object[] { qualifName }), nameCS);
			}
			handledStereotypes.add(qualifName);
		}

		// handle feature keys
		SimpleNameCS staticNameCS = null, composesNameCS = null;
		Set<String> handledFeatureKeys = new HashSet<String>(2);
		for (SimpleNameCS nameCS : propCS.getFeatureKeys()) {
			String keyName = nameCS.getValue();
			if ("composes".equals(keyName)) { //$NON-NLS-1$
				if (eFeature instanceof EReference) {
					((EReference) eFeature).setContainment(true);
				}
				else {
					env.reportError(NLS.bind(ValidationMessages.IntermClassifier_referenceOnlyFeatureKey,
							new Object[] { keyName }), nameCS);
				}
				composesNameCS = nameCS;
			}
			else if ("references".equals(keyName)) { //$NON-NLS-1$
				if (eFeature instanceof EReference) {
					((EReference) eFeature).setContainment(false);
				}
				else {
					env.reportError(NLS.bind(ValidationMessages.IntermClassifier_referenceOnlyFeatureKey,
							new Object[] { keyName }), nameCS);
				}
			}
			else if ("readonly".equals(keyName)) { //$NON-NLS-1$
				eFeature.setChangeable(false);
			}
			else if ("derived".equals(keyName)) { //$NON-NLS-1$
				env.reportWarning(NLS.bind(ValidationMessages.IntermClassifier_unsupportedFeatureKey,
						new Object[] { keyName }), nameCS);
			}
			else if ("static".equals(keyName)) { //$NON-NLS-1$
				IntermediateClassFactory.markFeatureAsStatic(eFeature);
				staticNameCS = nameCS;
			}

			if (handledFeatureKeys.contains(keyName)) {
				env.reportWarning(NLS.bind(ValidationMessages.IntermClassifier_duplicatedFeatureKey,
						new Object[] { keyName }), nameCS);
			}
			handledFeatureKeys.add(keyName);
		}

		if (composesNameCS != null && staticNameCS != null) {
			env.reportError(NLS.bind(ValidationMessages.IntermClassifier_incompatibleFeatureKeys,
					new Object[] { composesNameCS.getValue(), staticNameCS.getValue() }), composesNameCS);
			env.reportError(NLS.bind(ValidationMessages.IntermClassifier_incompatibleFeatureKeys,
					new Object[] { composesNameCS.getValue(), staticNameCS.getValue() }), staticNameCS);
		}

		if (propCS.getMultiplicity() != null) {
			MultiplicityDef multiplcityDef = visitMultiplicityDefCS(propCS.getMultiplicity(), env);
			eFeature.setLowerBound(multiplcityDef.lower);
			eFeature.setUpperBound(multiplcityDef.upper);
		}

		eFeature.setOrdered(propCS.isIsOrdered());

		return eFeature;
	}

	private static class MultiplicityDef {
		public int lower = 0;
		public int upper = 1;
	}

	private MultiplicityDef visitMultiplicityDefCS(MultiplicityDefCS multiplicityCS, QvtOperationalEnv env) {
		MultiplicityDef multiplicityDef = new MultiplicityDef();

		try {
			multiplicityDef.lower = Integer.valueOf(multiplicityCS.getLowerBound().getSymbol());
			if ("*".equals(multiplicityCS.getUpperBound().getSymbol())) { //$NON-NLS-1$
				multiplicityDef.upper = -1;
			}
			else {
				multiplicityDef.upper = Integer.valueOf(multiplicityCS.getUpperBound().getSymbol());
			}

			// check UML constraints [7.3.32]
			if (multiplicityDef.lower < 0) {
				throw new NumberFormatException(ValidationMessages.IntermClassifier_multiplicityInvalidLowerBound);
			}
			if (multiplicityDef.upper >= 0 && multiplicityDef.lower > multiplicityDef.upper) {
				throw new NumberFormatException(ValidationMessages.IntermClassifier_multiplicityInvalidRange);
			}
			if (multiplicityDef.upper == 0 && multiplicityDef.lower == 0) {
				throw new NumberFormatException(ValidationMessages.IntermClassifier_multiplicityEmptyRange);
			}
		}
		catch (NumberFormatException ex) {
			env.reportError(ex.getLocalizedMessage(), multiplicityCS);
			// default multiplicity from specification [8.4.6]
			multiplicityDef = new MultiplicityDef();
		}

		return multiplicityDef;
	}

	private void importsCS(MappingModuleCS parsedModuleCS, UnitProxy unit, Module module, QvtOperationalModuleEnv env, ExternalUnitElementsProvider importResolver) {

		EMap<String, List<QvtOperationalModuleEnv>> importMap = new BasicEMap<String, List<QvtOperationalModuleEnv>>(5);

		// first pass: process all valid imports
		for (ImportCS nextImportedCS : parsedModuleCS.getImports()) {
			if(nextImportedCS.getPathNameCS() == null) {
				// nothing meaningful to represent in AST
				continue;
			}

			String unitQualifiedName = QvtOperationalParserUtil.getStringRepresentation(nextImportedCS.getPathNameCS(), "."); //$NON-NLS-1$
			EList<SimpleNameCS> importedUnitQName = nextImportedCS.getPathNameCS().getSimpleNames();
			List<QvtOperationalModuleEnv> moduleEnvironments = importResolver.getModules(QvtOperationalParserUtil.getSequenceOfNames(importedUnitQName));

			if(moduleEnvironments.isEmpty()) {
				// skip module which has compilation error(s)
				continue;
			}

			for (QvtOperationalModuleEnv nextImportedEnv : moduleEnvironments) {
				URI sourceURI = getSourceURI(nextImportedEnv);
				nextImportedCS.setAst(sourceURI);
				nextImportedCS.getPathNameCS().setAst(sourceURI);
			}

			importMap.put(unitQualifiedName, moduleEnvironments);

			// report legacy library import statements
			if(nextImportedCS instanceof LibraryImportCS) {
				// warn about specific library import deprecation
				env.reportWarning(NLS.bind(ValidationMessages.DeprecatedLibraryImportStatement, new Object[] { unitQualifiedName }), nextImportedCS.getPathNameCS());
			}
		}

		List<ModuleUsageCS> moduleUsages = new ArrayList<ModuleUsageCS>(parsedModuleCS.getHeaderCS().getModuleUsages());

		// make set of module imports corresponding to module usage references
		Set<String> usedModulePathes = new HashSet<String>();
		for (ModuleUsageCS moduleUsageCS : moduleUsages) {
			for (ModuleRefCS moduleRefCS : moduleUsageCS.getModuleRefs()) {
				PathNameCS modulePathNameCS = moduleRefCS.getPathNameCS();
				if (modulePathNameCS == null) {
					continue;
				}

				String moduleName = QvtOperationalParserUtil.getStringRepresentation(modulePathNameCS.getSimpleNames(), "."); //$NON-NLS-1$
				List<QvtOperationalModuleEnv> moduleEnvs = importMap.get(moduleName);

				if (moduleEnvs != null) {
					usedModulePathes.add(moduleName);
				}
				else if (modulePathNameCS.getSimpleNames().size() == 1) {
					// backward compatibility on using short module name as module reference
					moduleName = modulePathNameCS.getSimpleNames().get(0).getValue();

					done:
						for (String unitQName : importMap.keySet()) {
							List<QvtOperationalModuleEnv> localModuleEnvs = importMap.get(unitQName);

							for (QvtOperationalModuleEnv nextModuleEnv : localModuleEnvs) {
								Module nextImportedModule = nextModuleEnv.getModuleContextType();
								if (nextImportedModule != null && moduleName.equals(nextImportedModule.getName())) {
									usedModulePathes.add(unitQName);
									break done;
								}
							}
						}
				}
			}
		}

		// second pass: for all valid imports create new 'extension' module usage reference in case module import doesn't have corresponding usage reference
		for (ImportCS nextImportedCS : parsedModuleCS.getImports()) {
			if(nextImportedCS.getPathNameCS() == null) {
				// nothing meaningful to represent in AST
				continue;
			}

			String unitQualifiedName = QvtOperationalParserUtil.getStringRepresentation(nextImportedCS.getPathNameCS(), "."); //$NON-NLS-1$
			EList<SimpleNameCS> importedUnitQName = nextImportedCS.getPathNameCS().getSimpleNames();
			List<QvtOperationalModuleEnv> moduleEnvironments = importResolver.getModules(QvtOperationalParserUtil.getSequenceOfNames(importedUnitQName));

			if(moduleEnvironments.isEmpty()) {
				// skip module which has compilation error(s)
				continue;
			}

			if (!usedModulePathes.contains(unitQualifiedName)) {
				// we might have duplicates in imports, so avoid exceptions in environments
				usedModulePathes.add(unitQualifiedName);

				// process imports here only in case of the legacy implicit import by extension,
				// pass the responsibility to module usage analysis
				List<QvtOperationalModuleEnv> moduleEnvs = importMap.get(unitQualifiedName);
				for (QvtOperationalModuleEnv nextModuleEnv : moduleEnvs) {
					Module nextImportedModule = nextModuleEnv.getModuleContextType();
					ModuleUsageCS result = org.eclipse.m2m.internal.qvt.oml.cst.CSTFactory.eINSTANCE.createModuleUsageCS();
					result.setImportKind(ImportKindEnum.EXTENSION);
					result.setStartOffset(nextImportedCS.getStartOffset());
					result.setEndOffset(nextImportedCS.getEndOffset());

					PathNameCS pathNameCS = CSTFactory.eINSTANCE.createPathNameCS();
					for (SimpleNameCS simpleNameCS : nextImportedCS.getPathNameCS().getSimpleNames()) {
						SimpleNameCS copyCS = CSTFactory.eINSTANCE.createSimpleNameCS();
						copyCS.setType(simpleNameCS.getType());
						copyCS.setValue(simpleNameCS.getValue());
						pathNameCS.getSimpleNames().add(copyCS);
					}
					SimpleNameCS moduleNameCS = CSTFactory.eINSTANCE.createSimpleNameCS();
					moduleNameCS.setType(SimpleTypeEnum.IDENTIFIER_LITERAL);
					moduleNameCS.setValue(nextImportedModule.getName());
					pathNameCS.getSimpleNames().add(moduleNameCS);
					pathNameCS.getSimpleNames().add(moduleNameCS);


					ModuleRefCS moduleRefCS = org.eclipse.m2m.internal.qvt.oml.cst.CSTFactory.eINSTANCE.createModuleRefCS();
					moduleRefCS.setPathNameCS(pathNameCS);
					moduleRefCS.setStartOffset(nextImportedCS.getPathNameCS().getStartOffset());
					moduleRefCS.setEndOffset(nextImportedCS.getPathNameCS().getEndOffset());
					result.getModuleRefs().add(moduleRefCS);

					moduleUsages.add(result);
				}
			}
		}

		for (ModuleUsageCS nextModuleUsage : moduleUsages) {
			moduleUsageCS(nextModuleUsage, unit, module, env, importMap);
		}
	}

	private void moduleUsageCS(ModuleUsageCS moduleUsageCS, UnitProxy unit, Module module, QvtOperationalModuleEnv env, EMap<String, List<QvtOperationalModuleEnv>> importMap) {
		class ModuleDesc {
			public ModuleDesc(Module moduleIn, QvtOperationalModuleEnv envIn) {
				module = moduleIn;
				env = envIn;
			}
			public Module module;
			public QvtOperationalModuleEnv env;
		};

		EList<ModuleRefCS> moduleRefs = moduleUsageCS.getModuleRefs();
		for (ModuleRefCS moduleRefCS : moduleRefs) {
			List<ModuleDesc> candidateModules = new ArrayList<ModuleDesc>(2);
			HashMap<Module, ModuleDesc> resolvedModules = new HashMap<Module, ModuleDesc>(2);

			PathNameCS modulePathNameCS = moduleRefCS.getPathNameCS();
			if(modulePathNameCS == null) {
				// should already have reported CST parse error
				continue;
			}

			List<QvtOperationalModuleEnv> moduleEnvs = new ArrayList<QvtOperationalModuleEnv>(2);
			List<QvtOperationalModuleEnv> legacyModuleEnvs = new ArrayList<QvtOperationalModuleEnv>(2);
			List<SimpleNameCS> pathUnit = new ArrayList<SimpleNameCS>(Arrays.asList((SimpleNameCS[])modulePathNameCS.getSimpleNames().toArray())); // clone
			pathUnit.remove(pathUnit.size()-1);
			String unitFQName = QvtOperationalParserUtil.getStringRepresentation(pathUnit, "."); //$NON-NLS-1$
			String moduleFQName = QvtOperationalParserUtil.getStringRepresentation(modulePathNameCS.getSimpleNames(), "."); //$NON-NLS-1$
			String shortModuleName = modulePathNameCS.getSimpleNames().get(modulePathNameCS.getSimpleNames().size()-1).getValue();

			// Add to search list: current unit (sibling envs)
			if (unitFQName.equals(unit.getNamespace() + "." + unit.getName()) //$NON-NLS-1$
					|| unitFQName.length() == 0) {
				moduleEnvs.addAll(env.getFileParent().getInnerEnvironments());
			}

			// Add to search list: env for fully qualified module name (support for case of <module name> = <unit name>)
			List<QvtOperationalModuleEnv> moduleEnvsFQ = importMap.get(moduleFQName);
			if (moduleEnvsFQ != null) {
				moduleEnvs.addAll(moduleEnvsFQ);
				legacyModuleEnvs.addAll(moduleEnvsFQ);
			}

			// Add to search list: env for fully qualified unit name
			if (pathUnit.size() > 0) {
				List<QvtOperationalModuleEnv> moduleEnvsUnitFQ = importMap.get(unitFQName);
				if (moduleEnvsUnitFQ != null) {
					moduleEnvs.addAll(moduleEnvsUnitFQ);
					legacyModuleEnvs.addAll(moduleEnvsUnitFQ);
				}
			}

			if (moduleEnvsFQ == null && modulePathNameCS.getSimpleNames().size() == 1) {
				// Add to search list: all imported files - backward compatibility on using short module name as module reference
				for (String unitQName : importMap.keySet()) {
					List<QvtOperationalModuleEnv> localModuleEnvs = importMap.get(unitQName);
					moduleEnvs.addAll(localModuleEnvs);
				}
			}

			for (QvtOperationalModuleEnv nextModuleEnv : moduleEnvs) {
				Module nextImportedModule = nextModuleEnv.getModuleContextType();
				if (nextImportedModule != null && shortModuleName.equals(nextImportedModule.getName())) {
					candidateModules.add(new ModuleDesc(nextImportedModule, nextModuleEnv));
				}
			}

			if (candidateModules.size() == 0) {
				for (QvtOperationalModuleEnv nextModuleEnv : legacyModuleEnvs) {
					Module nextImportedModule = nextModuleEnv.getModuleContextType();
					if (nextImportedModule != null) {
						candidateModules.add(new ModuleDesc(nextImportedModule, nextModuleEnv));
					}
				}
			}

			// Check module signatures
			for (ModuleDesc moduleDesc : candidateModules) {
				Module nextImportedModule = moduleDesc.module;

				List<ModelType> refereceSignatureModelTypes = new ArrayList<ModelType>(5);
				EList<ParameterDeclarationCS> signatureParams = moduleRefCS.getParameters();
				if(signatureParams.isEmpty()) {
					// be tolerant, we are not specific about the model types, select only by name
					resolvedModules.put(moduleDesc.module, moduleDesc);
					continue;
				}

				for (ParameterDeclarationCS nextParamCS : signatureParams) {
					TypeSpecCS nextTypeCS = nextParamCS.getTypeSpecCS();
					if(nextTypeCS != null && nextTypeCS.getTypeCS() instanceof PathNameCS) {
						PathNameCS modelTypeCS = (PathNameCS) nextTypeCS.getTypeCS();
						EClassifier modelType = modelTypeCS.getSimpleNames().isEmpty() ? null : env.getModelType(modelTypeCS.getSimpleNames().get(0).getValue());
						if(modelType instanceof ModelType) {
							refereceSignatureModelTypes.add((ModelType) modelType);
						}
					}
				}

				List<ModelType> importedSignatureTypes;
				if(nextImportedModule instanceof OperationalTransformation) {
					OperationalTransformation ot = (OperationalTransformation) nextImportedModule;
					importedSignatureTypes = QvtOperationalUtil.collectValidModelParamaterTypes(ot);
				} else {
					// the only place where a library can declare its signature
					importedSignatureTypes = nextImportedModule.getUsedModelType();
				}

				if(refereceSignatureModelTypes.size() == importedSignatureTypes.size()) {
					boolean compatible = true;
					for (int i = 0; i < refereceSignatureModelTypes.size(); i++) {
						if(!QvtOperationalUtil.isCompatibleModelType(
								refereceSignatureModelTypes.get(i), importedSignatureTypes.get(i))) {
							compatible = false;
							break;
						}
					}

					if(compatible) {
						resolvedModules.put(moduleDesc.module, moduleDesc);
					}
				}
			}

			int matchCount = resolvedModules.size();
			switch(matchCount) {
			case 1:
				ModuleDesc moduleDesc = resolvedModules.entrySet().iterator().next().getValue();
				QvtOperationalModuleEnv importedModuleEnv = moduleDesc.env;
				Module importedModule = moduleDesc.module;

				ImportKindEnum importKindCS = moduleUsageCS.getImportKind();
				ImportKind kind = importKindCS == ImportKindEnum.ACCESS ? ImportKind.ACCESS : ImportKind.EXTENSION;

				if (env != importedModuleEnv) {
					env.addImport(kind, importedModuleEnv);
				}

				ModuleImport moduleImport = ExpressionsFactory.eINSTANCE.createModuleImport();
				moduleImport.setImportedModule(importedModule);
				moduleImport.setKind(kind);

				moduleImport.setStartPosition(moduleUsageCS.getStartOffset());
				moduleImport.setEndPosition(moduleUsageCS.getEndOffset());
				modulePathNameCS.setAst(importedModule);

				if(kind == ImportKind.EXTENSION) {
					if (importedModule.isIsBlackbox()) {
						String message = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_extendsOfBlackboxModule,
								QvtOperationalParserUtil.getStringRepresentation(modulePathNameCS));
						env.reportError(message, moduleRefCS);
					}

					if (module instanceof OperationalTransformation && importedModule instanceof OperationalTransformation) {
						validateImportedSignature(env, (OperationalTransformation) module, (OperationalTransformation) importedModule, moduleImport);
					}

					if (module instanceof Library && !(importedModule instanceof Library) && moduleUsageCS.eContainer() instanceof TransformationHeaderCS) {
						env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_libraryCantExtendNonLibrary,
								QvtOperationalParserUtil.getStringRepresentation(modulePathNameCS)), moduleRefCS);
					}
				}

				module.getModuleImport().add(moduleImport);
				break;
			case 0:
				String message = NLS.bind(ValidationMessages.UnresolvedModuleReference,
						QvtOperationalParserUtil.getStringRepresentation(modulePathNameCS));
				env.reportError(message, moduleRefCS);
				break;
			default:
				env.reportError(NLS.bind(ValidationMessages.AmbiguousModuleReference,
						QvtOperationalParserUtil.getStringRepresentation(modulePathNameCS)), moduleRefCS);
			}
		}
	}

	private void createModuleProperties(Module module, MappingModuleCS moduleCS, QvtOperationalModuleEnv env) throws SemanticException {

		for (ModulePropertyCS propCS : moduleCS.getProperties()) {
			EStructuralFeature prop = visitModulePropertyCS(propCS, env);
			if (prop == null) {
				continue;
			}

			EStructuralFeature eFeature = null;
			if (propCS instanceof ContextualPropertyCS) {

				if (module.isIsBlackbox()) {
					String warning = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_intermediatePropertyDefinitionInsideBlackboxModule,
							QvtOperationalParserUtil.getPropertyProblemNode(prop, env));
					env.reportError(warning, propCS);
				}

				EClass ctxType = ((ContextualProperty) prop).getContext();
				if (ctxType != null && env.lookupProperty(ctxType, prop.getName()) != null) {
					// need to check now for duplicates, as MDT OCL lookup now returns the most specific
					// redefinition [244886], so further checking lookup might not reach the original valid feature
					// being redefined (thus no collision would be detectable)
					HiddenElementAdapter.markAsHidden(prop);
				}
				if (module instanceof OperationalTransformation) {
					((OperationalTransformation) module).getIntermediateProperty().add(prop);
				}
				else {
					env.reportError(NLS.bind(ValidationMessages.IntermediatePropertyNotAllowed, new Object[] {}), propCS);
				}
				eFeature = prop;
				// using AST-CST map as this mapping is not optional but always required
				env.getASTNodeToCSTNodeMap().put(prop, propCS);
			} else {

				if (propCS instanceof LocalPropertyCS && module.isIsBlackbox()) {
					String warning = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_localPropertyDefinitionInsideBlackboxModule, prop.getName());
					env.reportError(warning, propCS);
				}

				//eFeature = env.getUMLReflection().createProperty(prop.getName(), prop.getEType());
				eFeature = prop;
				//QvtOperationalParserUtil.addLocalPropertyAST(eFeature, prop);

				EStructuralFeature feature = env.lookupProperty(module, prop.getName());

				if (feature != null) {
					EClassifier owner = getOCLEnvironment().getUMLReflection().getOwningClassifier(feature);

					// report error only if the existing feature is defined for the same module, i.e. if its owner is an explicit supertype of the module
					if(TypeUtil.compatibleTypeMatch(env, module, owner)) {
						HiddenElementAdapter.markAsHidden(eFeature);
						env.reportError(NLS.bind(ValidationMessages.ModulePropertyAlreadyDefined, new Object[] { prop.getName() }), propCS.getSimpleNameCS());
					}
				}

				if (propCS instanceof ConfigPropertyCS) {
					module.getConfigProperty().add(eFeature);
				}
			}

			module.getEStructuralFeatures().add(eFeature);
		}

		if (module instanceof OperationalTransformation) {

			OperationalTransformation transformation = (OperationalTransformation) module;

			IntermediatePropertyHierarchy intermPropDefHierarchy = transformation.getIntermediateProperty().isEmpty() ? null : new IntermediatePropertyHierarchy(module, env);
			for (EStructuralFeature prop : transformation.getIntermediateProperty()) {
				if (prop instanceof ContextualProperty) {
					ContextualProperty ctxProperty = (ContextualProperty) prop;
					EClass ctxType = ctxProperty.getContext();
					EStructuralFeature lookupProperty = ctxType != null ? env.lookupProperty(ctxType, prop.getName()) : null;

					boolean isAlreadyDefined = (lookupProperty != null && lookupProperty != ctxProperty) |
							HiddenElementAdapter.isMarkedAsHidden(ctxProperty);
					if(isAlreadyDefined || intermPropDefHierarchy.hasHierarchyClashes(ctxProperty)) {
						HiddenElementAdapter.markAsHidden(ctxProperty);
						String message = NLS.bind(ValidationMessages.IntermediatePropertyAlreadyDefined, prop.getName());

						int startPos = ctxProperty.getStartPosition();
						int endPos = ctxProperty.getEndPosition();
						CSTNode cstNode = QvtOperationalParserUtil.getPropertyProblemNode(prop, env);
						if(cstNode != null) {
							startPos = cstNode.getStartOffset();
							endPos = cstNode.getEndOffset();
						}
						env.reportError(message, startPos, endPos);
					}
				}
			}

			if (transformation.isIsBlackbox()) {
				Collection<CallHandler> handlers = BlackboxRegistry.INSTANCE.getBlackboxCallHandler((OperationalTransformation) module, env);
				if (handlers.isEmpty()) {
					String warning = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_noBlackboxImplementationFound,
							QvtOperationalParserUtil.getMappingModuleQualifiedName(moduleCS.getHeaderCS()));
					env.reportWarning(warning, moduleCS.getHeaderCS());
				}
				if (handlers.size() > 1) {
					String warning = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_ambiguousBlackboxImplementationFound,
							QvtOperationalParserUtil.getMappingModuleQualifiedName(moduleCS.getHeaderCS()));
					env.reportWarning(warning, moduleCS.getHeaderCS());
				}
			}
		}
	}

	protected void visitTransformationHeaderCS(TransformationHeaderCS headerCS,	QvtOperationalModuleEnv env, Module module) {
		for (QualifierKindCS nextQualifierCS : headerCS.getQualifiers()) {
			switch (nextQualifierCS) {
			case BLACKBOX:
				module.setIsBlackbox(true);
				break;
			case ABSTRACT:
			case STATIC:
				String warnMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unsupportedQualifierOnTransformation, nextQualifierCS.getName(),
						QvtOperationalParserUtil.getMappingModuleQualifiedName(headerCS));
				env.reportWarning(warnMessage, headerCS.getStartOffset(), headerCS.getPathNameCS().getEndOffset());
				break;
			}
		}

		Collection<QualifierKindCS> qualifierDups = QvtOperationalParserUtil.selectDuplicateQualifiers(headerCS.getQualifiers());
		for(QualifierKindCS duplicate : qualifierDups) {
			String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_duplicateQualifierOnTransformation,
					duplicate.getName(), QvtOperationalParserUtil.getMappingModuleQualifiedName(headerCS));
			env.reportWarning(errMessage, headerCS.getStartOffset(), headerCS.getPathNameCS().getEndOffset());
		}

		if(!QvtOperationalParserUtil.hasSimpleName(headerCS)) {
			env.reportError(NLS.bind(ValidationMessages.moduleNameMustBeSimpleIdentifierError, new Object[] {
					QvtOperationalParserUtil.getMappingModuleQualifiedName(headerCS) }), headerCS.getPathNameCS());
		}

		String unitSimpleName = QvtOperationalParserUtil.getMappingModuleSimpleName(headerCS);

		module.setName(unitSimpleName);
		module.setNsPrefix(unitSimpleName);

		headerCS.setAst(module);
		if(headerCS.getPathNameCS() != null) {
			headerCS.getPathNameCS().setAst(module);
		}

		if (module instanceof OperationalTransformation) {
			visitOperationalTransformationSignature(headerCS, env, (OperationalTransformation) module);
		} else if (module instanceof Library) {
			visitLibrarySignature(headerCS, env, (Library) module);
		} else {
			throw new RuntimeException("Unknown module type: " + module); //$NON-NLS-1$
		}

		if (headerCS.getTransformationRefineCS() != null) {
			env.reportWarning(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_transfRefineNotSupported,
					new Object[] { }), headerCS.getTransformationRefineCS());
		}
	}

	protected void visitOperationalTransformationSignature(TransformationHeaderCS headerCS,
			QvtOperationalModuleEnv env, OperationalTransformation module) {
		Set<String> paramNames = new LinkedHashSet<String>();
		for (ParameterDeclarationCS paramCS : headerCS.getParameters()) {
			EClassifier type = null;
			TypeCS paramTypeCS = (paramCS.getTypeSpecCS() != null) ? paramCS.getTypeSpecCS().getTypeCS() : null;
			boolean isSimpleName = false;
			if (paramTypeCS instanceof PathNameCS) {
				PathNameCS typePathNameCS = (PathNameCS) paramTypeCS;
				isSimpleName = typePathNameCS.getSimpleNames().size() == 1;
				type = typePathNameCS.getSimpleNames().isEmpty() ? null : env.getModelType(typePathNameCS.getSimpleNames().get(0).getValue());
				paramTypeCS.setAst(type);
			}
			if (type == null || !isSimpleName) {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_transfParamWrongType,
						new Object[] { }), paramTypeCS);
			}

			ModelParameter varParam = ExpressionsFactory.eINSTANCE.createModelParameter();
			paramCS.setAst(varParam);

			varParam.setRepresentedParameter(varParam);
			varParam.setStartPosition(paramCS.getStartOffset());
			varParam.setEndPosition(paramCS.getEndOffset());

			SimpleNameCS paramNameCS = paramCS.getSimpleNameCS();
			if(paramNameCS != null) {
				varParam.setName(paramNameCS.getValue());
				paramNameCS.setAst(varParam);
			} else {
				// just set the parameter position, to reduce nulls AST and avoid serialization issues
				varParam.setName(QvtEnvironmentBase.GENERATED_NAME_SPECIAL_PREFIX + String.valueOf(headerCS.getParameters().indexOf(paramCS)));
			}
			varParam.setEType(type);
			DirectionKindEnum directionKind = paramCS.getDirectionKind();
			if (directionKind == DirectionKindEnum.DEFAULT) {
				directionKind = DirectionKindEnum.IN;
			}
			varParam.setKind((DirectionKind) ExpressionsFactory.eINSTANCE.createFromString(
					ExpressionsPackage.eINSTANCE.getDirectionKind(), directionKind.getLiteral()));

			if (paramNames.contains(varParam.getName())) {
				env.reportError(NLS.bind(ValidationMessages.SameParamName, new Object[] { varParam.getName() }),
						(paramNameCS == null || paramNameCS.getValue().length() == 0) ? paramCS : paramNameCS);
			}
			paramNames.add(varParam.getName());

			module.getModelParameter().add(varParam);

			if(myCompilerOptions.isGenerateCompletionData()) {
				ASTBindingHelper.createCST2ASTBinding(paramCS, varParam);
			}
		}
	}

	private void visitLibrarySignature(TransformationHeaderCS headerCS,
			QvtOperationalModuleEnv env, Library module) {
		Set<ModelType> usedModelTypes = new HashSet<ModelType>();
		for (ParameterDeclarationCS paramCS : headerCS.getParameters()) {
			TypeCS paramTypeCS = (paramCS.getTypeSpecCS() != null) ? paramCS.getTypeSpecCS().getTypeCS() : null;
			if ((paramCS.getTypeSpecCS() != null) && (paramCS.getTypeSpecCS().getSimpleNameCS() != null)) {
				env.reportError(ValidationMessages.QvtOperationalVisitorCS_LibrarySignatureErrorModelExtentSpecified, paramCS.getTypeSpecCS().getSimpleNameCS());
			}
			if ((paramCS.getDirectionKind() != null) && (paramCS.getDirectionKind() != DirectionKindEnum.DEFAULT)) {
				env.reportError(ValidationMessages.QvtOperationalVisitorCS_LibrarySignatureErrorDirectionKindSpecified, paramCS);
			}
			if (paramCS.getSimpleNameCS() != null) {
				env.reportError(ValidationMessages.QvtOperationalVisitorCS_LibrarySignatureErrorParameterNameSpecified, paramCS);
			}
			ModelType modelType = null;
			if (paramTypeCS instanceof PathNameCS) {
				PathNameCS typePathNameCS = (PathNameCS) paramTypeCS;
				if (typePathNameCS.getSimpleNames().size() == 1) {
					modelType = env.getModelType(typePathNameCS.getSimpleNames().get(0).getValue());
					if (modelType != null && !usedModelTypes.add(modelType)) {
						env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_LibrarySignatureErrorDuplicateModelType,
								new Object[] { typePathNameCS.getSimpleNames().get(0) }), paramCS);
					}
				}
			}
			if (modelType == null) {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_transfParamWrongType,
						new Object[] { }), paramCS);
			} else if (!module.getUsedModelType().contains(modelType)) {
				module.getUsedModelType().add(modelType); // Normally, all used model types are
				// already present in the Library AST node
			}
		}
	}

	protected ModelType visitModelTypeCS(ModelTypeCS modelTypeCS, QvtOperationalModuleEnv env,
			Module module, ResourceSet resolutionRS) throws SemanticException {
		if (modelTypeCS == null) {
			return null;
		}

		SimpleNameCS identifierCS = modelTypeCS.getIdentifierCS();
		ModelType modelType = QvtOperationalStdLibrary.INSTANCE.createModel(identifierCS != null ? identifierCS.getValue() : null);
		identifierCS.setAst(modelType);

		module.getEClassifiers().add(modelType);
		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(modelTypeCS, modelType);
		}

		modelType.setStartPosition(modelTypeCS.getStartOffset());
		modelType.setEndPosition(modelTypeCS.getEndOffset());

		if (modelTypeCS.getComplianceKindCS() != null) {
			String complianceKind = visitLiteralExpCS(modelTypeCS.getComplianceKindCS(), env);
			if (complianceKind != null && complianceKind.length() > 0 && !QvtOperationalEnv.METAMODEL_COMPLIANCE_KIND_STRICT.equals(complianceKind)) {
				env.reportWarning(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unsupportedMetamodelComplianceKind,
						new Object[] { complianceKind }), modelTypeCS.getComplianceKindCS());
			}
			modelType.setConformanceKind(complianceKind);
		}

		for (int i = 0, n = modelTypeCS.getPackageRefs().size(); i < n; ++i) {
			PackageRefCS packageRefCS = modelTypeCS.getPackageRefs().get(i);

			EPackage resolvedMetamodel = null;

			StringLiteralExpCS uriCS = packageRefCS.getUriCS();
			if (uriCS != null) {
				String metamodelUri = visitLiteralExpCS(uriCS, env);
				resolvedMetamodel = resolveMetamodel(env, metamodelUri, Collections.<SimpleNameCS>emptyList(), uriCS);
				uriCS.setAst(resolvedMetamodel);
				// since metamodel URI is specified but can't be resolved then no further lookup should be done
				if (resolvedMetamodel == null) {
					continue;
				}
			}

			PathNameCS pathNameCS = packageRefCS.getPathNameCS();
			if (pathNameCS != null && !pathNameCS.getSimpleNames().isEmpty()) {
				String metamodelName = QvtOperationalParserUtil.getStringRepresentation(
						pathNameCS, EmfUtil.PATH_SEPARATOR);

				if (resolvedMetamodel == null) {
					resolvedMetamodel = resolveMetamodel(env, null, pathNameCS.getSimpleNames(), pathNameCS);
				}
				else {
					resolvedMetamodel = checkMetamodelPath(env, resolvedMetamodel, pathNameCS, metamodelName);
				}

				pathNameCS.setAst(resolvedMetamodel);
			}

			if (resolvedMetamodel != null) {
				validateMetamodel(env, resolvedMetamodel, modelType, module, packageRefCS);
				modelType.getMetamodel().add(resolvedMetamodel);

				for (++i; i < n; ++i) {
					packageRefCS = modelTypeCS.getPackageRefs().get(i);
					env.reportWarning(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_metamodelSinglePackageRefSupported,
							new Object[] { }), packageRefCS);
				}
				break;
			}
		}

		if (modelType.getMetamodel().isEmpty()) {
			return null;
		}

		modelTypeCS.setAst(modelType);
		return modelType;
	}

	private EPackage checkMetamodelPath(QvtOperationalModuleEnv env, EPackage resolvedMetamodel,
			PathNameCS pathNameCS, String metamodelName) {

		EList<SimpleNameCS> path = pathNameCS.getSimpleNames();
		// lookup nested package started from package specified by URI
		EPackage localPackage = MetamodelRegistry.lookupPackage(resolvedMetamodel, QvtOperationalParserUtil.getSequenceOfNames(path));
		if (localPackage != null) {
			return localPackage;
		}

		// lookup nested package started from root package of package specified by URI
		EPackage rootMetamodel = EmfUtil.getRootPackage(resolvedMetamodel);

		if (rootMetamodel != resolvedMetamodel) {
			localPackage = MetamodelRegistry.lookupPackage(rootMetamodel, QvtOperationalParserUtil.getSequenceOfNames(path));

			boolean isContainedBy = false;
			EPackage curPkg = localPackage;
			while (curPkg != null) {
				if (curPkg == resolvedMetamodel) {
					isContainedBy = true;
					break;
				}
				curPkg = curPkg.getESuperPackage();
			}

			if (localPackage != null && isContainedBy) {
				return localPackage;
			}
		}

		env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_metamodelPackageRefInconsistent,
				new Object[] { metamodelName }), pathNameCS);

		return null;
	}

	private void validateMetamodel(QvtOperationalModuleEnv env, EPackage resolvedMetamodel,
			ModelType modelType, Module module, CSTNode cstNode) throws SemanticException {

		String metamodelName = (resolvedMetamodel.getNsURI() == null ? resolvedMetamodel.getName() : resolvedMetamodel.getNsURI());
		List<EPackage> metamodels = modelType.getMetamodel();
		if (metamodels.contains(resolvedMetamodel)) {
			env.reportWarning(NLS.bind(ValidationMessages.DuplicateMetamodelImport,
					new Object[] { metamodelName }), cstNode);
		}
		if (module.getUsedModelType().contains(resolvedMetamodel)) {
			env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_metamodelAlreadyUsed,
					new Object[] { metamodelName }), cstNode);
		}
	}

	private EPackage resolveMetamodel(QvtOperationalModuleEnv env, String metamodelUri, List<SimpleNameCS> packagePath, CSTNode cstNode) {
		EPackage resolvedMetamodel = null;
		String metamodelName = (packagePath.isEmpty() ? metamodelUri : QvtOperationalParserUtil.getStringRepresentation(packagePath, EmfUtil.PATH_SEPARATOR));
		try {
			List<EPackage> registerMetamodels = MetamodelResolutionHelper.registerMetamodel(env, metamodelUri, QvtOperationalParserUtil.getSequenceOfNames(packagePath));

			if (registerMetamodels.isEmpty()) {
				env.reportError(NLS.bind(ValidationMessages.failedToResolveMetamodelError,
						new Object[] { metamodelName }), cstNode);
			}
			else {
				resolvedMetamodel = registerMetamodels.get(0);
			}

			if (registerMetamodels.size() > 1) {
				List<String> uriList = new ArrayList<String>(registerMetamodels.size());
				for (EPackage pack : registerMetamodels) {
					uriList.add(pack.getNsURI());
				}
				env.reportWarning(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_metamodelNameAmbiguous,
						new Object[] { metamodelName, uriList }), cstNode);
			}
		} catch (RuntimeException e) {
			env.reportError(NLS.bind(ValidationMessages.failedToResolveMetamodelError,
					new Object[] { metamodelName }), cstNode);
			QvtPlugin.error(e);
		}

		return resolvedMetamodel;
	}

	protected EAnnotation legacyRenameCS(RenameCS renameCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) throws SemanticException {

		String message = ValidationMessages.DeprecatedRenameStatement;
		QvtOperationalUtil.reportWarning(env, message, renameCS.getStartOffset(), renameCS.getEndOffset());


		String originalName = visitLiteralExpCS(renameCS.getOriginalName(), env);
		String newName = renameCS.getSimpleNameCS().getValue();
		return renameProperty(renameCS.getTypeCS(), renameCS.getOriginalName(), originalName, renameCS.getSimpleNameCS(), newName, env);
	}

	private EAnnotation renameProperty(TypeCS typeCS, CSTNode oringinalPropertyCS, String originalName, CSTNode newNameCS, String newName,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) throws SemanticException {

		EClassifier type = visitTypeCS(typeCS, null, env);
		if (type == null) {
			return null;
		}

		EStructuralFeature originalProperty = env.lookupProperty(type, originalName);
		if (originalProperty == null) {
			QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.noPropertyInTypeError, originalName,
					QvtOperationalTypesUtil.getTypeFullName(type)), oringinalPropertyCS);
			return null;
		}

		EStructuralFeature newProperty = env.lookupProperty(type, newName);
		if (newProperty != null) {
			QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.propertyAlreadyExistsInTypeError, newName,
					QvtOperationalTypesUtil.getTypeFullName(type)), newNameCS);
			return null;
		}

		EAnnotation aliasTag = createTag(QvtOperationalEnv.TAG_ALIAS, newName, originalProperty);
		// FIXME - we should required QVT specific requirement env in this operation argument
		if(env instanceof QvtOperationalEnv) {
			QvtOperationalModuleEnv moduleEnv = getModuleContextEnv((QvtOperationalEnv) env);
			if(moduleEnv.getModuleContextType() != null) {
				moduleEnv.getModuleContextType().getOwnedTag().add(aliasTag);
			}
		}

		return aliasTag;
	}

	private List<org.eclipse.ocl.ecore.OCLExpression> visitMappingSectionCS(MappingSectionCS mappingSectionCS,
			QvtOperationalEnv env) throws SemanticException {
		try {
			if (mappingSectionCS instanceof MappingInitCS) {
				env.enterInitMappingSection();
			}
			List<org.eclipse.ocl.ecore.OCLExpression> statements = new ArrayList<org.eclipse.ocl.ecore.OCLExpression>(mappingSectionCS
					.getStatements().size());
			for (OCLExpressionCS statementCS : mappingSectionCS.getStatements()) {
				if (statementCS == null) {
					continue;
				}
				org.eclipse.ocl.ecore.OCLExpression statement = visitOclExpressionCS(statementCS, env);
				if (statement != null) {
					statements.add(statement);
				}
			}
			return statements;
		}
		finally {
			if (mappingSectionCS instanceof MappingInitCS) {
				env.exitInitMappingSection();
			}
		}
	}

	protected void visitMappingMethodCS(MappingMethodCS methodCS, QvtOperationalModuleEnv env, ImperativeOperation declaredOperation)
			throws SemanticException {
		methodCS.setAst(declaredOperation);

		QvtOperationalEnv methodEnv = env.getFactory().createOperationContext(env, declaredOperation);
		// put parameters to local map
		MappingDeclarationCS mappingDeclarationCS = methodCS.getMappingDeclarationCS();
		if (mappingDeclarationCS != null) {
			for (ParameterDeclarationCS paramCS : mappingDeclarationCS.getParameters()) {
				VarParameter param = (VarParameter)paramCS.getAst();
				methodEnv.addElement(param.getName(), param, true);
			}
		}

		if (methodCS instanceof ConstructorCS) {
			visitConstructorCS((ConstructorCS) methodCS, env, methodEnv, declaredOperation);
		}
		else if (methodCS instanceof MappingRuleCS) {
			visitMappingRuleCS((MappingRuleCS) methodCS, env, methodEnv, (MappingOperation)declaredOperation);
		}
		else {
			visitMappingQueryCS((MappingQueryCS) methodCS, env, methodEnv, declaredOperation);
		}

		if(QvtOperationalParserUtil.getOwningModule(declaredOperation).isIsBlackbox()) {
			String warning = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_operationDefinitionInsideBlackboxModule,
					QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
			env.reportError(warning, QvtOperationalParserUtil.getMethodHeaderProblemNodeCS(methodCS));
		}

		if(declaredOperation.isIsBlackbox() && !QvtOperationalParserUtil.isDisjunctiveMappingOperation(methodCS)) {
			Collection<CallHandler> handlers = BlackboxRegistry.INSTANCE.getBlackboxCallHandler(declaredOperation, env);
			if (handlers.isEmpty()) {
				String warning = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_noBlackboxImplementationFound,
						QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
				env.reportWarning(warning, QvtOperationalParserUtil.getMethodHeaderProblemNodeCS(methodCS));
			}
			if (handlers.size() > 1) {
				String warning = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_ambiguousBlackboxImplementationFound,
						QvtOperationalParserUtil.getMappingStringRepresentation(methodCS));
				env.reportWarning(warning, QvtOperationalParserUtil.getMethodHeaderProblemNodeCS(methodCS));
			}
		}
	}

	private void visitConstructorCS(ConstructorCS methodCS, QvtOperationalEnv env, QvtOperationalEnv newEnv, ImperativeOperation constructor) throws SemanticException {
		constructor.setEndPosition(methodCS.getEndOffset());

		if (constructor.getContext() == null) {
			env.reportError(ValidationMessages.ContextlessConstructor, methodCS.getMappingDeclarationCS());
		}
		else {
			if (!constructor.getContext().getEType().getName().equals(constructor.getName())) {
				env.reportError(ValidationMessages.ConstructorNameMismatch, methodCS.getMappingDeclarationCS());
			}
		}

		newEnv.deleteElement(Environment.SELF_VARIABLE_NAME);

		VarParameter varResult = ExpressionsFactory.eINSTANCE.createVarParameter();
		varResult.setName(Environment.RESULT_VARIABLE_NAME);
		if (constructor.getContext() != null) {
			varResult.setEType(constructor.getContext().getEType());
		}
		varResult.setKind(DirectionKind.OUT);
		varResult.setRepresentedParameter(varResult);
		newEnv.addElement(varResult.getName(), varResult, false);

		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(methodCS, constructor, newEnv);
		}

		if (methodCS.getBody() != null ) {
			OperationBody body = ExpressionsFactory.eINSTANCE.createConstructorBody();
			constructor.setBody(body);
			body.setStartPosition(methodCS.getBody().getStartOffset());
			body.setEndPosition(methodCS.getBody().getEndOffset());

			List<org.eclipse.ocl.ecore.OCLExpression> expressions = new ArrayList<org.eclipse.ocl.ecore.OCLExpression>(methodCS.getBody().getBodyExpressions().size());
			for (OCLExpressionCS exprCS : methodCS.getBody().getBodyExpressions()) {
				if (exprCS == null) {
					continue;
				}
				org.eclipse.ocl.ecore.OCLExpression expr = visitOclExpressionCS(exprCS, newEnv);
				if (expr != null) {
					expressions.add(expr);
				}
			}

			body.getContent().addAll(expressions);
		}

		// adjust implicit variables for serialization
		consolidateImplicitVariables(newEnv);
		//
	}

	private ImperativeOperation visitMappingRuleCS(MappingRuleCS methodCS, QvtOperationalEnv env, QvtOperationalEnv newEnv, final MappingOperation operation)
			throws SemanticException {
		env.registerMappingOperation(operation);
		operation.setEndPosition(methodCS.getEndOffset());

		if (QvtOperationalParserUtil.isContextual(operation) && operation.getContext().getKind() == DirectionKind.OUT) {
			env.reportError(ValidationMessages.ContextParamMustBeInOrInout, methodCS.getMappingDeclarationCS()
					.getDirectionKindCS());
		}

		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(methodCS, operation, newEnv);
		}

		for (OCLExpressionCS guardExp : methodCS.getGuards()) {
			org.eclipse.ocl.ecore.OCLExpression guard = visitOclExpressionCS(guardExp, newEnv);
			if (guard != null) {
				EClassifier guardType = guard.getType();
				if (guardType != newEnv.getOCLStandardLibrary().getBoolean()) {
					newEnv.reportError(NLS.bind(ValidationMessages.mappingGuardNotBooleanError,
							new Object[] { QvtOperationalTypesUtil.getTypeFullName(guardType) }), guardExp);
				}
				else {
					operation.getWhen().add(guard);
				}
			}
		}

		BlockExp whereExp = null;

		if (!methodCS.getPosts().isEmpty()) {
			whereExp = ImperativeOCLFactory.eINSTANCE.createBlockExp();
			whereExp.setStartPosition(methodCS.getPosts().get(0).getStartOffset());
			whereExp.setEndPosition(methodCS.getPosts().get(methodCS.getPosts().size()-1).getEndOffset());
			whereExp.setType(env.getOCLStandardLibrary().getOclVoid());
		}

		for (OCLExpressionCS postExp : methodCS.getPosts()) {
			org.eclipse.ocl.ecore.OCLExpression post = visitOclExpressionCS(postExp, newEnv);
			if (post != null) {
				EClassifier postType = post.getType();
				if (postType != newEnv.getOCLStandardLibrary().getBoolean()) {
					newEnv.reportError(NLS.bind(ValidationMessages.mappingWhereNotBooleanError,
							new Object[] { QvtOperationalTypesUtil.getTypeFullName(postType) }), postExp);
				}
				else {
					whereExp.getBody().add(post);
				}
			}
		}

		operation.setWhere(whereExp);

		List<org.eclipse.ocl.ecore.OCLExpression> inits = Collections.emptyList();
		if ((methodCS.getMappingBody() != null) && (methodCS.getMappingBody().getMappingInitCS() != null)) {
			inits = visitMappingSectionCS(methodCS.getMappingBody().getMappingInitCS(), newEnv);
		}

		// legacy support for 'result' tuples inside mapping body and 'end' section
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=432112
		if(methodCS.getMappingDeclarationCS().getResult().size() > 1) {
			Variable<EClassifier, EParameter> var = EcoreFactory.eINSTANCE.createVariable();
			var.setName(Environment.RESULT_VARIABLE_NAME);
			var.setType(operation.getEType());
			newEnv.addElement(var.getName(), var, true);
		}

		MappingBody body = null;
		MappingBodyCS mappingBodyCS = (methodCS.getMappingBody() == null) ? null : methodCS.getMappingBody().getMappingBodyCS();
		if (mappingBodyCS != null) {
			body = visitMappingBodyCS(mappingBodyCS, newEnv, methodCS);
			if (body != null) {
				body.setStartPosition(methodCS.getMappingBody().getStartOffset());
				body.setEndPosition(methodCS.getMappingBody().getEndOffset());
			}
		} else {
			body = ExpressionsFactory.eINSTANCE.createMappingBody();
			body.setStartPosition(mappingBodyCS == null ? methodCS.getStartOffset() : mappingBodyCS.getStartOffset());
			body.setEndPosition(mappingBodyCS == null ? methodCS.getEndOffset() : mappingBodyCS.getEndOffset());
		}

		List<org.eclipse.ocl.ecore.OCLExpression> ends = Collections.emptyList();
		if ((methodCS.getMappingBody() != null) && (methodCS.getMappingBody().getMappingEndCS() != null)) {
			ends = visitMappingSectionCS(methodCS.getMappingBody().getMappingEndCS(), newEnv);
		}

		operation.setBody(body);

		if (body != null) {
			body.getInitSection().addAll(inits);
			body.getEndSection().addAll(ends);
		}

		for (EParameter parameter : operation.getEParameters()) {
			VarParameter varParameter = (VarParameter) parameter;
			if (varParameter.getKind() == DirectionKind.OUT) {
				checkAbstractOutParamsInitialized(varParameter, operation, methodCS, env);
			}
		}
		for (VarParameter varParameter : operation.getResult()) {
			checkAbstractOutParamsInitialized(varParameter, operation, methodCS, env);
		}

		processMappingExtensions(methodCS, operation, env);

		// adjust implicit variables for serialization
		consolidateImplicitVariables(newEnv);

		return operation;
	}

	private static void consolidateImplicitVariables(QvtOperationalEnv newEnv) {
		EOperation eOperation = newEnv.getContextOperation();
		OperationBody body = null;
		if(eOperation instanceof ImperativeOperation) {
			body = ((ImperativeOperation)eOperation).getBody();
		}

		if(body == null) {
			return;
		}
		for (Variable<EClassifier, EParameter> var : newEnv.getImplicitVariables()) {
			if(var.eContainer() == null) {
				body.getVariable().add((org.eclipse.ocl.ecore.Variable)var);
			}
		}
		for (Variable<EClassifier, EParameter> var : newEnv.getVariables()) {
			if(var.eContainer() == null) {
				body.getVariable().add((org.eclipse.ocl.ecore.Variable)var);
			}
		}
	}

	private void visitMappingQueryCS(MappingQueryCS methodCS, QvtOperationalEnv env, QvtOperationalEnv newEnv, ImperativeOperation helper)
			throws SemanticException {
		helper.setEndPosition(methodCS.getEndOffset());

		if (QvtOperationalParserUtil.isContextual(helper) && helper.getContext().getKind() != DirectionKind.IN) {
			env.reportError(ValidationMessages.ContextParamMustBeIn, methodCS.getMappingDeclarationCS()
					.getSimpleNameCS());
		}

		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(methodCS, helper, newEnv);
		}

		if (methodCS.getBody() != null) {
			OperationBody body = ExpressionsFactory.eINSTANCE.createOperationBody();
			helper.setBody(body);
			body.setStartPosition(methodCS.getBody().getStartOffset());
			body.setEndPosition(methodCS.getBody().getEndOffset());

			List<org.eclipse.ocl.ecore.OCLExpression> expressions = new ArrayList<org.eclipse.ocl.ecore.OCLExpression>();
			for (OCLExpressionCS exprCS : methodCS.getBody().getBodyExpressions()) {
				if (exprCS == null) {
					continue;
				}
				org.eclipse.ocl.ecore.OCLExpression expr = visitOclExpressionCS(exprCS, newEnv);
				if (expr != null) {
					expressions.add(expr);
				}
			}

			body.getContent().addAll(expressions);
		}

		if (helper.getResult().size() <= 1) {
			EClassifier returnType = (helper.getResult().isEmpty() ? helper.getEType() : helper.getResult().get(0).getEType());
			OCLExpression<EClassifier> helperLastExp = helper.getBody() != null && !helper.getBody().getContent().isEmpty()
					? helper.getBody().getContent().get(helper.getBody().getContent().size()-1) : null;
					EClassifier helperType = helperLastExp != null ? helperLastExp.getType() : null;
					if (returnType == env.getOCLStandardLibrary().getOclVoid()) {
						// OK
					}
					else if (helperType != null && !QvtOperationalParserUtil.isAssignableToFrom(env, returnType, helperType)
							&& !isOclExpDeterministic(helperLastExp)) {
						env.reportError(NLS.bind(ValidationMessages.bodyTypeNotCompatibleWithReturnTypeError, new Object[] {
								QvtOperationalTypesUtil.getTypeFullName(helperType),
								QvtOperationalTypesUtil.getTypeFullName(returnType)
						}),
								methodCS.getMappingDeclarationCS());
					}

					// adjust implicit variables for serialization
					consolidateImplicitVariables(newEnv);
					//
		}

		// add warning if single expression in non-simple body definition does not use return expression
		// as only during CST analysis we know that non-simple body variant is used
		// => query foo() : String = 'foo';
		if(!methodCS.isIsSimpleDefinition() && helper.getResult().size() == 1 && helper.getBody() != null) {
			EList<org.eclipse.ocl.ecore.OCLExpression> contents = helper.getBody().getContent();
			if(contents.size() == 1 && !isOclExpDeterministic(contents.get(0))) {
				env.reportWarning(ValidationMessages.useReturnExpForOperationResult, methodCS.getMappingDeclarationCS());
			}
		}
	}

	static boolean isOclExpDeterministic(org.eclipse.ocl.expressions.OCLExpression<EClassifier> exp) {
		return exp instanceof ReturnExp
				|| exp instanceof RaiseExp
				|| (exp instanceof BlockExp && isBlockExpDeterministic((BlockExp) exp))
				|| (exp instanceof org.eclipse.ocl.ecore.IfExp && isIfExpDeterministic((org.eclipse.ocl.ecore.IfExp) exp))
				|| (exp instanceof SwitchExp && isSwitchExpDeterministic((SwitchExp) exp))
				|| (exp instanceof ComputeExp && isOclExpDeterministic(((ComputeExp) exp).getBody()))
				;
	}

	private static boolean isBlockExpDeterministic(BlockExp exp) {
		return exp.getBody().size() == 1
				&& isOclExpDeterministic(exp.getBody().get(0));
	}

	private static boolean isIfExpDeterministic(org.eclipse.ocl.ecore.IfExp exp) {
		return isOclExpDeterministic(exp.getThenExpression())
				&& isOclExpDeterministic(exp.getElseExpression());
	}

	private static boolean isSwitchExpDeterministic(SwitchExp exp) {
		if (!isOclExpDeterministic(exp.getElsePart())) {
			return false;
		}
		for (AltExp altExp : exp.getAlternativePart()) {
			if (!isOclExpDeterministic(altExp.getBody())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * TODO - make a common resolution operation, reusable in for ResolveInExp too.
	 */
	private List<MappingOperation> resolveMappingOperationReference(ScopedNameCS identifierCS, QvtOperationalEnv env) {
		List<MappingOperation> result = Collections.emptyList();

		TypeCS typeCS = identifierCS.getTypeCS();
		EClassifier owningType = null;
		if(typeCS != null) {
			owningType = visitTypeCS(typeCS, null, env);
			if(owningType != null && identifierCS.getName() != null) {
				result = env.lookupMappingOperations(TypeUtil.resolveType(env, owningType), identifierCS.getName());
			}
		} else if(identifierCS.getName() != null) {
			// TODO - review why lookup does not return MappingOperation type collection
			owningType = env.getModuleContextType();
			result = env.lookupMappingOperations(owningType, identifierCS.getName());
		}
		// filter out inherited and overridden mappings
		if(!result.isEmpty()) {
			List<MappingOperation> ownerLocalOpers = new ArrayList<MappingOperation>(result.size());
			for (MappingOperation operation : result) {
				EClassifier owner = env.getUMLReflection().getOwningClassifier(operation);
				if ((typeCS == null && owner == null) || (TypeUtil.resolveType(env, owner) == owningType)) {
					ownerLocalOpers.add(operation);
				}
			}

			List<MappingOperation> filteredMappings = new ArrayList<MappingOperation>(ownerLocalOpers.size());
			for (MappingOperation mapping : ownerLocalOpers) {
				if (!ownerLocalOpers.contains(mapping.getOverridden())) {
					filteredMappings.add(mapping);
				}
			}

			result = filteredMappings;
		}
		// validate the result
		if(result.isEmpty()) {
			if(owningType != null) {
				// unresolved type reported above by visitTypeCS(...)
				String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unresolvedMappingOperationReference,
						QvtOperationalParserUtil.getStringRepresentation(identifierCS));
				env.reportError(errMessage, identifierCS);
			}
		} else if(result.size() > 1) {
			String errMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_ambiguousMappingOperationReference,
					QvtOperationalParserUtil.getStringRepresentation(identifierCS));
			env.reportError(errMessage, identifierCS);
		}

		return result;
	}

	private void processMappingExtensions(MappingRuleCS mappingCS, MappingOperation operation, QvtOperationalEnv env) {
		if((mappingCS.getMappingDeclarationCS() != null) && !mappingCS.getMappingDeclarationCS().getMappingExtension().isEmpty()) {
			for (MappingExtensionCS extensionCS : mappingCS.getMappingDeclarationCS().getMappingExtension()) {
				MappingExtensionKindCS kind = extensionCS.getKind();

				for (ScopedNameCS identifierCS : extensionCS.getMappingIdentifiers()) {
					List<MappingOperation> mappings = resolveMappingOperationReference(identifierCS, env);
					if(mappings.size() == 1) {
						boolean isAdded = false;
						MappingOperation extendedMapping = mappings.get(0);
						if(kind == MappingExtensionKindCS.INHERITS) {
							if (operation.isIsBlackbox()) {
								env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unsupportedInheritanceOnBlackboxMapping,
										QvtOperationalParserUtil.getStringRepresentation(identifierCS), QvtOperationalParserUtil.getMappingStringRepresentation(mappingCS)),
										identifierCS);
							}
							if (extendedMapping.isIsBlackbox()) {
								env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unsupportedInheritedBlackboxMapping,
										QvtOperationalParserUtil.getStringRepresentation(identifierCS), QvtOperationalParserUtil.getMappingStringRepresentation(mappingCS)),
										identifierCS);
							}
							isAdded = operation.getInherited().add(extendedMapping);
							MappingExtensionHelper.bind2SourceElement(operation, identifierCS, kind);
						}
						else if(kind == MappingExtensionKindCS.MERGES) {
							if (extendedMapping.isIsBlackbox()) {
								env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_unsupportedMergedBlackboxMapping,
										QvtOperationalParserUtil.getStringRepresentation(identifierCS), QvtOperationalParserUtil.getMappingStringRepresentation(mappingCS)),
										identifierCS);
							}
							isAdded = operation.getMerged().add(extendedMapping);
							MappingExtensionHelper.bind2SourceElement(operation, identifierCS, kind);
						}
						else if(kind == MappingExtensionKindCS.DISJUNCTS) {
							isAdded = operation.getDisjunct().add(extendedMapping);
							MappingExtensionHelper.bind2SourceElement(operation, identifierCS, kind);
						}

						if(!isAdded) {
							// Note: duplicates checked here as the mapping AST is {unique, ordered}
							env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_duplicateMappingRereferenceInExtensionKind,
									kind.getName(), QvtOperationalParserUtil.getStringRepresentation(identifierCS)),
									identifierCS);
						}

						if(getCompilerOptions().isGenerateCompletionData()) {
							ASTBindingHelper.createCST2ASTBinding(identifierCS, extendedMapping, false, null);
						}
					}
				}
			}
		}
	}

	protected boolean visitMappingDeclarationCS(MappingMethodCS mappingMethodCS, QvtOperationalModuleEnv env, ImperativeOperation operation) throws SemanticException {
		MappingDeclarationCS mappingDeclarationCS = mappingMethodCS.getMappingDeclarationCS();
		if (mappingDeclarationCS == null) {
			return false;
		}
		mappingDeclarationCS.setAst(operation);
		operation.setIsBlackbox(mappingMethodCS.isBlackBox());
		operation.setStartPosition(mappingDeclarationCS.getStartOffset());
		operation.setEndPosition(mappingDeclarationCS.getEndOffset());
		operation.setName(mappingDeclarationCS.getSimpleNameCS().getValue());

		DirectionKind contextDirection = DirectionKind.IN;
		if ((mappingDeclarationCS.getDirectionKindCS() != null) && (mappingDeclarationCS.getDirectionKindCS().getDirectionKind() != DirectionKindEnum.DEFAULT)) {
			contextDirection = (DirectionKind) ExpressionsFactory.eINSTANCE.createFromString(
					ExpressionsPackage.eINSTANCE.getDirectionKind(), mappingDeclarationCS.getDirectionKindCS()
					.getDirectionKind().getLiteral());
		}

		TypeCS contextTypeCS = mappingDeclarationCS.getContextType();
		EClassifier contextType;
		if (contextTypeCS != null) {
			contextType = visitTypeCS(contextTypeCS, contextDirection, env);
			if (contextType == null) {
				contextType = env.getModuleContextType();
			}
		} else {
			contextType = env.getModuleContextType();
		}

		boolean isEntryPoint = QvtOperationalEnv.MAIN.equals(mappingDeclarationCS.getSimpleNameCS().getValue());
		boolean isMapping = operation instanceof MappingOperation;
		boolean createMappingParams = isEntryPoint || isMapping;

		List<EParameter> params = operation.getEParameters();
		for (ParameterDeclarationCS paramCS : mappingDeclarationCS.getParameters()) {
			VarParameter param = visitParameterDeclarationCS(paramCS, createMappingParams, env);
			if (param == null) {
				return false;
			}

			params.add(param);
		}

		if (contextTypeCS != null) {
			MappingParameter mappingParam = createMappingParams ? ExpressionsFactory.eINSTANCE.createMappingParameter() : null;
			VarParameter varContext = createMappingParams ? mappingParam : ExpressionsFactory.eINSTANCE.createVarParameter();

			varContext.setRepresentedParameter(varContext);
			varContext.setName(Environment.SELF_VARIABLE_NAME);
			varContext.setStartPosition(contextTypeCS.getStartOffset());
			varContext.setEndPosition(contextTypeCS.getEndOffset());

			varContext.setEType(contextType);
			varContext.setKind(contextDirection);
			if(mappingParam != null) {
				if (mappingParam.getExtent() == null) {
					mappingParam.setExtent(env.resolveModelParameter(contextType, varContext.getKind()));
				}
			}

			operation.setContext(varContext);
		}

		EList<ParameterDeclarationCS> resultParams = mappingDeclarationCS.getResult();
		for(ParameterDeclarationCS nextResultParamCS : resultParams) {
			TypeSpecPair nextResultTypeSpec = visitTypeSpecCS(nextResultParamCS.getTypeSpecCS(), DirectionKind.OUT, env);
			if (nextResultTypeSpec.myType == null) {
				nextResultTypeSpec.myType = env.getOCLStandardLibrary().getOclVoid();
			}

			VarParameter varResult = createMappingResultParam(nextResultParamCS, createMappingParams, nextResultTypeSpec, env);
			if(resultParams.size() == 1) {
				varResult.setName(Environment.RESULT_VARIABLE_NAME);
				if(nextResultParamCS.getSimpleNameCS() != null) {
					String message = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_resultParamNameNotAllowed, nextResultParamCS.getSimpleNameCS().getValue());
					env.reportError(message, nextResultParamCS.getSimpleNameCS());
				}

				if(nextResultParamCS.isSetDirectionKind()) {
					env.reportError(ValidationMessages.QvtOperationalVisitorCS_resultParamNameDirectionNotAllowed, nextResultParamCS);
				}
			}
			operation.getResult().add(varResult);
		}

		// set operation return type
		if(resultParams.isEmpty()) {
			// no result parameter should exist
			operation.setEType(env.getOCLStandardLibrary().getOclVoid());
		} else if(resultParams.size() > 1) {
			// create extra tuple-type the result type
			operation.setEType(createResultTupleType(operation, env));
		} else {
			operation.setEType(operation.getResult().get(0).getEType());
		}

		return true;
	}

	private EClassifier createResultTupleType(ImperativeOperation operation, QvtOperationalEnv env) {
		EList<VarParameter> resultParams = operation.getResult();
		assert resultParams.size() > 1;

		List<Variable<EClassifier, EParameter>> parts = new ArrayList<Variable<EClassifier,EParameter>>(resultParams.size());
		for(VarParameter resultParam : resultParams) {
			Variable<EClassifier, EParameter> var = oclFactory.createVariable();
			uml.setName(var, resultParam.getName());
			uml.setType(var, resultParam.getEType());

			parts.add(var);
		}
		return env.getTypeResolver().resolve((EClassifier)env.getOCLFactory().createTupleType(parts));
	}

	private VarParameter createMappingResultParam(ParameterDeclarationCS paramCS, boolean createMappingParam, TypeSpecPair typeSpec, QvtOperationalEnv env) {
		MappingParameter mappingParam = createMappingParam ? ExpressionsFactory.eINSTANCE.createMappingParameter() : null;
		VarParameter varResult = createMappingParam ? mappingParam : ExpressionsFactory.eINSTANCE.createVarParameter();
		varResult.setStartPosition(paramCS.getStartOffset());
		varResult.setEndPosition(paramCS.getEndOffset());

		SimpleNameCS simpleNameCS = paramCS.getSimpleNameCS();
		if(simpleNameCS != null) {
			simpleNameCS.setAst(varResult);
			varResult.setName(simpleNameCS.getValue());
		}

		if(varResult.getName() == null){
			varResult.setName(""); //$NON-NLS-1$
		}

		varResult.setEType(typeSpec.myType);
		varResult.setKind(DirectionKind.OUT);

		if(mappingParam != null) {
			mappingParam.setExtent(typeSpec.myExtent);
			if (mappingParam.getExtent() == null) {
				QvtOperationalModuleEnv moduleEnv = getModuleContextEnv(env);
				mappingParam.setExtent(moduleEnv.resolveModelParameter(typeSpec.myType, varResult.getKind()));
			}
		}

		if(getCompilerOptions().isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(paramCS, varResult);
		}

		varResult.setRepresentedParameter(varResult);

		paramCS.setAst(varResult);

		return varResult;
	}

	@SuppressWarnings("unchecked")
	private org.eclipse.ocl.ecore.OCLExpression visitAssignStatementCS(AssignStatementCS expressionCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		OCLExpressionCS lValueCS = expressionCS.getLValueCS();
		if ((lValueCS instanceof OperationCallExpCS)
				|| !(lValueCS instanceof VariableExpCS) && !(lValueCS instanceof FeatureCallExpCS)) {
			QvtOperationalUtil.reportError(env, ValidationMessages.notAnLValueError, lValueCS);
			return null;
		}

		org.eclipse.ocl.ecore.OCLExpression rightExpr = visitOclExpressionCS(expressionCS.getOclExpressionCS(), env);
		if (rightExpr == null) {
			return null;
		}

		org.eclipse.ocl.ecore.OCLExpression lValue = oclExpressionCS(lValueCS, env);

		boolean isMutation = QvtOperationalUtil.isMutableCollectionType(lValue.getType()) && expressionCS.isIncremental();

		if (lValue instanceof VariableExp<?, ?>) {
			VariableExp<EClassifier, EParameter> variableExp = (VariableExp<EClassifier, EParameter>) lValue;
			String referredVariableName = variableExp.getName();

			Variable<EClassifier, EParameter> variable = variableExp.getReferredVariable();
			if (variable == null) { // We mustn't be here. Must have been detected by OCL
				QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.unresolvedNameError, new Object[] { referredVariableName }),
						lValueCS);
				return null;
			}
			else {
				Variable<EClassifier, EParameter> implicitSource = env.lookupImplicitSourceForProperty(referredVariableName);
				if (implicitSource != null) {
					String typeName = implicitSource.getType() != null ? implicitSource.getType().getName() : ""; //$NON-NLS-1$"
					QvtOperationalUtil.reportWarning(env, NLS.bind(ValidationMessages.HidingVariable,
							new Object[] { referredVariableName, typeName }), lValueCS);
				}
			}

			if (Environment.RESULT_VARIABLE_NAME.equals(referredVariableName)) {
				if (!isInsideMappintInitSection(expressionCS)) {
					if (expressionCS.isIncremental()) {
						if (!QvtOperationalUtil.isMutableCollectionType(variable.getType())) {
							QvtOperationalUtil.reportError(env, ValidationMessages.QvtOperationalVisitorCS_incrementalAssignmentResultNotAllowed, lValueCS);
							return null;
						}
					}
					else {
						QvtOperationalUtil.reportError(env, ValidationMessages.QvtOperationalVisitorCS_assignmentResultNotAllowed, lValueCS);
						return null;
					}
				}
			}
			if (Environment.SELF_VARIABLE_NAME.equals(referredVariableName)) {
				QvtOperationalUtil.reportError(env, ValidationMessages.CantAssignToSelf, lValueCS);
				return null;
			}
			if (QvtOperationalEnv.THIS.equals(referredVariableName)) {
				QvtOperationalUtil.reportError(env, ValidationMessages.CantAssignToThis, lValueCS);
				return null;
			}

			QvtOperationalParserUtil.validateVariableModification(variable, lValueCS, null, env, !isMutation);
			QvtOperationalParserUtil.validateAssignment(false, variable.getName(), variable.getType(), rightExpr.getType(),
					expressionCS.isIncremental(), lValueCS, expressionCS.getOclExpressionCS(), env);
		} else if (lValue instanceof PropertyCallExp<?, ?>) {
			PropertyCallExp<EClassifier, EStructuralFeature> propertyCallExp = (PropertyCallExp<EClassifier, EStructuralFeature>) lValue;
			EStructuralFeature property = propertyCallExp.getReferredProperty();
			if (property == null) { // We mustn't be here. This case is to be handled below
				QvtOperationalUtil.reportError(env, ValidationMessages.invalidPropertyReferenceError, lValueCS);
				return null;
			} else {
				if (!property.isChangeable()) {
					QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.ReadOnlyProperty, property.getName()), lValueCS);
				} else {
					OCLExpression<EClassifier> source = propertyCallExp.getSource();

					if (source.getType() instanceof TupleType && !isMutation) {
						QvtOperationalUtil.reportError(env,
								NLS.bind(ValidationMessages.tupleMutationError, property.getName(), source.getName()),
								lValueCS);
						return null;
					}

					if (source instanceof VariableExp) {
						VariableExp<EClassifier, EParameter> sourceExp = (VariableExp<EClassifier, EParameter>) source;
						Variable<EClassifier, EParameter> sourceVariable = sourceExp.getReferredVariable();
						QvtOperationalParserUtil.validateVariableModification(sourceVariable, lValueCS, property, env, false);
					}

					QvtOperationalParserUtil.validateAssignment(true, property.getName(), env.getUMLReflection().getOCLType(property),
							rightExpr.getType(), expressionCS.isIncremental(), lValueCS, expressionCS.getOclExpressionCS(), env);
				}
			}
		} else {
			// TODO: This code is to be transferred to the AST validator
			if ((lValue != null) && !(lValue instanceof InvalidLiteralExp)) { // to avoid induced errors on unresolved variables
				QvtOperationalUtil.reportError(env, ValidationMessages.notAnLValueError, lValueCS);
				return null;
			}
		}

		AssignExp result = ImperativeOCLFactory.eINSTANCE.createAssignExp();
		result.setStartPosition(expressionCS.getStartOffset());
		result.setEndPosition(expressionCS.getEndOffset());
		result.getValue().add(rightExpr);
		result.setIsReset(!expressionCS.isIncremental());
		result.setLeft(lValue);
		if (lValue != null) {
			result.setType(lValue.getType());
		}
		return result;
	}

	private boolean isInsideMappintInitSection(AssignStatementCS expressionCS) {
		EObject eContainer = expressionCS.eContainer();
		while (eContainer != null) {
			if (eContainer instanceof MappingInitCS) {
				return true;
			}
			eContainer = eContainer.eContainer();
		}
		return false;
	}

	private VarParameter visitParameterDeclarationCS(ParameterDeclarationCS paramCS, boolean createMappingParam,
			QvtOperationalModuleEnv env) throws SemanticException {
		DirectionKindEnum directionKindEnum = paramCS.getDirectionKind();
		if (directionKindEnum == DirectionKindEnum.DEFAULT) {
			directionKindEnum = DirectionKindEnum.IN;
		}
		DirectionKind directionKind = (DirectionKind) ExpressionsFactory.eINSTANCE.createFromString(
				ExpressionsPackage.eINSTANCE.getDirectionKind(), directionKindEnum.getLiteral());
		TypeSpecPair typeSpec = visitTypeSpecCS(paramCS.getTypeSpecCS(), directionKind, env);
		if (typeSpec.myType == null) {
			typeSpec.myType = env.getOCLStandardLibrary().getOclVoid();
		}

		MappingParameter mappingParam = createMappingParam ? ExpressionsFactory.eINSTANCE.createMappingParameter() : null;
		VarParameter varParam = createMappingParam ? mappingParam : ExpressionsFactory.eINSTANCE.createVarParameter();
		varParam.setRepresentedParameter(varParam);
		varParam.setStartPosition(paramCS.getStartOffset());
		varParam.setEndPosition(paramCS.getEndOffset());

		SimpleNameCS paramNameCS = paramCS.getSimpleNameCS();
		if(paramNameCS != null) {
			paramNameCS.setAst(varParam);
			varParam.setName(paramNameCS.getValue());
		} else {
			varParam.setName(""); //$NON-NLS-1$
		}
		varParam.setEType(typeSpec.myType);
		varParam.setKind(directionKind);

		if(mappingParam != null) {
			mappingParam.setExtent(typeSpec.myExtent);
			if (mappingParam.getExtent() == null) {

				boolean isEntryPoint = false;
				if (paramCS.eContainer() instanceof MappingDeclarationCS) {
					isEntryPoint = QvtOperationalEnv.MAIN.equals(((MappingDeclarationCS) paramCS.eContainer()).getSimpleNameCS().getValue());
				}

				if (isEntryPoint) {
					mappingParam.setExtent(env.resolveModelParameterDeprecated(typeSpec.myType, directionKind));
				}
				else {
					mappingParam.setExtent(env.resolveModelParameter(typeSpec.myType, directionKind));
				}
				//if (mappingParam.getExtent() == null && directionKind == DirectionKind.OUT) {
				//	env.reportError(ValidationMessages.OutParamWithoutExtent, paramCS);
				//}
			}
		}

		// AST binding
		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(paramCS, varParam);
		}
		//

		paramCS.setAst(varParam);

		return varParam;
	}

	protected VariableInitExp variableInitializationCS(VariableInitializationCS varInitCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		boolean hasExplicitInitExpression = varInitCS.getOclExpressionCS() != null;
		boolean hasDeclType = varInitCS.getTypeCS() != null;
		String varName = varInitCS.getSimpleNameCS().getValue();

		VariableInitExp result = ImperativeOCLFactory.eINSTANCE.createVariableInitExp();
		result.setStartPosition(varInitCS.getStartOffset());
		result.setEndPosition(varInitCS.getEndOffset());

		result.setName(varName);
		result.setWithResult(varInitCS.isWithResult());

		org.eclipse.ocl.ecore.Variable referredVar = EcoreFactory.eINSTANCE.createVariable();
		result.setReferredVariable(referredVar);

		referredVar.setName(varName);

		EClassifier declaredType = null;
		EClassifier derivedInitType = null;
		if (hasDeclType) {
			// Note: visiting typeCS reports a type resolution issue
			declaredType = visitTypeCS(varInitCS.getTypeCS(), null, env);
		}

		if(hasExplicitInitExpression) {
			OCLExpressionCS initExpCS = varInitCS.getOclExpressionCS();
			org.eclipse.ocl.ecore.OCLExpression exp = visitOclExpressionCS(initExpCS, env);
			referredVar.setInitExpression(exp);
			if(exp != null) {
				derivedInitType = referredVar.getInitExpression().getType();
				if (derivedInitType instanceof TypeType<?, ?>) {
					QvtOperationalUtil.reportError(env, ValidationMessages.VariableTypeTypeError,
							initExpCS.getStartOffset(), initExpCS.getEndOffset());
				}
			}
		}

		referredVar.setType(declaredType != null ? declaredType : derivedInitType);
		result.setType(referredVar.getType());

		if (!hasExplicitInitExpression && declaredType != null) {
			// FIXME - should not be initialized at AST level but at evaluation level
			org.eclipse.ocl.ecore.OCLExpression defaultInitializationValue = createDefaultInitializationValue(declaredType, env);
			referredVar.setInitExpression(defaultInitializationValue);
		}

		OCLExpression<EClassifier> initExpression = referredVar.getInitExpression();
		if (declaredType != null && derivedInitType != null) {
			if(!QvtOperationalParserUtil.isAssignableToFrom(env, declaredType, derivedInitType)) {
				QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.TypeMismatchError, new Object[] {
						QvtOperationalTypesUtil.getTypeFullName(declaredType),
						QvtOperationalTypesUtil.getTypeFullName(derivedInitType)
				}), initExpression.getStartPosition(), initExpression.getEndPosition());
			}
		}

		if (env.lookupLocal(varName) != null) {
			QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.NameAlreadyDefinedError,
					new Object[] { varName }), result.getStartPosition(), result.getEndPosition());
		} else {
			env.addElement(referredVar.getName(), referredVar, true);
		}

		// AST binding
		if(myCompilerOptions.isGenerateCompletionData()) {
			if(result.getName() != null) {
				Variable<EClassifier, EParameter> envVar = env.lookupLocal(result.getName());
				if(envVar != null) {
					ASTBindingHelper.createCST2ASTBinding(varInitCS, envVar);
				}
			}
		}
		//

		varInitCS.setAst(result);

		return result;
	}

	private ReturnExp visitReturnExpCS(ReturnExpCS returnExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		ReturnExp result = ImperativeOCLFactory.eINSTANCE.createReturnExp();
		initStartEndPositions(result, returnExpCS);
		if(returnExpCS.getValue() != null) {
			org.eclipse.ocl.ecore.OCLExpression value = oclExpressionCS(returnExpCS.getValue(), env);
			result.setValue(value);
			if(value != null) {
				result.setType(value.getType());
			}
		}

		if(result.getType() == null) {
			result.setType(env.getOCLStandardLibrary().getOclVoid());
		}

		return result;
	}

	private MappingBody visitMappingBodyCS(MappingBodyCS mappingBodyCS, QvtOperationalEnv env, MappingMethodCS mappingRuleCS)
			throws SemanticException {

		MappingBody body = ExpressionsFactory.eINSTANCE.createMappingBody();
		body.setStartPosition(mappingBodyCS.getStartOffset());
		body.setEndPosition(mappingBodyCS.getEndOffset());

		//Spec 07-07-07: 8.2.1.19 MappingBody
		//The rule for interpreting a body in which there is no population keyword is as follows:
		//(1) If the mapping operation defines a unique result, the list of expressions in the body is the list of expressions of the -
		//unique - implicit object expression (see ObjectExp) contained by the population section.
		//(2) If the mapping operation defines more than one result, the list of expressions in the body is the list of expressions of
		//the population section.
		if (!mappingBodyCS.isHasPopulationKeyword()) {
			MappingDeclarationCS mappingDeclarationCS = mappingRuleCS.getMappingDeclarationCS();
			if (mappingDeclarationCS != null) {
				boolean isUniqueResult = mappingDeclarationCS.getResult().size() == 1;
				boolean isImplicitContext = mappingDeclarationCS.getResult().isEmpty()
						&& (mappingDeclarationCS.getContextType() != null)
						&& (mappingDeclarationCS.getDirectionKindCS() != null)
						&& (mappingDeclarationCS.getDirectionKindCS().getDirectionKind() == DirectionKindEnum.INOUT);
				if (isUniqueResult || isImplicitContext) {
					ObjectExpCS implicitObjectExpCS = org.eclipse.m2m.internal.qvt.oml.cst.CSTFactory.eINSTANCE.createObjectExpCS();
					implicitObjectExpCS.setIsImplicit(true);

					SimpleNameCS resultVar = CSTFactory.eINSTANCE.createSimpleNameCS();
					String implicitVarName = isUniqueResult ? Environment.RESULT_VARIABLE_NAME : Environment.SELF_VARIABLE_NAME;
					resultVar.setValue(implicitVarName);

					implicitObjectExpCS.setSimpleNameCS(resultVar);
					implicitObjectExpCS.setStartOffset(mappingBodyCS.getStartOffset());
					implicitObjectExpCS.setEndOffset(mappingBodyCS.getEndOffset());

					implicitObjectExpCS.getExpressions().addAll(mappingBodyCS.getStatements());
					mappingBodyCS.getStatements().add(implicitObjectExpCS);

					ObjectExp objectExp = visitObjectExpCS(implicitObjectExpCS, env, false);
					body.getContent().add(objectExp);
					return body;
				}
			}
		}
		List<org.eclipse.ocl.ecore.OCLExpression> expressions = visitMappingSectionCS(mappingBodyCS, env);
		body.getContent().addAll(expressions);

		return body;
	}

	private int getStartOffset(ASTNode astNode, CSTNode cstNodeOpt) {
		if(cstNodeOpt != null) {
			return cstNodeOpt.getStartOffset();
		}
		return astNode.getStartPosition();
	}

	private int getEndOffset(ASTNode astNode, CSTNode cstNodeOpt) {
		if(cstNodeOpt != null) {
			return cstNodeOpt.getEndOffset();
		}
		return astNode.getEndPosition();
	}

	private void validateObjectExp(ObjectExp objectExp, ObjectExpCS objectExpCS, QvtOperationalEnv env) {
		EClass derivedInstantiatedClass = objectExp.getInstantiatedClass();
		Variable<EClassifier, EParameter> referredObject  = objectExp.getReferredObject();
		if(derivedInstantiatedClass == null && (referredObject != null && QVTUMLReflection.isUserModelElement(referredObject.getType()))) {
			derivedInstantiatedClass = (EClass)referredObject.getType();
		}

		if((derivedInstantiatedClass == null) || !QVTUMLReflection.isUserModelElement(derivedInstantiatedClass)) {
			CSTNode problemCS = null;
			if(objectExpCS != null) {
				problemCS = objectExpCS.getTypeSpecCS();
				if(problemCS == null) {
					problemCS = objectExpCS.getSimpleNameCS();
				}
				if(problemCS == null) {
					problemCS = objectExpCS;
				}
			}

			int startOffs = getStartOffset(objectExp, problemCS);
			int endOffs = getEndOffset(objectExp, problemCS);
			if(referredObject != null)	{
				if(referredObject.getType() != null && (referredObject.getType() instanceof CollectionType<?,?> == false)) {
					// we failed to figure out the class but type is available, let's report it's classifier only
					env.reportError(NLS.bind(ValidationMessages.nonModelTypeError,
							QvtOperationalParserUtil.safeGetQualifiedName(env, referredObject.getType())), startOffs, endOffs);
				}
			}
		}

		// check for the type conformance only if instantiatedClass class was explicitly set
		if(referredObject != null) {
			if(objectExp.getType() != null) {
				CSTNode problemCS = null;
				if(objectExpCS != null) {
					problemCS = objectExpCS.getSimpleNameCS();
					if(problemCS == null) {
						problemCS = objectExpCS.getTypeSpecCS() != null ? objectExpCS.getTypeSpecCS() : objectExpCS;
					}
				}

				EClassifier referredType = referredObject.getType();
				EClassifier actualType = objectExp.getType();
				// Note : invalid AST might have type node missing, so just check, a problem would be reported by variable validation
				if(referredType != null && (TypeUtil.getRelationship(env, actualType, referredType) & UMLReflection.SAME_TYPE) == 0) {
					String actualTypeName = QvtOperationalParserUtil.safeGetQualifiedName(env, actualType);
					String referredTypeName = QvtOperationalParserUtil.safeGetQualifiedName(env, referredType);
					String errorMessage = NLS.bind(ValidationMessages.QvtOperationalVisitorCS_instantiatedTypeDoesNotConformToReferredType,
							actualTypeName, referredTypeName);

					env.reportError(errorMessage, getStartOffset(objectExp, problemCS), getEndOffset(objectExp, problemCS));
				}

				if(referredObject.getRepresentedParameter() instanceof MappingParameter) {
					MappingParameter mappingPar = (MappingParameter) referredObject.getRepresentedParameter();
					if(mappingPar.getKind() == DirectionKind.IN) {
						env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_canNotModifyINParameter, referredObject.getName()), problemCS);
					}
				}
			}
		} else if(derivedInstantiatedClass != null && (derivedInstantiatedClass.isAbstract() || derivedInstantiatedClass.isInterface())) {
			// always creates a new instance, ensure non-abstract type.
			String typeName = QvtOperationalParserUtil.safeGetQualifiedName(env, derivedInstantiatedClass);
			env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_canNotInstantiateAbstractType, typeName), objectExpCS);
		}
	}

	private org.eclipse.ocl.ecore.OCLExpression visitExpressionStatementCS(ExpressionStatementCS expressionCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		return visitOclExpressionCS(expressionCS.getOclExpressionCS(), env);
	}

	private static EStructuralFeature createESFeature(EClassifier type) {
		if(type instanceof EClass) {
			return org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEReference();
		}
		return org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEAttribute();
	}

	private EStructuralFeature visitConfigPropertyCS(ConfigPropertyCS propCS, QvtOperationalModuleEnv env) {
		SimpleNameCS simpleNameCS = propCS.getSimpleNameCS();
		String name = simpleNameCS != null ? simpleNameCS.getValue() : ""; //$NON-NLS-1$

		EClassifier type = null;
		if (propCS.getTypeCS() != null) {
			type = visitTypeCS(propCS.getTypeCS(), null, env);
			if (type != null) {
				if (!QvtOperationalUtil.isCreateFromStringSupported(type)) {
					env.reportError(NLS.bind(ValidationMessages.ConfigPropertyTypeUnsupported, new Object[] { type
							.getName() }), propCS.getTypeCS());
				}
			}
		} else {
			env.reportError(NLS.bind(ValidationMessages.ConfigPropertyMustHaveType,
					new Object[] { name }), simpleNameCS != null ? simpleNameCS : propCS);
		}

		EStructuralFeature feature = createESFeature(type);
		simpleNameCS.setAst(feature);

		feature.setName(name);
		feature.setEType(type);

		ASTSyntheticNode astNode = ASTSyntheticNodeAccess.createASTNode(feature);
		astNode.setStartPosition(propCS.getStartOffset());
		astNode.setEndPosition(propCS.getEndOffset());

		return feature;
	}

	private EStructuralFeature visitLocalPropertyCS(LocalPropertyCS propCS, QvtOperationalEnv env) {
		EClassifier type = null;
		if (propCS.getTypeCS() != null) {
			type = visitTypeCS(propCS.getTypeCS(), null, env);
		}

		EStructuralFeature prop = createESFeature(type);
		SimpleNameCS simpleNameCS = propCS.getSimpleNameCS();
		simpleNameCS.setAst(prop);

		prop.setName(simpleNameCS.getValue());
		prop.setEType(type);

		ASTSyntheticNode astNode = ASTSyntheticNodeAccess.createASTNode(prop);
		astNode.setStartPosition(propCS.getStartOffset());
		astNode.setEndPosition(propCS.getEndOffset());

		OCLExpression<EClassifier> exp = null;
		if (propCS.getOclExpressionCS() != null) {
			OCLExpressionCS initExpCS = propCS.getOclExpressionCS();
			exp = visitOclExpressionCS(initExpCS, env);
			QvtOperationalParserUtil.setInitExpression(prop, exp);
			if(exp != null) {
				if (exp.getType() instanceof TypeType<?, ?>) {
					QvtOperationalUtil.reportError(env, ValidationMessages.PropertyTypeTypeError,
							initExpCS.getStartOffset(), initExpCS.getEndOffset());
				}
			}
		}

		if (prop.getEType() == null && exp != null) {
			prop.setEType(exp.getType());
		}

		if(exp != null) {
			EClassifier realType = exp.getType();
			EClassifier declaredType = prop.getEType();
			if (!QvtOperationalParserUtil.isAssignableToFrom(env, declaredType, realType)) {
				env.reportError(NLS.bind(ValidationMessages.TypeMismatchError,
						new Object[] { QvtOperationalTypesUtil.getTypeFullName(declaredType), QvtOperationalTypesUtil.getTypeFullName(realType) }),
						astNode.getStartPosition(), astNode.getEndPosition());
			}
		}

		return prop;
	}

	private ContextualProperty visitContextualPropertyCS(ContextualPropertyCS propCS, QvtOperationalModuleEnv env) {
		ContextualProperty prop = ExpressionsFactory.eINSTANCE.createContextualProperty();
		prop.setStartPosition(propCS.getStartOffset());
		prop.setEndPosition(propCS.getEndOffset());

		ScopedNameCS scopedNameCS = propCS.getScopedNameCS();
		scopedNameCS.setAst(prop);
		prop.setName(scopedNameCS.getName());

		EClassifier type = null;
		if (propCS.getTypeCS() != null) {
			type = visitTypeCS(propCS.getTypeCS(), null, env);
			if (type == null) {
				return prop;
			}
		}

		org.eclipse.ocl.ecore.OCLExpression exp = null;
		if (propCS.getOclExpressionCS() != null) {
			env.reportWarning(NLS.bind(ValidationMessages.IntermediatePropertiesInitNotSupported,
					new Object[] { }), propCS.getOclExpressionCS());
			exp = visitOclExpressionCS(propCS.getOclExpressionCS(), env);
		}

		if (type == null && exp != null) {
			type = exp.getType();
		}
		if (type == null) {
			return null;
		}

		prop.setEType(type);
		prop.setInitExpression(exp);


		if (exp != null) {
			EClassifier realType = exp.getType();
			EClassifier declaredType = prop.getEType();
			if (!QvtOperationalParserUtil.isAssignableToFrom(env, declaredType, realType)) {
				env.reportError(NLS.bind(ValidationMessages.TypeMismatchError,
						new Object[] { QvtOperationalTypesUtil.getTypeFullName(declaredType), QvtOperationalTypesUtil.getTypeFullName(realType) }),
						prop.getStartPosition(), prop.getEndPosition());
			}
		}

		EClassifier contextType = visitTypeCS(scopedNameCS.getTypeCS(), null, env);
		if (contextType != null) {
			if(contextType instanceof EClass) {
				prop.setContext((EClass)contextType);
			} else {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_ContextualPropertyTypeIsNotClass,
						new Object[] { prop.getName() }), scopedNameCS.getTypeCS());
			}
		}
		else {
			env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_ContextualPropertyTypeIsMissed,
					new Object[] { }), scopedNameCS);
		}

		return prop;
	}

	protected EStructuralFeature visitModulePropertyCS(ModulePropertyCS propCS, QvtOperationalModuleEnv env) {
		EStructuralFeature result = null;
		if (propCS instanceof ConfigPropertyCS) {
			result = visitConfigPropertyCS((ConfigPropertyCS) propCS, env);
		}
		else if (propCS instanceof LocalPropertyCS) {
			result = visitLocalPropertyCS((LocalPropertyCS) propCS, env);
		}
		else if (propCS instanceof ContextualPropertyCS) {
			result = visitContextualPropertyCS((ContextualPropertyCS) propCS, env);
		}
		else {
			assert false : "Unexpected CS class: " + propCS; //$NON-NLS-1$
		}

		// AST binding
		if (myCompilerOptions.isGenerateCompletionData()) {
			propCS.setAst(result);

			if(result instanceof ASTNode) {
				ASTBindingHelper.createCST2ASTBinding(propCS, (ASTNode)result, env);
			} else if (result != null) {
				ASTSyntheticNode astNode = ASTSyntheticNodeAccess.getASTNode(result);
				ASTSyntheticNodeAccess.setCST(astNode, propCS);
			}
		}
		//
		return result;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitResolveExpCS(ResolveExpCS resolveExpCS, QvtOperationalEnv env) {
		ResolveExp resolveExp = populateResolveExp(resolveExpCS, env, ExpressionsFactory.eINSTANCE.createResolveExp(), null, null);
		//        if (resolveExp.getSource() == null) {
		//            env.reportError(NLS.bind(ValidationMessages.ResolveExpMustHaveASource, new Object[] { }), resolveExpCS);
		//        }

		DeprecatedImplicitSourceCallHelper.validateCallExp(resolveExpCS, resolveExp, env);

		return resolveExp;
	}

	private void validateResolveExp(ResolveExp  resolveExp,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		if(resolveExp.isIsDeferred()) {
			if(!QvtResolveUtil.isSuppportedAsDeferredAssigned(resolveExp)) {
				int startPos = resolveExp.getStartPosition();
				assert startPos > 0;
				QvtOperationalUtil.reportWarning(env, ValidationMessages.lateResolveNotUsedInDeferredAssignment, startPos, resolveExp.getEndPosition());
			}
		}
	}

	private org.eclipse.ocl.ecore.OCLExpression visitResolveInExpCS(ResolveInExpCS resolveInExpCS, QvtOperationalEnv env) {
		ResolveInExp resolveInExp = ExpressionsFactory.eINSTANCE.createResolveInExp();
		TypeCS contextTypeCS = resolveInExpCS.getInMappingType();
		EClassifier eClassifier = (contextTypeCS == null) ? null : visitTypeCS(contextTypeCS, null, env); // mapping context type
		eClassifier = eClassifier != null ? eClassifier : env.getModuleContextType();
		String mappingName = resolveInExpCS.getInMappingName().getValue();
		List<MappingOperation> rawMappingOperations = env.lookupMappingOperations(eClassifier, mappingName);
		List<MappingOperation> unfilteredMappingOperations = new ArrayList<MappingOperation>(2);
		List<MappingOperation> mappingOperations = new ArrayList<MappingOperation>(2);

		for (MappingOperation operation : rawMappingOperations) {
			EClassifier owner = env.getUMLReflection().getOwningClassifier(operation);
			if (((contextTypeCS == null) && (owner == null))
					|| (TypeUtil.resolveType(env, owner) == eClassifier)) {
				unfilteredMappingOperations.add(operation);
			}
		}
		for (MappingOperation operation : unfilteredMappingOperations) {
			if (!unfilteredMappingOperations.contains(operation.getOverridden())) {
				mappingOperations.add(operation);
			}
		}

		String mappingFQName = (eClassifier == null) ? "" : eClassifier.getName() + EmfUtil.PATH_SEPARATOR; //$NON-NLS-1$
		mappingFQName += mappingName;
		EClassifier mappingResultType = null;
		EClassifier mappingContextType = null;

		if (mappingOperations.size() == 1) {
			env.registerResolveInExp(resolveInExp, eClassifier, mappingName);
			mappingResultType = mappingOperations.get(0).getEType();
			mappingContextType = QvtOperationalParserUtil.getContextualType(mappingOperations.get(0));
		} else {
			if (mappingOperations.size() == 0) {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_ResolveInMappingNotFound, new Object[] {
						mappingFQName}), resolveInExpCS.getInMappingName() != null ? resolveInExpCS.getInMappingName() : resolveInExpCS);
			} else if (mappingOperations.size() > 1) {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_ambiguousMappingOperationReference,
						new Object[] { mappingFQName }), resolveInExpCS.getInMappingName());
				env.registerResolveInExp(resolveInExp, eClassifier, mappingName);
			}
		}

		org.eclipse.ocl.ecore.OCLExpression result = populateResolveExp(resolveInExpCS, env, resolveInExp, mappingResultType, mappingContextType);
		//        DeprecatedImplicitSourceCallHelper.validateCallExp(resolveInExpCS, result, env);

		if(mappingOperations.size() == 1) {
			MappingOperation mapping = mappingOperations.get(0);
			boolean isValidMapping = resolveInExp.isIsInverse() ? mapping.getContext() != null : !mapping.getResult().isEmpty();
			if (!isValidMapping) {
				env.reportWarning(
						NLS.bind(ValidationMessages.QvtOperationalVisitorCS_invalidResolveInMapping, mappingFQName),
						resolveInExpCS);
			}
			else if (resolveInExp.getTarget() != null) {
				EClassifier targetVariableType = resolveInExp.getTarget().getType();
				EClassifier expectedTargetType = resolveInExp.isIsInverse() ? mappingContextType : mappingResultType;

				if((TypeUtil.getRelationship(env, expectedTargetType, targetVariableType) & UMLReflection.RELATED_TYPE) == 0) {
					env.reportWarning(
							NLS.bind(ValidationMessages.QvtOperationalVisitorCS_incompatibleTargetVariableType,
									new Object[] {
											QvtOperationalTypesUtil.getTypeFullName(targetVariableType),
											mappingFQName
							}),
							resolveInExpCS.getTarget());
				}
			}
		}

		return result;
	}

	private ResolveExp populateResolveExp(ResolveExpCS resolveExpCS, QvtOperationalEnv env, ResolveExp resolveExp,
			EClassifier mappingResultType, EClassifier mappingContextType) {
		// AST binding
		if(myCompilerOptions.isGenerateCompletionData()) {
			ASTBindingHelper.createCST2ASTBinding(resolveExpCS, resolveExp, env);
		}
		//
		if (resolveExpCS.getSource() != null) {
			OCLExpression<EClassifier> sourceExp = visitOclExpressionCS(resolveExpCS.getSource(), env);
			resolveExp.setSource(sourceExp);
		} else {
			//            // lookup for implicit source
			//            Variable<EClassifier,EParameter> implicitSource = env.lookupImplicitSourceForResolveExp();
			//            if (implicitSource != null) {
			//                VariableExp<EClassifier,EParameter> vexp = org.eclipse.ocl.expressions.ExpressionsFactory.eINSTANCE.createVariableExp();
			//
			//                vexp.setType(implicitSource.getType());
			//                vexp.setReferredVariable(implicitSource);
			//                vexp.setName(implicitSource.getName());
			//
			//                resolveExp.setSource(vexp);
			//            }
		}
		resolveExp.setOne(resolveExpCS.isOne());
		resolveExp.setIsInverse(resolveExpCS.isIsInverse());
		resolveExp.setIsDeferred(resolveExpCS.isIsDeferred());

		VariableCS targetVarCS = resolveExpCS.getTarget();
		if (targetVarCS != null) { // at least type is defined
			org.eclipse.ocl.ecore.Variable variable = EcoreFactory.eINSTANCE.createVariable();
			targetVarCS.setAst(variable);

			EClassifier type = visitTypeCS(targetVarCS.getTypeCS(), null, env);
			variable.setType(type);

			boolean isTargetVarClashing = false;
			String targetVarName = targetVarCS.getName();
			if (targetVarName != null) {
				if (env.lookupLocal(targetVarName) != null) {
					isTargetVarClashing = true;

					env.reportError(NLS.bind(ValidationMessages.NameAlreadyDefinedError, new Object[] { targetVarName }),
							targetVarCS.getStartOffset(), targetVarCS.getEndOffset());
				}
				variable.setName(targetVarName);
			}
			resolveExp.setTarget(variable);

			// AST binding
			if(myCompilerOptions.isGenerateCompletionData()) {
				ASTBindingHelper.createCST2ASTBinding(targetVarCS, variable, env);
			}
			//

			if (resolveExp.isOne()) {
				resolveExp.setType(type);
			} else {
				EClassifier resolveType = (EClassifier) env.getOCLFactory().createSequenceType(type);
				resolveExp.setType(TypeUtil.resolveType(env, resolveType));
			}
			if (resolveExpCS.getCondition() != null) {
				if (!isTargetVarClashing && variable.getName() != null) {
					env.addElement(variable.getName(), variable, true);
				}
				org.eclipse.ocl.ecore.OCLExpression condExp = visitOclExpressionCS(resolveExpCS.getCondition(), env);
				resolveExp.setCondition(condExp);

				if (!isTargetVarClashing && variable.getName() != null) {
					env.deleteElement(variable.getName());
					// ensure to bind back to the ResolveExp container
					resolveExp.setTarget(variable);
				}
			}
		} else {
			EClassifier resultType = resolveExp.isIsInverse()
					? (mappingContextType == null ? env.getOCLStandardLibrary().getOclAny() : mappingContextType)
							: (mappingResultType == null ? env.getOCLStandardLibrary().getOclAny() : mappingResultType);
					if (resolveExp.isOne()) {
						resolveExp.setType(resultType);
					} else {
						EClassifier resolveType = (EClassifier) env.getOCLFactory().createSequenceType(resultType);
						resolveExp.setType(TypeUtil.resolveType(env, resolveType));
					}
		}
		resolveExp.setStartPosition(resolveExpCS.getStartOffset());
		resolveExp.setEndPosition(resolveExpCS.getEndOffset());

		if(resolveExpCS.isIsDeferred()) {
			addLateResolve(resolveExp);
		}

		OCLExpression<EClassifier> condition = resolveExp.getCondition();
		if(condition != null) {
			EClassifier condType = condition.getType();
			if(env.getOCLStandardLibrary().getBoolean() != condType) {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_booleanTypeExpressionExpected,
						env.getUMLReflection().getName(condType)), condition.getStartPosition(), condition.getEndPosition());
			}
		}

		return resolveExp;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitForExp(ForExpCS forExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		OCLExpression<EClassifier> source =
				getCollectionSourceExpression(forExpCS.getSource(), env);
		if (!(source.getType() instanceof CollectionType<?,?>)) {
			return (org.eclipse.ocl.ecore.OCLExpression)createDummyInvalidLiteralExp(env, forExpCS);
		}
		String name = forExpCS.getSimpleNameCS().getValue();

		ForExp astNode = ImperativeOCLFactory.eINSTANCE.createForExp();
		initASTMapping(env, astNode, forExpCS);
		astNode.setName(name);
		astNode.setStartPosition(forExpCS.getStartOffset());
		astNode.setEndPosition(forExpCS.getEndOffset());
		astNode.setType(env.getOCLStandardLibrary().getOclVoid());

		EList<Variable<EClassifier, EParameter>> iterators = astNode.getIterator();
		@SuppressWarnings("unchecked")
		CollectionType<EClassifier, EOperation> sourceCollectionType = (CollectionType<EClassifier, EOperation>) source.getType();
		Variable<EClassifier, EParameter> vdcl = null;
		if (forExpCS.getVariable1() != null) {
			vdcl = variableDeclarationCS(forExpCS.getVariable1(), env, true);
			vdcl.setType(sourceCollectionType.getElementType());
			iterators.add(vdcl);
		}

		Variable<EClassifier, EParameter> vdcl1 = null;
		if (forExpCS.getVariable2() != null) {
			vdcl1 = variableDeclarationCS(forExpCS.getVariable2(), env, true);
			vdcl1.setType(sourceCollectionType.getElementType());
			iterators.add(vdcl1);
		}

		if (forExpCS.getCondition() != null) {
			OCLExpression<EClassifier> conditionExp = oclExpressionCS(forExpCS.getCondition(), env);
			astNode.setCondition((org.eclipse.ocl.ecore.OCLExpression)conditionExp);
		}

		if (forExpCS.getBody() != null) {
			OCLExpression<EClassifier> bodyExp = oclExpressionCS(forExpCS.getBody(), env);
			astNode.setBody(bodyExp);
		}

		astNode.setSource(source);

		if (vdcl != null) {
			env.deleteElement(vdcl.getName());
		}
		if (vdcl1 != null) {
			env.deleteElement(vdcl1.getName());
		}

		return astNode;
	}

	private org.eclipse.ocl.ecore.OCLExpression visitImperativeIterateExp(ImperativeIterateExpCS imperativeIterateExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		OCLExpression<EClassifier> source =
				getCollectionSourceExpression(imperativeIterateExpCS.getSource(), env);
		if (!(source.getType() instanceof CollectionType<?,?>)) {
			return (org.eclipse.ocl.ecore.OCLExpression)createDummyInvalidLiteralExp(env, imperativeIterateExpCS);
		}
		String name = imperativeIterateExpCS.getSimpleNameCS().getValue();

		Variable<EClassifier, EParameter> vdcl = null;
		Variable<EClassifier, EParameter> vdcl1 = null;
		List<Variable<EClassifier, EParameter>> iterators = null;

		ImperativeIterateExp astNode;
		@SuppressWarnings("unchecked")
		CollectionType<EClassifier, EOperation> sourceCollectionType = (CollectionType<EClassifier, EOperation>) source.getType();

		if (imperativeIterateExpCS.getVariable1() != null) {
			vdcl = variableDeclarationCS(imperativeIterateExpCS.getVariable1(), env, true);

			astNode = ImperativeOCLFactory.eINSTANCE.createImperativeIterateExp();
			initASTMapping(env, astNode, imperativeIterateExpCS);
			astNode.setName(name);
			iterators = astNode.getIterator();
			if (vdcl.getType() == null) {
				vdcl.setType(sourceCollectionType.getElementType());
			}
			iterators.add(vdcl);

			if (imperativeIterateExpCS.getVariable2() != null) {
				vdcl1 = variableDeclarationCS(imperativeIterateExpCS.getVariable2(), env, true);

				if (vdcl1.getType() == null) {
					vdcl1.setType(sourceCollectionType.getElementType());
				}
				iterators.add(vdcl1);
			}
		} else  {
			astNode = ImperativeOCLFactory.eINSTANCE.createImperativeIterateExp();
			initASTMapping(env, astNode, imperativeIterateExpCS);
			astNode.setName(name);
			iterators = astNode.getIterator();
			// Synthesize the iterator expression.
			vdcl = genVariableDeclaration(imperativeIterateExpCS, "visitImperativeIterateExp", env, null, //$NON-NLS-1$
					sourceCollectionType.getElementType(), null, false, true, false);
			iterators.add(vdcl);
		}

		TRACE("visitImperativeIterateExp: ", name);//$NON-NLS-1$

		EClassifier resultElementType = null;

		if (name.equals("xselect") || name.equals("selectOne")) {//$NON-NLS-1$ //$NON-NLS-2$
			resultElementType = sourceCollectionType.getElementType();
		} else {
			// Body may be defined explicitly - then it is the collectselect(One) shorthand or xcollect/collectOne.
			// It may be contained it the target variable - then it is the full notation of collectselect(One).
			if (imperativeIterateExpCS.getBody() != null) {
				OCLExpression<EClassifier> bodyExp = oclExpressionCS(imperativeIterateExpCS.getBody(), env);
				astNode.setBody(bodyExp);
				if (((imperativeIterateExpCS.getTarget() == null) || (imperativeIterateExpCS.getTarget().getInitExpression() == null))
						&& (name.equals("collectselect") || name.equals("collectselectOne"))) { //$NON-NLS-1$ //$NON-NLS-2$
					// This is the case with collectselect(One) shorthand
					// list->prop[res| res.startswith("_")];
					// equivalent to
					// list->collectselect(i;res = i.prop | res.startswith("_"))
					if (!isInnermostIteratorRelated(vdcl, bodyExp)) {
						QvtOperationalUtil.reportError(env, NLS.bind(ValidationMessages.QvtOperationalVisitorCS_FeatureNotFoundForType,
								new Object[] {QvtOperationalTypesUtil.getTypeFullName(vdcl.getType())}),
								imperativeIterateExpCS.getBody());
					}
					if (imperativeIterateExpCS.getTarget() == null) {
						Variable<EClassifier, EParameter> targetVdcl = genVariableDeclaration(imperativeIterateExpCS, "visitImperativeIterateExp", env, null, //$NON-NLS-1$
								bodyExp.getType(), null, false, true, false);
						astNode.setTarget((org.eclipse.ocl.ecore.Variable)targetVdcl);
					}
				}
			}
			if (imperativeIterateExpCS.getTarget() != null) {
				Variable<EClassifier, EParameter> targetVdcl = variableDeclarationCS(imperativeIterateExpCS.getTarget(), env, true);
				if (targetVdcl.getInitExpression() != null) {
					astNode.setBody(targetVdcl.getInitExpression()); // the body is transferred from the target variable due to containment
				}
				astNode.setTarget((org.eclipse.ocl.ecore.Variable)targetVdcl);
			}
			if (astNode.getBody() != null) {
				resultElementType = astNode.getBody().getType();
				if ((astNode.getTarget() != null) && (astNode.getTarget().getType() == null)) {
					astNode.getTarget().setType(resultElementType);
				}
			}
			if (resultElementType == null) {
				resultElementType = sourceCollectionType.getElementType();
				if ((astNode.getTarget() != null) && (astNode.getTarget().getType() == null)) {
					astNode.getTarget().setType(resultElementType);
				}
			}
		}

		if (imperativeIterateExpCS.getCondition() != null) {
			org.eclipse.ocl.ecore.OCLExpression conditionExp = oclExpressionCS(imperativeIterateExpCS.getCondition(), env);
			astNode.setCondition(conditionExp);
			if (conditionExp instanceof TypeExp<?>) {
				@SuppressWarnings("unchecked")
				TypeExp<EClassifier> typedCondition = (TypeExp<EClassifier>) conditionExp;
				EClassifier rawTypeType = TypeUtil.resolveType(env, typedCondition.getType());
				if (rawTypeType instanceof TypeType<?,?>) {
					@SuppressWarnings("unchecked")
					TypeType<EClassifier, EOperation> typeType = (TypeType<EClassifier, EOperation>) rawTypeType;
					resultElementType = typeType.getReferredType();
				}
			} else if ((conditionExp != null) && (conditionExp.getType() != getBoolean())) {
				QvtOperationalUtil.reportError(env, ValidationMessages.QvtOperationalVisitorCS_WrongImperativeIteratorConditionType,
						conditionExp.getStartPosition(), conditionExp.getEndPosition());
			}
		}

		if (astNode.getTarget() != null) {
			env.deleteElement(astNode.getTarget().getName());
		}

		if (name.equals("selectOne") || name.equals("collectOne") || name.equals("collectselectOne")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			astNode.setType(resultElementType);
		} else if (name.equals("xselect")) { //$NON-NLS-1$
			EClassifier resultCollectionType = resolveCollectionType(env,
					sourceCollectionType instanceof ListType ? CollectionKind.SEQUENCE_LITERAL : sourceCollectionType.getKind(), resultElementType);
			astNode.setType(resultCollectionType);
		} else { // xcollect and collectselect
			boolean isSequenceType = (sourceCollectionType instanceof SequenceType<?, ?>
			|| sourceCollectionType instanceof OrderedSetType<?, ?>
			|| sourceCollectionType instanceof ListType);
			EClassifier resultCollectionType = isSequenceType ? resolveCollectionType(env, CollectionKind.SEQUENCE_LITERAL, resultElementType)
					: resolveCollectionType(env, CollectionKind.BAG_LITERAL, resultElementType);
			astNode.setType(resultCollectionType);
		}

		astNode.setSource(source);

		env.deleteElement(vdcl.getName());
		if (vdcl1 != null) {
			env.deleteElement(vdcl1.getName());
		}
		// ensure AST containment tree
		astNode.getIterator().addAll(iterators);

		initStartEndPositions(astNode, imperativeIterateExpCS);
		return astNode;
	}

	private boolean isInnermostIteratorRelated(Variable<EClassifier, EParameter> vdcl, OCLExpression<EClassifier> bodyExp) {
		if (bodyExp instanceof CallExp) {
			CallExp bodyCallExp = (CallExp) bodyExp;
			if (bodyCallExp.getSource() instanceof VariableExp<?,?>) {
				@SuppressWarnings("unchecked")
				VariableExp<EClassifier, EParameter> sourceExp = (VariableExp<EClassifier, EParameter>) bodyCallExp.getSource();
				return sourceExp.getReferredVariable() == vdcl;
			}
			return false;
		}
		return true; // might be switch exp, for example
	}

	private MappingCallExp createMappingCallExp(MappingCallExpCS expressionCS, OCLExpression<EClassifier> result) {
		if (result instanceof OperationCallExp<?,?>) {
			@SuppressWarnings("unchecked")
			OperationCallExp<EClassifier, EOperation> operationCallExp = (OperationCallExp<EClassifier, EOperation>) result;
			EOperation operation = operationCallExp.getReferredOperation();
			if (QvtOperationalUtil.isMappingOperation(operation)) {
				MappingCallExp mappingCallExp = ExpressionsFactory.eINSTANCE.createMappingCallExp();
				mappingCallExp.setStartPosition(operationCallExp.getStartPosition());
				mappingCallExp.setEndPosition(operationCallExp.getEndPosition());
				mappingCallExp.getArgument().addAll(operationCallExp.getArgument());
				mappingCallExp.setReferredOperation(operation);
				mappingCallExp.setSource(operationCallExp.getSource());
				mappingCallExp.setType(operationCallExp.getType());
				mappingCallExp.setIsStrict(expressionCS.isStrict());

				mappingCallExp.setOperationCode(operationCallExp.getOperationCode());

				mappingCallExp.setPropertyStartPosition(operationCallExp.getPropertyStartPosition());
				mappingCallExp.setPropertyEndPosition(operationCallExp.getPropertyEndPosition());

				return mappingCallExp;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void validateOperationCall(OperationCallExpCS opCallCS, OperationCallExp<EClassifier, EOperation> operationCallExp,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		if (QvtOperationalParserUtil.isTypeCast(operationCallExp.getReferredOperation())) {
			if (operationCallExp.getSource() != null && operationCallExp.getArgument().size() == 1) {
				EClassifier sourceType = operationCallExp.getSource().getType();
				EClassifier argumentType = operationCallExp.getArgument().get(0)
						.getType();
				if (argumentType instanceof TypeType<?,?>) {
					TypeType<EClassifier, EOperation> argTypeType = (TypeType<EClassifier, EOperation>) argumentType;
					if(QvtOperationalParserUtil.isIncorrectCast(sourceType, argTypeType.getReferredType())) {
						QvtOperationalUtil.reportWarning(env, ValidationMessages.incorrectCastWarning, opCallCS);
					}
				}
			}
		}

		if (PredefinedType.EQUAL_NAME.equals(opCallCS.getSimpleNameCS().getValue())
				|| PredefinedType.NOT_EQUAL_NAME.equals(opCallCS.getSimpleNameCS().getValue())) {
			if (operationCallExp.getSource() != null && operationCallExp.getArgument().size() == 1) {
				EClassifier sourceType = operationCallExp.getSource().getType();
				EClassifier argumentType = operationCallExp.getArgument().get(0)
						.getType();

				if (!QvtOperationalParserUtil.isAssignableToFrom(env, sourceType, argumentType)
						&& !QvtOperationalParserUtil.isAssignableToFrom(env, argumentType, sourceType)) {
					QvtOperationalUtil.reportWarning(env, NLS.bind(ValidationMessages.TypesDoNotConform, new Object[] {
							QvtOperationalTypesUtil.getTypeFullName(argumentType),
							QvtOperationalTypesUtil.getTypeFullName(sourceType) }), opCallCS);
				}
			}
		}

		if (QvtOperationalUtil.isMappingOperation(operationCallExp.getReferredOperation())) {
			if (false == opCallCS instanceof MappingCallExpCS) {
				QvtOperationalUtil.reportWarning(env, NLS.bind(ValidationMessages.QvtOperationalVisitorCS_mapKeywordNotUsed,
						operationCallExp.getReferredOperation().getName()), opCallCS);
			}
		}

		if (operationCallExp.getReferredOperation() != null) {
			CallHandler callHandler = CallHandlerAdapter.getDispatcher(operationCallExp.getReferredOperation());
			if (callHandler != null && callHandler.isMutator() && operationCallExp.getSource() instanceof VariableExp) {
				VariableExp<EClassifier, EParameter> sourceExp = (VariableExp<EClassifier, EParameter>) operationCallExp.getSource();
				Variable<EClassifier, EParameter> sourceVariable = sourceExp.getReferredVariable();
				OCLExpressionCS varCS = opCallCS.getSource();
				QvtOperationalParserUtil.validateVariableModification(sourceVariable, varCS, null, env, false);
			}
		}
	}


	private void validateImportedSignature(QvtOperationalEnv env, OperationalTransformation module, OperationalTransformation importedModule, ASTNode astNode) {
		Set<ModelParameter> processedParams = new HashSet<ModelParameter>();
		Set<ModelParameter> consideredParams = new HashSet<ModelParameter>();
		for (ModelParameter importedParam : importedModule.getModelParameter()) {
			for (ModelParameter param : module.getModelParameter()) {
				if (consideredParams.contains(param)) {
					continue;
				}
				if (QvtOperationalUtil.isModelParamEqual(param, importedParam, true)) {
					consideredParams.add(param);
					processedParams.add(importedParam);
					break;
				}
			}
		}

		for (ModelParameter importedParam : importedModule.getModelParameter()) {
			if (processedParams.contains(importedParam)) {
				continue;
			}
			boolean isCorrespondanceFound = false;
			for (ModelParameter param : module.getModelParameter()) {
				if (consideredParams.contains(param)) {
					continue;
				}
				if (QvtOperationalUtil.isModelParamEqual(param, importedParam, false)) {
					consideredParams.add(param);
					isCorrespondanceFound = true;
					break;
				}
			}
			if (!isCorrespondanceFound) {
				env.reportWarning(
						NLS.bind(ValidationMessages.QvtOperationalVisitorCS_incompatibleTransfSignature, importedModule.getName(), module.getName()),
						astNode.getStartPosition(), astNode.getEndPosition());
				return;
			}
		}
	}

	private void checkMainMappingConformance(QvtOperationalEnv env, ImperativeOperation operation) {
		Set<ModelParameter> usedExtent = new HashSet<ModelParameter>(operation.getEParameters().size());
		for (EParameter param : operation.getEParameters()) {
			MappingParameter varParam = (MappingParameter) param;
			if (usedExtent.contains(varParam.getExtent())) {
				env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_extentDuplicateUse, null),
						varParam.getStartPosition(), varParam.getEndPosition());
			}
			if (varParam.getKind() != DirectionKind.OUT) {
				usedExtent.add(varParam.getExtent());
			}

			if (varParam.getExtent() == null) {
				if(varParam.getKind() != DirectionKind.IN) {
					env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_extentFailToInfer,
							QvtOperationalTypesUtil.getTypeFullName(varParam.getEType())),
							varParam.getStartPosition(), varParam.getEndPosition());
				}
			}
			else if (varParam.getExtent().getKind() == DirectionKind.IN) {
				if (varParam.getKind() != DirectionKind.IN) {
					env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_extentDirectionMismatch, null),
							varParam.getStartPosition(), varParam.getEndPosition());
				}
			}
			else if (varParam.getExtent().getKind() == DirectionKind.OUT) {
				if (varParam.getKind() != DirectionKind.OUT) {
					env.reportError(NLS.bind(ValidationMessages.QvtOperationalVisitorCS_extentDirectionMismatch, null),
							varParam.getStartPosition(), varParam.getEndPosition());
				}
			}
		}
	}

	private void checkAbstractOutParamsInitialized(VarParameter varParameter, MappingOperation operation, MappingRuleCS methodCS,
			QvtOperationalEnv env) {
		EClassifier type = varParameter.getEType();
		if (type instanceof EClass) {
			EClass eClass = (EClass) type;
			if (!QvtOperationalUtil.isInstantiable(eClass)) {
				MappingInitCS init = (methodCS.getMappingBody() == null) ? null : methodCS.getMappingBody().getMappingInitCS();
				if (init != null) {
					// TODO: The check could be more accurate
					return;
				}
				boolean isAbstract = (methodCS.getMappingDeclarationCS() != null && methodCS.getMappingDeclarationCS()
						.getQualifiers().contains(QualifierKindCS.ABSTRACT));
				if (!operation.isIsBlackbox() && !isAbstract) {
					boolean hasDisjunct = QvtOperationalParserUtil.isDisjunctiveMappingOperation(methodCS);
					if (!hasDisjunct) {
						env.reportError(ValidationMessages.QvtOperationalVisitorCS_AbstractTypesNotInitialized,
								QvtOperationalParserUtil.getMethodHeaderProblemNodeCS(methodCS));
					}
				}
			}
		}
	}

	//	private boolean validateInitializedValueCS(VariableInitializationCS varInitCS, VariableInitExp result,
	//			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
	//			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
	//
	//		result.setName(varInitCS.getSimpleNameCS().getValue());
	//
	//		EClassifier type;
	//		if (varInitCS.getTypeCS() != null) {
	//			type = visitTypeCS(varInitCS.getTypeCS(), null, env);
	//			if (type == null) {
	//				return false;
	//			}
	//		} else {
	//			type = null;
	//		}
	//
	//		if (varInitCS.getOclExpressionCS() == null) {
	//		    if (type == null) {
	//		        return false;
	//		    } else {
	//		    	org.eclipse.ocl.ecore.OCLExpression defaultInitializationValue = createDefaultInitializationValue(type, env);
	//		        if (defaultInitializationValue == null) {
	//		            NullLiteralExp<EClassifier> nullLiteralExp = oclFactory.createNullLiteralExp();
	//		            nullLiteralExp.setType(getOclVoid());
	//		            defaultInitializationValue = (org.eclipse.ocl.ecore.NullLiteralExp)nullLiteralExp;
	//		        }
	//		        result.setValue(defaultInitializationValue);
	//		    }
	//		} else {
	//	        org.eclipse.ocl.ecore.OCLExpression exp = visitOclExpressionCS(varInitCS.getOclExpressionCS(), env);
	//	        if (exp == null) {
	//	            return false;
	//	        }
	//	        result.setValue(exp);
	//		}
	//		result.setType(type);
	//		return true;
	//	}

	// FIXME - should not be initialized at AST level but at evaluation level
	private org.eclipse.ocl.ecore.OCLExpression createDefaultInitializationValue(EClassifier type,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		// 8.2.2.10 VariableInitExp
		// A variable may not declare an initialization value. In this case a default value is assumed (an empty collection for a collection,
		// zero for any numeric type, the empty string for a string and null for all other elements.
		// A. Igdalov: Spec says nothing about Booleans and OclInvalid. Abstract collections (Collection, opposed to Bags, OrderedSets, etc.) are also neglected.
		// Thus, this implementation assigns false to Booleans, Invalid to OclInvalids and nulls to abstract collections.
		EClassifier resolvedType = env.getTypeResolver().resolve(type);
		OCLStandardLibrary<EClassifier> oclStdLib = getStandardLibrary();
		if (resolvedType == oclStdLib.getOclInvalid()) {
			InvalidLiteralExp<EClassifier> invalidLiteralExp = oclFactory.createInvalidLiteralExp();
			invalidLiteralExp.setType(oclStdLib.getOclInvalid());
			return (org.eclipse.ocl.ecore.InvalidLiteralExp)invalidLiteralExp;
		}

		return null;
	}

	private void addLateResolve(ResolveExp resolve) {
		assert resolve.isIsDeferred();
		if(myLateResolveExps == null) {
			myLateResolveExps = new LinkedList<ResolveExp>();
		}
		myLateResolveExps.add(resolve);
	}

	private List<ResolveExp> getAllLateResolves() {
		return myLateResolveExps != null ? myLateResolveExps : Collections.<ResolveExp>emptyList();
	}

	private void validate(Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		for (ResolveExp lateResolve : getAllLateResolves()) {
			validateResolveExp(lateResolve, env);
		}
	}

	/**
	 * SimpleNameCS
	 * @param simpleNameCS the <code>SimpleNameCS</code> <code>CSTNode</code>
	 * @param fEnv the OCL environment
	 * @param source the source of the <code>SimpleNameCS</code>
	 * @return the parsed <code>OCLExpression</code>
	 */
	private OCLExpression<EClassifier> customSimpleNameCS(
			SimpleNameCS simpleNameCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			OCLExpression<EClassifier> source) {

		if ((source != null) && isErrorNode(source)) {
			// don't attempt to parse navigation from an unparseable source
			return source; // return the same unparseable token
		}

		String simpleName = null;
		EClassifier classifier = null;

		/* A name can be a variable defined by a Variable declaration, the special
		  variable "self", an attribute or a reference to another object.
		  If the source is not null, then the last token was a "." or "->"
		  The source is used to establish the navigation.
		  If no type is provided, then either the name is a the use of a variable,
		  or there is an implicit variable declaration (self or an iterator)
		  that is the source.
		 */
		switch (simpleNameCS.getType().getValue()) {
		case SimpleTypeEnum.SELF:
		case SimpleTypeEnum.KEYWORD:
		case SimpleTypeEnum.IDENTIFIER:
			simpleName = simpleNameCS.getValue();
			break;
		case SimpleTypeEnum.INTEGER:
		case SimpleTypeEnum.STRING:
		case SimpleTypeEnum.REAL:
		case SimpleTypeEnum.BOOLEAN:
		case SimpleTypeEnum.OCL_ANY:
		case SimpleTypeEnum.OCL_VOID:
		case SimpleTypeEnum.OCL_INVALID:
		case SimpleTypeEnum.OCL_MESSAGE:
			// if we have a source, then this is a feature call
			if (source == null) {
				classifier = primitiveTypeCS(simpleNameCS.getType(), env);
				simpleName = uml.getName(classifier);
			}
			break;
		}


		/*
		 * The source may be a collection type (for example, in self.children.name, children
		 * may be a set.)_  In this case, we have to get the element type of children, so
		 * that the attribute name can be found.
		 * The source type can also be a tuple type. In this case, we need to get the
		 * EClass of the tuple.
		 *
		 */
		EClassifier sourceElementType = null;
		if (source != null) {
			sourceElementType = source.getType();
			if (sourceElementType instanceof CollectionType<?,?>) {
				@SuppressWarnings("unchecked")
				CollectionType<EClassifier, EOperation> ct = (CollectionType<EClassifier, EOperation>) sourceElementType;

				sourceElementType = ct.getElementType();
			}
		}

		// cascaded alternatives for a simpleNameCS
		OCLExpression<EClassifier> astNode = simpleTypeName(simpleNameCS, env, source,
				classifier, simpleName);
		if (astNode == null) {
			astNode = simpleVariableName(simpleNameCS, env, source, simpleName);
		}
		if (astNode == null) {
			astNode = simplePropertyName(simpleNameCS, env, source,
					sourceElementType, simpleName);
		}
		if (astNode == null) {
			astNode = simpleAssociationClassName(simpleNameCS, env, source,
					sourceElementType, simpleName);
		}
		if (astNode == null) {
			astNode = simpleUndefinedName(simpleNameCS, env, source, simpleName);
		}

		// FIXME - we should ask MDT OCL for a support to handle this in a better way
		/*
		 * If the source type is a collection, then need there is an implicit COLLECT
		 * or imperative COLLECT operator.
		 */
		if ((source != null) && (source.getType() instanceof CollectionType<?,?>)
				&& (astNode instanceof FeatureCallExp<?>)) {
			CallExpCS callExpCS = (CallExpCS) simpleNameCS.eContainer();
			org.eclipse.ocl.ecore.FeatureCallExp featureCallExp = (org.eclipse.ocl.ecore.FeatureCallExp) astNode;
			astNode = isArrowAccessToCollection(callExpCS, source) ?
					createImplicitXCollect(source, featureCallExp, env, simpleNameCS)
					: createImplicitCollect(source, featureCallExp, env, simpleNameCS);
		}

		return astNode;
	}

	@Override
	protected PropertyCallExp<EClassifier, EStructuralFeature> simplePropertyName(
			SimpleNameCS simpleNameCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter,
			EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			OCLExpression<EClassifier> source, EClassifier owner, String simpleName) {

		if (source == null) {
			Variable<EClassifier, EParameter> implicitSource = env.lookupImplicitSourceForProperty(simpleName);
			source = createVariableExp(env, simpleNameCS, implicitSource);
			owner = source.getType();
		}

		return super.simplePropertyName(simpleNameCS, env, source, owner, simpleName);

	}

	protected EClassifier listTypeCS(ListTypeCS listTypeCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		TypeCS elementTypeCS = listTypeCS.getTypeCS();
		EClassifier elementType = typeCS(elementTypeCS, env);
		elementTypeCS.setAst(elementType);

		ListType result = getListType(elementType, env);
		if(result == null) {
			return env.getOCLStandardLibrary().getOclVoid();
		}


		CollectionType<EClassifier, EOperation> astNode = result;
		initTypePositions(astNode, elementTypeCS);

		listTypeCS.setAst(result);
		return result;
	}

	protected CollectionLiteralExp<EClassifier> listLiteralExpCS(ListLiteralExpCS listLiteralExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		CollectionLiteralExp<EClassifier> astNode = oclFactory.createCollectionLiteralExp();

		EClassifier elementType = null;

		initASTMapping(env, astNode, listLiteralExpCS);
		astNode.setKind(CollectionKind.SEQUENCE_LITERAL);

		EList<CollectionLiteralPart<EClassifier>> collectionParts = astNode.getPart();

		EList<CollectionLiteralPartCS> collectionLiteralPartsCS = listLiteralExpCS
				.getCollectionLiteralParts();

		if (!collectionLiteralPartsCS.isEmpty()) {
			Iterator<CollectionLiteralPartCS> i = collectionLiteralPartsCS
					.iterator();

			CollectionLiteralPartCS colPart = i.next();
			CollectionLiteralPart<EClassifier> collectionLiteralPartExp = collectionLiteralPartCS(colPart, env);
			collectionParts.add(collectionLiteralPartExp);
			elementType = collectionLiteralPartExp.getType();

			if (isErrorNode(collectionLiteralPartExp)) {
				// propagate error stigma to the collection literal
				markAsErrorNode(astNode);
			}

			while (i.hasNext()) {
				collectionLiteralPartExp = collectionLiteralPartCS(i.next(),
						env);

				EClassifier type1 = collectionLiteralPartExp.getType();
				elementType = getCommonSuperType(colPart,
						"collectionLiteralExpCS", env, elementType, type1); //$NON-NLS-1$
				collectionParts.add(collectionLiteralPartExp);

				if (isErrorNode(collectionLiteralPartExp)) {
					// propagate error stigma to the collection literal
					markAsErrorNode(astNode);
				}
			}
		}

		if (elementType == null) {
			elementType = env.getOCLStandardLibrary().getOclVoid();
		}

		EClassifier resultType = getListType(elementType, env);
		if(resultType == null) {
			resultType = env.getOCLStandardLibrary().getOclVoid();
		}

		astNode.setType(resultType);

		listLiteralExpCS.setAst(astNode);

		return astNode;
	}

	protected EClassifier dictionaryTypeCS(DictionaryTypeCS dictTypeCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		TypeCS keyTypeCS = dictTypeCS.getKey();
		EClassifier keyType = typeCS(keyTypeCS, env);
		keyTypeCS.setAst(keyType);

		TypeCS valueTypeCS = dictTypeCS.getValue();
		EClassifier valueType = typeCS(valueTypeCS, env);
		valueTypeCS.setAst(valueType);

		DictionaryType result = getDictionaryType(keyType, valueType, env);
		if(result == null) {
			return env.getOCLStandardLibrary().getOclVoid();
		}

		initTypePositions(result, keyTypeCS);
		dictTypeCS.setAst(result);
		return result;
	}

	/**
	 * Remark: Null argument safe operation
	 */
	protected DictionaryType getDictionaryType(EClassifier keyType, EClassifier valueType,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		if(env instanceof QVTOEnvironment && keyType != null && valueType != null) {
			QVTOEnvironment qvtEnv = (QVTOEnvironment) env;
			return qvtEnv.getTypeResolver().resolveDictionaryType(keyType, valueType);
		}
		return null;
	}

	/**
	 * Remark: Null argument safe operation
	 */
	protected ListType getListType(EClassifier elementType,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		if(env instanceof QVTOEnvironment && elementType != null) {
			QVTOEnvironment qvtEnv = (QVTOEnvironment) env;
			return qvtEnv.getTypeResolver().resolveListType(elementType);
		}
		return null;
	}

	protected DictLiteralExp dictionaryLiteralExp(DictLiteralExpCS dictLiteralExpCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		DictLiteralExp result = ImperativeOCLFactory.eINSTANCE.createDictLiteralExp();

		for (DictLiteralPartCS nextPartCS : dictLiteralExpCS.getParts()) {
			DictLiteralPart literalPartAST = dictionaryLiteralPart(nextPartCS, env);
			result.getPart().add(literalPartAST);
		}

		EClassifier commonKeyType = null;
		EClassifier commonnValueType = null;
		boolean commonSuperTypeError = false;

		for (DictLiteralPart literalPartAST : result.getPart()) {
			EClassifier nextKeyType = literalPartAST.getKey() != null ? literalPartAST.getKey().getType() : null;
			EClassifier nextValueType = literalPartAST.getValue() != null ? literalPartAST.getValue().getType() : null;

			if(nextKeyType != null && nextValueType != null) {
				if(commonKeyType != null && commonnValueType != null) {
					commonKeyType = TypeUtil.commonSuperType(dictLiteralExpCS, env, nextKeyType, commonKeyType);
					if(commonKeyType != null) {
						commonnValueType = TypeUtil.commonSuperType(dictLiteralExpCS, env, nextValueType, commonnValueType);
					}
				} else {
					// first iteration, take the first types
					commonKeyType = nextKeyType;
					commonnValueType = nextValueType;
				}
			}

			commonSuperTypeError = commonKeyType == null || commonnValueType == null;
			if(commonSuperTypeError) {
				break;
			}
		}

		EClassifier resultType = getDictionaryType(commonKeyType, commonnValueType, env);
		if(resultType == null) {
			resultType = env.getOCLStandardLibrary().getOclVoid();
		}

		result.setType(resultType);

		initStartEndPositions(result, dictLiteralExpCS);
		dictLiteralExpCS.setAst(result);
		return result;
	}

	protected DictLiteralPart dictionaryLiteralPart(DictLiteralPartCS dictLiteralPartCS,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {

		DictLiteralPart result = ImperativeOCLFactory.eINSTANCE.createDictLiteralPart();
		result.setKey(oclExpressionCS(dictLiteralPartCS.getKey(), env));
		result.setValue(oclExpressionCS(dictLiteralPartCS.getValue(), env));
		dictLiteralPartCS.setAst(result);
		return result;
	}

	@Override
	protected boolean isErrorNode(TypedElement<EClassifier> expr) {
		if(myErrorNodes == null) {
			return false;
		}

		return myErrorNodes.contains(expr);
	}

	@Override
	protected void markAsErrorNode(TypedElement<EClassifier> expr) {
		if(myErrorNodes == null) {
			myErrorNodes = new java.util.HashSet<TypedElement<?>>();
		}
		myErrorNodes.add(expr);
	}

	protected EClassifier resolveCollectionType(
			Environment<?, EClassifier, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> env,
			CollectionKind kind, EClassifier elementType) {
		return TypeUtil.resolveCollectionType(env, kind, elementType);
	}

	private static void createPropertyCallASTBinding(
			CallExpCS propertyCallExpCS,
			OCLExpression<EClassifier> result,
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) {
		ASTNode boundAST = result;
		CSTNode boundCST = propertyCallExpCS;
		if(result instanceof IteratorExp<?,?>) {
			@SuppressWarnings("unchecked")
			IteratorExp<EClassifier, EParameter> itExpAST = (IteratorExp<EClassifier, EParameter>) result;
			if(propertyCallExpCS instanceof IteratorExpCS) {
				IteratorExpCS itExpCST = (IteratorExpCS) propertyCallExpCS;
				if(itExpCST.getBody() != null) {
					boundCST = itExpCST.getBody();
				}
			}
			if(itExpAST.getBody() instanceof FeatureCallExp<?>) {
				boundAST = itExpAST.getBody();
			}
		} else if(result instanceof ImperativeIterateExp) {
			ImperativeIterateExp impIterExpAST = (ImperativeIterateExp) result;
			if(propertyCallExpCS instanceof ImperativeIterateExpCS) {
				ImperativeIterateExpCS itExpCST = (ImperativeIterateExpCS) propertyCallExpCS;
				if(itExpCST.getBody() != null) {
					boundCST = itExpCST.getBody();
				}
			}
			if(impIterExpAST.getBody() instanceof FeatureCallExp<?>) {
				boundAST = impIterExpAST.getBody();
			}
		}
		ASTBindingHelper.createCST2ASTBinding(boundCST, boundAST, env);
	}

	private static List<ModelParameter> getModelParameter(Module module) {
		if(module instanceof OperationalTransformation) {
			OperationalTransformation operationalTransformation = (OperationalTransformation) module;
			return operationalTransformation.getModelParameter();
		}
		return Collections.emptyList();
	}

	private static QvtOperationalModuleEnv getModuleContextEnv(QvtOperationalEnv env) {
		Internal<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject>
		nextParent = env;
		while(nextParent != null) {
			if(nextParent instanceof QvtOperationalModuleEnv) {
				return (QvtOperationalModuleEnv) nextParent;
			}
			nextParent = nextParent.getInternalParent();
		}

		return null;
	}

	private static QvtOperationalEnv toQVTOperationalEnv(Environment<EPackage, EClassifier,
			EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject,
			CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env) throws IllegalArgumentException {

		QvtOperationalEnv adapter = OCLUtil.getAdapter(env, QvtOperationalEnv.class);
		if(adapter == null) {
			throw new IllegalArgumentException("QVTOperationalEnv is required"); //$NON-NLS-1$
		}

		return adapter;
	}

	private static EAnnotation createTag(String tagId, String value, EModelElement element) {
		EAnnotation annotation = org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.getDetails().put(tagId, value);
		annotation.getReferences().add(element);
		return annotation;
	}

	public static URI getSourceURI(QvtOperationalModuleEnv env) {
		if(env instanceof QvtOperationalFileEnv) {
			QvtOperationalFileEnv fileEnv = (QvtOperationalFileEnv) env;
			return fileEnv.getFile();
		}
		else if (env.getFileParent() instanceof QvtOperationalFileEnv) {
			QvtOperationalFileEnv fileEnv = env.getFileParent();
			return fileEnv.getFile();
		}
		else if (env.getModuleContextType() != null) {
			return env.getModuleContextType().eResource().getURI();
		}
		return null;
	}

}
