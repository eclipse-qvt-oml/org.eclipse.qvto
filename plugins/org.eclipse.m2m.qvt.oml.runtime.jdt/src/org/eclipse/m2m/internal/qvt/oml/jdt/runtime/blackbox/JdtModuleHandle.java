/*******************************************************************************
 * Copyright (c) 2016, 2021 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.jdt.runtime.blackbox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.blackbox.java.ModuleHandle;
import org.eclipse.m2m.qvt.oml.blackbox.java.Module;

public class JdtModuleHandle extends ModuleHandle {
	
	private final Class<?> moduleJavaClass;
		
	JdtModuleHandle(String moduleName, Class<?> moduleJavaClass) {
		super(moduleJavaClass.getName(), moduleName);
		this.moduleJavaClass = moduleJavaClass;
	}
	
	@Override
	public Class<?> getModuleJavaClass() {
		return moduleJavaClass;
	}
	
	private static Annotation getModuleAnnotation(Class<?> c) throws ClassNotFoundException {
		Class<?> moduleAnnotationClass = c.getClassLoader().loadClass(Module.class.getCanonicalName());
				
		for(Annotation a : c.getAnnotations()) {
			if (a.annotationType() == moduleAnnotationClass) {
				return a;
			}
		}
		
		return null;
	}
	
	@Override
	public List<String> getUsedPackages() {				
		try {						
			Annotation annotation = getModuleAnnotation(getModuleJavaClass());
			
			if (annotation != null) {
				Method packageURIsMethod = annotation.getClass().getMethod("packageURIs"); //$NON-NLS-1$
				Object packageURIs = packageURIsMethod.invoke(annotation);
				
				if (packageURIs instanceof String[]) {
					return Arrays.asList((String[]) packageURIs);
				}
			}
		}
		catch(Exception e) {
			QvtPlugin.error(e);
		}
		
		return super.getUsedPackages();
	}

}
