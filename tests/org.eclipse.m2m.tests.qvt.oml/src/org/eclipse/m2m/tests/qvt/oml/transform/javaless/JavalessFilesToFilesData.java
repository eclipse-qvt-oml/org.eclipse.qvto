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
package org.eclipse.m2m.tests.qvt.oml.transform.javaless;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.tests.qvt.oml.TestProject;
import org.eclipse.m2m.tests.qvt.oml.transform.FilesToFilesData;
import org.eclipse.m2m.tests.qvt.oml.transform.ModelTestData;

public class JavalessFilesToFilesData extends ModelTestData {
	public JavalessFilesToFilesData(ModelTestData data) {
        super(data.getName(), data.getContext());
        if(!JavalessUtil.isValidJavalessData(data)) {
        	throw new IllegalArgumentException("Non-javaless data: " + data); //$NON-NLS-1$
        }
        
        myFileData = (FilesToFilesData) data;
        myPatchedData = null;
        
        ecoreFileMetamodels.addAll(data.getEcoreMetamodels());
        ecoreFileMetamodels.add(URI.createURI(JavalessUtil.JAVALESS_METAMODEL));
    }
    
	@Override
	public void prepare(TestProject testProject) throws Exception {
		super.prepare(testProject);
		getExpected(testProject.getProject());
	}
	
    @Override
	public List<URI> getIn(IProject project) {
    	List<URI> in = new ArrayList<URI>(getPatchedData(project).getFromFiles().size());
    	for (String fromFile : getPatchedData(project).getFromFiles()) {
	    	IPath filePath = project.getProject().getFullPath().append(MODEL_FOLDER).append(getName()).append(fromFile);
	    	URI uri = URI.createPlatformResourceURI(filePath.toString(), true);
	    	in.add(uri);
    	}
    	return in;
    }
    
    @Override
	public List<URI> getExpected(IProject project) {
    	List<URI> expected = new ArrayList<URI>(getPatchedData(project).getExpectedFiles().size());
    	for (String expectedFile : getPatchedData(project).getExpectedFiles()) {
	    	IPath filePath = project.getProject().getFullPath().append(MODEL_FOLDER).append(getName()).append(expectedFile);
	    	URI uri = URI.createPlatformResourceURI(filePath.toString(), true);
	    	expected.add(uri);
    	}
	    return expected;
    }
    
    @Override
    public URI getTrace(IProject project) {
    	IPath filePath = project.getProject().getFullPath().append(MODEL_FOLDER).append(getName()).append(getName()).addFileExtension(MDAConstants.QVTO_TRACEFILE_EXTENSION);
    	return URI.createPlatformResourceURI(filePath.toString(), true);
    }
    
    @Override
	public File getTransformation(IProject project) {
    	File destFolder = getDestFolder(project);
        return getFile(destFolder, getPatchedData(project).getName() + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT); 
    }
    
    private FilesToFilesData getPatchedData(IProject project) {
    	if(myPatchedData == null) {
    		try {
				myPatchedData = createPatchedData(project);
				project.getFolder(new Path(MODEL_FOLDER + IPath.SEPARATOR + myFileData.getName())).refreshLocal(IResource.DEPTH_INFINITE, null);
			} 
    		catch (Exception e) {
				throw new RuntimeException(e);
			}
    	}
    	
    	return myPatchedData;
    }
    
    private FilesToFilesData createPatchedData(IProject project) throws IOException {
		File destFolder = getDestFolder(project);
		
		String patchedName = myFileData.getName() + "_javaless"; //$NON-NLS-1$
		{
			File[] allQvtSources = destFolder.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT);
				}
			});
			
			for (File origTransformationFile : allQvtSources) {
	    		//origTransformationFile = getFile(destFolder, myFileData.getName() + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT);
	    		String contents = FileUtil.getStreamContents(new FileInputStream(origTransformationFile), ENCODING);
	
	    		contents = JavalessUtil.changeTransformationName(contents, myFileData.getName(), patchedName);
	    		contents = JavalessUtil.patchContents(contents);
	    		
	    		String plainName = origTransformationFile.getName();
	    		plainName = plainName.substring(0, plainName.indexOf(MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT));
	    		String newQVTFileName =  plainName + "_javaless" + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT; //$NON-NLS-1$	    		
				createFile(destFolder, newQVTFileName, contents);
			}
		}
		
		List<String> patchedFromFiles = new ArrayList<String>(myFileData.getFromFiles().size());
		for (String fromFile : myFileData.getFromFiles()) {
			String patchedFromFileName = fromFile + ".javaless"; //$NON-NLS-1$
			{
				File fromEcore = getFile(destFolder, fromFile);
	    		String contents = FileUtil.getStreamContents(new FileInputStream(fromEcore), ENCODING);
	    		contents = JavalessUtil.patchContents(contents);
	    		createFile(destFolder, patchedFromFileName, contents);
			}
			patchedFromFiles.add(patchedFromFileName);
		}
		
		List<String> patchedExpectedFiles = new ArrayList<String>(myFileData.getExpectedFiles().size());
		for (String expectedFile : myFileData.getExpectedFiles()) {
			String patchedExpectedFileName = expectedFile + ".javaless"; //$NON-NLS-1$;
			{
				File expectedEcore = getFile(destFolder, expectedFile);
	    		String contents = FileUtil.getStreamContents(new FileInputStream(expectedEcore), ENCODING);
	    		contents = JavalessUtil.patchContents(contents);
	    		createFile(destFolder, patchedExpectedFileName, contents);
			}
			patchedExpectedFiles.add(patchedExpectedFileName);
		}
		
		FilesToFilesData patchedData = new FilesToFilesData(patchedName, patchedFromFiles, patchedExpectedFiles, myFileData.getContext());
		
		for (URI metamodelURI : ecoreFileMetamodels) {
			patchedData.includeMetamodelFile(metamodelURI.toString());
		}
		
		return patchedData;
    }
    
    @Override
    public String getTestDataFolder() {
    	return myFileData.getTestDataFolder();
    }
    
    private final FilesToFilesData myFileData;
    private FilesToFilesData myPatchedData;
}
