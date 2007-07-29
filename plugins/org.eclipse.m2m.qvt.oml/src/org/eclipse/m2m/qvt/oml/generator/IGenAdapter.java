/*******************************************************************************
 * Copyright (c) 2007 Borland Software Corporation
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.qvt.oml.generator;

import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface IGenAdapter extends IExecutableExtension {

	boolean isApplicable(EStructuralFeature eStructuralFeature);
	
	String getFeatureGenName(EStructuralFeature eStructuralFeature, String genName);
	
}
