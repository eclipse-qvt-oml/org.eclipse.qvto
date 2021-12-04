/*******************************************************************************
 * Copyright (c) 2008, 2018 Borland Software Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.editor.ui;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnvFactory;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompiledUnit;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingMethodCS;
import org.eclipse.m2m.internal.qvt.oml.cst.MappingModuleCS;
import org.eclipse.m2m.internal.qvt.oml.editor.ui.hovers.SignatureUtil;
import org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeOperation;
import org.eclipse.ocl.utilities.UMLReflection;
import org.eclipse.osgi.util.NLS;

/**
 * Manages the override indicators for the given QVT element and annotation model.
 */
class OverrideIndicatorManager implements IQVTReconcilingListener {

	static final String ANNOTATION_TYPE = "org.eclipse.m2m.qvt.oml.ui.overrideIndicator"; //$NON-NLS-1$

	private IAnnotationModel fAnnotationModel;
	private Object fAnnotationModelLockObject;
	private Annotation[] fOverrideAnnotations;

	public OverrideIndicatorManager(IAnnotationModel annotationModel) {
		if(annotationModel == null) {
			throw new IllegalArgumentException();
		}
		fAnnotationModel = annotationModel;
		fAnnotationModelLockObject = getLockObject(fAnnotationModel);
	}

	/**
	 * Returns the lock object for the given annotation model.
	 *
	 * @param annotationModel the annotation model
	 * @return the annotation model's lock object
	 */
	private Object getLockObject(IAnnotationModel annotationModel) {
		if (annotationModel instanceof ISynchronizable) {
			Object lock= ((ISynchronizable)annotationModel).getLockObject();
			if (lock != null) {
				return lock;
			}
		}
		return annotationModel;
	}

	/**
	 * Updates the override and implements annotations based
	 * on the given compilation unit.
	 *
	 * @param unit the compilation unit
	 * @param progressMonitor the progress monitor
	 */
	protected void updateAnnotations(CompiledUnit unit, IProgressMonitor progressMonitor) {
		if (progressMonitor.isCanceled()) {
			return;
		}

		UMLReflection<?, EClassifier, EOperation, ?, ?, EParameter, ?, ?, ?, ?> uml =
			QvtOperationalEnvFactory.INSTANCE.createEnvironment().getUMLReflection();	// Global instance used since only want the UMLReflection.

		Map<Annotation, Position> annotationMap = Collections.emptyMap();

		if(unit != null && unit.getUnitCST() != null) {
			for(MappingModuleCS moduleCS : unit.getUnitCST().getModules()) {
				for(MappingMethodCS methodCS : moduleCS.getMethods()) {
					if (progressMonitor.isCanceled()) {
						return;
					}

					if(methodCS.getAst() instanceof ImperativeOperation) {
						ImperativeOperation imperativeOperation = (ImperativeOperation) methodCS.getAst();
						if(imperativeOperation.getOverridden() != null) {
							String text = NLS.bind(Messages.OverrideAnnotationText, SignatureUtil.getOperationSignature(uml, imperativeOperation.getOverridden()));
							Annotation annotation = new Annotation(ANNOTATION_TYPE, false, text);

							Position position = new Position(methodCS.getStartOffset(), 1);
							if(annotationMap.isEmpty()) {
								annotationMap = new HashMap<Annotation, Position>();
							}
							annotationMap.put(annotation, position);
						}
					}
				}
			}
		}

		if (progressMonitor.isCanceled()) {
			return;
		}

		synchronized (fAnnotationModelLockObject) {
			if (fAnnotationModel instanceof IAnnotationModelExtension) {
				((IAnnotationModelExtension)fAnnotationModel).replaceAnnotations(fOverrideAnnotations, annotationMap);
			} else {
				removeAnnotations();
				for(Map.Entry<Annotation, Position> mapEntry : annotationMap.entrySet()) {
					fAnnotationModel.addAnnotation(mapEntry.getKey(), mapEntry.getValue());
				}
			}

			fOverrideAnnotations = annotationMap.keySet().toArray(new Annotation[annotationMap.keySet().size()]);
		}
	}

	/**
	 * Removes all override indicators from this manager's annotation model.
	 */
	private void removeAnnotations() {
		if (fOverrideAnnotations == null) {
			return;
		}

		synchronized (fAnnotationModelLockObject) {
			if (fAnnotationModel instanceof IAnnotationModelExtension) {
				((IAnnotationModelExtension)fAnnotationModel).replaceAnnotations(fOverrideAnnotations, null);
			} else {
				for (int i = 0, length = fOverrideAnnotations.length; i < length; i++) {
					fAnnotationModel.removeAnnotation(fOverrideAnnotations[i]);
				}
			}
			fOverrideAnnotations= null;
		}
	}

	@Override
	public void reconciled(CompiledUnit unit, IProgressMonitor monitor) {
		updateAnnotations(unit, new NullProgressMonitor());
	}

	@Override
	public void aboutToBeReconciled() {
		// do nothing
	}
}
