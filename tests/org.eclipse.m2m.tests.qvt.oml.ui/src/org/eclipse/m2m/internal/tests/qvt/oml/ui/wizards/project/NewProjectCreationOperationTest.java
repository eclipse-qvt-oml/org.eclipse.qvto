/*******************************************************************************
 * Copyright (c) 2020 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.tests.qvt.oml.ui.wizards.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.m2m.internal.qvt.oml.project.QVTOProjectPlugin;
import org.eclipse.m2m.internal.qvt.oml.project.builder.QVTOBuilderConfig;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.PlatformPluginUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.ui.wizards.project.NewProjectCreationOperation;
import org.eclipse.m2m.internal.qvt.oml.ui.wizards.project.NewProjectData;
import org.eclipse.m2m.internal.tests.qvt.oml.ui.completion.CompletionTest;
import org.eclipse.pde.core.plugin.IExtensions;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginElement;
import org.eclipse.pde.core.plugin.IPluginExtension;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.IPluginObject;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.pde.core.project.IBundleProjectDescription;
import org.eclipse.pde.core.project.IBundleProjectService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

@RunWith(Parameterized.class)
public class NewProjectCreationOperationTest {
	
	@Parameters
	public static Iterable<NewProjectData> data() {
		return Arrays.asList(simpleProjectData, pluginProjectData, javaProjectData);
    }
	
	@Parameter(0)
	public NewProjectData data;
		
	IProject project;
	
	private static NewProjectData simpleProjectData = new NewProjectData();
	private static NewProjectData pluginProjectData = new NewProjectData();
	private static NewProjectData javaProjectData = new NewProjectData();
	
	NewProjectCreationOperation operation;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		simpleProjectData.setName("simpleProject"); //$NON-NLS-1$
		simpleProjectData.setCreateJava(false);
		simpleProjectData.setPlugin(false);
		simpleProjectData.setQVTSourceFolderName("simpleTransforms"); //$NON-NLS-1$
		
		pluginProjectData.setName("pluginProject"); //$NON-NLS-1$
		pluginProjectData.setID(CompletionTest.BUNDLE + ".pluginProject"); //$NON-NLS-1$
		pluginProjectData.setProviderName("Plugin Provider"); //$NON-NLS-1$
		pluginProjectData.setVersion("1.0.0.qualifier"); //$NON-NLS-1$
		pluginProjectData.setCreateJava(false);
		pluginProjectData.setPlugin(true);
		pluginProjectData.setQVTSourceFolderName("pluginTransforms"); //$NON-NLS-1$
		
		IExecutionEnvironment[] executionEnvironments = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
		IExecutionEnvironment environment = executionEnvironments[executionEnvironments.length-1];
		
		javaProjectData.setName("javaProject"); //$NON-NLS-1$
		javaProjectData.setID(CompletionTest.BUNDLE + "javaProject"); //$NON-NLS-1$
		javaProjectData.setProviderName("javaProvider"); //$NON-NLS-1$
		javaProjectData.setVersion("1.0.0.qualifier"); //$NON-NLS-1$
		javaProjectData.setCreateJava(true);
		javaProjectData.setPlugin(true);
		javaProjectData.setQVTSourceFolderName("javaTransforms"); //$NON-NLS-1$
		javaProjectData.setDoGenerateClass(true);
		javaProjectData.setfExecutionEnv(environment.getId());
		javaProjectData.setOutFolderName("bin"); //$NON-NLS-1$
		javaProjectData.setSourceFolderName("src"); //$NON-NLS-1$
		javaProjectData.setClassName("Activator"); //$NON-NLS-1$
	}
		
	@Before
	public void setUp() throws Exception {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(data.getName());
		operation = new NewProjectCreationOperation(project, data);
	}

	@Test
	public void testExecute() throws Exception {
		
		operation.execute(new NullProgressMonitor());
		
		assertTrue(project.exists());
		assertEquals(data.getName(), project.getName());
		assertTrue(project.hasNature(QVTOProjectPlugin.NATURE_ID));
		assertTrue(project.getFolder(data.getQVTSourceFolderName()).exists());
		
		QVTOBuilderConfig config = QVTOBuilderConfig.getConfig(project);
		assertNotNull(config);
		assertTrue(config.getSourceContainer().exists());
		
		if (data.isPlugin()) {
			
			assertTrue(project.hasNature(IBundleProjectDescription.PLUGIN_NATURE));
			
	    	BundleContext context = FrameworkUtil.getBundle(NewProjectCreationOperation.class).getBundleContext();
	    	ServiceReference<IBundleProjectService> serviceReference = context.getServiceReference(IBundleProjectService.class);
	    	IBundleProjectService service = context.getService(serviceReference);
	    	IBundleProjectDescription description = service.getDescription(project);
			
	    	assertEquals(data.getID(), description.getSymbolicName());
			assertEquals(data.getProviderName(), description.getBundleVendor());			
			assertEquals(data.getVersion(), description.getBundleVersion().toString());
				    	
	    	IPluginModelBase workspaceModel = PluginRegistry.findModel(project);
	    	
	    	assertNotNull(workspaceModel);
	    		    		
	    	IExtensions extensions = workspaceModel.getExtensions();
	    	
	    	boolean isSourceContainerExtensionFound = false;
	    	
	    	for (IPluginExtension extension : extensions.getExtensions()) {
	    		
	    		if (PlatformPluginUnitResolver.SOURCE_CONTAINER_POINT.equals(extension.getPoint())) {
	    			
	    			isSourceContainerExtensionFound = true;
	    			
	    			boolean isSourceContainerElementFound = false;
	    			
	    			for (IPluginObject child : extension.getChildren()) {
	    				if (PlatformPluginUnitResolver.SOURCE_CONTAINER.equals(child.getName())) {
	    					
	    					isSourceContainerElementFound = true;
	    					
	    					assertTrue(child instanceof IPluginElement);
	    					IPluginAttribute attribute = ((IPluginElement) child).getAttribute(PlatformPluginUnitResolver.CONTAINER_PATH);
	    					assertNotNull(attribute);
	    					assertEquals(data.getQVTSourceFolderName(), attribute.getValue());
	    				}
	    			}
	    		
	    			assertTrue(isSourceContainerElementFound);
	    		}
	    	}
	    	
	    	assertTrue(isSourceContainerExtensionFound);
		}
			
		if (data.isCreateJava()) {
							
			assertTrue(project.hasNature(JavaCore.NATURE_ID));
					
			assertTrue(project.getFolder(data.getSourceFolderName()).exists());
			assertTrue(project.getFolder(data.getOutFolderName()).exists());
			
			IJavaProject javaProject = JavaCore.create(project);
			
			if (data.isDoGenerateClass()) {
				assertNotNull(javaProject.findType(data.getClassName()));
			}
			
			IPath sourcePath = project.getFolder(data.getSourceFolderName()).getFullPath();
			
			assertNotNull(javaProject.getClasspathEntryFor(sourcePath));
			assertEquals(project.findMember(data.getOutFolderName()).getFullPath(), javaProject.getOutputLocation());
			
						
			boolean isExecutionEnvironmentFound = false;
				
			for (IClasspathEntry entry : javaProject.getRawClasspath()) {
			    String environmentId = JavaRuntime.getExecutionEnvironmentId(entry.getPath());
			    
			    if (environmentId != null && environmentId.equals(data.getfExecutionEnv())) {
			        isExecutionEnvironmentFound = true;
			    }
			}
			
			assertTrue(isExecutionEnvironmentFound);
		}		
	}
	
	@After
	public void tearDown() throws Exception {
		project.delete(true, new NullProgressMonitor());
		
		assertFalse(project.exists());
	}
}
