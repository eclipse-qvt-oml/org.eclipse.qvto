/*******************************************************************************
 * Copyright (c) 2016, 2022 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.jdt.runtime.blackbox;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry2;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

public class ProjectClassLoader extends URLClassLoader {

	private static Map<IJavaProject, ProjectClassLoader> loadersMap = new HashMap<IJavaProject, ProjectClassLoader>();

	ProjectClassLoader(IProject project) throws CoreException, MalformedURLException {
		this(JavaCore.create(project));
	}

	ProjectClassLoader(IJavaProject javaProject) throws CoreException, MalformedURLException {
		this(javaProject, new WorkspaceDependencyAnalyzer());
	}
	
	private ProjectClassLoader(IJavaProject javaProject, WorkspaceDependencyAnalyzer analyzer) throws CoreException, MalformedURLException {
		super(analyzer.getProjectClassPath(javaProject), analyzer.getParentClassLoader(javaProject));
				
		loadersMap.put(javaProject, this);
	}
	
	static synchronized boolean isProjectClassLoaderExisting(IJavaProject javaProject) {
		return loadersMap.containsKey(javaProject);
	}

	static synchronized ProjectClassLoader getProjectClassLoader(IProject project) throws CoreException, MalformedURLException {
		return getProjectClassLoader(JavaCore.create(project));
	}

	static synchronized ProjectClassLoader getProjectClassLoader(IJavaProject javaProject) throws CoreException, MalformedURLException {

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
		
		Iterable<IJavaProject> javaProjects = new ArrayList<IJavaProject>(loadersMap.keySet());
		
		for(IJavaProject javaProject : javaProjects) {
			resetProjectClassLoader(javaProject);
		}
	}
	
	@SuppressWarnings("restriction")
	private static List<String> getRequiredPluginLocations(IRuntimeClasspathEntry classPathEntry) throws CoreException {
		
		List<String> requiredPluginLocations = Collections.emptyList();
		
		IPath path = classPathEntry.getPath();
				
		if (path != null && path.equals(org.eclipse.pde.internal.core.PDECore.REQUIRED_PLUGINS_CONTAINER_PATH)) {
			IRuntimeClasspathEntry[] resolvedEntries = JavaRuntime.resolveRuntimeClasspathEntry(classPathEntry, classPathEntry.getJavaProject());
			
			requiredPluginLocations = new ArrayList<String>(resolvedEntries.length);
			
			for (IRuntimeClasspathEntry resolvedEntry : resolvedEntries) {
				String location = resolvedEntry.getLocation();
				requiredPluginLocations.add(location);
			}
		}
		else if (classPathEntry instanceof IRuntimeClasspathEntry2) {
			IRuntimeClasspathEntry2 compositeEntry = (IRuntimeClasspathEntry2) classPathEntry;
			IRuntimeClasspathEntry[] nestedEntries = compositeEntry.getRuntimeClasspathEntries(false);
			
			requiredPluginLocations = new ArrayList<String>();
			
			for (IRuntimeClasspathEntry nestedEntry : nestedEntries) {
				requiredPluginLocations.addAll(getRequiredPluginLocations(nestedEntry));
			}
		}
		
		return requiredPluginLocations;
	}
		
	private static List<String> getRequiredPluginLocations(IJavaProject javaProject) throws CoreException {
				
		List<String> requiredPluginLocations = new ArrayList<String>();
		
		IRuntimeClasspathEntry[] entries = JavaRuntime.computeUnresolvedRuntimeClasspath(javaProject);
		
		for (IRuntimeClasspathEntry entry : entries) {
			requiredPluginLocations.addAll(getRequiredPluginLocations(entry));			
		}
		
		return requiredPluginLocations;
	}
	
	private static IProject getProject(URI uri) throws CoreException {		
		IContainer[] containers = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocationURI(uri);
							
		for (IContainer container : containers) {
			IProject project = container.getProject();
			
			if (project != null && project.isOpen()) {
				if (project.hasNature(JavaCore.NATURE_ID)) {
					return project;
				};
			}
		}
		
		return null;
	}
		
	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		
		synchronized (getClassLoadingLock(name)) {
			Class<?> result = findLoadedClass(name);
			
			if (result == null) {		
				try {
					result = findClass(name);
				}
				catch(ClassNotFoundException e) {
					result = getParent().loadClass(name);
				}
				catch(Throwable t) {
					throw new ClassNotFoundException(name, t);
				}
			}
			
	        if (resolve) {
	            resolveClass(result);
	        }
	        
	        return result;
		}
	}
				
	private static class WorkspaceDependencyAnalyzer {
		
		private final Map<String, Boolean> dependencyCache = new HashMap<String, Boolean>();
		private final IPluginModelBase[] pluginModels = PluginRegistry.getAllModels();
		
		private boolean hasWorkspaceDependency(String pluginLocation) throws CoreException {
			IPluginModelBase pluginModel = getPluginModel(pluginLocation);
			return pluginModel != null ? hasWorkspaceDependency(pluginModel) : false;
		}
		
		private boolean hasWorkspaceDependency(IPluginModelBase pluginModel) throws CoreException {
			String pluginLocation = pluginModel.getInstallLocation();
			
			if (dependencyCache.containsKey(pluginLocation)) {
				return dependencyCache.get(pluginLocation);
			}
			
			boolean result = false;
			dependencyCache.put(pluginLocation, result);
			
			URI uri = URIUtil.toURI(pluginLocation);
			IProject pluginProject = getProject(uri);
			
			if (pluginProject != null) {
				result = true;
			}
			else {
				IPluginImport[] imports = pluginModel.getPluginBase().getImports();
				
				for(IPluginImport i : imports) {
					IPluginModelBase importedPlugin = PluginRegistry.findModel(i.getId());
					
					if (importedPlugin != null) {							
						if (hasWorkspaceDependency(importedPlugin)) {
							result = true;
							break;
						}
					}
				}			
			}
			
			dependencyCache.put(pluginLocation, result);
			return result;
		}
		
		private IPluginModelBase getPluginModel(String location) {
			IPath locationPath = new Path(location).removeTrailingSeparator();
			
			Map<IPluginModelBase, IPath> pluginLocations = new HashMap<IPluginModelBase, IPath>(1);
					
			for (IPluginModelBase pluginModel : pluginModels) {
				IPath pluginPath = new Path(pluginModel.getInstallLocation()).removeTrailingSeparator();
				
				if (pluginPath.isPrefixOf(locationPath)) {
					pluginLocations.put(pluginModel, pluginPath);
				}
			}
					
			do {		
				for (IPluginModelBase pluginModel : pluginLocations.keySet()) {
					IPath pluginPath = pluginLocations.get(pluginModel);
									
					if (pluginPath.equals(locationPath)) {	
						return pluginModel;
					}
				}
			
				locationPath = locationPath.removeLastSegments(1);
			} while (!locationPath.isEmpty());
			
			return null;
		}
		
		URL[] getProjectClassPath(IJavaProject javaProject) throws CoreException, MalformedURLException {
			List<String> classPathEntries = new ArrayList<String>(Arrays.asList(JavaRuntime.computeDefaultRuntimeClassPath(javaProject)));
			
			List<String> requiredPluginLocations = getRequiredPluginLocations(javaProject);
			
			for(String pluginLocation : requiredPluginLocations) {
				if (!hasWorkspaceDependency(pluginLocation)) {
					classPathEntries.remove(pluginLocation);
				}
			}
						
			List<URL> urlList = new ArrayList<URL>(classPathEntries.size());
			
			for (String entry : classPathEntries) {
				 URL url = new File(entry).toURI().toURL();
				 urlList.add(url);
			}
			
			return urlList.toArray(new URL[] {});
		}
		
		ClassLoader getParentClassLoader(IJavaProject javaProject) {
			
			ClassLoader root = ProjectClassLoader.class.getClassLoader();
			
			IPluginModelBase pluginModel = PluginRegistry.findModel(javaProject.getProject());
			
			if (pluginModel == null) {
				return root;
			}
			
			IPluginImport[] imports = pluginModel.getPluginBase().getImports();
			final List<IPluginModelBase> importedPlugins = new ArrayList<IPluginModelBase>(imports.length);
			
			for(IPluginImport i : imports) {
				IPluginModelBase importedPlugin = PluginRegistry.findModel(i.getId());
				
				if (importedPlugin != null) {
					importedPlugins.add(importedPlugin);
				}
			}
			
			if (importedPlugins.isEmpty()) {
				return root;
			}
			
			return new ClassLoader() {
				
				private Map<String, Class<?>> loadedClasses = new HashMap<String, Class<?>>();
				
				private Map<String, Set<String>> package2plugins = new HashMap<String, Set<String>>();
				
				private Set<String> getCandidateHostPlugins(String className) {
					
					String packageName = getPrefix(className);
					
					if (package2plugins.containsKey(packageName)) {
						return package2plugins.get(packageName);
					};
					
					while (packageName != null) {
						IPluginModelBase pluginModel = PluginRegistry.findModel(packageName);
						
						if (pluginModel != null && importedPlugins.contains(pluginModel)) {
							return Collections.singleton(pluginModel.getPluginBase().getId());
						}
						
						packageName = getPrefix(packageName);
					};
					
					return Collections.emptySet();
				}
								
				private Class<?> loadClassFromPlugin(String name, boolean resolve, String pluginId) throws ClassNotFoundException {
					Class<?> result = CommonPlugin.loadClass(pluginId, name);
					
			        if (resolve) {
			            resolveClass(result);
			        }
					
			        register(result, pluginId);
					
					return result;
				}
				
				private void register(Class<?> c, String pluginId) {
					loadedClasses.put(c.getName(), c);
			        
					Package p = c.getPackage();
					
					if (p != null) {
						String packageName = p.getName();
				        Set<String> plugins = package2plugins.get(packageName);
				        
				        if (plugins == null) {
				        	plugins = new LinkedHashSet<String>(1);
				        	package2plugins.put(packageName, plugins);
				        }
				        
				        if(!plugins.contains(pluginId)) {
				        	plugins.add(pluginId);
				        }
					}
				}
								
				@Override
				protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
													
					if (loadedClasses.containsKey(name)) {
						Class<?> result = loadedClasses.get(name);
						
						if (result == null) {
							throw new ClassNotFoundException(name);
						}
						else {
							return result;
						}
					}
					
					Set<String> candidateHosts = getCandidateHostPlugins(name);
										
					for(String host : candidateHosts) {
						try {							
							return loadClassFromPlugin(name, resolve, host);
						}
						catch(ClassNotFoundException e) {
							continue;
						}
					}
				
					for(IPluginModelBase importedPlugin : importedPlugins) {
																	
						try {
							String pluginId = importedPlugin.getPluginBase().getId();
							
							if(!candidateHosts.contains(pluginId)) {
								return loadClassFromPlugin(name, resolve, pluginId);
							}
						}
						catch (ClassNotFoundException e) {
							continue;
						}
						
					}
					
					loadedClasses.put(name, null);
					throw new ClassNotFoundException(name);
				}
			};
		}
		
		private static String getPrefix(String fullyQualifiedName) {
			int lastIndex = fullyQualifiedName.lastIndexOf('.');
			
			if (lastIndex == -1) {
				return null;
			}
			else {
				return fullyQualifiedName.substring(0, lastIndex);
			}
		}
	}
}
