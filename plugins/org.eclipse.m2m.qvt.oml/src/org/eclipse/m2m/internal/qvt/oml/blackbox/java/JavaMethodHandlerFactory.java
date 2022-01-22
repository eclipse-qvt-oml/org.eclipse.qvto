/*******************************************************************************
 * Copyright (c) 2008, 2019 Borland Software Corporation and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bug 289982
 *     Camille Letavernier - Bug 458651
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.ast.env.InternalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModuleInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.NumberConversions;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtInterruptedExecutionException;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtRuntimeException;
import org.eclipse.m2m.internal.qvt.oml.evaluator.TransformationInstance;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandler;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandlerAdapter;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation;
import org.eclipse.ocl.types.OCLStandardLibrary;

class JavaMethodHandlerFactory {

	private static int FAILURE_COUNT_TOLERANCE = 5;

	final private Object fInvalid;
	final private QvtOperationalModuleEnv fModuleEnv;
	
	JavaMethodHandlerFactory(QvtOperationalModuleEnv moduleEnv) {
		fInvalid = moduleEnv.getOCLStandardLibrary().getInvalid();
		fModuleEnv = moduleEnv;
	}
	
	/**
	 * @deprecated Call {@link #JavaMethodHandlerFactory(QvtOperationalModuleEnv)} instead.
	 */
	@Deprecated
	JavaMethodHandlerFactory(OCLStandardLibrary<EClassifier> oclStdLib) {
		fInvalid = oclStdLib.getInvalid();
		fModuleEnv = null;
	}

	CallHandler createHandler(Method method) {
		if(method == null) {
			throw new IllegalArgumentException();
		}

		Operation opAnnotation = method.getAnnotation(Operation.class);
		return new Handler(method, opAnnotation != null && opAnnotation.contextual(),
				opAnnotation != null && opAnnotation.withExecutionContext());
	}

	private Object getInvalidResult() {
		return fInvalid;
	}

	private class Handler extends CallHandler {

		private final Method fMethod;
		private final Class<?>[] fCachedParamTypes;
		private final boolean fIsContextual;
		private final boolean fWithExecutionContext;
		private final boolean fRequiresNumConversion;
		private volatile int fFatalErrorCount;

		Handler(Method method, boolean isContextual, boolean isWithExecutionContext) {
			assert method != null;

			fMethod = method;
			fCachedParamTypes = fMethod.getParameterTypes();
			fIsContextual = isContextual;
			fWithExecutionContext = isWithExecutionContext;
			fRequiresNumConversion = requiresNumberConversion();
			fFatalErrorCount = 0;
		}

		@Override
		public Object invoke(ModuleInstance module, Object source, Object[] args, QvtOperationalEvaluationEnv evalEnv) {
			try {
				if(isDisabled()) {
					return getInvalidResult();
				}

				Object[] actualArgs = prepareArguments(source, args, evalEnv);
				Object javaCallSource = null;

				boolean isStatic = Modifier.isStatic(fMethod.getModifiers());
				if(!isStatic) {
					Class<?> moduleJavaClass = fMethod.getDeclaringClass();
					javaCallSource = getJavaCallSource(module, moduleJavaClass, evalEnv); //module.getAdapter(moduleJavaClass);
					assert javaCallSource != null;
				}

				return fMethod.invoke(javaCallSource, actualArgs);
			}
			catch (Throwable t) {	// IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException
				if (t instanceof InvocationTargetException) {
					t = ((InvocationTargetException)t).getTargetException();
				}
				if (t instanceof OperationCanceledException) {
					throw new QvtInterruptedExecutionException();
				}
				incrementFatalErrorCount();
				QvtPlugin.error(NLS.bind(JavaBlackboxMessages.MethodInvocationError, fMethod), t);
				String localized = "\nCaused by: " + t.getClass().getName() + //$NON-NLS-1$
						(t.getLocalizedMessage() == null ? "" : ": " + t.getLocalizedMessage()); //$NON-NLS-1$ //$NON-NLS-2$
				evalEnv.getAdapter(InternalEvaluationEnv.class).throwQVTException(
						new QvtRuntimeException(NLS.bind(JavaBlackboxMessages.MethodInvocationError, fMethod) + localized, t));
				return CallHandlerAdapter.getInvalidResult(evalEnv);
			}
		}

		private void incrementFatalErrorCount(){
			fFatalErrorCount++;
		}

		private Object getJavaCallSource(ModuleInstance moduleInstance, Class<?> javaClass, QvtOperationalEvaluationEnv evalEnv)
				throws IllegalAccessException, InstantiationException {

			Object callSource = moduleInstance.getAdapter(javaClass);
			if (callSource != null) {
				return callSource;
			}

			TransformationInstance rootTransformation = evalEnv.getRoot().getAdapter(InternalEvaluationEnv.class).getCurrentTransformation();

			callSource = rootTransformation.getAdapter(javaClass);
			if (callSource == null) {
				callSource = javaClass.newInstance();
				rootTransformation.getAdapter(ModuleInstance.Internal.class).addAdapter(callSource);
			}

			moduleInstance.getAdapter(ModuleInstance.Internal.class).addAdapter(callSource);

			return callSource;
		}

		private boolean isDisabled() {
			return fFatalErrorCount > FAILURE_COUNT_TOLERANCE;
		}

		private Object[] prepareArguments(Object source, Object[] args, QvtOperationalEvaluationEnv evalEnv) {
			int argCount = args.length;
			if (fIsContextual) {
				argCount++;
			}
			if (fWithExecutionContext) {
				argCount++;
			}

			Object resultArgs[] = new Object[argCount];

			int argIndex = 0;
			if (fWithExecutionContext) {
				resultArgs[argIndex] = evalEnv.getContext();
				argIndex++;
			}
			if (fIsContextual) {
				
				// wrap dynamic EObject in proxy
				if(source instanceof EObject) {
					EObject eObject = (EObject) source;
					
					if (EmfUtil.isDynamic(eObject) && !fCachedParamTypes[argIndex].isAssignableFrom(eObject.getClass())) {
						if (fCachedParamTypes[argIndex].isInterface()) {
							source = convertEObjectToProxy(eObject, fCachedParamTypes[argIndex]);
						}
					}
				}
				
				resultArgs[argIndex] = source;
				argIndex++;
			}

			// filter out possible OclInvalid argument values passed from AST based evaluation
			// source can't be this case as the call can not be made
			for (int i = 0; i < args.length; i++) {
				Object nextArg = args[i];
				if(nextArg == getInvalidResult()) {
					// convert OclInvalid to 'null' as java reflection invocation would fail
					// with the argument class incompatible to the method signature
					nextArg = null;
				}
				// number have to converted as java binary compatible
				if(fRequiresNumConversion) {
					nextArg = NumberConversions.convertNumber(nextArg, fCachedParamTypes[argIndex]);
				}
				// wrap dynamic EObject in proxy 
				if(nextArg instanceof EObject) {
					EObject eObject = (EObject) nextArg;
					
					if (EmfUtil.isDynamic(eObject) && !fCachedParamTypes[argIndex].isAssignableFrom(eObject.getClass())) {
						if (fCachedParamTypes[argIndex].isInterface()) {
							nextArg = convertEObjectToProxy(eObject, fCachedParamTypes[argIndex]);
						}
					}
				}
				resultArgs[argIndex++] = nextArg;
			}

			return resultArgs;
		}
				
		private boolean requiresNumberConversion() {
			assert fMethod != null;

			for (Class<?> paramType : fMethod.getParameterTypes()) {
				if(Number.class.isAssignableFrom(paramType)) {
					return true;
				}
			}
			return false;
		}
		
		private Object convertEObjectToProxy(final EObject eObject, Class<?> proxyClass) {
			
			InvocationHandler handler = new InvocationHandler() {
				
				public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
									
					List<EOperation> operations = eObject.eClass().getEAllOperations();
					
					for (EOperation operation : operations) {
						if (isMatch(method, operation)) {							
							return eObject.eInvoke(operation, args == null ? ECollections.newBasicEList() : ECollections.newBasicEList(args));
						}
					}
					
					List<EStructuralFeature> features = eObject.eClass().getEAllStructuralFeatures();
					
					for (EStructuralFeature feature : features) {
						if (method.getName().equalsIgnoreCase("get" + feature.getName())) { //$NON-NLS-1$
							return eObject.eGet(feature);
						}
						else if (method.getName().equalsIgnoreCase("set" + feature.getName())) { //$NON-NLS-1$
							Object newValue = feature.isMany() ? ECollections.newBasicEList(args) : args.length > 0 ? args[0] : null;
							eObject.eSet(feature, newValue);
							return null;
						}
					};
					
					return method.invoke(eObject, args);
				}
			};
			
			return Proxy.newProxyInstance(proxyClass.getClassLoader(), new Class<?>[] {proxyClass}, handler);
		}
		
		private boolean isMatch(Method method, EOperation operation) {
			if (!operation.getName().equals(method.getName())) {
				return false;
			}
				
			EClassifier eType = fModuleEnv.getUMLReflection().getOCLType(operation.getEType());
			
			List<String> nsURIs;
			Java2QVTTypeResolver typeResolver;
			EClassifier eClassifier;
		
			if (eType != null) {
				nsURIs = Collections.singletonList(eType.getEPackage().getNsURI());
				typeResolver = new Java2QVTTypeResolver(fModuleEnv, nsURIs, new BasicDiagnostic());
				eClassifier = typeResolver.toEClassifier(method.getGenericReturnType(), Java2QVTTypeResolver.STRICT_TYPE);
			
				if (eClassifier != eType) {
					return false;
				}
			}
				
			List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());
			List<EParameter> eParameters = operation.getEParameters();
			
			if (parameterTypes.size() != eParameters.size()) {
				return false;
			}
				
			Iterator<Class<?>> paramTypesIterator = parameterTypes.iterator();
		
			for (EParameter param : eParameters) {
				eType = fModuleEnv.getUMLReflection().getOCLType(param.getEType());
				nsURIs = Collections.singletonList(eType.getEPackage().getNsURI());
				
				Class<?> parameterType = paramTypesIterator.next();
				typeResolver = new Java2QVTTypeResolver(fModuleEnv, nsURIs, new BasicDiagnostic());
				eClassifier = typeResolver.toEClassifier(parameterType, Java2QVTTypeResolver.STRICT_TYPE);
				
				if (eClassifier != eType) {
					return false;
				}
			}
			
			return true;
		}
	}
}
