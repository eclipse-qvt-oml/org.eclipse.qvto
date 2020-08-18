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
import java.util.Collections;
import java.util.List;

import org.eclipse.m2m.qvt.oml.blackbox.java.Module;


public abstract class ModuleHandle {
	
	private final String className;
	private final String simpleName;
	
	public ModuleHandle(String className, String moduleName) {
		if(className == null || moduleName == null) {
			throw new IllegalArgumentException();
		}
		
		this.className = className;			
		this.simpleName = moduleName;
	}
	
	
	public String getModuleName() {
		return simpleName;
	}
	
	public String getJavaClassName() {
		return className;
	}
	
	public List<String> getUsedPackages() {
		try {
			Module annotation = getModuleJavaClass().getAnnotation(Module.class);
			return annotation == null ? Collections.<String>emptyList() : Arrays.asList(annotation.packageURIs());
		}
		catch(ClassNotFoundException e) {
			return Collections.emptyList();
		}
	}
		
	public Class<?> getModuleJavaClass() throws ClassNotFoundException {
		return Class.forName(className);
	}
	
	@Override
	public String toString() {			
		return simpleName + ", javaClass: " + className; //$NON-NLS-1$   
	}		
}