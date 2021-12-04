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
package org.eclipse.m2m.qvt.oml.debug.core.vm;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitor;
import org.eclipse.m2m.qvt.oml.util.IContext;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.EvaluationVisitor;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.SendSignalAction;



public final class DebugEnvironmentFactory extends QvtOperationalEnvFactory {

	private final IQVTODebuggerShell debuggerShell;
	private long envUIDGenerator = 0;

	@Deprecated /* @deprecated see Bug 577620 pass a package registry */
	public DebugEnvironmentFactory(IQVTODebuggerShell debuggerShell) {
		this(debuggerShell, EPackage.Registry.INSTANCE);
	}

	public DebugEnvironmentFactory(IQVTODebuggerShell debuggerShell, Registry packageRegistry) {
		super(packageRegistry);
		this.debuggerShell = debuggerShell;
	}

	@Override
	public EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> createEvaluationEnvironment(
			EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> parent) {

		if(parent instanceof QvtOperationalEvaluationEnv) {
			QvtOperationalEvaluationEnv qvtParentEnv = (QvtOperationalEvaluationEnv) parent;
			return new DebugEvaluationEnvironment(qvtParentEnv.getContext(), qvtParentEnv, envUIDGenerator++);
		}

		return super.createEvaluationEnvironment(parent);
	}

	@Override
	public QvtOperationalEvaluationEnv createEvaluationEnvironment(IContext context, QvtOperationalEvaluationEnv parent) {
		return new DebugEvaluationEnvironment(context, parent, envUIDGenerator++);
	}

	@Override
	public EvaluationVisitor<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> createEvaluationVisitor(
			Environment<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject> env,
			EvaluationEnvironment<EClassifier, EOperation, EStructuralFeature, EClass, EObject> evalEnv,
			Map<? extends EClass, ? extends Set<? extends EObject>> extentMap) {

		if ((env instanceof QvtOperationalEnv == false) || (evalEnv instanceof QvtOperationalEvaluationEnv == false)) {
			return super.createEvaluationVisitor(env, evalEnv, extentMap);
		}

		if ((debuggerShell instanceof IQVTODebuggerShellExtension) && ((IQVTODebuggerShellExtension)debuggerShell).isSessionStarted()) {
			return super.createEvaluationVisitor(env, evalEnv, extentMap);
		}

	    // ADD SYNC mechanism, for this point being a QVTO VM started, as from here we have access to the
	    // evaluator => DBG creation event?

		QVTODebugEvaluator evaluatorVisitor = new QVTODebugEvaluator((QvtOperationalEnv) env,
				(QvtOperationalEvaluationEnv) evalEnv, debuggerShell);

		QvtOperationalEvaluationVisitor result = evaluatorVisitor.createDebugInterceptor();
		return result;
	}
}