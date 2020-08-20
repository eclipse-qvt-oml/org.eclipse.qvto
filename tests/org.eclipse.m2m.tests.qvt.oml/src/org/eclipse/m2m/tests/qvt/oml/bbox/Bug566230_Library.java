/*******************************************************************************
 * Copyright (c) 2020 Christopher Gerking and others.
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

import java.util.Collections;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation.Kind;

public class Bug566230_Library {
	
	@Operation (kind=Kind.QUERY)
	public Iterable<EPackage> getIterable() {
		return Collections.singleton(EcorePackage.eINSTANCE);
	}
	
}
