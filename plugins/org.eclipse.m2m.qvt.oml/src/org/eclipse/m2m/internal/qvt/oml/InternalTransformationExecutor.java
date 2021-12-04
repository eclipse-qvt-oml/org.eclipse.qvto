/*******************************************************************************
 * Copyright (c) 2009, 2019 R.Dvorak and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Radek Dvorak - initial API and implementation
 *     Christopher Gerking - bugs 431082, 537041
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml;

import static org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtilPlugin.isSuccess;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.internal.qvt.oml.ast.env.InternalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.ModelExtentContents;
import org.eclipse.m2m.internal.qvt.oml.ast.env.ModelParameterExtent;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtEvaluationResult;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalFileEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalStdLibrary;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.evaluator.InternalEvaluator;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModelInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModelParameterHelper;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtException;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtInterruptedExecutionException;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtRuntimeException;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtStackOverFlowError;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.library.Context;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;
import org.eclipse.m2m.qvt.oml.ExecutionContext;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.util.IContext;
import org.eclipse.ocl.EvaluationVisitor;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.SendSignalAction;

/**
 * Internal transformation executor
 *
 * @since 3.0
 */
public class InternalTransformationExecutor {

	private ExecutionDiagnostic fLoadDiagnostic;
	private OperationalTransformation fOperationalTransformation;
	private QvtOperationalEnvFactory fEnvFactory;
	private Transformation fTransformation;

	/**
	 * Constructs the executor for the given transformation URI.
	 * <p>
	 * No attempt to resolve and load the transformation is done at this step
	 *
	 * @param uri
	 *            the URI of an existing transformation
	 */
	public InternalTransformationExecutor(URI uri) {
		this(new CstTransformation(uri));
	}

	public InternalTransformationExecutor(URI uri, EPackage.Registry registry) {
		this(new CstTransformation(uri, registry));
	}

	public InternalTransformationExecutor(Transformation transformation) {
		if (transformation == null) {
			throw new IllegalArgumentException("null transformation"); //$NON-NLS-1$
		}

		fTransformation = transformation;
	}

	public URI getURI() {
		return fTransformation.getURI();
	}

	public ResourceSet getResourceSet() {
		return fTransformation.getResourceSet();
	}

	/**
	 * Attempts to load the transformation referred by this executor and checks
	 * if it is valid for execution.
	 * <p>
	 * <b>Remark:</b></br> Only the first performs the actual transformation
	 * loading, subsequent calls to this method will return the existing
	 * diagnostic.
	 *
	 * @return the diagnostic indicating possible problems of the load action
	 */
	public Diagnostic loadTransformation(IProgressMonitor monitor) {
		try {
			if (fLoadDiagnostic == null) {
				doLoad(monitor);
			}
			return fLoadDiagnostic;
		}
		finally {
			monitor.done();
		}
	}

	/**
	 * Retrieves compiled unit if the referencing URI gets successfully resolved
	 * <p>
	 * <b>Remark</b>: This method invocation causes the referenced transformation to
	 * load if not already done before by direct call to
	 * {@linkplain #loadTransformation()} or
	 * {@linkplain #execute(ExecutionContext, ModelExtent...)}
	 *
	 * @return compiled unit or <code>null</code> if it failed to be obtained
	 */
	public CompiledUnit getUnit() {
		return fTransformation.getUnit();
	}

	/**
	 * Executes the transformation referred by this executor using the given
	 * model parameters and execution context.
	 *
	 * @param executionContext
	 *            the context object keeping the execution environment details
	 * @param modelParameters
	 *            the actual model arguments to the transformation
	 *
	 * @return the diagnostic object indicating the execution result status,
	 *         also keeping the details of possible problems
	 * @throws IllegalArgumentException
	 *             if the context or any of the model parameters is
	 *             <code>null</code>
	 */
	public ExecutionDiagnostic execute(ExecutionContext executionContext, ModelExtent... modelParameters) {
		// Java API check for nulls etc.
		if (executionContext == null) {
			throw new IllegalArgumentException();
		}

		IProgressMonitor monitor = executionContext.getProgressMonitor();

		try {
			SubMonitor progress = SubMonitor.convert(monitor, NLS.bind(Messages.Executor_Executing, getURI().toString()), 2);

			checkLegalModelParams(modelParameters);

			// ensure transformation unit is loaded
			loadTransformation(progress.split(1));

			// check if we have successfully loaded the transformation unit
			if (!isSuccess(fLoadDiagnostic)) {
				return fLoadDiagnostic;
			}

			try {
				return doExecute(modelParameters,
						new Context(executionContext, progress.split(1)));
			} catch (QvtRuntimeException e) {
				return createExecutionFailure(e);
			}
		} finally {
			if (monitor != null) {
				monitor.done();
			}
		}
	}

	private ExecutionDiagnostic doExecute(ModelExtent[] args, IContext context) {
		QvtOperationalEnvFactory factory = getEnvironmentFactory();
		QvtOperationalEvaluationEnv evaluationEnv = factory
				.createEvaluationEnvironment(context, null);

		ExecutionDiagnostic modelParamsDiagnostic = initArguments(evaluationEnv, fOperationalTransformation, args);
		if (!isSuccess(modelParamsDiagnostic)) {
			return modelParamsDiagnostic;
		}

		QvtOperationalFileEnv rootEnv = factory.createEnvironment(getURI());
		EvaluationVisitor<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> evaluator = factory
				.createEvaluationVisitor(rootEnv, evaluationEnv, null);

		// perform the actual execution
		assert evaluator instanceof InternalEvaluator : "expecting InternalEvaluator implementation"; //$NON-NLS-1$
		InternalEvaluator rawEvaluator = (InternalEvaluator) evaluator;

		Object evalResult = rawEvaluator.execute(fOperationalTransformation);

		// unpack the internal extents into the passed model parameters
		if (evalResult instanceof QvtEvaluationResult) {
			int extentIndex = 0;
			for (int i = 0; i < fOperationalTransformation.getModelParameter().size(); ++i) {
				ModelParameter p = fOperationalTransformation.getModelParameter().get(i);
				if (p.getKind() == DirectionKind.IN) {
					continue;
				}

				ModelExtentContents extent = ((QvtEvaluationResult) evalResult).getModelExtents().get(extentIndex++);
				args[i].setContents(extent.getAllRootElements());
			}
		}
		else {
			List<Object> resultArgs = evaluationEnv.getOperationArgs();
			int i = 0;
			for (Object nextResultArg : resultArgs) {
				ModelInstance modelInstance = (ModelInstance) nextResultArg;
				ModelParameterExtent extent = modelInstance.getExtent();

				List<EObject> allRootElements = extent.getContents().getAllRootElements();
				try {
					args[i++].setContents(allRootElements);
				} catch (UnsupportedOperationException e) {
					return new ExecutionDiagnosticImpl(Diagnostic.ERROR, ExecutionDiagnostic.MODEL_PARAMETER_MISMATCH,
							NLS.bind(Messages.ReadOnlyExtentModificationError, i - 1));
				}
			}
		}

		// do some handy processing with traces
		Trace traces = evaluationEnv.getAdapter(InternalEvaluationEnv.class).getTraces();
		handleExecutionTraces(traces);

		return ExecutionDiagnosticImpl.createOkInstance();
	}

	protected void handleExecutionTraces(Trace traces) {
		// nothing interesting here
	}

	private void doLoad(IProgressMonitor monitor) {
		fOperationalTransformation = fTransformation.getTransformation(monitor);

		fLoadDiagnostic = fTransformation.getDiagnostic();
	}

	private ExecutionDiagnostic initArguments(
			QvtOperationalEvaluationEnv evalEnv,
			OperationalTransformation transformationModel, ModelExtent[] args) {

		EList<ModelParameter> modelParameters = transformationModel.getModelParameter();
		if (modelParameters.size() > args.length) {
			return new ExecutionDiagnosticImpl(Diagnostic.ERROR,
					ExecutionDiagnostic.MODEL_PARAMETER_MISMATCH, NLS.bind(
							Messages.InvalidModelParameterCountError,
							args.length, modelParameters.size()));
		}

		ExecutionDiagnostic result = ExecutionDiagnosticImpl.createOkInstance();
		List<ModelParameterExtent> extents = new ArrayList<ModelParameterExtent>(modelParameters.size());

		int argCount = 0;
		for (ModelParameter modelParam : modelParameters) {
			ModelParameterExtent nextExtent;
			ModelExtent nextArg = args[argCount++];

			if (modelParam.getKind() != org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind.OUT) {
				nextExtent = new ModelParameterExtent(nextArg.getContents(), getResourceSet(), modelParam);
			} else {
				nextExtent = new ModelParameterExtent(getResourceSet());
			}

			evalEnv.addModelExtent(nextExtent);
			extents.add(nextExtent);
		}

		List<ModelInstance> modelArgs = ModelParameterHelper
				.createModelArguments(transformationModel, extents);
		evalEnv.getOperationArgs().addAll(modelArgs);

		return result;
	}

	public OperationalTransformation getTransformation() {
		loadTransformation(new NullProgressMonitor());
		return fOperationalTransformation;
	}

	public void setEnvironmentFactory(QvtOperationalEnvFactory factory) {
		fEnvFactory = factory;
	}

	protected QvtOperationalEnvFactory getEnvironmentFactory() {
		return fEnvFactory != null ? fEnvFactory : new QvtOperationalEnvFactory(getResourceSet().getPackageRegistry());
	}

	public void cleanup() {
		setEnvironmentFactory(null);

		fTransformation.cleanup();
	}


	private static ExecutionDiagnostic createExecutionFailure(
			QvtRuntimeException qvtRuntimeException) {
		int code = 0;
		int severity = Diagnostic.ERROR;
		String message = qvtRuntimeException.getLocalizedMessage();
		Object[] data = new Object[] {qvtRuntimeException};

		if (qvtRuntimeException instanceof QvtException) {
			code = ((QvtException) qvtRuntimeException).getExceptionType() == QvtOperationalStdLibrary.INSTANCE.getAssertionFailedClass() ?
					ExecutionDiagnostic.FATAL_ASSERTION : ExecutionDiagnostic.EXCEPTION_THROWN;
		} else if (qvtRuntimeException instanceof QvtInterruptedExecutionException) {
			code = ExecutionDiagnostic.USER_INTERRUPTED;
			severity = Diagnostic.CANCEL;
		} else {
			code = ExecutionDiagnostic.EXCEPTION_THROWN;
			if (qvtRuntimeException instanceof QvtStackOverFlowError == false) {
				Throwable cause = qvtRuntimeException.getCause();
				data = new Object[] { cause != null ? cause : qvtRuntimeException };
			} else {
				message = Messages.StackTraceOverFlowError;
			}
		}

		if (message == null) {
			message = NLS.bind(Messages.QVTRuntimeExceptionCaught,
					qvtRuntimeException.getClass().getName());
		}
		ExecutionDiagnosticImpl diagnostic = new ExecutionDiagnosticImpl(severity,
				code, message, data);
		diagnostic.setStackTrace(qvtRuntimeException.getQvtStackTrace());

		return diagnostic;
	}

	private void checkLegalModelParams(ModelExtent[] extents)
			throws IllegalArgumentException {
		if (extents == null) {
			throw new IllegalArgumentException("Null model parameters"); //$NON-NLS-1$
		}

		for (int i = 0; i < extents.length; i++) {
			if (extents[i] == null) {
				throw new IllegalArgumentException(
						"Null model parameter[" + i + "]"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}

	@Override
	public String toString() {
		return "QVTO-Executor: " + fTransformation.getURI(); //$NON-NLS-1$
	}

	public static class TracesAwareExecutor extends InternalTransformationExecutor {

		private Trace fTraces;

		public TracesAwareExecutor(URI uri, EPackage.Registry registry) {
			super(uri, registry);
		}

		public TracesAwareExecutor(URI uri) {
			super(uri);
		}

		public TracesAwareExecutor(Transformation transformation) {
			super(transformation);
		}

		public Trace getTraces() {
			return fTraces;
		}

		@Override
		protected void handleExecutionTraces(Trace traces) {
			super.handleExecutionTraces(traces);
			fTraces = traces;
		}

		@Override
		public void cleanup() {
			super.cleanup();
			fTraces = null;
		}

	}

}
