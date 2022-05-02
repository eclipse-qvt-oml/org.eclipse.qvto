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
package org.eclipse.m2m.internal.qvt.oml.blackbox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.blackbox.java.StandaloneBlackboxProvider;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandler;

/*
 * TODO - handle collisions of multiple descriptors of the same qualified name
 */
public class BlackboxRegistry {

	public static final BlackboxRegistry INSTANCE = EMFPlugin.IS_ECLIPSE_RUNNING ? new BlackboxRegistry.Eclipse()
			: new BlackboxRegistry();

	private final StandaloneBlackboxProvider fStandaloneProvider = StandaloneBlackboxProvider.INSTANCE;
	private final List<BlackboxProvider> fProviders;

	public BlackboxRegistry() {
		fProviders = Collections.<BlackboxProvider>singletonList(fStandaloneProvider);
	}

	protected List<? extends BlackboxProvider> getProviders() {
		return fProviders;
	}

	public BlackboxUnitDescriptor getCompilationUnitDescriptor(String qualifiedName, ResolutionContext context) {
		for (BlackboxProvider provider : getProviders()) {
			BlackboxUnitDescriptor descriptor = provider.getUnitDescriptor(qualifiedName, context);
			if (descriptor != null) {
				return descriptor;
			}
		}
		return null;
	}

	public List<BlackboxUnitDescriptor> getCompilationUnitDescriptors(ResolutionContext loadContext) {
		ArrayList<BlackboxUnitDescriptor> result = new ArrayList<BlackboxUnitDescriptor>();
		for (BlackboxProvider provider : getProviders()) {
			for (BlackboxUnitDescriptor abstractCompilationUnitDescriptor : provider.getUnitDescriptors(loadContext)) {
				result.add(abstractCompilationUnitDescriptor);
			}
		}
		return result;
	}

	public void cleanup() {
		for (BlackboxProvider provider : getProviders()) {
			provider.cleanup();
		}
	}

	public Collection<CallHandler> getBlackboxCallHandler(ImperativeOperation operation, QvtOperationalModuleEnv env) {
		Collection<CallHandler> result = Collections.emptyList();
		for (BlackboxProvider provider : getProviders()) {
			Collection<CallHandler> handlers = provider.getBlackboxCallHandler(operation, env);
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
		for (BlackboxProvider provider : getProviders()) {
			Collection<CallHandler> handlers = provider.getBlackboxCallHandler(transformation, env);
			if (!handlers.isEmpty()) {
				if (result.isEmpty()) {
					result = new LinkedList<CallHandler>();
				}
				result.addAll(handlers);
			}
		}
		return result;
	}
	
	public void addStandaloneModule(Class<?> cls, String unitQualifiedName, String moduleName, String[] packageURIs) {
		fStandaloneProvider.registerDescriptor(cls, unitQualifiedName, moduleName, packageURIs);
	}


	private static class Eclipse extends BlackboxRegistry {

		private static final String CLASS_ATTR = "class"; //$NON-NLS-1$

		private static final String PROVIDER_ELEMENT = "provider"; //$NON-NLS-1$

		private static final String BLACKBOX_PROVIDER_EXTENSION = "blackboxProvider"; //$NON-NLS-1$

		private final List<BlackboxProvider> fProviders;

		@Override
		protected List<? extends BlackboxProvider> getProviders() {
			return fProviders;
		}

		Eclipse() {
			fProviders = new LinkedList<BlackboxProvider>();
			readProviders(fProviders);
		}

		private void readProviders(List<BlackboxProvider> providers) {
			IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor(QvtPlugin.ID,
					BLACKBOX_PROVIDER_EXTENSION);

			for (IConfigurationElement element : configs) {
				try {
					if (element.getName().equals(PROVIDER_ELEMENT)) {
						Object extension = element.createExecutableExtension(CLASS_ATTR);
						if (extension instanceof BlackboxProvider == false) {
							QvtPlugin.error("Provider must implement AbstractBlackboxProvider interface: " + extension); //$NON-NLS-1$
							continue;
						}

						providers.add((BlackboxProvider) extension);
					}
				} catch (CoreException e) {
					QvtPlugin.getDefault().log(e.getStatus());
				} catch (RuntimeException e) {
					QvtPlugin.error(e);
				}
			}
		}
	}

}
