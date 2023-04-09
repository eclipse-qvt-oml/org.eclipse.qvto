/*******************************************************************************
 * Copyright (c) 2009, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.qvt.oml;

import java.io.PrintWriter;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;

/**
 * A diagnostic interface representing the result status of a transformation
 * execution.
 * 
 * @since 2.0
 * 
 * @noextend This interface is not intended to be extended by clients.
 */
public interface ExecutionDiagnostic extends Diagnostic, DiagnosticChain {

	/**
	 * The execution was terminated by failed QVT assertion.
	 */
	public static final int FATAL_ASSERTION = 100;

	/**
	 * The execution received user interruption request
	 */
	public static final int USER_INTERRUPTED = 110;

	public static final int EXCEPTION_THROWN = 120;

	public static final int VALIDATION = 130;

	public static final int MODEL_PARAMETER_MISMATCH = 140;
		
	public static final int NON_FATAL_ASSERTION = 150;
	
	public static final int TRANSFORMATION_LOAD_FAILED = 200;

	public static final String SOURCE = "org.eclipse.m2m.qvt.oml.execution"; //$NON-NLS-1$

		
	/**
	 * Gets the stack trace that resulted from interrupted execution either by
	 * user termination request or exception thrown
	 * 
	 * @return list of trace elements or an empty list
	 */
	public List<ExecutionStackTraceElement> getStackTrace();

	/**
	 * Prints the execution stack-trace (if available) of this diagnostic to the
	 * specified print writer.
	 * 
	 * @param writer
	 *            <code>PrintWriter</code> to use for output
	 */
	public void printStackTrace(PrintWriter writer);
}
