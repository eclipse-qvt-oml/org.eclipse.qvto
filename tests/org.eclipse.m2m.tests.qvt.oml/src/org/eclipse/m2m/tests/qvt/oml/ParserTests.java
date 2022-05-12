/*******************************************************************************
 * Copyright (c) 2007, 2022 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Alex Paperno - bugs 272869, 268636, 413130, 404647, 414363, 401521,
 *                         414616, 419299, 414619, 415024, 413391, 424584
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({QvtLibraryASTTest.class, TestQvtParser.class, Bug577992.class, Bug578265.class, Bug579914.class})
public class ParserTests {
    
    static class TestData {
        public TestData(String dir, int errCount) { 
        	this(dir, errCount, -1);
        }
        
        public TestData(String dir, int errCount, int warnCount) {
            myDir = dir; 
            myErrCount = errCount; 
            myWarnCount = warnCount;
            usesSourceAnnotations = false;
        }
        
        public TestData includeMetamodel(String relativePath) {
        	URI relativeURI = URI.createURI(relativePath);
        	ecoreFileMetamodels.add(relativeURI);
        	return this;
        }
        
        public String getDir() { return myDir; }
        public int getErrCount() { return myErrCount; }
        public int getWarnCount() { return myWarnCount; }
        
        public int getAllProblemsCount() {
			return myErrCount + myWarnCount;
		}
        
        public boolean usesSourceAnnotations() {
        	return usesSourceAnnotations;
		}
        
        public List<URI> getMetamodels() {
        	return ecoreFileMetamodels;
        }
        
        @Override
        public String toString() {
        	return getDir();
        }
        
        private final String myDir;
        private final int myErrCount;
        private final int myWarnCount;
        protected final List<URI> ecoreFileMetamodels = new ArrayList<URI>();
        private boolean usesSourceAnnotations;

        /**
         * Creates that should be check for match of compilation problems with expected problem 
         * annotation in the test QVT sources 
         */
        public static TestData createSourceChecked(String dir, int errCount, int warnCount) {
        	TestData data = new TestData(dir, errCount, warnCount);
        	data.usesSourceAnnotations = true; 
        	return data;
        }
    }    
}
