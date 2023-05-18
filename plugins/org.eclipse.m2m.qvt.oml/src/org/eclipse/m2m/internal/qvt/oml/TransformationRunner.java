/*******************************************************************************
 * Copyright (c) 2009, 2018 R.Dvorak and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Radek Dvorak - initial API and implementation
 *     Christopher Gerking - bug 431082
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml;

import static org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtilPlugin.isSuccess;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.m2m.internal.qvt.oml.InternalTransformationExecutor.TracesAwareExecutor;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.common.MdaException;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.ModelContent;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QVTEvaluationOptions;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;
import org.eclipse.m2m.qvt.oml.ExecutionContext;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;

public class TransformationRunner  {

	private final URI fTransformationURI;	
	private final TracesAwareExecutor fExecutor;
	private final List<URI> fModelParamURIs;
	private URI fTraceFileURI;
	private boolean fIsSaveTrace;
	private boolean fIsIncrementalUpdate;
	
	private BasicDiagnostic fDiagnostic;
	private List<ModelExtent> fModelParams;		
	private ModelExtentHelper fExtentHelper;
	private org.eclipse.m2m.qvt.oml.util.Trace fIncrementalTrace;
	
	
	public TransformationRunner(URI transformationURI, EPackage.Registry packageRegistry, List<URI> modelParamURIs) {
		this(getTransformation(transformationURI, packageRegistry), modelParamURIs);
	}
	
	protected TransformationRunner(Transformation transformation, List<URI> modelParamURIs) {
		if (transformation == null || modelParamURIs == null
				|| modelParamURIs.contains(null)) {
			throw new IllegalArgumentException();
		}

		fExecutor = new TracesAwareExecutor(transformation);
		fTransformationURI = transformation.getURI();
		fModelParamURIs = modelParamURIs;
	}
	
	private static Transformation getTransformation(URI transformationURI, EPackage.Registry packageRegistry) {
		return packageRegistry == null ? new CstTransformation(transformationURI) : 
			new CstTransformation(transformationURI, packageRegistry);
	}
		
	protected InternalTransformationExecutor getExecutor() {
		return fExecutor;
	};
	
	public URI getTransformationURI() {
		return fTransformationURI;
	}
	
	public void setTraceFile(URI traceFileURI) {
		fTraceFileURI = traceFileURI;
	}
	
	public void setSaveTrace(boolean isSaveTrace) {
		fIsSaveTrace = isSaveTrace;
	}

	public void setIncrementalUpdate(boolean isIncrementalUpdate) {
		fIsIncrementalUpdate = isIncrementalUpdate;
	}

	public Diagnostic initialize() {
		if(fDiagnostic != null) {
			return fDiagnostic;
		}
		
		fDiagnostic = QvtPlugin.createDiagnostic("Transformation runner initialize"); //$NON-NLS-1$
		
		Diagnostic loadDiagnostic = fExecutor.loadTransformation(new NullProgressMonitor());
		if(!isSuccess(loadDiagnostic)) {
			fDiagnostic.add(loadDiagnostic);
		}

		handleLoadTransformation(loadDiagnostic);
		
		OperationalTransformation transformation = fExecutor.getTransformation();
		if(transformation == null) {
			return fDiagnostic;
		}
		
		// Note: initialized here already loaded transformation is required
		fExtentHelper = new ModelExtentHelper(transformation, fModelParamURIs, fExecutor.getResourceSet());
		
		Diagnostic extentsDiagnostic = Diagnostic.OK_INSTANCE; 
		try {
			fModelParams = fExtentHelper.loadExtents();
		} catch (DiagnosticException e) {
			extentsDiagnostic = e.getDiagnostic();
		}
		
		handleLoadExtents(extentsDiagnostic);
		if(!isSuccess(extentsDiagnostic)) {
			fDiagnostic.add(extentsDiagnostic);
		}
		
		fIncrementalTrace = null;
		if (fIsIncrementalUpdate) {
			Diagnostic traceDiagnostic = Diagnostic.OK_INSTANCE; 

			ModelContent traceContent = EmfUtil.safeLoadModel(fTraceFileURI, fExecutor.getResourceSet());
			if (traceContent != null) {
				fIncrementalTrace = new org.eclipse.m2m.qvt.oml.util.Trace(traceContent.getContent());
			}
			else {
				traceDiagnostic = QvtPlugin.createWarnDiagnostic(
						NLS.bind(Messages.FailToLoadTraceForIncrementalUpdateExecution, fTraceFileURI));
			}
			
			handleLoadTrace(traceDiagnostic);
			if(!isSuccess(traceDiagnostic)) {
				fDiagnostic.add(traceDiagnostic);
			}
		}
		
		// FIXME - 
		// add validation for configuration properties and param count
		// into the internal executor

		// TODO - collect WARN, INFO diagnostics?
		return fDiagnostic;
	}
	
	protected QvtOperationalEnvFactory getEnvFactory() {
		return new QvtOperationalEnvFactory(); 
	}
	
	protected void handleLoadTransformation(Diagnostic diagnostic) {
		// do nothing
	}	
			
	protected void handleLoadExtents(Diagnostic diagnostic) {
		// do nothing
	}	

	protected void handleLoadTrace(Diagnostic diagnostic) {
		// do nothing
	}	

	protected void handleExecution(ExecutionDiagnostic execDiagnostic) {
		// do nothing
	}
	
	protected void handleSaveExtents(Diagnostic diagnostic) {
		// do nothing
	}		


	public Diagnostic execute(ExecutionContext context) {
		Diagnostic diagnostic = initialize();
		
		if(!isSuccess(diagnostic)) {
			return diagnostic;
		}

		fExecutor.setEnvironmentFactory(getEnvFactory());
		try {			
			ModelExtent[] params = fModelParams.toArray(new ModelExtent[fModelParams.size()]);
			
			if (fIncrementalTrace != null) {
				context.getSessionData().setValue(QVTEvaluationOptions.INCREMENTAL_UPDATE_TRACE, fIncrementalTrace);
			}
			ExecutionDiagnostic execDiagnostic = fExecutor.execute(context, params);
			handleExecution(execDiagnostic);
			
			if(!isSuccess(execDiagnostic)) {
				// skip saving any output
				return execDiagnostic;
			}

			// can continue and save output
			Diagnostic saveExtentsDiagnostic = fExtentHelper.saveExtents(context.getSessionData().getValue(QVTEvaluationOptions.FLAG_QVTO_UNPARSE_ENABLED));
			handleSaveExtents(saveExtentsDiagnostic);
			
			if(!isSuccess(saveExtentsDiagnostic)) {
				return saveExtentsDiagnostic;
			}

			Diagnostic saveTracesDiagnostic = saveTraces(fExecutor.getTraces());
			if(!isSuccess(saveTracesDiagnostic)) {
				return saveTracesDiagnostic;
			}

			return execDiagnostic;
		} finally {
			fExecutor.cleanup();
		}			
	}
	
	private Diagnostic saveTraces(Trace trace) { 
		if(fTraceFileURI != null && fIsSaveTrace) {
			try {
				new TraceSerializer(trace).saveTraceModel(fTraceFileURI);
			} catch (MdaException e) {
				String message = NLS.bind("Failed to save trace model uri={0}", fTraceFileURI);
				return new BasicDiagnostic(Diagnostic.ERROR, QvtPlugin.ID, 0,
						message, new Object[] { e });
			}
		}
		
		return Diagnostic.OK_INSTANCE;
	}
}
