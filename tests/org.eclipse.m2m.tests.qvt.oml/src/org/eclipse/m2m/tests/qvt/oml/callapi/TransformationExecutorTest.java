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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.m2m.internal.qvt.oml.InternalTransformationExecutor;
import org.eclipse.m2m.internal.qvt.oml.blackbox.BlackboxRegistry;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtilPlugin;
import org.eclipse.m2m.internal.qvt.oml.emf.util.ModelContent;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QVTEvaluationOptions;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.util.Trace;
import org.eclipse.m2m.tests.qvt.oml.transform.FileToFileData;
import org.eclipse.m2m.tests.qvt.oml.transform.ModelTestData;
import org.eclipse.m2m.tests.qvt.oml.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;


public abstract class TransformationExecutorTest extends TestCase {

	final String name;
	final List<String> inModels;
	final List<String> outModels;
	final Map<String, Object> configProps;
	
	TransformationExecutor executor;
	ExecutionContextImpl executionContext;
	List<BasicModelExtent> extents;
	UriCreator uriCreator;
	List<DirectionKind> paramKinds;
	ResourceSet resSet;
	int severity;
	int code;
	

	static class UriCreator extends FileToFileData implements TestUtil.UriProvider {
		UriCreator(String name) {
			super(name);
		}
		
		URI getTransformationUri() {
			return URI.createPlatformPluginURI(getBundle() + IPath.SEPARATOR + getTestDataFolder() + IPath.SEPARATOR
					+ ModelTestData.MODEL_FOLDER + IPath.SEPARATOR + getName() + IPath.SEPARATOR + getName()
					+ MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT, true);
		}
		
		public URI getModelUri(String name) {
			return URI.createPlatformPluginURI(getBundle() + IPath.SEPARATOR + getTestDataFolder() + IPath.SEPARATOR
					+ ModelTestData.MODEL_FOLDER + IPath.SEPARATOR + getName() + IPath.SEPARATOR + name, true);
		}
		
		URI getTraceUri() {
			return getTransformationUri().trimFileExtension().appendFileExtension(MDAConstants.QVTO_TRACEFILE_EXTENSION);
		}
		
	}
	
	public TransformationExecutorTest(String name, List<String> inModels) {
		this(name, inModels, Collections.<String>emptyList());
	}
	
	public TransformationExecutorTest(String name, List<String> inModels, List<String> outModels) {
		this(name, inModels, outModels, Collections.<String, Object>emptyMap());
	}
	
	public TransformationExecutorTest(String name, List<String> inModels, List<String> outModels, String[][] configProps) {
		this(name, inModels, outModels, makeConfigProps(configProps));
	}

	public TransformationExecutorTest(String name, List<String> inModels, List<String> outModels, Map<String, Object> configProps) {
		this(name, inModels, outModels, configProps, Diagnostic.OK, 0);
	}
	
	public TransformationExecutorTest(String name, List<String> inModels, List<String> outModels, Map<String, Object> configProps, int severity, int code) {
		super(TransformationExecutorTest.class.getSimpleName() + " : " + name);
		
		this.name = name;
		this.inModels = inModels;
		this.outModels = outModels;
		this.configProps = configProps;
		this.uriCreator = new UriCreator(name);
		this.severity = severity;
		this.code = code;
	}

	@Override
	@Before
	public void setUp() {
		resSet = getMetamodelResolutionRS();
		paramKinds = getParamKinds();
		extents = new ArrayList<BasicModelExtent>(paramKinds.size());
		
		int inIndex = 0;
		for (int i = 0; i < paramKinds.size(); ++i) {
			BasicModelExtent extent = new BasicModelExtent();
			if (paramKinds.get(i) != DirectionKind.OUT && inIndex < inModels.size()) {
				URI modelUri = uriCreator.getModelUri(inModels.get(inIndex++));
				Resource resource = resSet.getResource(modelUri, true);
				extent = new BasicModelExtent(resource.getContents());
			}
			extents.add(extent);
		}
		
		executor = new TransformationExecutor(uriCreator.getTransformationUri(), resSet.getPackageRegistry());
		executionContext = new ExecutionContextImpl();
		for (String key : configProps.keySet()) {
			executionContext.setConfigProperty(key, configProps.get(key));
		}
		
		URI traceUri = uriCreator.getTraceUri();
		if (resSet.getURIConverter().exists(traceUri, Collections.emptyMap())) {
			ModelContent traceContent = EmfUtil.safeLoadModel(traceUri, resSet);
			if (traceContent != null) {
				executionContext.getSessionData().setValue(QVTEvaluationOptions.INCREMENTAL_UPDATE_TRACE, new Trace(traceContent.getContent()));
			}
		}
	}
	
	@Override
	@After
	public void tearDown() throws Exception {
		executor.cleanup();
		BlackboxRegistry.INSTANCE.cleanup();
	}
	
	/**
	 * Test if blackbox transformations can be successfully executed when running via TransformationExecutor.
	 */
	@Override
	@Test
	public void runTest() throws Throwable {
		ExecutionDiagnostic diagnostic = executor.execute(executionContext, extents.toArray(new ModelExtent[extents.size()]));
		if (severity != diagnostic.getSeverity()
				|| code != diagnostic.getCode()
				) {
			fail(diagnostic.getMessage() + " : " + diagnostic.getStackTrace());
		}
	}
	
	protected List<URI> getEcoreMetamodels() {
		return Collections.emptyList();
	}
	
    private static Map<String, Object> makeConfigProps(String[][] props) {
        Map<String, Object> cfgMap = new LinkedHashMap<String, Object>();
        for (int i = 0; i < props.length; i++) {
            cfgMap.put(props[i][0], props[i][1]);
        }
        return cfgMap;
    }
    
    private List<DirectionKind> getParamKinds() {
		InternalTransformationExecutor executor = new InternalTransformationExecutor(uriCreator.getTransformationUri(),
				resSet.getPackageRegistry());

		Diagnostic loadDiagnostic = executor.loadTransformation(new NullProgressMonitor());
		if (!EmfUtilPlugin.isSuccess(loadDiagnostic)) {
			return Collections.emptyList();
		}

		OperationalTransformation transformation = executor.getTransformation();
		if (transformation == null) {
			return Collections.emptyList();
		}
		
		List<DirectionKind> paramKinds = new ArrayList<DirectionKind>(transformation.getModelParameter().size());
		for (ModelParameter p : transformation.getModelParameter()) {
			paramKinds.add(p.getKind());
		}
		
		executor.cleanup();
		
		return paramKinds;
    }
    
    protected ResourceSet createResourceSet() {
    	return new ResourceSetImpl();
    }
    
    protected ResourceSet getMetamodelResolutionRS() {
    	return TestUtil.getMetamodelResolutionRS(createResourceSet(), getEcoreMetamodels(), uriCreator);
    }
	
}
