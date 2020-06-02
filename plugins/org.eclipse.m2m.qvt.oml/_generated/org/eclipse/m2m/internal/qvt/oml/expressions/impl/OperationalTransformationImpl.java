/**
 * Copyright (c) 2007, 2019 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *
 * $Id: OperationalTransformationImpl.java,v 1.3 2008/12/18 15:18:10 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.OperationalTransformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operational Transformation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationalTransformationImpl#getIntermediateClass <em>Intermediate Class</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationalTransformationImpl#getIntermediateProperty <em>Intermediate Property</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.OperationalTransformationImpl#getModelParameter <em>Model Parameter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationalTransformationImpl extends ModuleImpl implements OperationalTransformation {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getIntermediateClass() <em>Intermediate Class</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntermediateClass()
	 * @generated
	 * @ordered
	 */
	protected EList<EClass> intermediateClass;

	/**
	 * The cached value of the '{@link #getIntermediateProperty() <em>Intermediate Property</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntermediateProperty()
	 * @generated
	 * @ordered
	 */
	protected EList<EStructuralFeature> intermediateProperty;

	/**
	 * The cached value of the '{@link #getModelParameter() <em>Model Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelParameter()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelParameter> modelParameter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationalTransformationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.OPERATIONAL_TRANSFORMATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EClass> getIntermediateClass() {
		if (intermediateClass == null) {
			intermediateClass = new EObjectResolvingEList<EClass>(EClass.class, this, ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_CLASS);
		}
		return intermediateClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EStructuralFeature> getIntermediateProperty() {
		if (intermediateProperty == null) {
			intermediateProperty = new EObjectResolvingEList<EStructuralFeature>(EStructuralFeature.class, this, ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_PROPERTY);
		}
		return intermediateProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelParameter> getModelParameter() {
		if (modelParameter == null) {
			modelParameter = new EObjectContainmentEList<ModelParameter>(ModelParameter.class, this, ExpressionsPackage.OPERATIONAL_TRANSFORMATION__MODEL_PARAMETER);
		}
		return modelParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__MODEL_PARAMETER:
				return ((InternalEList<?>)getModelParameter()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_CLASS:
				return getIntermediateClass();
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_PROPERTY:
				return getIntermediateProperty();
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__MODEL_PARAMETER:
				return getModelParameter();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_CLASS:
				getIntermediateClass().clear();
				getIntermediateClass().addAll((Collection<? extends EClass>)newValue);
				return;
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_PROPERTY:
				getIntermediateProperty().clear();
				getIntermediateProperty().addAll((Collection<? extends EStructuralFeature>)newValue);
				return;
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__MODEL_PARAMETER:
				getModelParameter().clear();
				getModelParameter().addAll((Collection<? extends ModelParameter>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_CLASS:
				getIntermediateClass().clear();
				return;
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_PROPERTY:
				getIntermediateProperty().clear();
				return;
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__MODEL_PARAMETER:
				getModelParameter().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_CLASS:
				return intermediateClass != null && !intermediateClass.isEmpty();
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__INTERMEDIATE_PROPERTY:
				return intermediateProperty != null && !intermediateProperty.isEmpty();
			case ExpressionsPackage.OPERATIONAL_TRANSFORMATION__MODEL_PARAMETER:
				return modelParameter != null && !modelParameter.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //OperationalTransformationImpl
