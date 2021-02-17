/*******************************************************************************
 * Copyright (c) 2009, 2020 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bug 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ui.wizards.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Combo;

public interface ProjectIntegration {

	void setupProject(IProject project, NewProjectData data, IProgressMonitor monitor) throws CoreException;		
	
	String getRequiredExecutionEnv(String executionEnv);
	
	String getClassField(String id, String suffix);
	
	IStatus validateJavaTypeName(String name);
	
	void fillExecutionEnvironments(Combo combo);
	
}
