/*******************************************************************************
 * Copyright (c) 2008, 2022 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 289982, 326871, 427237
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.blackbox;

import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.stdlib.CallHandler;


public abstract class BlackboxUnitDescriptor {
	
	public static final String URI_QVTO_SCHEME = "qvto"; //$NON-NLS-1$
	public static final String URI_BLACKBOX_AUTHORITY = "blackbox"; //$NON-NLS-1$	
	
	private URI fURI;
	private String fQualifiedName;
	private String fDescription;
	private BlackboxProvider fProvider;
	
	/**
	 * @throws IllegalArgumentException
	 */
	protected BlackboxUnitDescriptor(BlackboxProvider provider, String qualifiedName, String... uriSegments) {
		if(provider == null || qualifiedName == null) {
			throw new IllegalArgumentException("null 'qualified name' or 'provider'"); //$NON-NLS-1$
		}
		
		if(uriSegments == null || uriSegments.length == 0) {
			throw new IllegalArgumentException("Invalid unit uri segments"); //$NON-NLS-1$
		}
		
		fProvider = provider;
		fQualifiedName = qualifiedName;
		// TODO - better error handling of invalid segments
		fURI = URI.createHierarchicalURI(URI_QVTO_SCHEME, URI_BLACKBOX_AUTHORITY, null, uriSegments, getUnitQuery(), getFragment());		
	}
	
	protected String getUnitQuery() {
		return null;
	}

	protected String getFragment() {
		return null;
	}

	protected BlackboxUnitDescriptor(BlackboxProvider provider, String qualifiedName) {
		this(provider, qualifiedName, qualifiedName);
	}
		
	protected BlackboxProvider getProvider() {
		return fProvider;
	}
	
	protected void setDescription(String description) {
		fDescription = description;
	}

	public final URI getURI() {
		return fURI;
	}
	
	public URI reconvertURI() {
		return getURI();
	}
	
	public String getDescription() {
		return fDescription; 
	}	
	
	public String getQualifiedName() {
		return fQualifiedName;
	}
	
	@Override
	public String toString() {
		return "Descriptor: " + fQualifiedName + " - " + getProvider().toString(); //$NON-NLS-1$ //$NON-NLS-2$ 
	}
	
	public abstract BlackboxUnit load(LoadContext context) throws BlackboxException;
	
	public abstract Collection<CallHandler> getBlackboxCallHandler(ImperativeOperation operation, QvtOperationalModuleEnv env);
	
	public abstract Collection<CallHandler> getBlackboxCallHandler(OperationalTransformation transformation, QvtOperationalModuleEnv env);

}