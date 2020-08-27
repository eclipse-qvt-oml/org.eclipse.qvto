/*******************************************************************************
 * Copyright (c) 2008, 2018 Borland Software Corporation and others.
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.common.io.FileUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.URIUtils;
import org.eclipse.m2m.qvt.oml.ExecutionContext;
import org.eclipse.m2m.tests.qvt.oml.transform.ModelTestData;
import org.eclipse.m2m.tests.qvt.oml.transform.TestQvtInterpreter;
import org.eclipse.m2m.tests.qvt.oml.transform.TransformTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JavalessQvtTest extends TestQvtInterpreter {
		
	private static final String PREFIX = "javaless_"; //$NON-NLS-1$
	
	private final boolean isPatchOutput;
	 
	public JavalessQvtTest(ModelTestData testData, boolean patchOutput) {
		super(createJavalessData(testData));
		setName(PREFIX + testData.getName());
		isPatchOutput = patchOutput;
	}
	
	@Parameters(name="{0}, {1}")
	public static Iterable<Object[]> javalessData() {
		ModelTestData[] rawData = TransformTests.createTestData();
		List<Object[]> data = new ArrayList<Object[]>(rawData.length);
	
		for (ModelTestData d : rawData) {
	        if(!JAVALESS_EXCLUDES.contains(d.getName()) && JavalessUtil.isValidJavalessData(d)) {
	            data.add(
	            	new Object[] {d, JAVALESS_PATCH_OUTPUT.contains(d.getName())}
	            );
	        };
	    }
		
		return data;
	}
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	@Test
	public void runTest() throws Exception {
		super.runTest();
	}
	
	public static JavalessFilesToFilesData createJavalessData(ModelTestData testData) {
		if(testData instanceof JavalessFilesToFilesData) {
			return (JavalessFilesToFilesData) testData;
		}
		
		if(!JavalessUtil.isValidJavalessData(testData)) {
			throw new IllegalArgumentException("Not a valid javaless test"); //$NON-NLS-1$
		}
		
		return new JavalessFilesToFilesData(testData);
	}
	
	@Override
	protected ITransformer getTransformer() {
		return new DefaultTransformer(getData(), getProject()) {
			@Override
			public List<URI> transform(IFile transformation, List<URI> inUris, URI traceUri, ExecutionContext qvtContext) throws Exception {
				List<URI> outUris = super.transform(transformation, inUris, traceUri, qvtContext);
				processOutputUri(outUris);
				return outUris;
			}
		};
	}

	protected void processOutputUri(List<URI> outUris) throws Exception {
		if (!isPatchOutput) {
			return;
		}
		for (URI uri : outUris) {
			File file = URIUtils.getResource(uri).getLocation().toFile();
			String contents = FileUtil.getStreamContents(new FileInputStream(file), ModelTestData.ENCODING);
			contents = JavalessUtil.patchContents(contents);
			FileUtil.setContents(file, new ByteArrayInputStream(contents.getBytes(ModelTestData.ENCODING)));
		}
	}
	
	private static final Set<String> JAVALESS_EXCLUDES = new HashSet<String>(Arrays.asList(new String[] {
    		// uses getDataTypeInstance() defined on ecore 
            "primtypesecore", //$NON-NLS-1$
            
            // failed to save compiled XMI (ocl problems) 
            "operation_override", "import_access_extends", "import_access_extends_cfgprop", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            // fqn access are not correctly patched 
            "importedInstances", "fqn_noncontextual", "fqnOperationCalls_271789", "fqnMainCalls_271987", "fqnMainCalls_272937", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
            "transf_inheritance", //$NON-NLS-1$
            
            // input models use reference that is not in required format: eSuperTypes = "#//NEW_Class1" instead of "//@eClassifiers.0" 
            "assigntonullowner", //$NON-NLS-1$
            "resolveall", //$NON-NLS-1$
            "bug420970", //$NON-NLS-1$
          	"bug467600", //$NON-NLS-1$
            
            // uses getEClassifier() defined on ecore 
            "bug2787", "bug2839", "bug2437_4", "bug2437_5", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    		
            // invoking ecore-specific blackbox operations, which cause mismatching param types in javaless mode
            "blackboxlib_237781", //$NON-NLS-1$
            "uml2_stereotypeApplication", //$NON-NLS-1$
            "bug289982_importless", //$NON-NLS-1$
          	"bug289982", //$NON-NLS-1$
          	"bug326871", //$NON-NLS-1$
          	"bug326871a", //$NON-NLS-1$
          	"bug466705", //$NON-NLS-1$ 
            "blackboxlib_annotation_java", //$NON-NLS-1$
            "bug486579", //$NON-NLS-1$
            "bug425066", //$NON-NLS-1$
            "bug565747", //$NON-NLS-1$
            
    		// EObjects are created inside blackbox transformation and later merged with javaless objects 
            "bug427237a", //$NON-NLS-1$
            
            // EObjects for incremental update are loaded from original 'in.ecore' so they don't match with those from 'in.ecore.javaless' 
            "bug463572", //$NON-NLS-1$ 
                  	
            // use of Eclipse project references requires patching across multiple projects
          	"bug433937", //$NON-NLS-1$
          	
    }));

    private static final Set<String> JAVALESS_PATCH_OUTPUT = new HashSet<String>(Arrays.asList(new String[] {
    		// EObjects are created inside blackbox transformation thus don't belong to javaless package 
            "bug427237", //$NON-NLS-1$
    }));
}
