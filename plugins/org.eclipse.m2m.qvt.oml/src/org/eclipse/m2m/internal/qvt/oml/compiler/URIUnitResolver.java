/*******************************************************************************
 * Copyright (c) 2009, 2021 R.Dvorak and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Radek Dvorak - initial API and implementation
 *     Christopher Gerking - bug 539910
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.compiler;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;


public class URIUnitResolver extends DelegatingUnitResolver {

	private final class Unit extends UnitProxy {

		private Unit(String namespace, String unitName, URI unitURI) {
			super(namespace, unitName, unitURI);
		}
		
		@Override
		public int getContentType() {
			return UnitProxy.TYPE_CST_STREAM;
		}

		@Override
		public UnitContents getContents() throws IOException {
			return new UnitContents.CSTContents() {
				public Reader getContents() throws IOException {
					InputStream is = URIConverter.INSTANCE.createInputStream(getURI());
					return new InputStreamReader(is, "UTF-8"); //$NON-NLS-1$
				}
			};
		}

		@Override
		public UnitResolver getResolver() {
			return URIUnitResolver.this;
		}
	}
	

	private List<URI> fBaseURIs;
	
	public URIUnitResolver(URI baseURI) {
		this(Collections.singletonList(baseURI));
	}
	
	public URIUnitResolver(List<URI> baseURIs) {
		this(baseURIs, true);
	}
			
	public URIUnitResolver(List<URI> baseURIs, boolean classpath) {
		if(baseURIs == null || baseURIs.contains(null)) {
			throw new IllegalArgumentException();
		}
		
		fBaseURIs = new ArrayList<URI>(baseURIs.size());
		
		for (URI uri : baseURIs) {
			URI normalizedURI = uri;
			
			if(normalizedURI.isFile() && normalizedURI.scheme() == null) {
				normalizedURI = URI.createFileURI(new File(normalizedURI.toFileString()).getAbsolutePath());
			}
			
			if(!normalizedURI.hasTrailingPathSeparator()) {
				// Note: URI represents the empty segment as trailing path separator
				normalizedURI = normalizedURI.appendSegment(""); //$NON-NLS-1$
			}
									
			fBaseURIs.add(normalizedURI);
		}
		
		// enable resolution of black-box module dependencies and (if applicable) classpath imports
		UnitResolver parentResolver = classpath ? new CompositeUnitResolver(BlackboxUnitResolver.DEFAULT, ClassPathUnitResolver.INSTANCE) : BlackboxUnitResolver.DEFAULT;
		setParent(parentResolver);
	}
		
	@Override
	protected UnitProxy doResolveUnit(String qualifiedName) {
		for (URI baseURI : fBaseURIs) {
			UnitProxy unit = doResolveUnit(baseURI, qualifiedName);
			if(unit != null) {
				return unit;
			}
		}
		
		return null;
	}

	private UnitProxy doResolveUnit(URI baseURI, String qualifiedName) {
		try {
			String namespace = null;
			String[] nameSegments = ResolverUtils.getNameSegments(qualifiedName);
			if(nameSegments.length > 1) {
				namespace = ResolverUtils.toQualifiedName(nameSegments, 0, nameSegments.length - 2);
			}
	
			String unitFilePath = ResolverUtils.toNamespaceRelativeUnitFilePath(qualifiedName);
			URI unitURI = URI.createURI(unitFilePath).resolve(baseURI);
			if(!URIConverter.INSTANCE.exists(unitURI, Collections.EMPTY_MAP)) {
				return null;
			}
			
			String unitName = nameSegments[nameSegments.length - 1];
			return new Unit(namespace, unitName, unitURI);
		} catch(RuntimeException e) {
			QvtPlugin.getDefault().log(e);
		} 
		
		return null;
	}
}
