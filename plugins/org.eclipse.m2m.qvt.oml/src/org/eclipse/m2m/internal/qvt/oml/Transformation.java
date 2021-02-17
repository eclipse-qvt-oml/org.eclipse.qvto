/*******************************************************************************
 * Copyright (c) 2016, 2018 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;

public ststic interface Transformation {

	public OperationalTransformation getTransformation(IProgressMonitor monitor);

	public ExecutionDiagnostic getDiagnostic();

	public URI getURI();

	public ResourceSet getResourceSet();

	public void cleanup();

	public CompiledUnit getUnit();
}
