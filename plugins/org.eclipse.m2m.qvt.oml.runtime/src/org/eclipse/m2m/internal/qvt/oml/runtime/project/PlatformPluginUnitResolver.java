/*******************************************************************************
 * Copyright (c) 2009, 2020 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bug 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.runtime.project;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.compiler.BlackboxUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompositeUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.DelegatingUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.ResolverUtils;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitContents;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitResolver;
import org.eclipse.m2m.internal.qvt.oml.runtime.QvtRuntimePlugin;
import org.osgi.framework.Bundle;

/**
 * @author dvorak
 */
public class PlatformPluginUnitResolver extends DelegatingUnitResolver {
	
    public static final String SOURCE_CONTAINER_POINT = QvtRuntimePlugin.ID + ".qvtTransformationContainer"; //$NON-NLS-1$
    public static final String SOURCE_CONTAINER = "sourceContainer"; //$NON-NLS-1$
    public static final String CONTAINER_PATH = "path"; //$NON-NLS-1$
	
	private final Bundle fBundle;
	private final List<IPath> fSrcContainers;
    private static final Map<String, Set<IPath>> ourPluginSourceContainers = loadPluginSourceContainers();

	public PlatformPluginUnitResolver(Bundle bundle) {
		this(bundle, getSourceContainers(bundle));
	}
	
	public PlatformPluginUnitResolver(Bundle bundle, IPath... sourceContainers) {
		if (bundle == null) {
			throw new IllegalArgumentException("null bundle"); //$NON-NLS-1$
		}
		
		if(sourceContainers == null) {
			throw new IllegalArgumentException("null source containter"); //$NON-NLS-1$
		}		
		
		List<IPath> containers = Arrays.asList(sourceContainers);
		if(containers.contains(null)) {
			throw new IllegalArgumentException("null source containter"); //$NON-NLS-1$
		}
		
		fBundle = bundle;
		fSrcContainers = new ArrayList<IPath>(containers);		
	}	

	@Override
	public UnitProxy doResolveUnit(String qualifiedName) {
		IPath unitNsRelativePath = new Path(ResolverUtils.toNamespaceRelativeUnitFilePath(qualifiedName));
		
		for (IPath nextContainer : fSrcContainers) {
			IPath unitBundleRelativePath = nextContainer.append(unitNsRelativePath);
			
			URL unitURL = fBundle.getEntry(unitBundleRelativePath.toString());
			if (unitURL == null) {
				continue;
			}
		
			IPath unitAbsolutePath = new Path(fBundle.getSymbolicName()).append(unitBundleRelativePath);
			URI unitURI = URI.createPlatformPluginURI(unitAbsolutePath.toString(), false);
			String[] segments = ResolverUtils.getNameSegments(qualifiedName);
			String namespace = null;
			if (segments.length > 1) {
				namespace = ResolverUtils.toQualifiedName(segments, 0, segments.length - 2);
			}

			String name = segments[segments.length - 1];
			return new BundleUnit(namespace, name, unitURI, unitURL);			
		}
		
		return null;
	}

	public static void setupResolver(PlatformPluginUnitResolver resolver, boolean useBlackbox, boolean useDeployed) {
		if(useBlackbox && !useDeployed) {
			resolver.setParent(BlackboxUnitResolver.DEFAULT);
		} else if(useBlackbox && useDeployed) {
			resolver.setParent(new CompositeUnitResolver(
					DeployedImportResolver.INSTANCE, 
					BlackboxUnitResolver.DEFAULT));
		}		
	}
	
	private static Map<String, Set<IPath>> loadPluginSourceContainers() {
		Map<String, Set<IPath>> sourceContainers = new HashMap<String, Set<IPath>>();
	    IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor(SOURCE_CONTAINER_POINT);
	    for (int i = 0; i < configurationElements.length; i++) {
	        IConfigurationElement element = configurationElements[i];
	        
	        if (SOURCE_CONTAINER.equals(element.getName())) {
		        String namespace = element.getNamespaceIdentifier();
		        String containerPath = element.getAttribute(CONTAINER_PATH);
		        if (containerPath == null || containerPath.trim().length() == 0) {
		        	continue;
		        }
		        Set<IPath> containers = sourceContainers.get(namespace);
		        if (containers == null) {
		        	containers = new HashSet<IPath>();
		        	sourceContainers.put(namespace, containers);
		        }
	        	containers.add(new Path(containerPath));
	        }
	    }
	    return Collections.unmodifiableMap(sourceContainers);
	}
	
	static IPath[] getSourceContainers(Bundle bundle) {
		Set<IPath> containers = ourPluginSourceContainers.get(bundle.getSymbolicName());
		if (containers != null) {
			return containers.toArray(new IPath[containers.size()]);
		}
				
		return new IPath[] { Path.ROOT };
	}

   
	private final class BundleUnit extends UnitProxy {

		private final URL url;

		private BundleUnit(String namespace, String unitName, URI unitURI, URL unitURL) {
			super(namespace, unitName, unitURI);
			this.url = unitURL;
		}

		@Override
		public int getContentType() {
			return UnitProxy.TYPE_CST_STREAM;
		}

		@Override
		public UnitContents getContents() throws IOException {
			return new UnitContents.CSTContents() {
				public Reader getContents() throws IOException {
					URLConnection connection = url.openConnection();

					String charset = connection.getContentEncoding();
					if (charset == null) {
						charset = "UTF-8"; //$NON-NLS-1$
					}

					return new InputStreamReader(connection.getInputStream(), charset);
				}
			};
		}

		@Override
		public UnitResolver getResolver() {
			return PlatformPluginUnitResolver.this;
		}
	}
}
