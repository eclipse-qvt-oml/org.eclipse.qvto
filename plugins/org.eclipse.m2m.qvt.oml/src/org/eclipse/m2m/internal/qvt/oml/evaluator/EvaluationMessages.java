/*******************************************************************************
 * Copyright (c) 2007, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Alex Paperno - bugs 420970
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.evaluator;

import org.eclipse.m2m.internal.qvt.oml.NLS;

public class EvaluationMessages extends NLS {
    private static final String BUNDLE_NAME = "org.eclipse.m2m.internal.qvt.oml.evaluator.messages";//$NON-NLS-1$

    private EvaluationMessages() {
    }
    
	public static String AssertFailedMessage;
	public static String ExtendedOclEvaluatorVisitorImpl_InvalidObjectExpType;
    public static String ExtendedOclEvaluatorVisitorImpl_ModuleNotExecutable;
    public static String ExtendedOclEvaluatorVisitorImpl_ReadOnlyInputModel;
    public static String FatalAssertionFailed;
    public static String NonFatalAssertionFailed;
	public static String QvtOperationalEvaluationVisitorImpl_invalidConfigPropertyValue;
	public static String QvtOperationalEvaluationVisitorImpl_UndefModelParamInTransf;
	public static String QvtOperationalEvaluationVisitorImpl_unexpectedRuntimeExc;
	public static String TerminatingExecution;
	public static String UknownSourceLabel;
	public static String MappingPreconditionFailed;
	public static String MappingPostconditionFailed;
	public static String ModelTypeConstraintFailed;
	public static String NoBlackboxOperationFound;
	public static String AmbiguousBlackboxOperationFound;
	public static String BlackboxMappingFailedToAssignResult;
	public static String ContentMergeForMultivaluedFeatureFailed;
	
	public static String IteratorNotImpl;
	
	public static String QvtOperationalEvaluationVisitorImpl_EvaluationFailed;
	public static String QvtOperationalEvaluationVisitorImpl_UndefinedConstructor;
	public static String QvtOperationalEvaluationVisitorImpl_FailedToInstantiateDatatype;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, EvaluationMessages.class);
    }

	
}
