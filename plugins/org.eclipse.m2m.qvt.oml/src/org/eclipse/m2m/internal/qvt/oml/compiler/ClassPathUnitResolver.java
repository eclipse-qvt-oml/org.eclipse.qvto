/*******************************************************************************
 * Copyright (c) 2009, 2021 Borland Software Corporation and others.
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
import java.util.Collections;

import org.eclipse.emf.common.util.URI;

public class ClassPathUnitResolver implements UnitResolver {
	
	public static ClassPathUnitResolver INSTANCE = new ClassPathUnitResolver();
	
	private ClassPathUnitResolver() {}

	public UnitProxy resolveUnit(String qualifiedName) {
				
		String resourcePath = ResolverUtils.toNamespaceRelativeUnitFilePath(qualifiedName);
		
		URL resourceUrl = ClassLoader.getSystemResource(resourcePath);
		
		if (resourceUrl != null) {
			int numberOfNameSegments = ResolverUtils.getNameSegments(qualifiedName).length;
			URI baseUri = URI.createURI(resourceUrl.toExternalForm(), true).trimSegments(numberOfNameSegments);
			
			DelegatingUnitResolver delegateResolver = new URIUnitResolver(Collections.singletonList(baseUri), false);
			return delegateResolver.resolveUnit(qualifiedName);
		}
		
		return null;
	}

}
