/*******************************************************************************
 * Copyright (c) 2008, 2021 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - Bug394188
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.callapi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.TransformationRunner;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.common.MdaException;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.internal.qvt.oml.compiler.ExeXMISerializer;
import org.eclipse.m2m.internal.qvt.oml.runtime.launch.QvtLaunchUtil;
import org.eclipse.m2m.tests.qvt.oml.AllTests;
import org.eclipse.m2m.tests.qvt.oml.TestProject;
import org.eclipse.m2m.tests.qvt.oml.transform.ModelTestData;
import org.eclipse.m2m.tests.qvt.oml.transform.TransformTests;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import junit.framework.TestCase;

/**
 * @author sboyko
 */
@RunWith(Parameterized.class)
public class TestExecutableTransfCopy extends TestCase {
	
	private static final String TEST_DIR_NAME = "transfExecCopy"; //$NON-NLS-1$
	
	// TODO - the following tests redefine virtual operations which are called from imported modules
	private static final Set<String> BANNED_TASKS = new HashSet<String>(Arrays.asList(
			"importedvirtuals", //$NON-NLS-1$
			"virt" //$NON-NLS-1$
			));

    @Parameters(name="{0}")
	public static Iterable<ModelTestData> data() {
    	ModelTestData[] allTestData = TransformTests.createTestData();
    	List<ModelTestData> testData = new ArrayList<ModelTestData>(allTestData.length);
    	
    	for (ModelTestData testTask : allTestData) {
    		if (!BANNED_TASKS.contains(testTask.getName())) {
    			testData.add(testTask);
    		}
    	}
    	return testData;
    }
    
    public TestExecutableTransfCopy(ModelTestData data) {
        super(data.getName());
        myData = data;
    }
    
    @Override
    @Test
    public void runTest() throws Throwable {
    	// 1. step - copy original .qvto into executable .qvtox
    	//
    	URI transformationURI = URI.createFileURI(getDestFile("QVToCopy.qvto").getAbsolutePath());

    	List<URI> transfParamURIs = new ArrayList<URI>(2);
    	transfParamURIs.add(new TransformationExecutorTest.UriCreator(myData.getName()) {
    		@Override
    		public String getTestDataFolder() {
    			return myData.getTestDataFolder();
    		};
    	}.getTransformationUri());
    	final URI execTransfURI = URI.createFileURI(getDestFile(myData.getName() + '.' + ExeXMISerializer.COMPILED_XMI_FILE_EXTENSION).getAbsolutePath());
    	transfParamURIs.add(execTransfURI);
    	
    	TransformationRunner runner = new TransformationRunner(transformationURI, new TestQvtExecutor(myData).getMetamodelResolutionRS().getPackageRegistry(), transfParamURIs);
    	
    	Diagnostic diag = runner.execute(QvtLaunchUtil.createContext(Collections.<String, Object>emptyMap()));
    	    	
    	IStatus status = BasicDiagnostic.toIStatus(diag);
    	if (!status.isOK()) {
    		throw new MdaException(status); 		
    	}
    	
    	// 2. step - execute just created .qvtox
    	//
    	TestQvtExecutor testQvtExecutor = new TestQvtExecutor(myData) {
    		{
    			uriCreator = new UriCreator(myData.getName()) {
    				
    				URI getTransformationUri() {
    					return execTransfURI;
    				}
    				
    				URI getTraceUri() {
    					return super.getTransformationUri().trimFileExtension().appendFileExtension(MDAConstants.QVTO_TRACEFILE_EXTENSION);
    				}
    			};
    		}
    	};
    	testQvtExecutor.setUp();
    	testQvtExecutor.runTest();
    	testQvtExecutor.tearDown();
    }
    
    @Override
    @Before
	public void setUp() throws Exception {
        TestUtil.turnOffAutoBuilding();     
        
        myProject = TestProject.getExistingProject(TEST_DIR_NAME);
        if(myProject != null) {
        	return;
        }
        
        myProject = new TestProject(TEST_DIR_NAME, new String[] {}, 0); 
        myProject.getProject().setDefaultCharset(ModelTestData.ENCODING, null);
        
        copyModelData(); 
    }
    
    @Override
    @After
	public void tearDown() throws Exception {
    	FileUtil.delete(getDestFile(myData.getName() + '.' + ExeXMISerializer.COMPILED_XMI_FILE_EXTENSION));
    }
    
    private IProject getProject() {
        return myProject.getProject();
    }
    
    private void copyModelData() throws Exception {
    	File srcFolder = TestUtil.getPluginRelativeFile(AllTests.BUNDLE_ID, ModelTestData.TEST_DATA_FOLDER 
    			+ IPath.SEPARATOR + ModelTestData.MODEL_FOLDER + IPath.SEPARATOR + TEST_DIR_NAME); 
        File destFolder = getDestFolder();
        destFolder.mkdirs();
        FileUtil.copyFolder(srcFolder, destFolder);
        getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
    }

    private File getDestFolder() {
        return new File(getProject().getLocation().toString() + IPath.SEPARATOR + ModelTestData.MODEL_FOLDER);
    }
    
    private File getDestFile(final String expectedName) {
        return new File(getDestFolder(), expectedName);
    }


    private final ModelTestData myData;
    private TestProject myProject;
    
}
