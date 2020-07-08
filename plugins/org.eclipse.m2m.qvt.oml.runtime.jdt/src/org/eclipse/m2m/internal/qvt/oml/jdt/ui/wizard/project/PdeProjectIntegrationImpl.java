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
package org.eclipse.m2m.internal.qvt.oml.jdt.ui.wizard.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.PlatformPluginUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.ui.wizards.project.NewProjectData;
import org.eclipse.m2m.internal.qvt.oml.ui.wizards.project.ProjectIntegration;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.build.IBuildEntry;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginReference;
import org.eclipse.pde.core.project.IBundleProjectDescription;
import org.eclipse.pde.core.project.IBundleProjectService;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModel;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModelBase;
import org.eclipse.pde.internal.core.project.PDEProject;
import org.eclipse.pde.internal.core.project.RequiredBundleDescription;
import org.eclipse.pde.internal.core.util.CoreUtility;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginClassCodeGenerator;
import org.eclipse.pde.internal.ui.wizards.plugin.PluginFieldData;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;

@SuppressWarnings("restriction")
public abstract class PdeProjectIntegrationImpl implements ProjectIntegration {
	
	private PluginClassCodeGenerator fGenerator;
	
	private PluginFieldData toFieldData(NewProjectData projectData) {
		PluginFieldData fieldData = new PluginFieldData();
		
		fieldData.setClassname(projectData.getClassName());
		fieldData.setDoGenerateClass(projectData.isDoGenerateClass());
		fieldData.setExecutionEnvironment(projectData.getfExecutionEnv());
		fieldData.setName(projectData.getName());
		fieldData.setOutputFolderName(projectData.getOutFolderName());
		fieldData.setProvider(projectData.getProviderName());
		fieldData.setSourceFolderName(projectData.getSourceFolderName());
		fieldData.setVersion(projectData.getVersion());
		fieldData.setUIPlugin(false);
		
		return fieldData;
	}
	
	public void setupProject(IProject project, final NewProjectData data, IProgressMonitor monitor) throws CoreException {
	
		try {
			if (data.isPlugin()) {
				
				SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
				
				fGenerator = new PluginClassCodeGenerator(project, data.getClassName(), toFieldData(data), false);
				
				// add plugin nature to project
				CoreUtility.addNatureToProject(project, IBundleProjectDescription.PLUGIN_NATURE, subMonitor.split(1));
					    			    	
				// generate the manifest.mf file
				createManifest(project, data, subMonitor.split(1));
				
				// generate the plugin.xml file
				createPluginXml(project, data, subMonitor.split(1));
				
				// generate the build.properties file
				createBuildProperties(project, data, subMonitor.split(1));
		    }
		} finally {
			SubMonitor.done(monitor);
		}

	}
		
	private IPluginReference[] getDependencies() {
		if (fGenerator == null) {
			return new IPluginReference[0];
		}
		return fGenerator.getDependencies();
	}
	
	protected void generateTopLevelPluginClass(IProgressMonitor monitor) throws CoreException {
		fGenerator.generate(monitor);
	}
	
	private void createManifest(IProject project, NewProjectData data, IProgressMonitor monitor) throws CoreException {
		
 		SubMonitor subMonitor = SubMonitor.convert(monitor);
		
		try {		
	    	BundleContext context = FrameworkUtil.getBundle(getClass()).getBundleContext();
	    	ServiceReference<IBundleProjectService> serviceReference = context.getServiceReference(IBundleProjectService.class);
	    	IBundleProjectService service = context.getService(serviceReference);
	    	IBundleProjectDescription description = service.getDescription(project);
	    		    		    	
	    	description.setBundleName(data.getName());
	    	description.setSymbolicName(data.getID());
	    	description.setBundleVersion(new Version(data.getVersion()));
	    	if (data.isDoGenerateClass() && !data.getClassName().isEmpty()) {
				description.setActivator(data.getClassName());
			}
			if (!data.getProviderName().isEmpty()) {
				description.setBundleVendor(data.getProviderName());
			}
			
			IPluginReference[] dependencies = getDependencies();
			if (dependencies.length > 0) {
				List<RequiredBundleDescription> requiredBundles = new ArrayList<RequiredBundleDescription>();
				for (IPluginReference pluginReference : dependencies) {	
					requiredBundles.add(new RequiredBundleDescription(pluginReference.getId(), new VersionRange(pluginReference.getVersion()), false, false));
				}
				description.setRequiredBundles(requiredBundles.toArray(new RequiredBundleDescription[] {}));
			}
	
			if (data.isCreateJava()) {
				String requiredEnv = getRequiredExecutionEnv(data.getfExecutionEnv());
				if (requiredEnv != null) {
					description.setExecutionEnvironments(new String[] {requiredEnv});
				}
			}
			
			description.setActivationPolicy(Constants.ACTIVATION_LAZY);
			
			description.setSingleton(true);
			
	    	description.apply(subMonitor);
	    	context.ungetService(serviceReference);
	    	
		} finally {
			SubMonitor.done(monitor);
		}
	}
	
	private void createBuildProperties(IProject project, NewProjectData data, IProgressMonitor monitor) throws CoreException {
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
		
		try {
			IFile buildProperties = PDEProject.getBuildProperties(project);
			WorkspaceBuildModel buildModel = new WorkspaceBuildModel(buildProperties);
			buildModel.load();
			buildModel.setEditable(true);
	
			if(data.isCreateJava()) {
				IPath sourceFolder = asBinIncludesFolder(getFolder(project, data.getSourceFolderName(), subMonitor.split(1)));
				IPath outFolder = asBinIncludesFolder(getFolder(project, data.getOutFolderName(), subMonitor.split(1)));
	
				IBuildEntry sourceEntry = buildModel.getFactory().createEntry(IBuildEntry.JAR_PREFIX + "."); //$NON-NLS-1$
				sourceEntry.addToken(sourceFolder.toString());
				buildModel.getBuild().add(sourceEntry);
				
				IBuildEntry outputEntry = buildModel.getFactory().createEntry(IBuildEntry.OUTPUT_PREFIX + "."); //$NON-NLS-1$
				outputEntry.addToken(outFolder.toString());
				buildModel.getBuild().add(outputEntry);
			}

			IBuildEntry binIncludesEntry = buildModel.getBuild().getEntry(IBuildEntry.BIN_INCLUDES);
			
			if(data.isCreateJava()) {
				binIncludesEntry.addToken("."); //$NON-NLS-1$
			}
	
			IContainer qvtContainer = getFolder(project, data.getQVTSourceFolderName(), subMonitor.split(1));
			IPath qvtFolder = asBinIncludesFolder(qvtContainer);
			if(!project.equals(qvtContainer) || !data.isCreateJava()) {
				binIncludesEntry.addToken(qvtFolder.toString());
			}
			
			binIncludesEntry.addToken(PDEProject.getPluginXml(project).getProjectRelativePath().toString());
			
			buildModel.save();
		}
		finally {
			SubMonitor.done(monitor);
		}
	}
	
	private void createPluginXml(IProject project, NewProjectData data, IProgressMonitor monitor) throws CoreException {
		
		try {
			// register QVT source container in plugin.xml
			IFile pluginXml = PDEProject.getPluginXml(project);
	    	WorkspacePluginModelBase pluginModel = new WorkspacePluginModel(pluginXml, false);
	    	pluginModel.load();
	    	pluginModel.setEditable(true);
	    	IPluginExtension pluginExtension = pluginModel.createExtension();
	    	IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(PlatformPluginUnitResolver.SOURCE_CONTAINER_POINT);
	    	pluginExtension.setPoint(extensionPoint.getUniqueIdentifier());
	    	IPluginElement element = pluginModel.createElement(pluginExtension);
	    	element.setName(PlatformPluginUnitResolver.SOURCE_CONTAINER);
	    	element.setAttribute(PlatformPluginUnitResolver.CONTAINER_PATH, data.getQVTSourceFolderName());
	    	pluginExtension.add(element);
	    	pluginModel.getExtensions().add(pluginExtension);
	    	pluginModel.save();
		}
		finally {
			SubMonitor.done(monitor);
		}
		
	}
		
	private IPath asBinIncludesFolder(IContainer container) {
		if(container.equals(container.getProject())) {
			return new Path("."); //$NON-NLS-1$
		}
		IPath result = container.getProjectRelativePath();   
		while(result.hasTrailingSeparator()) {
			result = result.removeTrailingSeparator();
		}
		// ensure single trailing slash
		return result.addTrailingSeparator(); 
	}
	
	protected IContainer getFolder(IProject project, String folderName, IProgressMonitor monitor) throws CoreException {
		try {
			if(folderName == null || folderName.trim().length() == 0) {
				return project;			
			}
			
			IFolder folder = project.getFolder(folderName);
			if(!folder.exists()) {
				createFolder(folder, monitor);
			}
			return folder;
		} finally {
			SubMonitor.done(monitor);
		}
	}
	
	protected void createFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
		CoreUtility.createFolder(folder);	
	}

}
