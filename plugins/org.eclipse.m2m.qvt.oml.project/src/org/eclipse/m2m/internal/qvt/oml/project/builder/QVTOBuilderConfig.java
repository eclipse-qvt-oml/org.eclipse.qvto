/*******************************************************************************
 * Copyright (c) 2009, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.project.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.emf.util.Logger;
import org.eclipse.m2m.internal.qvt.oml.project.Messages;
import org.eclipse.m2m.internal.qvt.oml.project.QVTOProjectPlugin;
import org.eclipse.m2m.internal.qvt.oml.project.QvtProjectUtil;
import org.eclipse.m2m.internal.qvt.oml.project.nature.NatureUtils;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.ProjectDependencyTracker;
import org.eclipse.osgi.util.NLS;


public class QVTOBuilderConfig {

    private static final String SRC_CONTAINER = "src_container"; //$NON-NLS-1$
    private static final String PROJECT_ROOT = "/"; //$NON-NLS-1$	
	
    private final IProject myProject;    
    private ICommand myCommand;
    private String myBuilderID;
    	
    
	public QVTOBuilderConfig(IProject project, String builderID) throws CoreException {
        myProject = project;
        myBuilderID = builderID;
        myCommand = NatureUtils.findCommand(myProject, myBuilderID);
    }

    public static QVTOBuilderConfig getConfig(IProject project) throws CoreException {
    	QVTOBuilderConfig config = new QVTOBuilderConfig(project, QVTOProjectPlugin.BUILDER_ID);
        return config;
    }

	public boolean isInSourceContainer(IResource resource) {		
		IContainer container = null;
		if(resource.exists()) {
			if(myProject.equals(resource.getProject())) {
				container = getSourceContainer();
			}
		}
		
		if(container != null && container.exists()) {
			return container.getProjectRelativePath().isPrefixOf(resource.getProjectRelativePath());
		}
		return false;
	}
    
    public void save() throws CoreException {
        NatureUtils.updateCommand(myProject, myCommand);
    }
    
    public boolean getGenerateJava() {
    	return false;
    }
    
    public IContainer getDefaultSourceContainer() {
    	return myProject;
    }
    
    public IContainer getSourceContainer() {
        IContainer containerPath = getRawSourceContainer();
        
        if(containerPath == null) {
            return myProject;
        }
        
        return containerPath;
    }
    
    public IContainer getRawSourceContainer() {
        String containerPath = getArgument(SRC_CONTAINER);

        if(containerPath != null) {
            IPath path = new Path(containerPath);
            if(path.isRoot()) {
            	return myProject;
            }
            
        	return myProject.getFolder(path);
        }
        return null;
    }
    
    
    public void setSourceContainer(IContainer container) {
        if(container.getProject() != myProject) {
            throw new IllegalArgumentException(NLS.bind(Messages.InvalidSourceContainer, container.getFullPath()));
        }

    	try { 
    		IProjectDescription pd = myProject.getProject().getDescription();
			NatureUtils.addBuilders(pd, new String[] { myBuilderID }, new String[0]);
			ICommand[] buildSpec = pd.getBuildSpec();
			
	        if(myCommand == null) {				
	        	myCommand = NatureUtils.findCommand(buildSpec, myBuilderID);
	        }		        	
	        if(myCommand != null) {		        	
	        	setBuildCommandArgument(SRC_CONTAINER, getPathString(container));
	        	for (int i = 0; i < buildSpec.length; i++) {
					if(myBuilderID.equals(buildSpec[i].getBuilderName())) {
						buildSpec[i] = myCommand;
					}
				}
	        	pd.setBuildSpec(buildSpec);
	        	
	        }
	        myProject.getProject().setDescription(pd, null);
		} catch (CoreException e) {
			QvtPlugin.getDefault().log(e.getStatus());
		}
    }
    
    public void addTransformationNature() {
        try {
			NatureUtils.addNature(myProject, QVTOProjectPlugin.NATURE_ID);
		} catch (CoreException e) {
			QvtPlugin.getDefault().log(e.getStatus());
		}
    }
    
    public IContainer[] getQvtContainers() {
        List<IContainer> containers = new ArrayList<IContainer>();        
        //containers.add(myProject);
		containers.add(getSourceContainer());
        
        try {
        	Set<IProject> referencedProjects = ProjectDependencyTracker.getAllReferencedProjects(myProject, true); 
			for (IProject nextProject : referencedProjects) {
				if(QvtProjectUtil.isQvtProject(nextProject)) {
					containers.add(getConfig(nextProject).getSourceContainer());
				}
			}
		} catch (CoreException e) {
			Logger.getLogger().log(Logger.SEVERE, "Can't get QVT source containers", e); //$NON-NLS-1$
		}
        
        return containers.toArray(new IContainer[containers.size()]);
    }
    
    public IProject[] getProjectDependencies(boolean recursive) {
    	return ProjectDependencyTracker.getAllReferencedProjects(myProject, recursive).toArray(new IProject[] {});
    }
    
    private String getPathString(IContainer container) {
        String containerPath;
        if(container == myProject) {
            containerPath = PROJECT_ROOT;
        }
        else {
            containerPath = container.getProjectRelativePath().toString();
        }
        
        return containerPath;
    }
    
    
    private String getArgument(String name) {
        if (myCommand == null) {
            return null;
        }
        Map<?,?> arguments = myCommand.getArguments();
        if(arguments == null) {
            return null;
        }
        
        return (String) myCommand.getArguments().get(name);
    }
    
	private void setBuildCommandArgument(String name, String value) {
    	assert myCommand != null;
    	
    	if(myCommand == null) return;
    	
        Map<String, String> arguments = myCommand.getArguments();
        if(arguments == null) {
            arguments = new HashMap<String, String>();
        }
        
        arguments.put(name, value);
        myCommand.setArguments(arguments);
    }        
}
