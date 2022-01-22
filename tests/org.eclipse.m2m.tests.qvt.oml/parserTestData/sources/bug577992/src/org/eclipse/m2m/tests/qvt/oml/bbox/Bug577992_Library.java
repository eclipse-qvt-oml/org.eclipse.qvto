/*******************************************************************************
 * Copyright (c) 2022 Christopher Gerking and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.bbox;

import org.eclipse.m2m.qvt.oml.blackbox.java.Module;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation;
import org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass;

@Module(packageURIs={"http://bug577992"})
public class Bug577992_Library {

	public Bug577992_Library() {
		 super();
	}
	
	@Operation(contextual = true)
	public TestClass copyName(TestClass self, TestClass testClass) {
		try {
			self.op(testClass);
		} catch(UnsupportedOperationException e) {
			//expected
		};
		
		self.setAttr(testClass.getAttr());
		
		self.eClass();
		
		return testClass;
	}	
}
