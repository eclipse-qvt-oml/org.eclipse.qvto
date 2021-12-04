/*******************************************************************************
 * Copyright (c) 2008, 2018 Borland Software Corporation and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - Bug394188
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.ocl2qvt;

import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ImportToNonTransformCtxHelper;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtRuntimeException;
import org.eclipse.m2m.internal.qvt.oml.library.Context;
import org.eclipse.m2m.qvt.oml.ExecutionStackTraceElement;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEvaluationEnvironment;
import org.eclipse.ocl.ecore.OCL.Query;
import org.eclipse.ocl.helper.OCLHelper;
import org.junit.Test;

public class ExtOCLEnvironmentWithQVTAccessTest extends OCLEnvironmentWithQVTAccessTest {

	public ExtOCLEnvironmentWithQVTAccessTest() {
		super();
	}

	@Override
	protected EcoreEvaluationEnvironment getEvaluationEnv(Query query) {
		return QvtOperationalEnvFactory.getInstance().createEvaluationEnvironment(new Context(), null);
	}

	@Override
	protected Object evaluate(EcoreEvaluationEnvironment evalEnv, Query query) {
		QvtOperationalEvaluationVisitorImpl visitor = QvtOperationalEvaluationVisitorImpl
				.createNonTransformationExecutionContextVisitor(
						QvtOperationalEnvFactory.getInstance().createEnvironment(),
						(QvtOperationalEvaluationEnv)evalEnv,
						new ImportToNonTransformCtxHelper(getImportedModules(), true)
						//getImportedModules(), new HashMap<Module, ModuleInstance>(3), new HashSet<ModuleInstance>()
						);

		return visitor.visitExpression(query.getExpression());
	}

	@Override
	protected Object evaluate(EcoreEvaluationEnvironment evalEnv, Query query, Object self) {
		evalEnv.add(Environment.SELF_VARIABLE_NAME, self);
		return evaluate(evalEnv, query);
	}

	@Test
	public void testcallQueryThrowingException() throws Exception {
		OCLHelper<EClassifier, EOperation, EStructuralFeature, Constraint> helper = fOCL.createOCLHelper();
		try {
			helper.setContext(EcorePackage.eINSTANCE.getENamedElement());
			helper.setValidating(true);

			org.eclipse.ocl.expressions.OCLExpression<EClassifier> q = helper.createQuery("callQueryThrowingException()");
			assertNull(helper.getProblems());
			try {
				evaluate(fOCL.createQuery(q));
				fail("QVT exception expected");
			} catch(QvtRuntimeException e) {
				List<? extends ExecutionStackTraceElement> stackElements = e.getQvtStackTrace();
				assertEquals(stackElements.size(), 2);
				ExecutionStackTraceElement e1 = stackElements.get(0);
				assertEquals("q1", e1.getModuleName());
				assertEquals("throwingException", e1.getOperationName());
				assertEquals(37, e1.getLineNumber());

				ExecutionStackTraceElement e2 = stackElements.get(1);
				assertEquals("q2", e2.getModuleName());
				assertEquals("callQueryThrowingException", e2.getOperationName());
				assertEquals(28, e2.getLineNumber());
			}
		} catch (ParserException e) {
			assertNotNull(helper.getProblems() != null);
		}
	}
}
