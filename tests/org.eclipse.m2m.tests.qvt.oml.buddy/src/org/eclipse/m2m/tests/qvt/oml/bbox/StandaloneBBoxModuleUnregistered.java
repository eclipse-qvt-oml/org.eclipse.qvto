/*******************************************************************************
 * Copyright (c) 2014, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.bbox;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.m2m.qvt.oml.blackbox.java.Module;

@Module(packageURIs={"http://www.eclipse.org/emf/2002/Ecore"})
public class StandaloneBBoxModuleUnregistered {

	public StandaloneBBoxModuleUnregistered() {
		 super();
	}
	
	public String echoFromBBoxModuleUnregistered(String str) {
		return str;
	}
	
	public EClass modifyClassNameUnregistered(EClass cls, String name) {
		cls.setName(name);
		return cls;
	}
	
}
