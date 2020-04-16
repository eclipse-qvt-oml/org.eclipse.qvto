/*******************************************************************************
 * Copyright (c) 2007, 2020 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 289982, 326871, 427237, 472482
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalVisitorCS;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.ValidationMessages;
import org.eclipse.m2m.internal.qvt.oml.compiler.BlackboxUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModuleInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModuleInstanceFactory;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandler;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandlerAdapter;

public abstract class BlackboxProvider {
		
	private static final ResolutionContext GLOBAL_RESOLUTION_CONTEXT = new ResolutionContextImpl(BlackboxUnitResolver.GLOBAL_CONTEXT);

	public interface InstanceAdapterFactory {
		Object createAdapter(EObject moduleInstance);
	}

	protected BlackboxProvider() {
		super();
	}

	protected BlackboxUnit createBlackboxUnit(
			QvtOperationalModuleEnv moduleEnv) {
		return createBlackboxUnit(Collections.singletonList(moduleEnv));
	}

	protected BlackboxUnit createBlackboxUnit(
			final List<QvtOperationalModuleEnv> loadedModules) {
		return new BlackboxUnit() {
			public List<QvtOperationalModuleEnv> getElements() {
				return Collections.unmodifiableList(loadedModules);
			}
			public Diagnostic getDiagnostic() {
				return Diagnostic.OK_INSTANCE;
			}
		};
	}

	public static void setInstanceAdapterFactory(Module module, final InstanceAdapterFactory factory) {
		ModuleInstanceFactory moduleInstanceFactory = (ModuleInstanceFactory) module
				.getEFactoryInstance();
		moduleInstanceFactory
				.addPostCreateHandler(new ModuleInstanceFactory.PostCreateHandler() {
					public void created(ModuleInstance moduleInstance) {
						Object adapterInstance = factory
								.createAdapter(moduleInstance);
						moduleInstance
								.getAdapter(ModuleInstance.Internal.class)
								.addAdapter(adapterInstance);
					}
				});
	}

	protected void setOperationHandler(EOperation operation,
			final CallHandler handler, boolean adaptSource) {
		CallHandler actualHandler = handler;
		if (adaptSource) {
			actualHandler = new CallHandler() {
				public Object invoke(ModuleInstance module, Object source,
						Object[] args, QvtOperationalEvaluationEnv evalEnv) {
					return handler.invoke(module, source, args, evalEnv);
				}
			};
		}

		CallHandlerAdapter.attach(operation, actualHandler);
	}

	public abstract Collection<? extends BlackboxUnitDescriptor> getUnitDescriptors(
			ResolutionContext resolutionContext);

	public abstract BlackboxUnitDescriptor getUnitDescriptor(
			String qualifiedName, ResolutionContext resolutionContext);
	
	public abstract void cleanup();
	
	private void handleBlackboxException(BlackboxException e, BlackboxUnitDescriptor descriptor) {
		
		Diagnostic diagnostic = e.getDiagnostic();
		if(diagnostic != null) {
			QvtPlugin.logDiagnostic(diagnostic);					
		} else {
			QvtPlugin.error(NLS.bind(ValidationMessages.FailedToLoadUnit, 
					new Object[] { descriptor.getQualifiedName() }), e);
		}
		
	}
	
	private ResolutionContext getResolutionContext(QvtOperationalModuleEnv env) {
		URI sourceURI = QvtOperationalVisitorCS.getSourceURI(env);
		return sourceURI != null ? new ResolutionContextImpl(sourceURI) : GLOBAL_RESOLUTION_CONTEXT;
	}
	
	public Collection<CallHandler> getBlackboxCallHandler(ImperativeOperation operation, QvtOperationalModuleEnv env) {
		Collection<CallHandler> result = Collections.emptyList();
		for (BlackboxUnitDescriptor d : getUnitDescriptors(getResolutionContext(env))) {
			if (env.getImportedNativeLibs().isEmpty()) {
				try {
					d.load(new LoadContext(env.getEPackageRegistry()));
				} catch (BlackboxException e) {
					handleBlackboxException(e, d);
					
					continue;
				}
			}
			else {
				if (!env.getImportedNativeLibs().containsKey(d.getURI())) {
					continue;
				}
			}
			
			Collection<CallHandler> handlers = d.getBlackboxCallHandler(operation, env);
			if (!handlers.isEmpty()) {
				if (result.isEmpty()) {
					result = new LinkedList<CallHandler>();
				}
				result.addAll(handlers);
			}
		}		
		return result;
	}
	
	public Collection<CallHandler> getBlackboxCallHandler(OperationalTransformation transformation, QvtOperationalModuleEnv env) {
		Collection<CallHandler> result = Collections.emptyList();
		for (BlackboxUnitDescriptor d : getUnitDescriptors(getResolutionContext(env))) {
			if (env.getImportedNativeLibs().isEmpty()) {
				try {
					d.load(new LoadContext(env.getEPackageRegistry()));
				} catch (BlackboxException e) {
					handleBlackboxException(e, d);
					
					continue;
				}
			}
			else {
				if (!env.getImportedNativeLibs().containsKey(d.getURI())) {
					continue;
				}
			}
			
			Collection<CallHandler> handlers = d.getBlackboxCallHandler(transformation, env);
			if (!handlers.isEmpty()) {
				if (result.isEmpty()) {
					result = new LinkedList<CallHandler>();
				}
				result.addAll(handlers);
			}
		}		
		return result;
	}
	
}
