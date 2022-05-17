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
/*
 * Created on Feb 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.m2m.tests.qvt.oml;

import org.eclipse.m2m.tests.qvt.oml.ant.TestQvtAntScript;
import org.eclipse.m2m.tests.qvt.oml.callapi.AllCallAPITests;
import org.eclipse.m2m.tests.qvt.oml.callapi.DebugExecutorTest;
import org.eclipse.m2m.tests.qvt.oml.callapi.SourceContainerTests;
import org.eclipse.m2m.tests.qvt.oml.callapi.TestExecutableTransfCopy;
import org.eclipse.m2m.tests.qvt.oml.callapi.TestQvtExecutor;
import org.eclipse.m2m.tests.qvt.oml.compile.AllCompileTests;
import org.eclipse.m2m.tests.qvt.oml.emf.TestEmfUtil;
import org.eclipse.m2m.tests.qvt.oml.env.QVTOperationalEnvTest;
import org.eclipse.m2m.tests.qvt.oml.ocl2qvt.OCLEnvWithQVTTests;
import org.eclipse.m2m.tests.qvt.oml.perf.ExecutionTimeGuardTest;
import org.eclipse.m2m.tests.qvt.oml.traces.TestTraceFile;
import org.eclipse.m2m.tests.qvt.oml.transform.TestQvtWalker;
import org.eclipse.m2m.tests.qvt.oml.transform.TransformManyModelTests;
import org.eclipse.m2m.tests.qvt.oml.transform.TransformTests;
import org.eclipse.m2m.tests.qvt.oml.transform.api.QvtoTransfHelperTests;
import org.eclipse.m2m.tests.qvt.oml.transform.javaless.JavalessQvtTest;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;


/**
 * @author vrepeshko
 * 
 */
@RunWith(Suite.class)
@SuiteClasses(
		{
			IntermediatePropertyCleanupTest.class,
			AllCompileTests.class,
			AllCallAPITests.class,
					
			QVTOperationalEnvTest.class,
			
			OCLEnvWithQVTTests.class,
			
			TestExternHelperCall.class,
			TestEmfUtil.class,
			TestMetamodelRegistry.class,
	        TestLaunchConfiguration.class,
	        ParserTests.class,
	
	        TransformTests.class,
	        JavalessQvtTest.class,
	        TestQvtWalker.class,
	        TestQvtExecutor.class,
	        DebugExecutorTest.class,
	        TransformManyModelTests.class,
	        QvtoTransfHelperTests.class,
			TestExecutableTransfCopy.class,
	        
	        TestQvtBuilder.class,        
	        TestSamples.class,
	        TestGraphWalker.class,
	        TestTraceFile.class,
	        	        
	        TestQvtResourceFactory.class,
	        
	        TestOCLSemantics.class,
	        TestOCLAnnotationSupport.class, 
	        
	        TestParsingMACLineBreaks.class,
	                
	        TestQvtAntScript.class,
	        SourceContainerTests.class,
	        
			ExecutionTimeGuardTest.class,
			
			UnparserTests.class
		}
)
public class AllTests extends TestCase {

	public static final String BUNDLE_ID = "org.eclipse.m2m.tests.qvt.oml"; //$NON-NLS-1$
		
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new JUnit4TestAdapter(AllTests.class));
	}
	
	@BeforeClass
	public static void prepare() {
		TestUtil.suppressGitPrefixPopUp();
	}
}
