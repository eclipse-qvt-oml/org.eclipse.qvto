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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnitDescriptor;
import org.eclipse.m2m.internal.qvt.oml.blackbox.ResolutionContext;


public class StandaloneBlackboxProvider extends JavaBlackboxProvider {
	
	public static final StandaloneBlackboxProvider INSTANCE = new StandaloneBlackboxProvider();
	
	private Map<String, JavaUnitDescriptor> fDescriptorMap = new LinkedHashMap<String, JavaUnitDescriptor>();
	
	@Override
	public BlackboxUnitDescriptor getUnitDescriptor(String qualifiedName, ResolutionContext resolutionContext) { 
		try {
			JavaUnitDescriptor d = fDescriptorMap.get(qualifiedName);
			if (d == null) {
				d = new StandaloneDescriptor(qualifiedName);
				fDescriptorMap.put(qualifiedName, d);
			}
			return d;
		} catch(ClassNotFoundException e) {
			return null;
		}
		catch(RuntimeException e) {
			return null;
		}
	}
	
	public void registerDescriptor(final Class<?> cls, String unitQualifiedName, String moduleName, final String[] packageURIs) {
		JavaUnitDescriptor d = fDescriptorMap.get(unitQualifiedName);
		
		if (d == null) {
			d = new JavaUnitDescriptor(unitQualifiedName) {};
			fDescriptorMap.put(unitQualifiedName, d);
		}
		
		try {
			d.addModuleHandle(
				new StandaloneModuleHandle(cls.getName(), moduleName) {
					@Override
					public Class<?> getModuleJavaClass() {
						return cls;
					}
					
					@Override
					public List<String> getUsedPackages() {
						return Arrays.asList(packageURIs);
					}
				}
			);
		} catch (ClassNotFoundException e) {
			QvtPlugin.error(e);
		}
	}

	@Override
	public Collection<JavaUnitDescriptor> getUnitDescriptors(ResolutionContext resolutionContext) {
		return fDescriptorMap.values();
	}

	@Override
	public void cleanup() {
		fDescriptorMap = new LinkedHashMap<String, JavaUnitDescriptor>();
	}
	
	private class StandaloneDescriptor extends JavaUnitDescriptor {		
				
		StandaloneDescriptor(String unitQualifiedName) throws ClassNotFoundException {
			this(unitQualifiedName, Collections.singletonList(getSimpleNameFromJavaClass(unitQualifiedName))); 
		}
		
		StandaloneDescriptor(String unitQualifiedName, List<String> moduleNames) throws ClassNotFoundException {
			super(unitQualifiedName); 
			
			for(String moduleName : moduleNames) {
				addModuleHandle(new StandaloneModuleHandle(unitQualifiedName, moduleName));
			}
		}
						
	}

}
