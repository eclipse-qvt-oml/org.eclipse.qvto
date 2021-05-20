/*******************************************************************************
 * Copyright (c) 2007, 2018 Borland Software Corporation and others.
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

import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2m.internal.qvt.oml.emf.util.Logger;
import org.eclipse.m2m.internal.qvt.oml.project.QVTOProjectPlugin;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.ProjectDependencyTracker;

/**
 * @deprecated Use {@link org.eclipse.m2m.internal.qvt.oml.runtime.project.ProjectDependencyTracker} instead.
 */
@Deprecated
class ProjectDependencyHelper {
	
	private ProjectDependencyHelper() {
		super();
	}
	
	/**
	 * Gets projects which are dependencies of the given project argument.
	 * <p>
	 * Remark: Only open project are open are traversed further in dependency
	 * lookup and are guaranteed to be transformation projects. Closed project
	 * dependencies are returned but can be of any nature.
	 * 
	 * @param project
	 *            the project to query for its dependencies
	 * @param recursive
	 *            if <code>false</code>, only the direct dependencies are
	 *            collected. Otherwise, the whole dependency graph is traversed
	 * @return arrya of dependency projects
	 */
	public static IProject[] getQvtProjectDependencies(IProject project, boolean recursive) {
		return ProjectDependencyTracker.getAllReferencedProjects(project, recursive).toArray(new IProject[] {});
	}	
	
	public static boolean isPluginProject(IProject project) {
		return (project.isOpen() && (
			project.exists(new Path("META-INF/MANIFEST.MF")) || //$NON-NLS-1$
			project.exists(new Path("plugin.xml")) //$NON-NLS-1$			
			));
	}

	public static  Set<IProject> collectQvtPluginWorkspaceDependencies(IProject dependantProject, Set<IProject> pluginDependencies, boolean recursive) {
	// FIXME - removing pde.core depency, crossproject dependant builds
	// are not fully supported at the moment anyway 
	// https://bugs.eclipse.org/bugs/show_bug.cgi?id=307676
//		IPluginModelBase plugin = findPluginModelByProject(dependantProject);
//		if(plugin != null) {
//			for (IPluginImport nextImport : plugin.getPluginBase().getImports()) {
//				String importID = nextImport.getId();
//				IPluginModelBase depPlugin = findPluginModelByID(importID);
//				if(depPlugin != null && depPlugin.getUnderlyingResource() != null) {
//					IProject projectDep = depPlugin.getUnderlyingResource().getProject();
//
//					if(!pluginDependencies.contains(projectDep) && isQvtProject(projectDep)) {
//						pluginDependencies.add(depPlugin.getUnderlyingResource().getProject());
//						if(recursive) {
//							collectQvtPluginWorkspaceDependencies(dependantProject, pluginDependencies, recursive);
//						}
//					}
//				}
//			}
//		}
		return pluginDependencies;
	}

	public static boolean isQvtProject(IProject project) {
		try {
			return project.isOpen() && project.hasNature(QVTOProjectPlugin.NATURE_ID);
		} catch (CoreException e) {
			Logger.getLogger().log(Level.SEVERE, "Failed to check project nature", e); //$NON-NLS-1$
		}
		return false;
	}

	public static Set<IProject> collectQvtProjectWorkspaceDependencies(IProject project, Set<IProject> qvtDependencies, boolean recursive) {
		if (project == null || !project.isAccessible()) {
			return Collections.emptySet();
		}
		
	    try { 
	    	IProject[] projects = project.getReferencedProjects();
	    	for (int i = 0; i < projects.length; i++) {
				IProject nextProject = projects[i];
				if(!nextProject.isOpen() || nextProject.hasNature(QVTOProjectPlugin.NATURE_ID)) {
					boolean wasProcessed = !qvtDependencies.add(nextProject);
					if(recursive && !wasProcessed) {
						collectQvtProjectWorkspaceDependencies(nextProject, qvtDependencies, recursive);
					}
				}
			}
	    	return qvtDependencies;
		} catch (CoreException e) {
			Logger.getLogger().log(Logger.SEVERE, "Can't get referenced projects", e); //$NON-NLS-1$
		}
	    
	    return Collections.emptySet();
	}
//
//    private static IPluginModelBase findPluginModelByProject(IProject project) {
//		return PluginRegistry.findModel(project);
//	}
//
//	private static IPluginModelBase findPluginModelByID(String importID) {
//		return PluginRegistry.findModel(importID);
//	}

}
