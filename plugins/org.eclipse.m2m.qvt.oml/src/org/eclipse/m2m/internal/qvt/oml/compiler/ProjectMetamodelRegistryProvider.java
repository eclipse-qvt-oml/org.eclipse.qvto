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
 *     Christopher Gerking - bug 537041
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.compiler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.mmregistry.EmfStandaloneMetamodelProvider;
import org.eclipse.m2m.internal.qvt.oml.emf.util.mmregistry.IMetamodelProvider;
import org.eclipse.m2m.internal.qvt.oml.emf.util.mmregistry.IMetamodelRegistryProvider;
import org.eclipse.m2m.internal.qvt.oml.emf.util.mmregistry.MetamodelRegistry;
import org.eclipse.m2m.internal.qvt.oml.emf.util.mmregistry.ProjectMetamodelProvider;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MetamodelURIMappingHelper;

public class ProjectMetamodelRegistryProvider implements IMetamodelRegistryProvider {

	private ResourceSet resolutionRSet;
	private	Map<String, MetamodelRegistry> perProjectRegs;
	private IMetamodelRegistryProvider delegateProvider;
	private EPackage.Registry delegateRegistry;
	
	public ProjectMetamodelRegistryProvider() {
		this(EPackage.Registry.INSTANCE);
	}
	
	public ProjectMetamodelRegistryProvider(EPackage.Registry registry) {		
		this(registry, null);
	}
		
	public ProjectMetamodelRegistryProvider(ResourceSet resourceSet) {
		this(resourceSet.getPackageRegistry(), resourceSet);
	}
	
	private ProjectMetamodelRegistryProvider(final EPackage.Registry registry, ResourceSet resourceSet) {
		
		if(registry == null) {
			throw new IllegalArgumentException();
		}
		
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();
			resourceSet.setPackageRegistry(new EPackageRegistryImpl(registry));
		}
		
		if (resourceSet instanceof ResourceSetImpl) {
		
			((ResourceSetImpl) resourceSet).setURIResourceMap(new HashMap<URI, Resource>() {
				
				public Resource get(Object key) {
					Resource resource = super.get(key);
					
					if (key instanceof URI) {
						URI uri = (URI) key;
					
						if (uri.isPlatformResource()) {
							if (!URIConverter.INSTANCE.exists(uri, null)) {
								if (!(resource instanceof Resource)) {
									URI platformPluginUri = URI.createPlatformPluginURI(uri.toPlatformString(false), false);	
									
									try {
										Resource pluginResource = EmfUtil.loadResource(platformPluginUri);
										EPackage rootPackage = EmfUtil.getFirstEPackageContent(pluginResource);
											
										if (rootPackage != null) {
											EPackage ePackage = registry.getEPackage(rootPackage.getNsURI());
											
											if (ePackage != null) {
												resource = ePackage.eResource();
												put(uri, resource);
											}
										}
									}
									catch(Exception e) {}
								}
							}
						}
					}				
					
					return resource;
				}
			});
		}
		
		resolutionRSet = resourceSet;
		
		delegateRegistry = registry;
		delegateProvider = new EmfStandaloneMetamodelRegistryProvider(delegateRegistry);
	}
	
	public ResourceSet getResolutionResourceSet() {
		return resolutionRSet;
	}
	
	public MetamodelRegistry getRegistry(IRepositoryContext context) {
		if(context == null) {
			throw new IllegalArgumentException("Null context"); //$NON-NLS-1$
		}
		
		URI uri = context.getURI();
		if(!uri.isPlatformResource()) {
			return delegateProvider.getRegistry(context);
		}
		
		IPath wsLocation = new Path(uri.toPlatformString(true));		
		IResource wsResource = ResourcesPlugin.getWorkspace().getRoot().findMember(wsLocation);		
		if(wsResource == null) {
			// not a file, could be a folder
			wsResource = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(wsLocation);		
		}
		
		if(wsResource != null) {
			IProject project = wsResource.getProject();
			if(MetamodelURIMappingHelper.hasMappingResource(project)) {
				if(perProjectRegs == null) {
					perProjectRegs = new HashMap<String, MetamodelRegistry>();
				}
				
				String projectKey = project.getFullPath().toString();
				MetamodelRegistry reg = perProjectRegs.get(projectKey);
				if (reg == null) {
					IMetamodelProvider provider = new ProjectMetamodelProvider(project, new EmfStandaloneMetamodelProvider(delegateRegistry), resolutionRSet);					
					reg = new MetamodelRegistry(provider);
					perProjectRegs.put(projectKey, reg);
				}
				return reg;
			}			
		}
		
		return delegateProvider.getRegistry(context);
	}
		
}
