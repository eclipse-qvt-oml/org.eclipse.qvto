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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnitDescriptor;
import org.eclipse.m2m.internal.qvt.oml.blackbox.ResolutionContext;
import org.eclipse.m2m.internal.qvt.oml.blackbox.java.JavaBlackboxProvider;
import org.eclipse.m2m.internal.qvt.oml.emf.util.URIUtils;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.ProjectDependencyTracker;

public class JdtBlackboxProvider extends JavaBlackboxProvider {

	public static final String URI_BLACKBOX_JDT_QUERY = "jdt"; //$NON-NLS-1$
	
	private static Map<IProject, Map<String, JdtDescriptor>> descriptors = new HashMap<IProject, Map<String, JdtDescriptor>>();
	
	@Override
	public Collection<? extends BlackboxUnitDescriptor> getUnitDescriptors(ResolutionContext resolutionContext) {
		IProject project = getProject(resolutionContext);
		if (project == null) {
			return Collections.emptyList();			
		}
		
		Set<IProject> referencedProjects = ProjectDependencyTracker.getAllReferencedProjects(project, true);
		
		List<IProject> projects = new ArrayList<IProject>(referencedProjects.size() + 1);
		projects.add(project);
		projects.addAll(referencedProjects);

		List<BlackboxUnitDescriptor> descriptors = new ArrayList<BlackboxUnitDescriptor>();
		for (IProject p : projects) {
			final List<String> classes = getAllClasses(p);
			
			for (String qualifiedName : classes) {
				BlackboxUnitDescriptor jdtUnitDescriptor = getJdtUnitDescriptor(p, qualifiedName);
				if (jdtUnitDescriptor != null) {
					descriptors.add(jdtUnitDescriptor);
				}
			}
		}
		
		return descriptors;
	}

	@Override
	public BlackboxUnitDescriptor getUnitDescriptor(String qualifiedName, ResolutionContext resolutionContext) {
		IProject project = getProject(resolutionContext);
		if (project == null) {
			return null;			
		}
		
		return getJdtUnitDescriptor(project, qualifiedName);
	}

	private BlackboxUnitDescriptor getJdtUnitDescriptor(IProject project, String qualifiedName) {
		
		Map<String, JdtDescriptor> projectDescriptors = descriptors.get(project);
		
		if (projectDescriptors != null) {
			if (projectDescriptors.containsKey(qualifiedName)) {
				return projectDescriptors.get(qualifiedName);
			}
		} else {
			projectDescriptors = new HashMap<String, JdtBlackboxProvider.JdtDescriptor>();
			descriptors.put(project, projectDescriptors);
		}
		
		try {
			if (!project.hasNature(JavaCore.NATURE_ID)) {
				return null;
			}
		} catch (CoreException e) {
			return null;
		}
		
		final IJavaProject javaProject = JavaCore.create(project);
		try {
			ClassLoader loader = ProjectClassLoader.getProjectClassLoader(javaProject);
			
			try {
				Class<?> moduleJavaClass = loader.loadClass(qualifiedName);
						
				JdtDescriptor descriptor = new JdtDescriptor(qualifiedName, moduleJavaClass) {
					@Override
					protected String getFragment() {
						return javaProject.getElementName();
					}
				};
				
				projectDescriptors.put(qualifiedName, descriptor);
				
				return descriptor;
			}
			catch (ClassNotFoundException e) {
				return null;
			}
			catch (NoClassDefFoundError e) {
				return null;
			}

		} catch (JavaModelException e) {
			QvtPlugin.error(e);
		} catch (MalformedURLException e) {
			QvtPlugin.error(e);
		}
		
		return null;
	}

	private IProject getProject(ResolutionContext resolutionContext) {
		IResource resource = URIUtils.getResource(resolutionContext.getURI());

		if (resource == null || !resource.exists()) {
			return null;
		}

		return resource.getProject();
	}
	
	private List<String> getAllClasses(IProject project) {
		final List<String> classes = new ArrayList<String>();

		try {
			IJavaProject javaProject = JavaCore.create(project);
			IResource folder = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getOutputLocation());
			final String folderPath = folder.getFullPath().toString();

			folder.accept(new IResourceProxyVisitor() {

				public boolean visit(IResourceProxy proxy) throws CoreException {
					if (proxy.getType() == IResource.FOLDER) {
						return true;
					}
					if (proxy.getType() == IResource.FILE) {
						if (proxy.getName().endsWith(".class")) {
							String filePath = proxy.requestFullPath().toString();
							filePath = filePath.substring(0, filePath.length() - 6);
							if (filePath.startsWith(folderPath)) {
								filePath = filePath.substring(folderPath.length() + 1);
							}
							classes.add(filePath.replace('/', '.'));
						}
					}
					return false;
				}

			}, IResource.NONE);
		} catch (CoreException e) {
			// ignore
		}

		return classes;
	}
	
	@Override
	public void cleanup() {
		ProjectClassLoader.resetAllProjectClassLoaders();
		descriptors.clear();		
	}
	
	static boolean requiresReset(IJavaProject javaProject) {
		return descriptors.containsKey(javaProject.getProject()) 
				|| ProjectClassLoader.isProjectClassLoaderExisting(javaProject);
	}
	
	static void reset(IJavaProject javaProject) {
		ProjectClassLoader.resetProjectClassLoader(javaProject);
		descriptors.remove(javaProject.getProject());
	}
		
	private class JdtDescriptor extends JavaBlackboxProvider.JavaUnitDescriptor {
		
		private final Class<?> fModuleJavaClass;
		private volatile int hashCode;
		
		public JdtDescriptor(String unitQualifiedName, Class<?> moduleJavaClass) {
			super(unitQualifiedName);
			addModuleHandle(new JdtModuleHandle(unitQualifiedName, moduleJavaClass));
			
			fModuleJavaClass = moduleJavaClass;
		}
		
		@Override
		protected String getUnitQuery() {
			return URI_BLACKBOX_JDT_QUERY;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof JdtDescriptor == false) {
				return false;
			}
			
			JdtDescriptor other = (JdtDescriptor) obj;

			return getQualifiedName().equals(other.getQualifiedName())
					&& fModuleJavaClass.equals(other.fModuleJavaClass);
		}
		
		@Override
		public int hashCode() {
			int result = hashCode;
			if (result == 0) {
				result = 17;
				result = 31 * result + getQualifiedName().hashCode();
				result = 31 * result + fModuleJavaClass.hashCode();
				hashCode = result;
			}
						
			return result;
		}
	}

}
