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


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalVisitorCS;
import org.eclipse.m2m.internal.qvt.oml.compiler.QvtCompilerOptions;
import org.eclipse.m2m.internal.qvt.oml.cst.completion.parser.LightweightParser;
import org.eclipse.m2m.internal.qvt.oml.cst.parser.QVTOLexer;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QvtOperationalEvaluationVisitorImpl;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.qvt.oml.debug.core.QVTODebugCore;
import org.eclipse.m2m.qvt.oml.debug.core.QVTODebugUtil;
import org.eclipse.ocl.OCLInput;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.cst.CSTNode;
import org.eclipse.ocl.cst.OCLExpressionCS;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.parser.OCLLexer;
import org.eclipse.ocl.utilities.ASTNode;

public class ConditionChecker {

	public static final int ERR_CODE_COMPILATION = 100;
	public static final int ERR_CODE_EVALUATION = 110;

	private final String fConditionBody;
	private final ASTNode fTargetASTElement;

	private OCLExpression<EClassifier> fConditionAST;
	private IStatus fConditionError;


	public ConditionChecker(String conditionBody, ASTNode targetASTElement) {
		if(conditionBody == null || targetASTElement == null) {
			throw new IllegalArgumentException();
		}

		fConditionBody = conditionBody;
		fTargetASTElement = targetASTElement;
	}

	public Object evaluate(QvtOperationalEvaluationVisitorImpl mainEvaluator) throws CoreException {
		OCLExpression<EClassifier> condition = getConditionAST();
		if (fConditionError != null) {
			throw new CoreException(fConditionError);
		}

		assert condition != null;
		// FIXME - use a watching thread to interrupt infinite loop execution
		QvtOperationalEvaluationEnv evalEnv = mainEvaluator.getOperationalEvaluationEnv().cloneEvaluationEnv();
		QvtOperationalEvaluationVisitorImpl dedicatedVisitor = new QvtOperationalEvaluationVisitorImpl(
				(QvtOperationalEnv) mainEvaluator.getEnvironment(), evalEnv);

		try {
			return condition.accept(dedicatedVisitor);
		} catch (Throwable e) {
			throw new CoreException(QVTODebugCore.createError(
					e.toString(), ERR_CODE_EVALUATION, e));
		}
	}

	public boolean checkCondition(QvtOperationalEvaluationVisitorImpl mainEvaluator) throws CoreException {
		return Boolean.TRUE.equals(evaluate(mainEvaluator));
	}

	public EClassifier getConditionType() {
		if (fConditionAST != null) {
			return fConditionAST.getType();
		}
		return null;
	}


    private ASTElementContextEnv getEnvironmentForASTElement() {
		QvtOperationalEnvFactory factory = new QvtOperationalEnvFactory(new EPackageRegistryImpl(EPackage.Registry.INSTANCE));
		QvtOperationalEnv rootEnv = null;

    	EObject moduleContext = fTargetASTElement;
    	while(moduleContext != null) {
    		if(moduleContext instanceof Module) {
    			rootEnv = QVTODebugUtil.getEnvironment((Module) moduleContext);
    			break;
    		}
    		moduleContext = moduleContext.eContainer();
    	}

    	if(rootEnv == null) {
    		rootEnv = factory.createEnvironment();
    	}

		QvtOperationalEnv contextEnv = null;
    	EObject operContext = fTargetASTElement;
    	while(operContext != null) {
    		if(operContext instanceof EOperation) {
    			contextEnv = factory.createOperationContext(rootEnv, (EOperation) operContext);
    		}
    		operContext = operContext.eContainer();
    	}

    	if(contextEnv == null) {
    		contextEnv = rootEnv;
    	}

    	ASTElementContextEnv targetContextEnv = new ASTElementContextEnv(contextEnv, fTargetASTElement);
		return targetContextEnv;
    }

	private OCLExpression<EClassifier> getConditionAST() {
		if(fConditionError != null) {
			return null;
		}

		if (fConditionAST == null) {
			fConditionAST = analyzeCondition();
		}

		return fConditionAST;
	}

    private OCLExpressionCS parseCondition(QvtOperationalEnv env) {
        try {
            QVTOLexer lexer = new QVTOLexer(env, new OCLInput(fConditionBody).getContent());

            LightweightParser parser = new LightweightParser(lexer);
            parser.enableCSTTokens(true);
            parser.getIPrsStream().resetTokenStream();
            lexer.lexer(parser.getIPrsStream());
            CSTNode cst = parser.parser(10);
            if(cst instanceof OCLExpressionCS) {
            	return (OCLExpressionCS) cst;
            }

            env.reportError("Not an OCL expression", -1, -1); //$NON-NLS-1$
        } catch (ParserException ex) {
        	// add parser error to environment
            env.reportError(ex.toString(), -1, -1);
        }

        return null;
    }

    private OCLExpression<EClassifier> analyzeCondition()  {
    	ASTElementContextEnv env = getEnvironmentForASTElement();
        OCLExpressionCS conditionCS = parseCondition(env);
        OCLExpression<EClassifier> ast = null;

        if (conditionCS != null && !env.hasErrors()) {
            OCLLexer oclLexer = new OCLLexer(env, new char[0]);

            QvtCompilerOptions options = new QvtCompilerOptions();
            options.setReportErrors(true);
            options.setShowAnnotations(false);
            options.setSourceLineNumbersEnabled(false);
            try {
	            QvtOperationalVisitorCS visitor = new QvtOperationalVisitorCS(oclLexer, options);
	            ast = visitor.analyzeExpressionCS(conditionCS, env);
	            if(ast == null) { // || ast.getType() != env.getOCLStandardLibrary().getBoolean()) {
	            	//env.reportError("Boolean type condition required", conditionCS);
	            	env.reportError("Invalid expression", conditionCS);
	            }
            } catch (Throwable e) {
            	fConditionError = QVTODebugCore.createError("Failed to parse condition", ERR_CODE_COMPILATION,  e);
            	QVTODebugCore.log(e);
            	return null;
			}
        }

    	if(env.hasErrors()) {
    		fConditionError = QVTODebugCore.createError(env.getErrorTxtBuffer().toString(), ERR_CODE_COMPILATION, null);
    	}

        return ast;
    }
}
