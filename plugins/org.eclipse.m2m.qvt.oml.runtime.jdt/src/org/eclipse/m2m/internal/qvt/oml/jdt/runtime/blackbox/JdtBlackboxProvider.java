/*******************************************************************************
 * Copyright (c) 2016, 2021 Christopher Gerking and others.
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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxException;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnit;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxUnitDescriptor;
import org.eclipse.m2m.internal.qvt.oml.blackbox.LoadContext;
import org.eclipse.m2m.internal.qvt.oml.blackbox.ResolutionContext;
import org.eclipse.m2m.internal.qvt.oml.blackbox.java.JavaBlackboxProvider;
import org.eclipse.m2m.internal.qvt.oml.compiler.BlackboxUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.ResolverUtils;
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
			final List<String> classes = getAllClasses(p, resolutionContext);
			
			for (String qualifiedName : classes) {
				BlackboxUnitDescriptor jdtUnitDescriptor = getJdtUnitDescriptor(project, qualifiedName);
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
					protected String getUnitQuery() {
						return URI_BLACKBOX_JDT_QUERY + "=" + javaProject.getElementName(); //$NON-NLS-1$
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

		} catch (CoreException e) {
			QvtPlugin.error(e);
		} catch (MalformedURLException e) {
			QvtPlugin.error(e);
		}
		
		return null;
	}

	private IProject getProject(ResolutionContext resolutionContext) {
		URI uri = resolutionContext.getURI();
		uri = reconvert(uri);
						
		IResource resource = URIUtils.getResource(uri);

		if (resource == null || !resource.exists()) {
			return null;
		}

		return resource.getProject();
	}
	
	private List<String> getAllClasses(IProject project, ResolutionContext context) {
		final List<String> classes = new ArrayList<String>();

		try {
			IJavaProject javaProject = JavaCore.create(project);
			IResource folder = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getOutputLocation());
			
			if (folder != null) {
				final String folderPath = folder.getFullPath().toString();
	
				folder.accept(new IResourceProxyVisitor() {
	
					public boolean visit(IResourceProxy proxy) throws CoreException {
						if (proxy.getType() == IResource.FOLDER) {
							return true;
						}
						if (proxy.getType() == IResource.FILE) {
							if (proxy.getName().endsWith(".class")) {
								if (!proxy.getName().contains("$")) {
									String filePath = proxy.requestFullPath().toString();
									filePath = filePath.substring(0, filePath.length() - 6);
									if (filePath.startsWith(folderPath)) {
										filePath = filePath.substring(folderPath.length() + 1);
									}
									String fqn = filePath.replace('/', '.');
									if (context.getImports().isEmpty() || context.getImports().contains(fqn)) {
										classes.add(fqn);
									}
								}
							}
						}
						return false;
					}
	
				}, IResource.NONE);
			}
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
	
	/**
	 * @deprecated Call {@link #reset(IJavaProject)} instead.
	 */
	@Deprecated
	public static void clearDescriptors(IProject project) {
		descriptors.remove(project);
	}
	
	static boolean requiresReset(IJavaProject javaProject) {
		return descriptors.containsKey(javaProject.getProject()) 
				|| ProjectClassLoader.isProjectClassLoaderExisting(javaProject);
	}
	
	static void reset(IJavaProject javaProject) {
		ProjectClassLoader.resetProjectClassLoader(javaProject);
		descriptors.remove(javaProject.getProject());
	}
	
	private static URI reconvert(URI uri) {		
		if (BlackboxUnitResolver.isBlackboxUnitURI(uri)) {
			String projectName = ResolverUtils.getQueryValue(uri, URI_BLACKBOX_JDT_QUERY);
			
			if (projectName != null) {
				return URI.createPlatformResourceURI(projectName, true);
			}
		}
		
		return uri;
	}
		
	private class JdtDescriptor extends JavaBlackboxProvider.JavaUnitDescriptor {
		
		private final Class<?> fModuleJavaClass;
		private volatile int hashCode;
		private EPackage.Registry fPackageRegistry;
		
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
				
		@Override
		public synchronized BlackboxUnit load(LoadContext context) throws BlackboxException {
			if (fPackageRegistry != context.getMetamodelRegistry()) {
				unload();
				fPackageRegistry = context.getMetamodelRegistry();
			}
						
			return super.load(context);
		}
		
		@Override
		public URI reconvertURI() {
			return reconvert(getURI());
		}
	}

}
