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
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.env;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalStdLibrary;
import org.eclipse.m2m.internal.qvt.oml.evaluator.InternalEvaluator;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QVTEvaluationOptions;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtGenericVisitorDecorator;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImportKind;
import org.eclipse.m2m.internal.qvt.oml.library.Context;
import org.eclipse.ocl.EvaluationVisitor;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.SendSignalAction;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class QVTOperationalEnvTest extends TestCase {

	private QvtOperationalEnvFactory factory;

	public QVTOperationalEnvTest(String name) {
		super(name);
	}

	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();

		factory = QvtOperationalEnvFactory.getInstance();
	}

	@Test
	public void testImportedEnvironments() throws Exception {
		QvtOperationalModuleEnv parentEnv = factory.createModuleEnvironment(QvtOperationalStdLibrary.createLibrary("Foo"));
		QvtOperationalModuleEnv importedEnv = factory.createModuleEnvironment(QvtOperationalStdLibrary.createLibrary("Imported"));

		parentEnv.addImport(ImportKind.EXTENSION, importedEnv);
		assertTrue(parentEnv.getImportsByExtends().contains(importedEnv));

		QvtOperationalEnv childEnv = factory.createEnvironment(parentEnv);

		QvtOperationalModuleEnv importedEnv2 = factory.createModuleEnvironment(QvtOperationalStdLibrary.createLibrary("Imported2"));
		childEnv.addImport(ImportKind.ACCESS, importedEnv2);

		assertEquals(parentEnv.getImportsByAccess(), childEnv.getImportsByAccess());
		assertEquals(parentEnv.getImportsByExtends(), childEnv.getImportsByExtends());
		assertTrue(childEnv.getImportsByExtends().contains(importedEnv));
		assertTrue(childEnv.getImportsByAccess().contains(importedEnv2));

		assertTrue(parentEnv.getImportsByAccess().contains(importedEnv2));
	}

	/**
	 * @see #testVisitorDecorators()
	 */
	public static class MyDecorator extends QvtGenericVisitorDecorator {
		public MyDecorator(InternalEvaluator qvtExtVisitor) {
			super(qvtExtVisitor);
		}
	}

	@Test
	public void testVisitorDecorators() throws Exception {
		// Specify a decorator class to be used
		List<Class<? extends QvtGenericVisitorDecorator>> decorators = new ArrayList<Class<? extends QvtGenericVisitorDecorator>>();
		decorators.add(MyDecorator.class);

		Context ctx = new Context();
		ctx.getSessionData().setValue(QVTEvaluationOptions.VISITOR_DECORATORS, decorators);

		QvtOperationalEvaluationEnv evalEnv = factory.createEvaluationEnvironment(ctx, null);
		QvtOperationalEnv env = factory.createEnvironment();

		// Create the visitor
		EvaluationVisitor<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject,
			CallOperationAction, SendSignalAction, Constraint, EClass, EObject> visitor = factory
				.createEvaluationVisitor(env, evalEnv, null);

		// Check that the visitor is of the decorator type
		assertTrue(visitor instanceof MyDecorator);
	}

}
