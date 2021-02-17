/*******************************************************************************
 * Copyright (c) 2007, 2020 Borland Software Corporation and others.
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
package org.eclipse.m2m.internal.tests.qvt.oml.ui;

import org.eclipse.m2m.internal.tests.qvt.oml.debugger.DebuggerTests;
import org.eclipse.m2m.internal.tests.qvt.oml.ui.completion.CompletionTests;
import org.eclipse.m2m.internal.tests.qvt.oml.ui.editor.AllEditorTests;
import org.eclipse.m2m.internal.tests.qvt.oml.ui.wizards.project.NewProjectCreationOperationTest;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;

/**
 * @author sboyko
 */
@RunWith(Suite.class)
@SuiteClasses({CompletionTests.class, AllEditorTests.class, DebuggerTests.class, NewProjectCreationOperationTest.class})
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(AllTests.class));
	}

	@BeforeClass
	public static void setUpBeforeClass() {
		TestUtil.suppressGitPrefixPopUp();
	}
}
