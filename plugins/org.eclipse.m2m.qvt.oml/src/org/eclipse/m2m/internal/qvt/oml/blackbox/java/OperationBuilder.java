/*******************************************************************************
 * Copyright (c) 2008, 2020 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 289982, 427237, 472482
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsFactory;
import org.eclipse.m2m.internal.qvt.oml.expressions.Helper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation.Kind;
import org.eclipse.m2m.qvt.oml.blackbox.java.Parameter;
import org.eclipse.m2m.qvt.oml.util.IContext;
import org.eclipse.ocl.Environment;

class OperationBuilder {
	
	private Java2QVTTypeResolver fTypeResolver;
	private BasicDiagnostic fProblems;
	
	OperationBuilder(Java2QVTTypeResolver typeResolver) {
		fTypeResolver = typeResolver;
	}
	
	public EOperation buildOperation(Method javaMethod) {
    	resetErrors();		
		return createOperation(javaMethod);
	}	
	
	public Diagnostic getDiagnostics() {
		return fProblems != null ? fProblems : Diagnostic.OK_INSTANCE;
	}
		
    private EOperation createOperation(Method method) {    	
    	EClassifier contextType = null;
    	String name = method.getName();
    	
    	Type resultType = null;
    	Type[] paramTypes = {};
    	
    	try {
    		resultType = method.getGenericReturnType();
    		paramTypes = method.getGenericParameterTypes();
    	}
    	catch (GenericSignatureFormatError e) {
    		reportError(e.getLocalizedMessage(), method);
    	}
    	catch (TypeNotPresentException e) {
    		reportError(e, method);
    	}
    	catch (MalformedParameterizedTypeException e) {
    		reportError(e, method);
    	}
    	
    	Annotation[][] annotations = method.getParameterAnnotations();

    	int paramTypesCount = paramTypes.length;
    	Operation operAnnotation = method.getAnnotation(Operation.class);
    	boolean isWithExecutionContext = operAnnotation != null && operAnnotation.withExecutionContext();
    	if(isWithExecutionContext && (paramTypesCount == 0 || paramTypes[0] != IContext.class)) {
    		reportError(NLS.bind(JavaBlackboxMessages.QvtoContextParameterRequired, method.getName()), method);
    		--paramTypesCount;
    	}

    	boolean isContextual = operAnnotation != null && operAnnotation.contextual();
    	if(isContextual && paramTypesCount == 0) {
    		reportError(NLS.bind(JavaBlackboxMessages.FirstContextualOperationParameterRequired, 
    				method), method);
    	}
    		
    	Operation.Kind operKind = getOperationKind(operAnnotation);
    	
        EOperation operation = null;        
        switch (operKind) {
			case OPERATION:
				operation = EcoreFactory.eINSTANCE.createEOperation();
				break;
			case MAPPING:
				operation = ExpressionsFactory.eINSTANCE.createMappingOperation();
				break;	
			case CONSTRUCTOR:
				if (!isContextual) {
					reportError(NLS.bind(JavaBlackboxMessages.ConstructorRequiresContextualOperation, method.getName()), method);
				}
				operation = ExpressionsFactory.eINSTANCE.createConstructor();
				break;
			case QUERY: case HELPER:
				Helper helper = ExpressionsFactory.eINSTANCE.createHelper();
        		helper.setIsQuery(operKind == Kind.QUERY);
        		operation = helper;
				break;
			case TRANSFORMATION:
				if (isContextual) {
					reportError(NLS.bind(JavaBlackboxMessages.TransformationRequiresContextlessOperation, method.getName()), method);
				}
				operation = EcoreFactory.eINSTANCE.createEOperation();
				break;
			default:
				assert false : "unsupported operation kind"; //$NON-NLS-1$
    			operation = EcoreFactory.eINSTANCE.createEOperation();
				break;
		}
                
        operation.setName(name);
        operation.setEType(fTypeResolver.toEClassifier(resultType, Java2QVTTypeResolver.ALLOW_SUBTYPE));
        if(operation.getEType() == null) {
        	reportError(NLS.bind(JavaBlackboxMessages.UnresolvedOclTypeForJavaType, method.getReturnType(), method), method);
        }
        
        int i = 0;
        final QvtOperationalModuleEnv environment = fTypeResolver.getEnvironment();
		for (Type paramType : paramTypes) {
            if (i == 0 && paramType == IContext.class) {
            	i++;
            	continue;
            }
            
            VarParameter varParam = (operKind != Kind.OPERATION) ? 
            		ExpressionsFactory.eINSTANCE.createVarParameter() : null;
            EParameter eParameter = varParam != null ? varParam : EcoreFactory.eINSTANCE.createEParameter();
            
            if(varParam != null) {
            	varParam.setKind(DirectionKind.IN);
            }
            
            eParameter.setEType(fTypeResolver.toEClassifier(paramType, Java2QVTTypeResolver.ALLOW_SUPERTYPE));
            if(eParameter.getEType() == null) {
            	reportError(NLS.bind(JavaBlackboxMessages.UnresolvedOclTypeForJavaType, paramType, method), method);
            }
            
            Parameter paramAnno = getParameterAnnotation(annotations[i]);
            String paramName;            
            if(paramAnno != null && paramAnno.name() != null) {
                paramName = paramAnno.name();            	
            } else {
            	paramName = "arg" + (isWithExecutionContext ? i-1 : i); //$NON-NLS-1$;            	
            }
            
            eParameter.setName(paramName);
                    	
			if(isContextual && (isWithExecutionContext ? i == 1 : i == 0)) {
        		if(operKind != Kind.OPERATION) {
        			assert operation instanceof ImperativeOperation;
        			assert varParam != null;
        			varParam.setName(Environment.SELF_VARIABLE_NAME);
        			((ImperativeOperation)operation).setContext(varParam);
        		}
        		
    			contextType = eParameter.getEType();        		
        	} else {
        		operation.getEParameters().add(eParameter);        		
        	}
        	
            i++;
        } // iterate parameters
        		
		if(operation instanceof ImperativeOperation) {
			ImperativeOperation imperativeOperation = (ImperativeOperation) operation;
			environment.defineImperativeOperation(imperativeOperation, false, true);
		} else if(contextType != null) {
			environment.getTypeResolver().resolveAdditionalOperation(contextType, operation);			
		}
				
        return operation;
    }
    
	static Kind getOperationKind(Operation operAnnotation) {
    	Operation.Kind operKind = (operAnnotation != null) ? operAnnotation.kind() : Operation.Kind.OPERATION;
    	if(operKind == Kind.OPERATION) {
    		// FIXME - avoid this, create typedef on the keeping this operation instead
    		operKind = Kind.HELPER;
    	}
		return operKind;
	}	
    
    private static Parameter getParameterAnnotation(Annotation[] allAnnotations) {
    	for (Annotation annotation : allAnnotations) {
			if(annotation.annotationType() == Parameter.class) {
				return (Parameter)annotation;
			}
		}
    	
    	return null;
    }
    
    private void resetErrors() {
    	fProblems = null;
    }
    
    private void reportError(Exception exception, Method problemMethod) {
    	reportError(exception.getLocalizedMessage(), exception, problemMethod);
    }
    
    private void reportError(String message, Method problemMethod) {
    	reportError(message, null, problemMethod);
    }
    
    private void reportError(String message, Exception exception, Method problemMethod) {
    	if(fProblems == null) {
    		fProblems = DiagnosticUtil.createRootDiagnostic(NLS.bind(JavaBlackboxMessages.LoadOperationDiagnostics, problemMethod));
    	}
    	
    	fProblems.add(DiagnosticUtil.createErrorDiagnostic(message, exception));
    }
}
