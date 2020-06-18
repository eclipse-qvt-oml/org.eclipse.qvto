/*******************************************************************************
 * Copyright (c) 2016, 2020 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.jdt.runtime.blackbox;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.ProjectDependencyTracker;

public class ProjectClassLoader extends URLClassLoader {

	private static Map<IJavaProject, ProjectClassLoader> loadersMap = new HashMap<IJavaProject, ProjectClassLoader>();

	ProjectClassLoader(IProject project) throws JavaModelException, MalformedURLException {
		this(JavaCore.create(project));
	}

	ProjectClassLoader(IJavaProject javaProject) throws JavaModelException, MalformedURLException {
		super(new URL[] {getProjectOutputURL(javaProject)}, getParentLoader(javaProject));

		loadersMap.put(javaProject, this);
	}

	static synchronized boolean isProjectClassLoaderExisting(IJavaProject javaProject) {
		return loadersMap.containsKey(javaProject);
	}

	static synchronized ProjectClassLoader getProjectClassLoader(IProject project) throws JavaModelException, MalformedURLException {
		return getProjectClassLoader(JavaCore.create(project));
	}

	static synchronized ProjectClassLoader getProjectClassLoader(IJavaProject javaProject) throws JavaModelException, MalformedURLException {

		ProjectClassLoader loader = loadersMap.get(javaProject);

		if (loader == null) {
			loader = new ProjectClassLoader(javaProject);
		}

		return loader;
	}

	static synchronized void resetProjectClassLoader(IJavaProject javaProject) {

		ProjectClassLoader loader = loadersMap.get(javaProject);

		if (loader != null) {
			try {			// FIXME Bug 474603#22
				Method closeMethod = loader.getClass().getMethod("close");
				closeMethod.invoke(loader);
			}
			catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				if (targetException instanceof IOException) {
					QvtPlugin.error(e);
				}
			}
			catch (Exception e) {}
			loadersMap.remove(javaProject);
		}
	}
	
	static synchronized void resetAllProjectClassLoaders() {

		for(IJavaProject javaProject : loadersMap.keySet()) {
			resetProjectClassLoader(javaProject);
		}
	}

	private static URL getProjectOutputURL(IJavaProject javaProject) throws JavaModelException, MalformedURLException {

		IPath projectRelativeOutputPath = javaProject.getOutputLocation().removeFirstSegments(1);
		IPath outputPath = javaProject.getProject().getLocation().append(projectRelativeOutputPath);

		return outputPath.addTrailingSeparator().toFile().toURI().toURL();
	}

	private static List<ClassLoader> getReferencedProjectLoaders(IJavaProject javaProject) {

		Set<IProject> referencedProjects = ProjectDependencyTracker.getAllReferencedProjects(javaProject.getProject(), false);

		List<ClassLoader> referencedLoaders = new ArrayList<ClassLoader>(referencedProjects.size());

		for(IProject referencedProject : referencedProjects) {

			try {
				referencedLoaders.add(ProjectClassLoader.getProjectClassLoader(referencedProject));
			}
			catch(JavaModelException e) {
				QvtPlugin.error(e);
			}
			catch(MalformedURLException e) {
				QvtPlugin.error(e);
			}
		}

		return referencedLoaders;
	}

	private static ClassLoader getParentLoader(IJavaProject javaProject) {

		List<ClassLoader> referencedLoaders = getReferencedProjectLoaders(javaProject);

		return referencedLoaders.isEmpty() ? ProjectClassLoader.class.getClassLoader() : new CompositeClassLoader(referencedLoaders);
	}

	private static class CompositeClassLoader extends ClassLoader {

		private List<? extends ClassLoader> composedLoaders;

		public CompositeClassLoader(List<? extends ClassLoader> composedLoaders) {
			this.composedLoaders = composedLoaders;
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {

			for (ClassLoader composed : composedLoaders) {
				try {
					Class<?> c = composed.loadClass(name);

					return c;
				}
				catch (ClassNotFoundException e) {
					continue;
				}
			}

			throw new ClassNotFoundException(name);
		}
	}
}
