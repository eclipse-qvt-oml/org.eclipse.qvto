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
 * $Id: ModelTypeImpl.java,v 1.8 2009/01/30 19:19:28 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.VisitableASTNode;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.utilities.ASTNode;
import org.eclipse.ocl.utilities.UtilitiesPackage;
import org.eclipse.ocl.utilities.Visitable;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModelTypeImpl#getStartPosition <em>Start Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModelTypeImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModelTypeImpl#getAdditionalCondition <em>Additional Condition</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModelTypeImpl#getConformanceKind <em>Conformance Kind</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ModelTypeImpl#getMetamodel <em>Metamodel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelTypeImpl extends EClassImpl implements ModelType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * The default value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartPosition()
	 * @generated
	 * @ordered
	 */
	protected static final int START_POSITION_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getStartPosition() <em>Start Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartPosition()
	 * @generated
	 * @ordered
	 */
	protected int startPosition = START_POSITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getEndPosition() <em>End Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPosition()
	 * @generated
	 * @ordered
	 */
	protected static final int END_POSITION_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getEndPosition() <em>End Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPosition()
	 * @generated
	 * @ordered
	 */
	protected int endPosition = END_POSITION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAdditionalCondition() <em>Additional Condition</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalCondition()
	 * @generated
	 * @ordered
	 */
	protected EList<OCLExpression> additionalCondition;

	/**
	 * The default value of the '{@link #getConformanceKind() <em>Conformance Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConformanceKind()
	 * @generated
	 * @ordered
	 */
	protected static final String CONFORMANCE_KIND_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConformanceKind() <em>Conformance Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConformanceKind()
	 * @generated
	 * @ordered
	 */
	protected String conformanceKind = CONFORMANCE_KIND_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMetamodel() <em>Metamodel</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMetamodel()
	 * @generated
	 * @ordered
	 */
	protected EList<EPackage> metamodel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.MODEL_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStartPosition() {
		return startPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartPosition(int newStartPosition) {
		int oldStartPosition = startPosition;
		startPosition = newStartPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODEL_TYPE__START_POSITION, oldStartPosition, startPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getEndPosition() {
		return endPosition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndPosition(int newEndPosition) {
		int oldEndPosition = endPosition;
		endPosition = newEndPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODEL_TYPE__END_POSITION, oldEndPosition, endPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EPackage> getMetamodel() {
		if (metamodel == null) {
			metamodel = new EObjectResolvingEList<EPackage>(EPackage.class, this, ExpressionsPackage.MODEL_TYPE__METAMODEL);
		}
		return metamodel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OCLExpression> getAdditionalCondition() {
		if (additionalCondition == null) {
			additionalCondition = new EObjectContainmentEList<OCLExpression>(OCLExpression.class, this, ExpressionsPackage.MODEL_TYPE__ADDITIONAL_CONDITION);
		}
		return additionalCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getConformanceKind() {
		return conformanceKind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConformanceKind(String newConformanceKind) {
		String oldConformanceKind = conformanceKind;
		conformanceKind = newConformanceKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MODEL_TYPE__CONFORMANCE_KIND, oldConformanceKind, conformanceKind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(final U v) {
		if(v instanceof org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) {
		  @SuppressWarnings("unchecked")    		
		  org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor<T> visitorExt = (org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) v;    	
		  return visitorExt.visitModelType(this);
		}
		return org.eclipse.m2m.internal.qvt.oml.expressions.util.ForeignVisitorDefaultValue.getDefaultValueForVisitor(v);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExpressionsPackage.MODEL_TYPE__ADDITIONAL_CONDITION:
				return ((InternalEList<?>)getAdditionalCondition()).basicRemove(otherEnd, msgs);
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
			case ExpressionsPackage.MODEL_TYPE__START_POSITION:
				return getStartPosition();
			case ExpressionsPackage.MODEL_TYPE__END_POSITION:
				return getEndPosition();
			case ExpressionsPackage.MODEL_TYPE__ADDITIONAL_CONDITION:
				return getAdditionalCondition();
			case ExpressionsPackage.MODEL_TYPE__CONFORMANCE_KIND:
				return getConformanceKind();
			case ExpressionsPackage.MODEL_TYPE__METAMODEL:
				return getMetamodel();
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
			case ExpressionsPackage.MODEL_TYPE__START_POSITION:
				setStartPosition((Integer)newValue);
				return;
			case ExpressionsPackage.MODEL_TYPE__END_POSITION:
				setEndPosition((Integer)newValue);
				return;
			case ExpressionsPackage.MODEL_TYPE__ADDITIONAL_CONDITION:
				getAdditionalCondition().clear();
				getAdditionalCondition().addAll((Collection<? extends OCLExpression>)newValue);
				return;
			case ExpressionsPackage.MODEL_TYPE__CONFORMANCE_KIND:
				setConformanceKind((String)newValue);
				return;
			case ExpressionsPackage.MODEL_TYPE__METAMODEL:
				getMetamodel().clear();
				getMetamodel().addAll((Collection<? extends EPackage>)newValue);
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
			case ExpressionsPackage.MODEL_TYPE__START_POSITION:
				setStartPosition(START_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.MODEL_TYPE__END_POSITION:
				setEndPosition(END_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.MODEL_TYPE__ADDITIONAL_CONDITION:
				getAdditionalCondition().clear();
				return;
			case ExpressionsPackage.MODEL_TYPE__CONFORMANCE_KIND:
				setConformanceKind(CONFORMANCE_KIND_EDEFAULT);
				return;
			case ExpressionsPackage.MODEL_TYPE__METAMODEL:
				getMetamodel().clear();
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
			case ExpressionsPackage.MODEL_TYPE__START_POSITION:
				return startPosition != START_POSITION_EDEFAULT;
			case ExpressionsPackage.MODEL_TYPE__END_POSITION:
				return endPosition != END_POSITION_EDEFAULT;
			case ExpressionsPackage.MODEL_TYPE__ADDITIONAL_CONDITION:
				return additionalCondition != null && !additionalCondition.isEmpty();
			case ExpressionsPackage.MODEL_TYPE__CONFORMANCE_KIND:
				return CONFORMANCE_KIND_EDEFAULT == null ? conformanceKind != null : !CONFORMANCE_KIND_EDEFAULT.equals(conformanceKind);
			case ExpressionsPackage.MODEL_TYPE__METAMODEL:
				return metamodel != null && !metamodel.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == Visitable.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ASTNode.class) {
			switch (derivedFeatureID) {
				case ExpressionsPackage.MODEL_TYPE__START_POSITION: return UtilitiesPackage.AST_NODE__START_POSITION;
				case ExpressionsPackage.MODEL_TYPE__END_POSITION: return UtilitiesPackage.AST_NODE__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == VisitableASTNode.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == Visitable.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ASTNode.class) {
			switch (baseFeatureID) {
				case UtilitiesPackage.AST_NODE__START_POSITION: return ExpressionsPackage.MODEL_TYPE__START_POSITION;
				case UtilitiesPackage.AST_NODE__END_POSITION: return ExpressionsPackage.MODEL_TYPE__END_POSITION;
				default: return -1;
			}
		}
		if (baseClass == VisitableASTNode.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (startPosition: "); //$NON-NLS-1$
		result.append(startPosition);
		result.append(", endPosition: "); //$NON-NLS-1$
		result.append(endPosition);
		result.append(", conformanceKind: "); //$NON-NLS-1$
		result.append(conformanceKind);
		result.append(')');
		return result.toString();
	}

} //ModelTypeImpl
