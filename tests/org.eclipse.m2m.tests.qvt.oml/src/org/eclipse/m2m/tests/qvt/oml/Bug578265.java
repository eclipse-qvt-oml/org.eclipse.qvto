/*******************************************************************************
 * Copyright (c) 2022 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.compiler.ExeXMISerializer;
import org.eclipse.m2m.internal.qvt.oml.compiler.QVTOCompiler;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompilerOptions;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitResolverFactory;
import org.eclipse.m2m.tests.qvt.oml.ParserTests.TestData;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class Bug578265 extends TestQvtParser {
		
	public Bug578265() {
		super(TestData.createSourceChecked("bug578265", 0, 0)); //$NON-NLS-1$
	}
		
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		TestUtil.buildProject(getTestProject().getProject());
	}
		
	@Override
	public void runTest() throws Exception {
				
		super.runTest();
				
		CompiledUnit[] compiledUnits = getCompiledResults();
		ExeXMISerializer.saveUnitXMI(compiledUnits, EPackage.Registry.INSTANCE);
		
		QVTOCompiler compiler = new QVTOCompiler();
		compiler.setUseCompiledXMI(true);
		
		for(CompiledUnit unit : compiledUnits) {
			URI xmiUri = ExeXMISerializer.toXMIUnitURI(unit.getURI());
			UnitProxy proxy = UnitResolverFactory.Registry.INSTANCE.getUnit(xmiUri);
			compiler.compile(new UnitProxy[] {proxy}, new QvtCompilerOptions(), new NullProgressMonitor());
			
			Resource r = compiler.getResourceSet().getResource(xmiUri, true);
			EcoreUtil.resolveAll(r);
			assertTrue(r.getErrors().isEmpty());
		}		
	}
}
