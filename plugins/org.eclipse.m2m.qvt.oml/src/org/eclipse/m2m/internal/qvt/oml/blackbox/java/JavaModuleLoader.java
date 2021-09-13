/*******************************************************************************
 * Copyright (c) 2008, 2021 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 289982, 326871, 472482
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalStdLibrary;
import org.eclipse.m2m.internal.qvt.oml.blackbox.LoadContext;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtilPlugin;
import org.eclipse.m2m.internal.qvt.oml.expressions.Library;

abstract class JavaModuleLoader {
	
	private BasicDiagnostic fDiagnostics;
	private QvtOperationalModuleEnv fEnv;
	private OperationBuilder fOperBuilder;

	JavaModuleLoader() {
		fDiagnostics = null;
	}

	public QvtOperationalModuleEnv getLoadedModule() {
		return fEnv;
	}
	
	public Diagnostic getDiagnostics() {
		return (fDiagnostics != null) ? fDiagnostics : Diagnostic.OK_INSTANCE;
	}
	
	public Diagnostic loadModule(ModuleHandle moduleHandle, Map<String, List<EOperation>> definedOperations, LoadContext loadContext) {
		fDiagnostics = DiagnosticUtil.createRootDiagnostic(NLS.bind(JavaBlackboxMessages.LoadModuleDiagnostics, moduleHandle));
		Class<?> javaClass;
		try {
			javaClass = moduleHandle.getModuleJavaClass();			
			if(!isModuleClassValid(javaClass)) {
				fDiagnostics.add(DiagnosticUtil.createErrorDiagnostic(NLS.bind(
					JavaBlackboxMessages.InvalidJavaClassForModule, 
					javaClass, moduleHandle)));
				// no sense to continue
				return fDiagnostics;
			}
		} catch (ClassNotFoundException e) {
			fDiagnostics.add(DiagnosticUtil.createErrorDiagnostic(NLS.bind(
				JavaBlackboxMessages.ModuleJavaClassNotFound, moduleHandle.getModuleName()), e));
			// no sense to continue
			return fDiagnostics;
		}
		
		Library module = QvtOperationalStdLibrary.createLibrary(moduleHandle.getModuleName());		
		fEnv = new QvtOperationalEnvFactory(loadContext.getMetamodelRegistry()).createModuleEnvironment(module);
		loadModule(fEnv, javaClass);
		
		Collection<String> usedPackages = new LinkedHashSet<String>(moduleHandle.getUsedPackages());
		
		Java2QVTTypeResolver typeResolver = new Java2QVTTypeResolver(fEnv, usedPackages, fDiagnostics);
		
		fOperBuilder = new OperationBuilder(typeResolver);
		
		try {
			Method[] methods = javaClass.getDeclaredMethods();
		
			for (Method method : methods) {
				if(!isLibraryOperation(method)) {
					continue;
				}
								
				EOperation operation = fOperBuilder.buildOperation(method);
				Diagnostic operationStatus = fOperBuilder.getDiagnostics();
				if(EmfUtilPlugin.isSuccess(operationStatus)) {
					loadOperation(operation, method);
					
					List<EOperation> listOp = definedOperations.get(operation.getName());
					if (listOp == null) {
						listOp = new LinkedList<EOperation>();
						definedOperations.put(operation.getName(), listOp);
					}
					listOp.add(operation);
				}
	
				if(operationStatus.getSeverity() != Diagnostic.OK) {
					fDiagnostics.add(operationStatus);
				}
	 		}
		} catch (NoClassDefFoundError e) {
			fDiagnostics.add(DiagnosticUtil.createErrorDiagnostic(NLS.bind(
					JavaBlackboxMessages.ModuleJavaClassNotLoadable, moduleHandle.getModuleName()), new Exception(e)));
			// no sense to continue
			return fDiagnostics;
		}

		return fDiagnostics;
	}
		
	protected abstract void loadModule(QvtOperationalModuleEnv moduleEnv, Class<?> javaModule);
	protected abstract void loadOperation(EOperation eOperation, Method javaOperation);
	
	private static boolean isLibraryOperation(Method method) {
		return Modifier.isPublic(method.getModifiers());
	}
	
	private static boolean isModuleClassValid(Class<?> javaClass) {
		Class<?>[] noParams = new Class<?>[0];
		try {
			Constructor<?> constructor = javaClass.getDeclaredConstructor(noParams);
			if(!Modifier.isPublic(constructor.getModifiers())) {
				return false;
			}
			else {		
				return Modifier.isPublic(javaClass.getModifiers());
			}
		} catch (Throwable t) { // bad class
			return false;
		}
	}
}
