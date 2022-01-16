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
package org.eclipse.m2m.internal.qvt.oml.emf.util.mmregistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;

public class EmfStandaloneMetamodelProvider implements IMetamodelProvider {
	
    private Registry fRegistry;	
	
    public EmfStandaloneMetamodelProvider() {
    	this(EPackage.Registry.INSTANCE);
    }
    
    public EmfStandaloneMetamodelProvider(EPackage.Registry packageRegistry) {
    	if(packageRegistry == null) {
    		throw new IllegalArgumentException();
    	}

    	fRegistry = packageRegistry;
    }
    
    public EPackage.Registry getPackageRegistry() {
    	
   		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.setPackageRegistry(fRegistry);
		resourceSet.getURIConverter().getURIMap().putAll(EcorePlugin.computePlatformURIMap(true));
		
		@SuppressWarnings("serial")
		EPackage.Registry registry = new EPackageRegistryImpl(fRegistry) {
						
			@Override
			public EPackage getEPackage(String nsURI) {
				
				EPackage ePackage = super.getEPackage(nsURI);
				
				if (ePackage == null) {
					if (nsURI != null && MetamodelRegistry.isMetamodelFileName(nsURI)) {
				
						URI uri = URI.createURI(nsURI);
											
						try {
							Resource r = resourceSet.getResource(uri, true);
													
							if (r != null) {
								ePackage = EmfUtil.getFirstEPackageContent(r);
							}
						} catch(Throwable t) {
							return null;
						};
					}
				};
									
				return ePackage;
			}
		};
					
		return registry;
    }
    
    public IMetamodelDesc[] getMetamodels() {    	
        final List<IMetamodelDesc> descs = new ArrayList<IMetamodelDesc>(fRegistry.size());        
        for (String uri : fRegistry.keySet()) {
        	IMetamodelDesc desc = getMetamodel(uri);
        	if (desc != null) {
        		descs.add(desc);
        	}
        }
        
        return descs.toArray(new IMetamodelDesc[descs.size()]);
    }
		
	public IMetamodelDesc getMetamodel(final String nsURI) {
		
		if (fRegistry.containsKey(nsURI)) {
			
			EPackage.Descriptor descriptor = new EPackage.Descriptor() {
				
				public EPackage getEPackage() {
					return fRegistry.getEPackage(nsURI);
				}
				
				public EFactory getEFactory() {
					EPackage ePackage = getEPackage();
					
					return ePackage == null ? null : ePackage.getEFactoryInstance();
				}
			};
			
			return new EmfMetamodelDesc(descriptor, nsURI);
		}
		
		return null;
    }
    
}
