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
import org.eclipse.core.runtime.Status;
import org.eclipse.m2m.internal.qvt.oml.ui.QVTUIPlugin;
import org.eclipse.swt.widgets.Combo;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ProjectIntegrationHelper {
	
	private static final ProjectIntegration projectIntegration = getProjectIntegration();
	
	private static ProjectIntegration getProjectIntegration() {
		try {
			BundleContext bundleContext = QVTUIPlugin.getDefault().getBundle().getBundleContext();
			@SuppressWarnings("unchecked")
			ServiceReference<ProjectIntegration> serviceReference =
				(ServiceReference<ProjectIntegration>) bundleContext.getServiceReference(ProjectIntegration.class.getName());
			return serviceReference == null ? null : bundleContext.getService(serviceReference);
		}
		catch (Throwable e) {
			QVTUIPlugin.log(e);
		}
		return null;
	}


	static boolean isProjectIntegration() {
		return projectIntegration != null;
	}
	
	static void setupProject(IProject project, NewProjectData data, IProgressMonitor monitor) throws CoreException {
		
		if (projectIntegration != null) {
			projectIntegration.setupProject(project, data, monitor);
		}		
	}

	static String getRequiredExecutionEnv(String executionEnv) {
		if (projectIntegration != null) {
			return projectIntegration.getRequiredExecutionEnv(executionEnv);
		}
		return null;
	}

	static String getClassField(String id, String suffix) {
		if (projectIntegration != null) {
			return projectIntegration.getClassField(id, suffix);
		}
		return ""; //$NON-NLS-1$
	}

	static IStatus validateJavaTypeName(String name) {
		if (projectIntegration != null) {
			return projectIntegration.validateJavaTypeName(name);
		}
		return Status.OK_STATUS;
	}

	static void fillExecutionEnvironments(Combo combo) {
		if (projectIntegration != null) {
			projectIntegration.fillExecutionEnvironments(combo);
		}
	}

}
