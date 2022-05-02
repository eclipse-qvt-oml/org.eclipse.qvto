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
 *     Christopher Gerking - bug 326871
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnitDescriptor;
import org.eclipse.m2m.internal.qvt.oml.blackbox.ResolutionContext;


public class BundleBlackboxProvider extends OSGiBlackboxProvider {
	
	private static final String EXTENSION_POINT = "javaBlackboxUnits"; //$NON-NLS-1$
		
	private static final String UNIT_ELEM = "unit";	//$NON-NLS-1$
	private static final String LIBRARY_ELEM = "library"; //$NON-NLS-1$	
	private static final String CLASS_ATTR = "class"; //$NON-NLS-1$
	private static final String NAME_ATTR = "name";	//$NON-NLS-1$
	private static final String NAMESPACE_ATTR = "namespace"; //$NON-NLS-1$
	private static final String DESC_ATTR = "description"; //$NON-NLS-1$

	private static final String METAMODEL_ELEM = "metamodel"; //$NON-NLS-1$
	private static final String NSURI_ATTR = "nsURI"; //$NON-NLS-1$
	
	private Map<String, BlackboxUnitDescriptor> fDescriptorMap;
		
	public BundleBlackboxProvider() {
	}	

	private Map<String, BlackboxUnitDescriptor> readDescriptors() {
    	Map<String, BlackboxUnitDescriptor> descriptors = new LinkedHashMap<String, BlackboxUnitDescriptor>();
        
        IConfigurationElement[] configs = Platform.getExtensionRegistry()
        		.getConfigurationElementsFor(QvtPlugin.ID, EXTENSION_POINT);

        for (IConfigurationElement element : configs) {
            try {
            	BundleDescriptor descriptor = createDescriptor(element);
        		String id = descriptor.getQualifiedName();            	
            	if(!descriptors.containsKey(id)) {
					descriptors.put(id, descriptor);
            	} else {
            		String message = NLS.bind(JavaBlackboxMessages.UnitAlreadyRegisteredContributionIgnored, id, descriptor.getContributorId());
					QvtPlugin.error(message);
            	}
            } catch (IllegalArgumentException e) {
            	QvtPlugin.error("Failed to read java black-box definition: " + e.getMessage()); //$NON-NLS-1$
            }
        }

        return descriptors;
    }
        
	private BundleDescriptor createDescriptor(IConfigurationElement configurationElement) throws IllegalArgumentException {
		if(UNIT_ELEM.equals(configurationElement.getName())) {
			String name = configurationElement.getAttribute(NAME_ATTR);
			String namespace = configurationElement.getAttribute(NAMESPACE_ATTR);		
			if(namespace == null) {
				namespace = configurationElement.getContributor().getName();
			}
			
			String description = configurationElement.getAttribute(DESC_ATTR);		
			String qualifiedName = namespace.length() == 0 ? name : namespace + CLASS_NAME_SEPARATOR + name;
			return new BundleDescriptor(configurationElement, qualifiedName, description);
		} else if(LIBRARY_ELEM.equals(configurationElement.getName())) {
			return new BundleDescriptor(configurationElement, deriveQualifiedNameFromSimpleDefinition(configurationElement), null);
		}
		
		throw new IllegalArgumentException("Unsupported configuration element " + configurationElement); //$NON-NLS-1$		
	}
    		
	private static String deriveQualifiedNameFromSimpleDefinition(IConfigurationElement moduleElement) {
		String className = moduleElement.getAttribute(CLASS_ATTR);		
		String name = moduleElement.getAttribute(NAME_ATTR);				
		if(name == null) {
			return className;
		}
		// name overridden in descriptor
		String packageName = getPackageNameFromJavaClass(className);
		if(packageName == null) {
			return name; // default package
		}
		return packageName + CLASS_NAME_SEPARATOR + name;
	}
	
	@Override
	public BlackboxUnitDescriptor getUnitDescriptor(String qualifiedName, ResolutionContext resolutionContext) {
		// TODO - Should we necessarily be available in all contexts ? 
		BlackboxUnitDescriptor descriptor = getDescriptorMap().get(qualifiedName);
		
		if (descriptor != null) {
			return descriptor;
		}
		
		return super.getUnitDescriptor(qualifiedName, resolutionContext);
	}

	@Override
	public Collection<BlackboxUnitDescriptor> getUnitDescriptors(ResolutionContext resolutionContext) {		
		Collection<? extends BlackboxUnitDescriptor> parentDescriptors = super.getUnitDescriptors(resolutionContext);
		
		Map<String, BlackboxUnitDescriptor> descriptorMap = new LinkedHashMap<String, BlackboxUnitDescriptor>(parentDescriptors.size());
		
		for (BlackboxUnitDescriptor d : parentDescriptors) {
			descriptorMap.put(d.getQualifiedName(), d);
		}
		
		// TODO - Should we necessarily be available in all contexts ?
		descriptorMap.putAll(getDescriptorMap());
		
		return descriptorMap.values();
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		fDescriptorMap = null;
	}
	
	private Map<String, BlackboxUnitDescriptor> getDescriptorMap() {
		if (fDescriptorMap != null) {
			return fDescriptorMap;
		}

		if(EMFPlugin.IS_ECLIPSE_RUNNING) {
			fDescriptorMap = readDescriptors();
		} else {
			fDescriptorMap = Collections.emptyMap();
		}
		
		return fDescriptorMap;
	}
	
	private class BundleDescriptor extends JavaUnitDescriptor {		
		
		private String fContributingBundleId; 

		BundleDescriptor(IConfigurationElement configurationElement, String unitQualifiedName, String description) {
			super(unitQualifiedName 
					//, configurationElement.getContributor().getName(), 
			);
			fContributingBundleId = configurationElement.getContributor().getName();
			
			if(configurationElement.getName().equals(LIBRARY_ELEM)) {
				addModuleHandle(configurationElement);				
			} else {
				IConfigurationElement[] libraries = configurationElement.getChildren(LIBRARY_ELEM);			
				for (IConfigurationElement moduleElement : libraries) {
					addModuleHandle(moduleElement);
				}
			}
		}
		
		String getContributorId() {
			return fContributingBundleId;
		}
		
		private void addModuleHandle(IConfigurationElement moduleElement) {
			String bundleId = moduleElement.getContributor().getName();
			String className = moduleElement.getAttribute(CLASS_ATTR);
			String moduleName = moduleElement.getAttribute(NAME_ATTR);
			if(moduleName == null) {
				// derive the name from the java class name
				moduleName = getSimpleNameFromJavaClass(className);
			}
			
			ModuleHandle moduleHandle = new BundleModuleHandle(bundleId, className, moduleName, readUsedPackagesNsURIs(moduleElement));
			addModuleHandle(moduleHandle);
		}
		
		private List<String> readUsedPackagesNsURIs(IConfigurationElement moduleConfigElement) {
			ArrayList<String> uris = new ArrayList<String>(3);
			for (IConfigurationElement nextElement : moduleConfigElement.getChildren(METAMODEL_ELEM)) {
				String nsURI = nextElement.getAttribute(NSURI_ATTR);
				if(nsURI != null) {
					uris.add(nsURI);
				}
			}
			
			return uris;
		}
			
	}

}
