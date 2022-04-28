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
 *     Christopher Gerking - Bug394188
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.transform;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MModelURIMapFactory;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MappingContainer;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.MetamodelURIMappingHelper;
import org.eclipse.m2m.internal.qvt.oml.emf.util.urimap.URIMapping;
import org.eclipse.m2m.internal.qvt.oml.runtime.launch.QvtLaunchUtil;
import org.eclipse.m2m.qvt.oml.ExecutionContext;
import org.eclipse.m2m.qvt.oml.TransformationExecutor.BlackboxRegistry;
import org.eclipse.m2m.tests.qvt.oml.AllTests;
import org.eclipse.m2m.tests.qvt.oml.TestProject;
import org.eclipse.m2m.tests.qvt.oml.api.framework.comparator.TreeComparator;
import org.eclipse.m2m.tests.qvt.oml.api.framework.comparator.edit.TreeEdit;
import org.eclipse.m2m.tests.qvt.oml.api.framework.comparator.emf.EmfObjectComparatorTreeNode;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public abstract class ModelTestData {
	
	public static final String MODEL_FOLDER = "models"; //$NON-NLS-1$
	public static final String TEST_DATA_FOLDER = "parserTestData"; //$NON-NLS-1$
	
    public ModelTestData(String name, ExecutionContext context) {
        myName = name;
        myContext = context;
    }
    
    public ModelTestData includeMetamodelFile(String relativePath) {
    	URI relativeURI = URI.createURI(relativePath);
    	ecoreFileMetamodels.add(relativeURI);
    	return this;
    }
    
    public String getName() {
        return myName;
    }
    
    @Override
    public String toString() {
    	return getName();
    }
    
    public static void compareWithExpected(String name, List<EObject> expected, List<EObject> out) {
    	TestCase.assertEquals("transf output differs in size to expected result", //$NON-NLS-1$ 
    			expected.size(), out.size());
    	for(int i = 0; i < out.size(); i++) {
    		compareWithExpected(name, expected.get(i), out.get(i));
    	}
    }
    
    public static void compareWithExpected(String name, EObject expected, EObject out) {        
        TestCase.assertNotSame("Actual output and expected output must not be the same instances", expected, out); //$NON-NLS-1$
    	TestCase.assertNotSame("Actual output and expected output must be in distinct resources", expected.eResource(), out.eResource()); //$NON-NLS-1$ 
        assertEquals(name + ":", expected, out); //$NON-NLS-1$
    }
    
    public static void assertEquals(String message, EObject left, EObject right) {
        TreeEdit edit = compare(left, right);
        if(edit.getCost() != 0) {
            throw new AssertionFailedError(message + " expected " + left + ", got " + right + " \nedit=" + edit); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }
    
    public static TreeEdit compare(EObject left, EObject right) {
        TreeEdit edit = new TreeComparator().compare(
                new EmfObjectComparatorTreeNode(null, left), 
                new EmfObjectComparatorTreeNode(null, right));
        //System.err.println("MiscUtils.compare(): edit=" + edit);
        return edit;
    }

    public File getTransformation(IProject project) { 
        File destFolder;
        try {
            destFolder = getDestFolder(myName, project);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return getFile(destFolder, myName + MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT); 
    }
    
    public boolean isUseCompiledXmi() {
    	return true;
    }
    
    public ExecutionContext getContext() { 
        return myContext; 
    }
    
    public List<URI> getEcoreMetamodels() {
    	return ecoreFileMetamodels;
    }
    
    public EPackage.Registry getMetamodelResolutionRegistry(IProject project, ResourceSet resSet) {
    	EPackage.Registry packageRegistry = null;
    	  	
    	if(!ecoreFileMetamodels.isEmpty()) {
    		packageRegistry = MetamodelURIMappingHelper.mappingsToEPackageRegistry(project, resSet);
    		TestCase.assertNotNull("EPackage registry for workspace ecore file must be ready", packageRegistry); //$NON-NLS-1$			
    	}
    	    	
    	return packageRegistry;
    }
    
    public void dispose(TestProject project) throws Exception {
//    	Trace trace = getContext().getTrace();
//		trace.getTraceRecords().clear();
//    	trace.getTraceRecordMap().clear();
//    	trace.getSourceToTraceRecordMap().clear();
//    	trace.getTargetToTraceRecordMap().clear();
    	
    	TestUtil.disposeJava(project);
    }
    
    abstract public List<URI> getIn(IProject project); 
    abstract public List<URI> getExpected(IProject project); 
    abstract public URI getTrace(IProject project); 
    
    public void prepare(TestProject project) throws Exception {    	
    	Resource res = MetamodelURIMappingHelper.createMappingResource(project.getProject());
    	MappingContainer container = MetamodelURIMappingHelper.createNewMappings(res);
    	
    	for (URI ecoreFileURI : ecoreFileMetamodels) { 
    		URI absoluteURI = ecoreFileURI;
    		if(ecoreFileURI.isRelative()) {
        		 IPath ecoreFilePath = project.getProject().getFullPath().append(MODEL_FOLDER).append(myName).append(ecoreFileURI.toString());
        		 absoluteURI = URI.createPlatformResourceURI(ecoreFilePath.toString(), true);  
    		}
        	
        	EPackage metamodelPackage = null;
        	Resource ecoreResource = null;
        	try {
        		ecoreResource = new ResourceSetImpl().getResource(absoluteURI, true);
        	} catch (WrappedException e) {
        		TestCase.fail("Failed to load metamodel EPackage. " + e.getMessage()); //$NON-NLS-1$
			}
        	
        	if(!ecoreResource.getContents().isEmpty()) {
        		EObject obj = ecoreResource.getContents().get(0);
        		if(obj instanceof EPackage) {
        			metamodelPackage = (EPackage) obj;
        		}
        	}
        	
        	if(metamodelPackage == null) {
        		TestCase.fail("No metamodel EPackage available in " + absoluteURI); //$NON-NLS-1$
        	}
        	
        	URIMapping mapping = MModelURIMapFactory.eINSTANCE.createURIMapping();        	
        	mapping.setTargetURI(absoluteURI.toString());
        	mapping.setSourceURI(metamodelPackage.getNsURI());
        	
        	container.getMapping().add(mapping);			
		}
    	
		res.save(Collections.emptyMap());
		TestUtil.prepareJava(project, getDestFolder(project.getProject()), ecoreFileMetamodels, getResourceSet(project.getProject()));
		project.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);		
    }
    
    protected File getDestFolder(IProject project) {
        try {
            return getDestFolder(myName, project);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private File getDestFolder(String name, IProject project) throws IOException {
        File srcRootFolder = TestUtil.getPluginRelativeFile(getBundle(), getTestDataFolder() + IPath.SEPARATOR + MODEL_FOLDER); 
        File srcFolder = getFolder(srcRootFolder, name);
        File destFolder = getFolder(new File(project.getLocation().toString() + IPath.SEPARATOR + MODEL_FOLDER + IPath.SEPARATOR), srcFolder.getName());
        return destFolder;
    }
    
    private static File getFolder(File folder, final String expectedName) {
        File dir = new File(folder, expectedName);
        if(!dir.exists() || !dir.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory: " + dir); //$NON-NLS-1$
        }
        
        return dir;
    }
    
    protected static File getFile(File folder, final String expectedName) {
        File file = new File(folder, expectedName);
        if(!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("No such file: " + file); //$NON-NLS-1$
        }
        
        return file;
    }
    
    protected static File createFile(File folder, String fname, String contents) throws IOException {
    	File file = new File(folder, fname);
    	FileUtil.setContents(file, new ByteArrayInputStream(contents.getBytes(ENCODING)));
        return file;
    }
    
    protected static ExecutionContext makeFileContext(String[][] props) {
        Map<String, Object> cfgMap = new HashMap<String, Object>();
        for (int i = 0; i < props.length; i++) {
            cfgMap.put(props[i][0], props[i][1]);
        }
        ExecutionContext context = QvtLaunchUtil.createContext(cfgMap);
        return context;
    }
    
    public String getTestDataFolder() {
    	return TEST_DATA_FOLDER;
    }
    
    public String getBundle() {
    	return AllTests.BUNDLE_ID;
    }
    
    public List<? extends EPackage> getUsedPackages() {
    	return Collections.singletonList(EcorePackage.eINSTANCE);
    }
    
    public void prepare(BlackboxRegistry blackboxRegistry) {
    }
    
    protected ResourceSet getResourceSet(IProject project) {
		final ResourceSet resSet = isUseCompiledXmi() ? CompiledUnit.createResourceSet() : new ResourceSetImpl();
		
		if (project != null) {
			Registry metamodelRegistry = getMetamodelResolutionRegistry(project, resSet);
			resSet.setPackageRegistry(metamodelRegistry);
		}
		
		return resSet;
	}
    
    
    private final String myName;
    private final ExecutionContext myContext;
    protected final List<URI> ecoreFileMetamodels = new ArrayList<URI>();
    
    public static final String ENCODING = "UTF-8"; //$NON-NLS-1$
}
