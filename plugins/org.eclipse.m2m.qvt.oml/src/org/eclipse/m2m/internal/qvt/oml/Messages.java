/*******************************************************************************
 * Copyright (c) 2007, 2019 Borland Software Corporation and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml;

/**
 * @since 2.0
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.m2m.internal.qvt.oml.messages"; //$NON-NLS-1$

	public static String CompilationErrorsFoundInUnit;

	public static String FailedToCompileUnitError;

	public static String InvalidModelParameterCountError;

	public static String NoTransformationEntryPointError;

	public static String NotTransformationInUnitError;

	public static String QVTRuntimeExceptionCaught;

	public static String ReadOnlyExtentModificationError;

	public static String StackTraceOverFlowError;

	public static String UnitNotFoundError;
	
	public static String TransformationExecutionCompleted;

	public static String QvtNamesChecker_blankIdentifierError;
	public static String QvtNamesChecker_illegalCharInIdentifierError;

	public static String QvtNamesChecker_InvalidCUnitName;
	public static String QvtNamesChecker_invalidQualifiedIdentifierError;
	public static String QvtNamesChecker_moduleIndentifierKindName;
	public static String QvtNamesChecker_namespaceIndentifierKindName;

	public static String QvtNamesChecker_reservedQVTWord;

	public static String InvalidSourceForOperationCall;

	public static String FailToLoadTraceForIncrementalUpdateExecution;

	public static String Compiler_Analyze;
	public static String Compiler_Compile;
	public static String Compiler_CompileSources;
	public static String Executor_Executing;
	public static String Visitor_Visit;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
