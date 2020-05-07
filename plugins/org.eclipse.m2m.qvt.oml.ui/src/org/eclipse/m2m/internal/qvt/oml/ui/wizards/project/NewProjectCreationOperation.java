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
 *     Christopher Gerking - bugs 319078, 414662, 536601, 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ui.wizards.project;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.m2m.internal.qvt.oml.project.builder.QVTOBuilderConfig;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class NewProjectCreationOperation extends WorkspaceModifyOperation {
		
	// instance fields
	private NewProjectData fData;
	
	private IProject fProjectHandle; 

	
	public NewProjectCreationOperation(IProject projectHandle, NewProjectData projectData) {
		if(projectHandle == null || projectData == null) {
			throw new IllegalArgumentException();
		}

		fData = projectData;
		fProjectHandle = projectHandle;
	}
	
	protected void createContents(IProgressMonitor monitor, IProject project) throws CoreException, InterruptedException {		
		try {
			SubMonitor subMonitor = SubMonitor.convert(monitor, Messages.NewQVTProjectWizard_Create, 2);
		
	    	IContainer srcContainer = project.getFolder(fData.getQVTSourceFolderName());
	    	if(srcContainer instanceof IFolder) {
	        	SourceContainerUpdater.ensureDestinationExists((IFolder) srcContainer, subMonitor.split(1));
	    	}
	
	    	QVTOBuilderConfig qvtConfig = QVTOBuilderConfig.getConfig(project);
	    	qvtConfig.setSourceContainer(srcContainer);
	    	qvtConfig.addTransformationNature();
	    	
	    	subMonitor.worked(1);
		} finally {
			SubMonitor.done(monitor);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.actions.WorkspaceModifyOperation#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, Messages.NewProjectCreationOperation_createQVTProjectTask, 2);
				
		try {
			createProject(subMonitor.split(1));
			createContents(subMonitor.split(1), fProjectHandle);
		}
		finally {
			SubMonitor.done(monitor);
		}
	}
	
	private void createProject(IProgressMonitor monitor) throws CoreException {
		
		try {
	        SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
			
	        URI location = URIUtil.toURI(fData.getLocation());
	
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			final IProjectDescription description = workspace.newProjectDescription(fProjectHandle.getName());
			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				location = null;
			}
			description.setLocationURI(location); 
							
			if(!fProjectHandle.exists()) {
				fProjectHandle.create(description, subMonitor.split(1));
			}
			subMonitor.setWorkRemaining(2);
	
	        fProjectHandle.open(IResource.BACKGROUND_REFRESH, subMonitor.split(1));
	                			
			ProjectIntegrationHelper.setupProject(fProjectHandle, fData, subMonitor.split(1));
		} finally {
			SubMonitor.done(monitor);
		}
	}
	
}
