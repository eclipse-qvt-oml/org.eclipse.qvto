/*******************************************************************************
 * Copyright (c) 2009, 2018 R.Dvorak and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Radek Dvorak - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.qvt.oml.debug.core.app;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.m2m.internal.qvt.oml.TransformationRunner;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.qvt.oml.ExecutionContext;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ExecutionStackTraceElement;
import org.eclipse.m2m.qvt.oml.debug.core.QVTODebugUtil;
import org.eclipse.m2m.qvt.oml.debug.core.vm.DebugEnvironmentFactory;
import org.eclipse.m2m.qvt.oml.debug.core.vm.DebuggableExecutorAdapter;
import org.eclipse.m2m.qvt.oml.debug.core.vm.IQVTODebuggerShell;

public class DebugTransformationRunner extends TransformationRunner {

	private IQVTODebuggerShell fDebugShell;
	private PrintWriter fErrorLog;

	public DebugTransformationRunner(URI transformationURI,
			EPackage.Registry packageRegistry,
			List<URI> modelParamURIs) {
		super(transformationURI, packageRegistry, modelParamURIs);

		fErrorLog = new PrintWriter(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				// do nothing I'm a <null> log
			}
		}, true);
	}

	public void setErrorLog(PrintWriter errorLog) {
		if(errorLog == null) {
			throw new IllegalArgumentException();
		}
		this.fErrorLog = errorLog;
	}

	@Override
	protected QvtOperationalEnvFactory getEnvFactory() {
		if(fDebugShell != null) {
			return new DebugEnvironmentFactory(fDebugShell, getExecutor().getResourceSet().getPackageRegistry());
		}
		return super.getEnvFactory();
	}

	@Override
	protected void handleLoadExtents(Diagnostic diagnostic) {
	}

	@Override
	protected void handleLoadTransformation(Diagnostic diagnostic) {
	}

	@Override
	protected void handleExecution(org.eclipse.m2m.qvt.oml.ExecutionDiagnostic execDiagnostic) {
		List<ExecutionStackTraceElement> stackTrace = execDiagnostic.getStackTrace();
		if(stackTrace != null && execDiagnostic.getCode() != ExecutionDiagnostic.USER_INTERRUPTED) {
			//fErrorLog.println(execDiagnostic);

			if (!stackTrace.isEmpty()) {
				fErrorLog.println("[QVTO Stack trace:]");
				execDiagnostic.printStackTrace(fErrorLog);
				fErrorLog.println();
			}
		}

		if(execDiagnostic.getException() != null) {
			fErrorLog.println("[Java cause:]");
			execDiagnostic.getException().printStackTrace(fErrorLog);
		}

		fErrorLog.flush();
	}

	@Override
	protected void handleSaveExtents(Diagnostic diagnostic) {

	}

	/**
	 * @since 2.6
	 */
	public DebuggableExecutorAdapter createDebuggableAdapter(final ExecutionContext context) {

		return new DebuggableExecutorAdapter() {
			@Override
			public Diagnostic execute() throws IllegalStateException {
				if(fDebugShell == null) {
					throw new IllegalStateException("Executor not connected to debugger"); //$NON-NLS-1$
				}

				CompiledUnit mainUnit = DebugTransformationRunner.this.getExecutor().getUnit();
				if(mainUnit != null) {
					QVTODebugUtil.attachEnvironment(mainUnit);
				}

				Diagnostic execDiagnostic = DebugTransformationRunner.this.execute(context);

				//if(execDiagnostic.getSeverity() != Diagnostic.OK) {
					fErrorLog.println(execDiagnostic);
				//}

				return execDiagnostic;
			}

			@Override
			public CompiledUnit getUnit() {
				return getExecutor().getUnit();
			}

			@Override
			public void connect(IQVTODebuggerShell debugShell) {
				fDebugShell = debugShell;
			}
		};
	}
}
