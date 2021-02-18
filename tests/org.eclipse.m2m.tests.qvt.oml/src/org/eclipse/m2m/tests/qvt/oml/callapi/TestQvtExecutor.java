/*******************************************************************************
 * Copyright (c) 2014, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Boyko Sergey - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.callapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompilerUtils;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.runtime.launch.QvtLaunchUtil;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.tests.qvt.oml.transform.FilesToFilesData;
import org.eclipse.m2m.tests.qvt.oml.transform.ModelTestData;
import org.eclipse.m2m.tests.qvt.oml.transform.TransformTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestQvtExecutor extends TransformationExecutorTest {
	
	private final List<URI> ecoreMetamodels;
	
	public TestQvtExecutor(ModelTestData data) {
		super(data.getName(), getInModels(data), getOutModels(data), QvtLaunchUtil.getConfigProperties(data.getContext()));
		ecoreMetamodels = data.getEcoreMetamodels();
		
		uriCreator = new UriCreator(name) {
			@Override
			public String getTestDataFolder() {
				return data.getTestDataFolder();
			};
		};
	}
		
	@Parameters(name="{0}")
	public static Iterable<ModelTestData> data() {
		return Arrays.asList(TransformTests.createTestData());
    }

	static List<String> getInModels(ModelTestData data) {
		if (data instanceof FilesToFilesData) {
			return ((FilesToFilesData) data).getFromFiles();
		}
		return Collections.emptyList();
	}
	
	static List<String> getOutModels(ModelTestData data) {
		if (data instanceof FilesToFilesData) {
			return ((FilesToFilesData) data).getExpectedFiles();
		}
		return Collections.emptyList();
	}
	
	@Override
	protected List<URI> getEcoreMetamodels() {
		return ecoreMetamodels;
	}
	
	@Override
	@Test
	public void runTest() throws Throwable {
		super.runTest();
		
		ResourceSet checkingRs = CompilerUtils.cloneRegistrations(resSet);
		CompilerUtils.addMappingsToResourceSet(checkingRs, uriCreator.getTransformationUri());
		
		int outIndex = 0;
		for (int i = 0; i < paramKinds.size(); ++i) {
			if (paramKinds.get(i) == DirectionKind.IN) {
				continue;
			}
			
			ModelExtent modelExtent = extents.get(i);
			URI modelUri = uriCreator.getModelUri(outModels.get(outIndex++));
			Resource resource = checkingRs.getResource(modelUri, true);
			ModelTestData.compareWithExpected(name, resource.getContents(), modelExtent.getContents());
		}
	}

}
