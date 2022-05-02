/*******************************************************************************
 * Copyright (c) 2022 Christopher Gerking and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnitDescriptor;
import org.eclipse.m2m.internal.qvt.oml.blackbox.ResolutionContext;
import org.eclipse.m2m.internal.qvt.oml.compiler.BlackboxUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.ResolverUtils;
import org.eclipse.m2m.qvt.oml.blackbox.java.Module;
import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.wiring.BundleWiring;

public class OSGiBlackboxProvider extends JavaBlackboxProvider {
	
	public static final String URI_BLACKBOX_OSGI_QUERY = "osgi"; //$NON-NLS-1$
	
	private final Map<Bundle, Map<String, BlackboxUnitDescriptor>> importedDescriptors = new HashMap<Bundle, Map<String, BlackboxUnitDescriptor>>();
	private final Map<Bundle, Map<String, BlackboxUnitDescriptor>> annotatedDescriptors = new HashMap<Bundle, Map<String, BlackboxUnitDescriptor>>();
		
	@Override
	public BlackboxUnitDescriptor getUnitDescriptor(String qualifiedName, ResolutionContext resolutionContext) {
		
		BlackboxUnitDescriptor standaloneDescriptor = StandaloneBlackboxProvider.INSTANCE.getUnitDescriptor(qualifiedName, resolutionContext);
		
		if (standaloneDescriptor != null) {
			return standaloneDescriptor;
		}
		
		Bundle bundle = getBundle(resolutionContext);
			
		if (bundle != null) {
			Map<String, BlackboxUnitDescriptor> descriptors = importedDescriptors.get(bundle);
						
			if (descriptors == null) {
				descriptors = new HashMap<String, BlackboxUnitDescriptor>();
				importedDescriptors.put(bundle, descriptors);
			}
			
			if (descriptors.containsKey(qualifiedName)) {
				return descriptors.get(qualifiedName);
			}
			
			BlackboxUnitDescriptor result = loadDescriptor(bundle, qualifiedName, false);
			
			if (result != null) {
				descriptors.put(qualifiedName, result);
			};
				
			return result;
		}
		
		return null;
	}
	
	@Override
	public Collection<? extends BlackboxUnitDescriptor> getUnitDescriptors(ResolutionContext resolutionContext) {
		
		Collection<? extends BlackboxUnitDescriptor> standaloneDescriptors = StandaloneBlackboxProvider.INSTANCE.getUnitDescriptors(resolutionContext);
		
		Bundle bundle = getBundle(resolutionContext);
				
		if (bundle != null) {
			
			Map<String, BlackboxUnitDescriptor> descriptors = new HashMap<String, BlackboxUnitDescriptor>(
					resolutionContext.getImports().isEmpty() ? getAnnotatedDescriptors(bundle) : getImportedDescriptors(bundle)
			);
						
			for (BlackboxUnitDescriptor d : standaloneDescriptors) {
				descriptors.put(d.getQualifiedName(), d);
			}
			
			return descriptors.values();
		}
			
		return standaloneDescriptors;
	}
	
	private static Bundle getBundle(ResolutionContext resolutionContext) {
		URI contextUri = reconvert(resolutionContext.getURI());
						
		if (contextUri.isPlatformPlugin() && contextUri.segmentCount() > 1) {	
			return Platform.getBundle(contextUri.segment(1));
		}
		
		return null;
	}
	
	private Map<String, BlackboxUnitDescriptor> getImportedDescriptors(final Bundle bundle) {
		Map<String, BlackboxUnitDescriptor> descriptors = importedDescriptors.get(bundle);
		
		if (descriptors == null) {
			return Collections.emptyMap();
		}
		
		return descriptors;
	}
	
	private static IPath[] getBundleClassPath(Bundle bundle) {
		String header = bundle.getHeaders().get(Constants.BUNDLE_CLASSPATH);
		
		try {
		    ManifestElement[] headerElements = ManifestElement.parseHeader(Constants.BUNDLE_CLASSPATH, header);
		    
		    if (headerElements == null || headerElements.length == 0) {
		    	return new IPath[] {Path.EMPTY};
		    }
									
			IPath[] classPath = new Path[headerElements.length];
			
			for (int i=0; i<headerElements.length; i++) {
				String value = headerElements[i].getValue();
							
				if (value.equals(".")) { //$NON-NLS-1$
					classPath[i] = Path.EMPTY;
				}
				else {
					classPath[i] = new Path(value);
				}
			}
			
			return classPath;
		} catch (BundleException e) {
			return new IPath[] {};
		}
	}
	
	private IPath resolveAgainstBundleClassPath(IPath path, IPath[] classPath) {		
		for (IPath entry : classPath) {
			if (entry.isPrefixOf(path)) {
				return path.makeRelativeTo(entry);
			}
		}
		
		return null;
	}
	
	private Map<String, BlackboxUnitDescriptor> getAnnotatedDescriptors(final Bundle bundle) {
		
		Map<String, BlackboxUnitDescriptor> descriptors = annotatedDescriptors.get(bundle);
		
		if (descriptors == null) {
			descriptors = new HashMap<String, BlackboxUnitDescriptor>();
					
			BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
						
			if (bundleWiring != null) {
				Collection<String> classResources = bundleWiring.listResources(Path.ROOT.toString(), "*.class", BundleWiring.LISTRESOURCES_RECURSE); //$NON-NLS-1$
				
				if (classResources != null) {
					IPath[] classPath = getBundleClassPath(bundle);
					
					for (String classResource : classResources) {
						if (!classResource.contains("$")) { //$NON-NLS-1$
							IPath path = resolveAgainstBundleClassPath(new Path(classResource), classPath);
							
							if (path != null) {
						    	String qualifiedName = ResolverUtils.toQualifiedName(path.removeFileExtension());			    	
						    	BlackboxUnitDescriptor descriptor = loadDescriptor(bundle, qualifiedName, true);
						    	
						    	if (descriptor != null) {
						    		descriptors.put(qualifiedName, descriptor);
						    	}
							}
						}
					}
				}
			}
			
			annotatedDescriptors.put(bundle, descriptors);
		}
						
		return descriptors;
	}
		
	private BlackboxUnitDescriptor loadDescriptor(final Bundle bundle, String qualifiedName, boolean isModuleAnnotationRequired) {			
		try {
			Class<?> cls = bundle.loadClass(qualifiedName);
			
			if (isModuleAnnotationRequired && !cls.isAnnotationPresent(Module.class)) {
				return null;
			}
			
			return new OSGiUnitDescriptor(cls, bundle, qualifiedName) {
				@Override
				protected String getUnitQuery() {
					return URI_BLACKBOX_OSGI_QUERY + "=" + bundle.getSymbolicName(); //$NON-NLS-1$
				}
			};
		}
		catch(Throwable t) {
			return null;
		}
	}
	
	private static URI reconvert(URI uri) {		
		if (BlackboxUnitResolver.isBlackboxUnitURI(uri)) {
			String bundleSymbolicName = ResolverUtils.getQueryValue(uri, URI_BLACKBOX_OSGI_QUERY);
			
			if (bundleSymbolicName != null) {
				return URI.createPlatformPluginURI(bundleSymbolicName, true);
			}
		}
		
		return uri;
	}
	
	@Override
	public void cleanup() {
		importedDescriptors.clear();
		annotatedDescriptors.clear();
	}
			
	private class OSGiUnitDescriptor extends JavaUnitDescriptor {
		
		private Bundle bundle;
		
		OSGiUnitDescriptor(final Class<?> cls, final Bundle bundle, String unitQualifiedName) {
			super(unitQualifiedName);
			
			this.bundle = bundle;
						
			addModuleHandle(new OSGiModuleHandle(cls));
		}
				
		@Override
		public URI reconvertURI() {
			return reconvert(getURI());
		}
						
		private class OSGiModuleHandle extends ModuleHandle {
			private Class<?> cls;
			
			OSGiModuleHandle(Class<?> cls) {
				super(cls.getCanonicalName(), cls.getSimpleName());
				this.cls = cls;
			}
			
			@Override
			public Class<?> getModuleJavaClass() {
				return cls;
			}
			
			@Override
			public String toString() {			
				return super.toString() + ", bundle: " + bundle.getSymbolicName(); //$NON-NLS-1$
			}
		}
	}

}
