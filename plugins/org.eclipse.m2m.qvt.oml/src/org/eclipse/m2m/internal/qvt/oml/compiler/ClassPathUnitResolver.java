/*******************************************************************************
 * Copyright (c) 2009, 2020 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.compiler;

import java.net.URL;

import org.eclipse.emf.common.util.URI;

public class ClassPathUnitResolver implements UnitResolver {
	
	public static ClassPathUnitResolver INSTANCE = new ClassPathUnitResolver();
	
	private ClassPathUnitResolver() {}

	public UnitProxy resolveUnit(String qualifiedName) {
				
		String resourcePath = ResolverUtils.toNamespaceRelativeUnitFilePath(qualifiedName);
		
		URL resourceUrl = ClassLoader.getSystemResource(resourcePath);
		
		if (resourceUrl != null) {
			int numberOfNameSegments = ResolverUtils.getNameSegments(qualifiedName).length;				
			String resourceUri = URI.decode(resourceUrl.toString());
			URI baseUri = URI.createURI(resourceUri).trimSegments(numberOfNameSegments);
			
			return new URIUnitResolver(baseUri).resolveUnit(qualifiedName);
		}
		
		return null;
	}

}
