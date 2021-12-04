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
 *     Christopher Gerking - bug 427237
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.runtime.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.internal.qvt.oml.ExecutionDiagnosticImpl;
import org.eclipse.m2m.internal.qvt.oml.ast.env.InternalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.ModelExtentContents;
import org.eclipse.m2m.internal.qvt.oml.ast.env.ModelParameterExtent;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtEvaluationResult;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalFileEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalParserUtil;
import org.eclipse.m2m.internal.qvt.oml.common.MdaException;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompilerOptions;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.emf.util.ModelContent;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModelInstance;
import org.eclipse.m2m.internal.qvt.oml.evaluator.ModelParameterHelper;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.internal.qvt.oml.library.Context;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.QvtTransformation.TransformationParameter.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.config.QvtConfigurationProperty;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.util.IContext;
import org.eclipse.ocl.EvaluationVisitor;
import org.eclipse.ocl.ecore.CallOperationAction;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.SendSignalAction;

public class QvtInterpretedTransformation implements QvtTransformation {

	public QvtInterpretedTransformation(IFile transformationFile) {
    	this(new WorkspaceQvtModule(transformationFile));
    }

	public QvtInterpretedTransformation(QvtModule qvtModule) {
		if(qvtModule == null) {
			throw new IllegalArgumentException("Null QvtModule"); //$NON-NLS-1$
		}
    	myModule = qvtModule;
    }

	public QvtModule getModule() {
		return myModule;
	}

	@Deprecated
    public ModelContent loadInput(URI inputObjectURI) throws MdaException {
    	return EmfUtil.loadModel(inputObjectURI, myModule.getResourceSet());
    }

    public void setQvtCompilerOptions(QvtCompilerOptions options) {
    	myModule.setQvtCompilerOptions(options);
    }

    @Deprecated
	public org.eclipse.m2m.internal.qvt.oml.runtime.generator.TransformationRunner.Out run(org.eclipse.m2m.internal.qvt.oml.runtime.generator.TransformationRunner.In in) throws MdaException {
        Module module = myModule.getModule();

        Iterator<TransformationParameter> itrParam = getParameters().iterator();
        List<ModelContent> inputs = new ArrayList<ModelContent>(in.getSources().length);
        for (ModelContent inSource : in.getSources()) {
        	TransformationParameter transfParam = null;
        	while (itrParam.hasNext()) {
        		transfParam = itrParam.next();
        		if (transfParam.getDirectionKind() != DirectionKind.OUT) {
        			break;
        		}
        		transfParam = null;
        	}
        	EObject mmClass = null;
        	if (transfParam != null) {
        		if (transfParam.getEntryType() != null) {
        			mmClass = transfParam.getEntryType();
        		}
        		else if (!transfParam.getMetamodels().isEmpty()) {
        			mmClass = transfParam.getMetamodels().get(0);
        		}
        	}
            try {
            	inputs.add(inSource.getResolvedContent(mmClass));
            }
            catch (WrappedException e) {
            	throw new MdaException(e.getCause());
            }
        }

        return evaluate(myModule.getResourceSet(), module, inputs, new Context(in.getContext()));
    }

	public String getModuleName() throws MdaException {
		return myModule.getModule().getName();
	}

	public URI getURI() {
		CompiledUnit unit = getUnit();
		return unit != null ? unit.getURI() : URI.createURI(""); //$NON-NLS-1$
	}

	public List<TransformationParameter> getParameters() throws MdaException {
		return myModule.getParameters();
	}

	public boolean hasEntryOperation() throws MdaException {
		return QvtOperationalParserUtil.getMainOperation(myModule.getModule()) != null;
	}

	public boolean isBlackbox() throws MdaException {
		return myModule.getModule().isIsBlackbox();
	}

    public Set<QvtConfigurationProperty> getConfigurationProperties() throws MdaException {
        return myModule.getConfigurationProperties();
    }

	public ResourceSet getResourceSet() {
		return myModule.getResourceSet();
	}

	public CompiledUnit getUnit() {
		try {
			return myModule.getUnit();
		} catch(MdaException e) {
			IStatus status = e.getStatus();

    		myDiagnostic = new ExecutionDiagnosticImpl(Diagnostic.ERROR, status.getCode(), status.getMessage());
			myDiagnostic.addAll(BasicDiagnostic.toDiagnostic(status));

			return null;
		}
	}

	public void cleanup() {
		myModule.cleanup();
	}

	protected QvtOperationalEnvFactory getEnvironmentFactory() {
		if(myEnvFactory == null) {
			myEnvFactory = new QvtOperationalEnvFactory(getResourceSet().getPackageRegistry());
		}
    	return myEnvFactory;
    }

	protected void setEnvironmentFactory(QvtOperationalEnvFactory factory) {
		myEnvFactory = factory;
	}

	@Deprecated
	private org.eclipse.m2m.internal.qvt.oml.runtime.generator.TransformationRunner.Out evaluate(ResourceSet rs, Module module, List<ModelContent> args, IContext context) {
		QvtOperationalEnvFactory factory = getEnvironmentFactory();

		QvtOperationalEvaluationEnv evaluationEnv = factory.createEvaluationEnvironment(context, null);
		// FIXME
		setArguments(evaluationEnv, (OperationalTransformation) module, args, rs);

		CompiledUnit unit;
		try {
			unit = myModule.getUnit();
		} catch (MdaException e) {
			// bad but we can not do better until
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=264335 is done
			throw new IllegalStateException("unit must be available"); //$NON-NLS-1$
		}
		QvtOperationalFileEnv rootEnv = factory.createEnvironment(unit.getURI());

		EvaluationVisitor<EPackage, EClassifier, EOperation, EStructuralFeature, EEnumLiteral, EParameter, EObject, CallOperationAction, SendSignalAction, Constraint, EClass, EObject>
			evaluator = factory.createEvaluationVisitor(rootEnv, evaluationEnv, null);

		Trace traces = evaluationEnv.getAdapter(InternalEvaluationEnv.class).getTraces();
		Object outObj = module.accept(evaluator);

        if (false == outObj instanceof QvtEvaluationResult) {
            return new org.eclipse.m2m.internal.qvt.oml.runtime.generator.TransformationRunner.Out(Collections.<ModelExtentContents>emptyList(),
            		Collections.emptyList(), traces);
        }

        return new org.eclipse.m2m.internal.qvt.oml.runtime.generator.TransformationRunner.Out(((QvtEvaluationResult) outObj).getModelExtents(),
        		((QvtEvaluationResult) outObj).getOutParamValues(), traces);
	}

	private static void setArguments(QvtOperationalEvaluationEnv evalEnv, OperationalTransformation transformation,
			List<ModelContent> args, ResourceSet compilerRS) {
		List<ModelParameterExtent> tranformArgs = new ArrayList<ModelParameterExtent>();
		int argCount = 0;
		for (ModelParameter modelParam : transformation.getModelParameter()) {
			ModelParameterExtent extent;
			if(modelParam.getKind() != org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind.OUT) {
				if(argCount >= args.size()) {
					throw new IllegalArgumentException("Invalid count of input arguments"); //$NON-NLS-1$
				}

				ModelContent nextArg = args.get(argCount++);

				if(nextArg == null) {
					throw new IllegalArgumentException("Non-null model argument is required"); //$NON-NLS-1$
		    	} else {
		    		extent = new ModelParameterExtent(nextArg.getContent(), compilerRS, modelParam);
		    	}

			} else {
				extent = new ModelParameterExtent(compilerRS);
			}

			evalEnv.addModelExtent(extent);
	    	tranformArgs.add(extent);
		}

		List<ModelInstance> modelArgs = ModelParameterHelper.createModelArguments(transformation, tranformArgs);
		evalEnv.getOperationArgs().addAll(modelArgs);
	}

	@Override
	public String toString() {
		try {
			return getModuleName();
		}
		catch (MdaException e) {
		}
		return super.toString();
	}

    private final QvtModule myModule;
    private QvtOperationalEnvFactory myEnvFactory;

    private ExecutionDiagnostic myDiagnostic = ExecutionDiagnosticImpl.createOkInstance();

    public OperationalTransformation getTransformation(IProgressMonitor monitor) {
    	try {
    		Module module = myModule.getModule();

    		return module instanceof OperationalTransformation ? (OperationalTransformation) module : null;
    	}
    	catch(MdaException e) {
    		IStatus status = e.getStatus();

    		myDiagnostic = new ExecutionDiagnosticImpl(Diagnostic.ERROR, status.getCode(), status.getMessage());
			myDiagnostic.addAll(BasicDiagnostic.toDiagnostic(status));

			return null;
    	}
    	finally {
    		monitor.done();
    	}
    }

    public ExecutionDiagnostic getDiagnostic() {
    	return myDiagnostic;
    }
}
