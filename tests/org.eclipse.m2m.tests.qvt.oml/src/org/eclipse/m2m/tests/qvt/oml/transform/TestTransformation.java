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
package org.eclipse.m2m.tests.qvt.oml.transform;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.common.MdaException;
import org.eclipse.m2m.internal.qvt.oml.common.io.CFile;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.internal.qvt.oml.common.io.eclipse.EclipseFile;
import org.eclipse.m2m.internal.qvt.oml.project.QVTOProjectPlugin;
import org.eclipse.m2m.internal.qvt.oml.project.builder.QVTOBuilder;
import org.eclipse.m2m.internal.qvt.oml.project.builder.QVTOBuilderConfig;
import org.eclipse.m2m.internal.qvt.oml.project.nature.NatureUtils;
import org.eclipse.m2m.internal.qvt.oml.runtime.launch.QvtLaunchUtil;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.QvtTransformation;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.QvtTransformation.TransformationParameter;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.QvtTransformation.TransformationParameter.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.TransformationUtil;
import org.eclipse.m2m.qvt.oml.ExecutionContext;
import org.eclipse.m2m.tests.qvt.oml.TestProject;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.After;
import org.junit.Before;

import junit.framework.TestCase;


public abstract class TestTransformation extends TestCase {
    private ModelTestData myData;
    private TestProject myProject;
    private File myDestFolder;

    public TestTransformation(ModelTestData data) {
        super(data.getName());
        
        myData = data;
        myProject = null;
    }
    
    public IProject getProject() {
        return myProject.getProject();
    }
    
    public TestProject getTestProject() {
        return myProject;
    }
    
    public ModelTestData getData() {
    	return myData;
    }
    
    protected String getProjectName() {
    	return "TransformationTest"; //$NON-NLS-1$
    }
    
    protected boolean isSaveXmi() {
    	return myData.isUseCompiledXmi();
    }
    
    @Override
    @Before
	public void setUp() throws Exception {
        TestUtil.turnOffAutoBuilding();     
        
        String name = getProjectName();
        myProject = TestProject.getExistingProject(name);
        if(myProject == null) {
            myProject = new TestProject(name, new String[] {}, 0); 
            myProject.getProject().setDefaultCharset(ModelTestData.ENCODING, null);
            
    		NatureUtils.addNature(getProject(), QVTOProjectPlugin.NATURE_ID);
    		
    		IProjectDescription description = getProject().getDescription();
    		ICommand[] buildSpec = description.getBuildSpec();
    		ICommand buildCommand = NatureUtils.findCommand(buildSpec, QVTOProjectPlugin.BUILDER_ID);
    		
    		assertNotNull(buildCommand);		
    		Map<String, String> arguments = buildCommand.getArguments();
    		arguments.put(QVTOBuilder.SAVE_AST_XMI, Boolean.toString(isSaveXmi()));
    		
    		buildCommand.setArguments(arguments);
    		description.setBuildSpec(buildSpec);
    		getProject().setDescription(description, null);
    		
    		QVTOBuilderConfig.getConfig(getProject()).setSourceContainer(getProject());    		
        }
        
        copyModelData(); 		       
        myData.prepare(myProject);
    }
    
	protected void buildTestProject() throws Exception { 
		getProject().build(IncrementalProjectBuilder.FULL_BUILD, new NullProgressMonitor());
		assertBuildOK(getProject());
    }
	
	protected void assertBuildOK(IProject project) throws Exception {
		IMarker[] problems = project.findMarkers(QVTOProjectPlugin.PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
		StringBuilder buf = new StringBuilder();
		IMarker firstError = null;
		try {			
			for (IMarker next : problems) {
				if(Integer.valueOf(IMarker.SEVERITY_ERROR).equals(next.getAttribute(IMarker.SEVERITY))) {
					firstError = next;
					break;
				}
			}
			
			if(firstError == null) {
				return;
			}
			
			IMarker marker = firstError;
			buf.append(marker.getAttribute(IMarker.MESSAGE));
			buf.append(", line:").append(marker.getAttribute(IMarker.LINE_NUMBER)); //$NON-NLS-1$
			buf.append(", path:").append(marker.getResource().getProjectRelativePath()); //$NON-NLS-1$
						
		} finally {
			try {
				// do clean even if we have failed
				if(firstError != null) {
					tearDown();
					getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}					
		
		fail(buf.toString());
	}
	
	protected void assertEqualContents(String expectedContents, String actualContents) {
		String lineSeparator = System.getProperty("line.separator");
		if (!"\n".equals(lineSeparator)) {
			expectedContents = expectedContents.replace(lineSeparator, "\n");
			actualContents = actualContents.replace(lineSeparator, "\n");
		}
		
		assertEquals(expectedContents, actualContents);
	}
    
    @Override
    @After
	public void tearDown() throws Exception {
    	if (myData == null) {
    		return;
    	}
    	
    	getData().dispose(myProject);
        if (myDestFolder.exists()) {
            FileUtil.delete(myDestFolder);
        }
    	myData = null;
    }
    
    public static interface IChecker {
        void check(ModelTestData data, IProject project) throws Exception;
    }
    
    public static class TransformationChecker implements IChecker {
        public TransformationChecker(ITransformer transformer) {
            myTransformer = transformer;
        }
        
        public void check(ModelTestData data, IProject project) throws Exception {
            IFile transformation = getIFile(data.getTransformation(project));
            
            try {
	            List<URI> transfResult = myTransformer.transform(transformation, data.getIn(project), data.getTrace(project), data.getContext());
	        	List<URI> expectedResultURIs = data.getExpected(project);
	        	
	        	ResourceSet rs = data.getResourceSet(project);
	        	int i = 0;
	        	for (URI actualResultURI : transfResult) {
	        		URI expectedURI = expectedResultURIs.get(i++);
	        		
	        		Resource expectedResource = rs.getResource(expectedURI, true);
	        		
	        		List<EObject> actualExtentObjects = rs.getResource(actualResultURI, true).getContents();
	        		List<EObject> expectedExtentObjects = expectedResource.getContents();
	        		ModelTestData.compareWithExpected(data.getName(), expectedExtentObjects, actualExtentObjects);
				}
            } catch (CoreException e) {
            	assertEquals(data.getExpectedSeverity(), e.getStatus().getSeverity());
                assertEquals(data.getExpectedCode(), e.getStatus().getCode());
            }
        }
        
        private final ITransformer myTransformer;
    };
    
    public static interface ITransformer {
    	List<URI> transform(IFile transformation, List<URI> inUris, URI traceUri, ExecutionContext context) throws Exception;
    }	

    protected void checkTransformation(IChecker checker) throws Exception {
        checker.check(myData, getProject());
        getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
    }
    
    private void copyModelData() throws Exception {
        File srcFolder = TestUtil.getPluginRelativeFile(myData.getBundle(), myData.getTestDataFolder() + "/models/" + myData.getName()); //$NON-NLS-1$
        myDestFolder = new File(getProject().getLocation().toString() + "/models/" + myData.getName()); //$NON-NLS-1$
        myDestFolder.mkdirs();
        FileUtil.copyFolder(srcFolder, myDestFolder);
        getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
    }
    
    public static IFile getIFile(File fileUnderWorkspace) {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IPath location = new Path(fileUnderWorkspace.getAbsolutePath());
        IFile ifile = workspace.getRoot().getFileForLocation(location);
        if(ifile == null) {
            throw new RuntimeException("File not found: " + fileUnderWorkspace); //$NON-NLS-1$
        }
        
        return ifile;
    }
        
    public static CFile getTraceFile(CFile qvtFile) throws MdaException {
        String fileName = qvtFile.getUnitName() + MDAConstants.QVTO_TRACEFILE_EXTENSION_WITH_DOT;
        CFile traceFile = qvtFile.getParent().getFile(fileName);
        return traceFile;
    }
    
    protected static CFile getModelExtentFile(CFile qvtFile, TransformationParameter transfParam) throws MdaException {
    	String fileExtension = transfParam.getMetamodels().isEmpty() ? TransformationUtil.DEFAULT_RESULT_EXTENSION
    			: transfParam.getMetamodels().get(0).getName();
        String fileName = qvtFile.getUnitName() + ".extent_" + transfParam.getName() + '.' + fileExtension; //$NON-NLS-1$
        CFile modelExtentFile = qvtFile.getParent().getFile(fileName);
        return modelExtentFile;
    }
    
	protected static List<URI> launchTransform(IFile transformationFile, List<URI> inUris, URI traceUri, ExecutionContext context,
			QvtTransformation transf) throws Exception {
				
		EclipseFile eclipseFile = new EclipseFile(transformationFile);
    	
    	List<URI> resultUris = new ArrayList<URI>(transf.getParameters().size());
    			
		List<URI> paramUris = new ArrayList<URI>(inUris);
		
    	Iterator<URI> itInUris = inUris.iterator();
		for (TransformationParameter transfParam : transf.getParameters()) {
			URI paramUri = null;
			if (transfParam.getDirectionKind() == DirectionKind.IN || transfParam.getDirectionKind() == DirectionKind.INOUT) {
    			paramUri = itInUris.next();
			}
			if (transfParam.getDirectionKind() == DirectionKind.OUT || transfParam.getDirectionKind() == DirectionKind.INOUT) {
				if (paramUri == null) {
					assertEquals(DirectionKind.OUT, transfParam.getDirectionKind());
			        CFile outFile = getModelExtentFile(eclipseFile, transfParam);	    			        
			        paramUri = URI.createFileURI(outFile.getFullPath());
			        
			        paramUris.add(paramUri);
				}
				
				resultUris.add(paramUri);
			}
		}
		
		QvtLaunchUtil.doLaunch(transf, paramUris, traceUri, context, traceUri != null, traceUri != null);
				
		transf.cleanup();    		
		return resultUris;
	}
}