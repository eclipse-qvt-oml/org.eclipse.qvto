/*******************************************************************************
 * Copyright (c) 2007, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ast.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.m2m.internal.qvt.oml.ast.env.InternalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalFileEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.DictionaryType;
import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ListType;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.cst.CSTNode;
import org.eclipse.ocl.cst.PathNameCS;
import org.eclipse.ocl.cst.TypeCS;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.SendSignalAction;
import org.eclipse.ocl.lpg.BasicEnvironment;
import org.eclipse.ocl.lpg.ProblemHandler;
import org.eclipse.ocl.lpg.ProblemHandler.Severity;
import org.eclipse.ocl.types.PrimitiveType;
import org.eclipse.ocl.util.OCLUtil;

public class QvtOperationalUtil {

	private QvtOperationalUtil() {
	}

	public static String getStringRepresentation(PathNameCS pathName, String pathSeparator) {
		return QvtOperationalParserUtil.getStringRepresentation(pathName, pathSeparator);
	}

	public static String getStringRepresentation(TypeCS typeCS) {
		return QvtOperationalParserUtil.getStringRepresentation(typeCS);
	}
	
	// FIXME - refactor out, we need to accept QVT specific environment 
	public static boolean isInvalid(Object value, EvaluationEnvironment<?, ?, ?, ?, ?> evalEnv) {
		InternalEvaluationEnv internEnv = OCLUtil.getAdapter(evalEnv, InternalEvaluationEnv.class);
		return internEnv != null && (value == internEnv.getInvalid());
	}
	
	public static Object getInvalid(EvaluationEnvironment<?, ?, ?, ?, ?> evalEnv) {
		InternalEvaluationEnv internEnv = OCLUtil.getAdapter(evalEnv, InternalEvaluationEnv.class);
		return internEnv != null ? internEnv.getInvalid() : null;
	}	
	
	public static boolean isUndefined(Object value, EvaluationEnvironment<?, ?, ?, ?, ?> evalEnv) {
		return value == null || isInvalid(value, evalEnv);
	}
	
	public static boolean isCreateFromStringSupported(EClassifier type) {
		return isPrimitiveType(type) || type instanceof EDataType;
	}

	public static boolean isPrimitiveType(Object type) {
		return type instanceof PrimitiveType<?>;
	}

	public static boolean isInstantiable(EClass cls) {
		return cls != null && !cls.isAbstract() && !cls.isInterface();
	}
	
	public static boolean isInstantiable(EClassifier classifier) {
		if(classifier instanceof EClass) {
			return isInstantiable((EClass) classifier);
		}
		
		return false;
	}	

    public static boolean isMappingOperation(EOperation operation) {
        return operation instanceof MappingOperation;
    }

    public static boolean isConstructorOperation(EOperation operation) {
        return operation instanceof Constructor;
    }

    public static boolean isImperativeOperation(EOperation operation) {
    	return operation instanceof ImperativeOperation;
    }

	public static boolean isModelParamEqual(ModelParameter param, ModelParameter importedParam, boolean isStrictCompare) {
		if (param.getKind() == DirectionKind.IN) {
			if (importedParam.getKind() != DirectionKind.IN) {
				return false;
			}
		}
		if (!isStrictCompare) {
			return true;
		}

		ModelType modelType = getModelType(param);
		ModelType importedModelType = getModelType(importedParam);
		if(modelType == null || importedModelType == null) {
			return false;
		}
		
		//return modelType.getMetamodel().containsAll(importedModelType.getMetamodel());
		return isCompatibleModelType(modelType, importedModelType);
	}
	
	public static boolean isCompatibleModelType(ModelType sourceType, ModelType targetType) {
		if(sourceType == targetType) {
			return true;
		}
		
		return sourceType.getMetamodel().containsAll(targetType.getMetamodel());		
	}
	
	public static ModelType getModelType(ModelParameter modelParameter) {
		EClassifier eType = modelParameter.getEType();
		return (eType instanceof ModelType) ? (ModelType) eType : null;
	}
    
    public static boolean isAbstract(EClassifier eClassifier) {
		if(eClassifier instanceof EClass) {    	
			EClass eClass = (EClass)eClassifier;
			return eClass.isAbstract() || eClass.isInterface();
		}
		return false;
    }
    
    public static boolean hasAbstractOutputParameter(ImperativeOperation operation) {
    	for (VarParameter nextParam : operation.getResult()) {
    		if(nextParam.getEType() != null && isAbstract(nextParam.getEType())) {
   				return true;
    		}
		}
    	
    	for (EParameter eParam : operation.getEParameters()) {
    		if(eParam instanceof VarParameter) {
    			VarParameter varParam = (VarParameter) eParam;
    			if(varParam.getEType() != null && isAbstract(varParam.getEType())) {
    				return true;
    			}
    		}
		}
    	return false;
    }
    

	public static void reportError(Environment<EPackage, EClassifier, EOperation, EStructuralFeature,
			EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint,
			EClass, EObject> env, String message, CSTNode node) {
		OCLUtil.getAdapter(env, BasicEnvironment.class).analyzerError(message, node.eClass().getName(), node);
	}

	public static void reportError(Environment<EPackage, EClassifier, EOperation, EStructuralFeature,
			EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint,
			EClass, EObject> env, String message, int startOffset, int endOffset) {
		OCLUtil.getAdapter(env, BasicEnvironment.class).analyzerError(message, "unknown", startOffset, endOffset); //$NON-NLS-1$
	}

	public static void reportWarning(Environment<EPackage, EClassifier, EOperation, EStructuralFeature,
			EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint,
			EClass, EObject> env, String message, CSTNode node) {
		
		OCLUtil.getAdapter(env, BasicEnvironment.class).analyzerWarning(message, node.eClass().getName(), node);		
	}

	public static void reportWarning(Environment<EPackage, EClassifier, EOperation, EStructuralFeature,
			EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint,
			EClass, EObject> env, String message, int startOffset, int endOffset) {
		
		ProblemHandler problemHandler = OCLUtil.getAdapter(env, ProblemHandler.class);
		if (problemHandler != null) {
			problemHandler.analyzerProblem(Severity.WARNING, message, "unknown", startOffset, endOffset); //$NON-NLS-1$
		}		
	}
	
	public static List<ModelType> collectValidModelParamaterTypes(OperationalTransformation transformation) {
		List<ModelType> types = new ArrayList<ModelType>(transformation.getModelParameter().size());
		
		for (ModelParameter parameter : transformation.getModelParameter()) {			
			EClassifier paramType = parameter.getType();
			if(paramType instanceof ModelType) {
				types.add((ModelType)paramType);
			}
		}
		
		return types;
	}
	
	public static boolean isMutableCollectionType(EClassifier classifier) {
		return classifier instanceof ListType || classifier instanceof DictionaryType;
	}
	
	public static ImperativeOperation getOverriddenOperation(EOperation operation) {
		return operation instanceof ImperativeOperation ? ((ImperativeOperation) operation).getOverridden() : null;
	}
	
	public static Collection<EOperation> filterOverriddenOperations(Collection<EOperation> operations) {
		Collection<EOperation> filtered = new LinkedHashSet<EOperation>(operations);
        for (EOperation next : operations) {
    		EOperation overridden = getOverriddenOperation(next);
    		if (overridden != null) {
    			filtered.remove(overridden);
    		}
        }
        
        return filtered;
	}

	/**
	 * Returns a URI representing the source of the given module environment.
	 * 
	 * @param env
	 *            the module environment
	 * @return a URI referring to the environment's source file (if available)
	 *         or to the resource of the module's context type (if existing),
	 *         <code>null</code> otherwise
	 */
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
