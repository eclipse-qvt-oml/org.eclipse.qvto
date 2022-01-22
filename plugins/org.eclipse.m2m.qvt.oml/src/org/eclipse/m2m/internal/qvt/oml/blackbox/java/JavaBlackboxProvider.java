/*******************************************************************************
 * Copyright (c) 2008, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 289982, 326871, 427237
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.ast.binding.ASTBindingHelper;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxException;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxProvider;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnit;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnitDescriptor;
import org.eclipse.m2m.internal.qvt.oml.blackbox.LoadContext;
import org.eclipse.m2m.internal.qvt.oml.blackbox.OperationMatcher;
import org.eclipse.m2m.internal.qvt.oml.common.util.StringLineNumberProvider;
import org.eclipse.m2m.internal.qvt.oml.cst.CSTFactory;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtilPlugin;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandler;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandlerAdapter;

public abstract class JavaBlackboxProvider extends BlackboxProvider {

	public static final String CLASS_NAME_SEPARATOR = "."; //$NON-NLS-1$
	static InstanceAdapterFactory createInstanceAdapterFactory(final Class<?> javaModuleClass) {
		return new InstanceAdapterFactory() {
			public Object createAdapter(EObject moduleInstance) {
				try {
					return javaModuleClass.newInstance();
				} catch (InstantiationException e) {
					// FIXME - choose a better exception
					throw new IllegalArgumentException("Illegal adapter instance", e); //$NON-NLS-1$
				} catch (IllegalAccessException e) {
					// FIXME - choose a better exception
					throw new IllegalArgumentException("Illegal adapter instance", e); //$NON-NLS-1$
				}
			}
		};
	}
	
	private JavaModuleLoader createJavaModuleLoader() {
		return new JavaModuleLoader() {
			JavaMethodHandlerFactory handlerFactory;

			@Override
			protected void loadModule(QvtOperationalModuleEnv moduleEnv, final Class<?> javaModuleClass) {
				handlerFactory = new JavaMethodHandlerFactory(moduleEnv);

				Module module = moduleEnv.getModuleContextType();
				// FIXME - workaround to make Environment available with the
				// module for
				// non-transformation execution context
				ASTBindingHelper.createCST2ASTBinding(CSTFactory.eINSTANCE.createLibraryCS(), module, moduleEnv);

				setInstanceAdapterFactory(module, createInstanceAdapterFactory(javaModuleClass));
			}

			@Override
			protected void loadOperation(EOperation operation, Method javaOperation) {
				setOperationHandler(operation, handlerFactory.createHandler(javaOperation), true);
			}
		};
	}

	protected static String getPackageNameFromJavaClass(String className) {
		int lastSeparatorPos = className.lastIndexOf(CLASS_NAME_SEPARATOR);
		if (lastSeparatorPos < 0) {
			return null;
		}

		return className.substring(0, lastSeparatorPos);
	}

	protected static String getSimpleNameFromJavaClass(String className) {
		int lastSeparatorPos = className.lastIndexOf(CLASS_NAME_SEPARATOR);
		if (lastSeparatorPos < 0) {
			return className;
		}

		return className.substring(lastSeparatorPos + 1);
	}

	public abstract class JavaUnitDescriptor extends BlackboxUnitDescriptor {

		private BlackboxUnit unit;
		
		private Map<ModuleHandle, Map<String, List<EOperation>>> fModules = new LinkedHashMap<ModuleHandle, Map<String, List<EOperation>>>();

		public JavaUnitDescriptor(String unitQualifiedName) {
			super(JavaBlackboxProvider.this, unitQualifiedName);
		}

		protected void addModuleHandle(ModuleHandle moduleHandle) {
			fModules.put(moduleHandle, new LinkedHashMap<String, List<EOperation>>());
		}

		public Collection<CallHandler> getBlackboxCallHandler(ImperativeOperation imperativeOp, QvtOperationalModuleEnv env) {
			Set<String> importedLibs = env.getImportedNativeLibs().get(getURI());
			Collection<CallHandler> result = Collections.emptyList();

			for (Map.Entry<ModuleHandle, Map<String, List<EOperation>>> nextEntry : fModules.entrySet()) {
				if (!env.getImportedNativeLibs().isEmpty()) {
					if (!importedLibs.contains(nextEntry.getKey().getModuleName())) {
						continue;
					}
				}

				List<EOperation> listOp = nextEntry.getValue().get(imperativeOp.getName());
				if (listOp == null) {
					continue;
				}

				for (EOperation libraryOp : listOp) {
					if (OperationMatcher.matchOperation(env, imperativeOp, libraryOp)) {
						if (result.isEmpty()) {
							result = new LinkedList<CallHandler>();
						}
						result.add(CallHandlerAdapter.getDispatcher(libraryOp));
					}
				}
			}

			return result;
		}

		public Collection<CallHandler> getBlackboxCallHandler(OperationalTransformation transformation,
				QvtOperationalModuleEnv env) {
			Set<String> importedLibs = env.getImportedNativeLibs().get(getURI());
			Collection<CallHandler> result = Collections.emptyList();

			for (Map.Entry<ModuleHandle, Map<String, List<EOperation>>> nextEntry : fModules.entrySet()) {
				if (!env.getImportedNativeLibs().isEmpty()) {
					if (!importedLibs.contains(nextEntry.getKey().getModuleName())) {
						continue;
					}
				}

				List<EOperation> listOp = nextEntry.getValue().get(transformation.getName());
				if (listOp == null) {
					continue;
				}

				for (EOperation libraryOp : listOp) {
					if (OperationMatcher.matchOperation(env, transformation, libraryOp)) {
						if (result.isEmpty()) {
							result = new LinkedList<CallHandler>();
						}
						result.add(CallHandlerAdapter.getDispatcher(libraryOp));
					}
				}
			}

			return result;
		}
		
		@Override
		public BlackboxUnit load(LoadContext context) throws BlackboxException {
						
			if (unit != null) {
				return unit;
			}

			JavaModuleLoader javaModuleLoader = createJavaModuleLoader();
			Resource unitResource = new EcoreResourceFactoryImpl().createResource(getURI());

			BasicDiagnostic errors = null;
			List<QvtOperationalModuleEnv> loadedModules = new LinkedList<QvtOperationalModuleEnv>();

			for (Map.Entry<ModuleHandle, Map<String, List<EOperation>>> nextEntry : fModules.entrySet()) {
				Diagnostic diagnostic = javaModuleLoader.loadModule(nextEntry.getKey(), nextEntry.getValue(), context);

				if (EmfUtilPlugin.isSuccess(diagnostic)) {
					QvtOperationalModuleEnv nextModuleEnv = javaModuleLoader.getLoadedModule();
					nextModuleEnv.getTypeResolver().getResource().setURI(getURI());
					ASTBindingHelper.createModuleSourceBinding(nextModuleEnv.getModuleContextType(), getURI(),
							new StringLineNumberProvider("")); //$NON-NLS-1$

					loadedModules.add(nextModuleEnv);
					unitResource.getContents().add(nextModuleEnv.getModuleContextType());

					if (diagnostic.getSeverity() != Diagnostic.OK) {
						QvtPlugin.logDiagnostic(diagnostic);
					}
				} else {
					if (errors == null) {
						String message = NLS.bind(JavaBlackboxMessages.BlackboxUnitLoadFailed, getQualifiedName());
						errors = DiagnosticUtil.createErrorDiagnostic(message);
					}

					errors.add(diagnostic);
				}
			}

			if (errors != null) {
				final Diagnostic failDiagnostic = errors;
				
				unit = new BlackboxUnit() {

					public List<QvtOperationalModuleEnv> getElements() {
						return Collections.emptyList();
					}

					public Diagnostic getDiagnostic() {
						return failDiagnostic;
					}
					
				};
				
				assert errors.getSeverity() == Diagnostic.ERROR;
				throw new BlackboxException(errors);
			}

			unit = createBlackboxUnit(loadedModules);
			
			return unit;
		}
		
		protected void unload() {
			for (Map<String, List<EOperation>> operations : fModules.values()) {
				operations.clear();
			}
			
			unit = null;
		}
		
	}

}
