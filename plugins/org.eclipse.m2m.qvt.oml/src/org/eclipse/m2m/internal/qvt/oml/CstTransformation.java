/*******************************************************************************
 * Copyright (c) 2016, 2018 Christopher Gerking and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Christopher Gerking - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml;

import static org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtilPlugin.isSuccess;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.common.MdaException;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.compiler.ExeXMISerializer;
import org.eclipse.m2m.internal.qvt.oml.compiler.QVTOCompiler;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitResolverFactory;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;

public class CstTransformation implements Transformation {
	
	private URI fURI;
	private EPackage.Registry fPackageRegistry;
	private CompiledUnit fCompiledUnit;
	private ExecutionDiagnostic fLoadDiagnostic;
	private OperationalTransformation fTransformation;	
	private QVTOCompiler fCompiler;
	
	public CstTransformation(URI uri) {
		this(uri, EPackage.Registry.INSTANCE);
	}
	
	public CstTransformation(URI uri, EPackage.Registry packageRegistry) {
		if (uri == null) {
			throw new IllegalArgumentException("null transformation URI"); //$NON-NLS-1$
		}
		fURI = uri;
		fPackageRegistry = packageRegistry == null ? EPackage.Registry.INSTANCE : packageRegistry;
	}
	
	protected CompiledUnit getCompiledUnit(IProgressMonitor monitor) throws MdaException {			
		if (ExeXMISerializer.COMPILED_XMI_FILE_EXTENSION.equals(fURI.fileExtension())) {		
			return new CompiledUnit(fURI, getCompiler().getResourceSet());
		}

		UnitProxy proxy = UnitResolverFactory.Registry.INSTANCE.getUnit(fURI);
		if (proxy == null) {
			fLoadDiagnostic = new ExecutionDiagnosticImpl(Diagnostic.ERROR,
					ExecutionDiagnostic.TRANSFORMATION_LOAD_FAILED, NLS.bind(
							Messages.UnitNotFoundError, fURI));
			return null;
		}
		
		QVTOCompiler compiler = getCompiler();
		return compiler.compile(proxy, null, monitor);		
	}
	
	private QVTOCompiler getCompiler() {
		if (fCompiler == null) {
			fCompiler = createCompiler();
		}
		
		return fCompiler;
	}
	
	protected QVTOCompiler createCompiler() {
		if(fPackageRegistry == null) {
			return new QVTOCompiler();
		}
		
		return new QVTOCompiler(fPackageRegistry);
	}
	
	private void doLoad(IProgressMonitor monitor) {
		fLoadDiagnostic = ExecutionDiagnosticImpl.createOkInstance();
		
		try {
			fCompiledUnit = getCompiledUnit(monitor);
		} catch (MdaException e) {
			fLoadDiagnostic = new ExecutionDiagnosticImpl(Diagnostic.ERROR,
					ExecutionDiagnostic.TRANSFORMATION_LOAD_FAILED, NLS.bind(
							Messages.FailedToCompileUnitError, fURI));

			fLoadDiagnostic.merge(BasicDiagnostic.toDiagnostic(e.getStatus()));
		}
		
		if (fCompiledUnit != null && isSuccess(fLoadDiagnostic)) {
			
			ExecutionDiagnostic compilationDiagnostic = createCompilationDiagnostic(fCompiledUnit);
			
			if (isSuccess(compilationDiagnostic)) {
				fLoadDiagnostic.addAll(compilationDiagnostic);
			}
			else {
				compilationDiagnostic.addAll(fLoadDiagnostic);
				fLoadDiagnostic = compilationDiagnostic;
				
				return;
			}
						
			fTransformation = doGetTransformation();
			
			if (fTransformation == null) {
				ExecutionDiagnostic transformationDiagnostic = new ExecutionDiagnosticImpl(
					Diagnostic.ERROR,
					ExecutionDiagnostic.TRANSFORMATION_LOAD_FAILED, 
					NLS.bind(Messages.NotTransformationInUnitError,
					fURI)
				);
				transformationDiagnostic.addAll(fLoadDiagnostic);
				fLoadDiagnostic = transformationDiagnostic;
				
				return;
			}
			
			ExecutionDiagnostic executabilityDiagnostic = checkIsExecutable(fTransformation);
			
			if (isSuccess(executabilityDiagnostic)) {
				fLoadDiagnostic.addAll(executabilityDiagnostic);
			}
			else {
				executabilityDiagnostic.addAll(fLoadDiagnostic);
				fLoadDiagnostic = executabilityDiagnostic;
			}
		}
	}
	
	private OperationalTransformation doGetTransformation() {
		// TODO - cached the transformation selected as main
		if(fCompiledUnit == null) {
			return null;
		}
		
		List<Module> allModules = fCompiledUnit.getModules();
		for (Module module : allModules) {
			if (module instanceof OperationalTransformation) {
				return (OperationalTransformation) module;
			}
		}

		return null;
	}
	
	private static ExecutionDiagnostic createCompilationDiagnostic(
			CompiledUnit compiledUnit) {
		
		ExecutionDiagnostic mainDiagnostic = ExecutionDiagnosticImpl.createOkInstance();
				
		if (!compiledUnit.getErrors().isEmpty()) {
			
			URI uri = compiledUnit.getURI();
			
			mainDiagnostic = new ExecutionDiagnosticImpl(
					Diagnostic.ERROR, ExecutionDiagnostic.VALIDATION, NLS.bind(
							Messages.CompilationErrorsFoundInUnit, uri.toString()));
			
			for (Diagnostic error : compiledUnit.getErrors()) {
				mainDiagnostic.add(error);
			}
		}
		
		for (Diagnostic warning : compiledUnit.getWarnings()) {
			mainDiagnostic.add(warning);
		}
		
		return mainDiagnostic;
	}
		
	public OperationalTransformation getTransformation(IProgressMonitor monitor) {
		loadTransformation(monitor);
		
		return fTransformation;
	}
	
	private static ExecutionDiagnostic checkIsExecutable(
			OperationalTransformation transformation) {
		
		if (transformation.isIsBlackbox()) {
			return ExecutionDiagnosticImpl.createOkInstance();
		}
		
		EList<EOperation> operations = transformation.getEOperations();
		for (EOperation oper : operations) {
			if (oper instanceof ImperativeOperation
					&& QvtOperationalEnv.MAIN.equals(oper.getName())) {
				return ExecutionDiagnosticImpl.createOkInstance();
			}
		}

		return new ExecutionDiagnosticImpl(Diagnostic.ERROR,
				ExecutionDiagnostic.VALIDATION, NLS.bind(
						Messages.NoTransformationEntryPointError,
						transformation.getName()));
	}
	
	/**
	 * Attempts to load the transformation and checks
	 * if it is valid for execution.
	 * <p>
	 * <b>Remark:</b></br> Only the first performs the actual transformation
	 * loading, subsequent calls to this method will return the existing
	 * diagnostic.
	 * 
	 * @return the diagnostic indicating possible problems of the load action
	 */
	private Diagnostic loadTransformation(IProgressMonitor monitor) {
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
	
	public ExecutionDiagnostic getDiagnostic() {
		loadTransformation(new NullProgressMonitor());
		
		return fLoadDiagnostic;
	}
	
	public URI getURI() {
		return fURI;
	}
		
	public ResourceSet getResourceSet() {
		return getUnit().getResourceSet();
	}
	
	public CompiledUnit getUnit() {
		loadTransformation(new NullProgressMonitor());
		return fCompiledUnit;
	}
	
	public void cleanup() {
		if (getResourceSet() != null) {
			EmfUtil.cleanupResourceSet(getResourceSet());
		}
				
		getCompiler().cleanup();
	}

}
