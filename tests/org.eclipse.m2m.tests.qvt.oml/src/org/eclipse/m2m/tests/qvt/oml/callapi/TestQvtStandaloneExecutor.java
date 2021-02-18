/*******************************************************************************
 * Copyright (c) 2015, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.callapi;

import java.io.File;
import java.util.Arrays;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.tests.qvt.oml.transform.ModelTestData;
import org.eclipse.m2m.tests.qvt.oml.transform.TransformTests;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestQvtStandaloneExecutor extends TestQvtExecutor {
	
	private ModelTestData data;
	
	public TestQvtStandaloneExecutor(ModelTestData data) {
		super(data);
		this.data = data;
		uriCreator = new FileUriCreator(data.getName()) {
			@Override
			public String getTestDataFolder() {
				return data.getTestDataFolder();
			}
		};
	}
	
	@Parameters(name="{0}")
	public static Iterable<ModelTestData> data() {
		return Arrays.asList(TransformTests.createTestData());
	}
	
	@Override
	@Before
	public void setUp() {
		data.prepare(TransformationExecutor.BlackboxRegistry.INSTANCE);	
		super.setUp();
	}
	
	@Override
	protected ResourceSet createResourceSet() {
		ResourceSet rs = super.createResourceSet();
    	rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new EcoreResourceFactoryImpl());
    	return rs;
	}
	
	protected ResourceSet getMetamodelResolutionRS() {
		ResourceSet rs = super.getMetamodelResolutionRS();
		
		for(String modelString : inModels) {
			String fileExtension = modelString.substring(modelString.lastIndexOf(".") + 1);
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(fileExtension, new XMIResourceFactoryImpl());
		}
		
		for(String modelString : outModels) {
			String fileExtension = modelString.substring(modelString.lastIndexOf(".") + 1);
			rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(fileExtension, new XMIResourceFactoryImpl());
		}
			
		for(EPackage pack : data.getUsedPackages()) {
			rs.getPackageRegistry().put(pack.getNsURI(), pack);
		}
				
		return rs;
	}
	
	static class FileUriCreator extends UriCreator {
		
		FileUriCreator(String name) {
			super(name);
		}
		
		URI getTransformationUri() {
			return URI.createFileURI(new File("").getAbsolutePath()).appendSegment(getTestDataFolder()).appendSegment(ModelTestData.MODEL_FOLDER).appendSegment(getName()).appendSegment(getName()).appendFileExtension(MDAConstants.QVTO_FILE_EXTENSION);
		}
		
		public URI getModelUri(String name) {
			return URI.createFileURI(new File("").getAbsolutePath()).appendSegment(getTestDataFolder()).appendSegment(ModelTestData.MODEL_FOLDER).appendSegment(getName()).appendSegment(name);
		}
		
	}
	
}
