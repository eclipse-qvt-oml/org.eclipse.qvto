/*******************************************************************************
 * Copyright (c) 2016, 2018 Uwe Ritzmann and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Uwe Ritzmann - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestQvtUnparser.class})
public class UnparserTests {
    
    static class TestData {
        public TestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public TestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public TestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
            myName = name;
            myErrCount = errCount; 
            myWarnCount = warnCount;
            myEclipseDiffCount = eclipseDiffCount;
            usesSourceAnnotations = false;
        }
        
        public String getName() { return myName; }
        public String getDir() { return myName; }
        public int getErrCount() { return myErrCount; }
        public int getWarnCount() { return myWarnCount; }
        public int getEclipseDiffCount() { return myEclipseDiffCount; }
        
        public int getAllProblemsCount() {
			return myErrCount + myWarnCount;
		}
        
        public boolean usesSourceAnnotations() {
        	return usesSourceAnnotations;
		}
        
        private final String myName;
        private final int myErrCount;
        private final int myWarnCount;
        private final int myEclipseDiffCount;
        private boolean usesSourceAnnotations;

        /**
         * Creates that should be check for match of compilation problems with expected problem 
         * annotation in the test QVT sources 
         */
        public static TestData createSourceChecked(String dir, int errCount, int warnCount) {
        	return createSourceChecked(dir, errCount, warnCount, 0);
        }

        public static TestData createSourceChecked(String dir, int errCount, int warnCount, int eclipseDiffCount) {
        	TestData data = new TestData(dir, errCount, warnCount, eclipseDiffCount);
        	data.usesSourceAnnotations = true; 
        	return data;
        }

        public String toString() {
        	return "TestData " + myName + " --> " + myErrCount + "/" + myWarnCount + "/" + myEclipseDiffCount; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
        
        public String getFolder() {
        	return "unparserTestData"; //$NON-NLS-1$
        }
        
        public String getSubFolder() {
        	return "sources"; //$NON-NLS-1$
        }
        
        public String getPluginID() {
        	return AllTests.BUNDLE_ID;
        }
    }
    
    static class ModelsTestData extends TestData {
        public ModelsTestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public ModelsTestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public ModelsTestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
        	super(name, errCount, warnCount, eclipseDiffCount);
        }
        
        @Override
        public String getFolder() {
        	return "parserTestData"; //$NON-NLS-1$
        }
        
        @Override
        public String getSubFolder() {
        	return "models"; //$NON-NLS-1$
        }
    }
    
    static class SourcesTestData extends TestData {
        public SourcesTestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public SourcesTestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public SourcesTestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
        	super(name, errCount, warnCount, eclipseDiffCount);
        }
        
        @Override
        public String getFolder() {
        	return "parserTestData"; //$NON-NLS-1$
        }
        
        @Override
        public String getSubFolder() {
        	return "sources"; //$NON-NLS-1$
        }
    }
    
    static class DeployedTestData extends TestData {
        public DeployedTestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public DeployedTestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public DeployedTestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
        	super(name, errCount, warnCount, eclipseDiffCount);
        }
        
        @Override
        public String getFolder() {
        	return "deployed"; //$NON-NLS-1$
        }
        
        @Override
        public String getSubFolder() {
        	return "."; //$NON-NLS-1$
        }
    }
    
    static class ApiTestData extends TestData {
        public ApiTestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public ApiTestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public ApiTestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
        	super(name, errCount, warnCount, eclipseDiffCount);
        }
        
        @Override
        public String getFolder() {
        	return "apiTestData"; //$NON-NLS-1$
        }
        
        @Override
        public String getSubFolder() {
        	return "."; //$NON-NLS-1$
        }
    }
    
    static class ExternLibTestData extends TestData {
        public ExternLibTestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public ExternLibTestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public ExternLibTestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
        	super(name, errCount, warnCount, eclipseDiffCount);
        }
        
        @Override
        public String getFolder() {
        	return "parserTestData"; //$NON-NLS-1$
        }
        
        @Override
        public String getSubFolder() {
        	return "externlib"; //$NON-NLS-1$
        }
        
        @Override
        public String getDir() {
        	return "."; //$NON-NLS-1$
        }
    }
    
    static class AntTestData extends TestData {
        public AntTestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public AntTestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public AntTestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
        	super(name, errCount, warnCount, eclipseDiffCount);
        }
        
        @Override
		public String getFolder() {
			return "antTestData"; //$NON-NLS-1$
		}
		
		@Override
		public String getSubFolder() {
			return "."; //$NON-NLS-1$
		}
		
		@Override
		public String getDir() {
			return "twomodels"; //$NON-NLS-1$
		}
    }
    
    static abstract class PluginProjectTestData extends TestData {
        public PluginProjectTestData(String name, int errCount) { 
        	this(name, errCount, -1);
        }
        
        public PluginProjectTestData(String name, int errCount, int warnCount) {
        	this(name, errCount, warnCount, 0);
        }
        
        public PluginProjectTestData(String name, int errCount, int warnCount, int eclipseDiffCount) {
        	super(name, errCount, warnCount, eclipseDiffCount);
        }
        
        @Override
		public String getPluginID() {
			return "org.eclipse.m2m.tests.qvto.pluginProject"; //$NON-NLS-1$
		}
    }
}
