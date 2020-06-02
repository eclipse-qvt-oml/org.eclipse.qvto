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
 * $Id: ContextualPropertyImpl.java,v 1.8 2009/03/15 11:44:40 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl;
import org.eclipse.m2m.internal.qvt.oml.expressions.ContextualProperty;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.VisitableASTNode;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.utilities.ASTNode;
import org.eclipse.ocl.utilities.UtilitiesPackage;
import org.eclipse.ocl.utilities.Visitable;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contextual Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ContextualPropertyImpl#getStartPosition <em>Start Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ContextualPropertyImpl#getEndPosition <em>End Position</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ContextualPropertyImpl#getContext <em>Context</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ContextualPropertyImpl#getInitExpression <em>Init Expression</em>}</li>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.ContextualPropertyImpl#getOverridden <em>Overridden</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContextualPropertyImpl extends EStructuralFeatureImpl implements ContextualProperty {
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
	 * The cached value of the '{@link #getContext() <em>Context</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected EClass context;

	/**
	 * The cached value of the '{@link #getInitExpression() <em>Init Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitExpression()
	 * @generated
	 * @ordered
	 */
	protected OCLExpression initExpression;

	/**
	 * The cached value of the '{@link #getOverridden() <em>Overridden</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOverridden()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature overridden;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContextualPropertyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.CONTEXTUAL_PROPERTY;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONTEXTUAL_PROPERTY__START_POSITION, oldStartPosition, startPosition));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONTEXTUAL_PROPERTY__END_POSITION, oldEndPosition, endPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getContext() {
		if (context != null && context.eIsProxy()) {
			InternalEObject oldContext = (InternalEObject)context;
			context = (EClass)eResolveProxy(oldContext);
			if (context != oldContext) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.CONTEXTUAL_PROPERTY__CONTEXT, oldContext, context));
			}
		}
		return context;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass basicGetContext() {
		return context;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContext(EClass newContext) {
		EClass oldContext = context;
		context = newContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONTEXTUAL_PROPERTY__CONTEXT, oldContext, context));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature getOverridden() {
		if (overridden != null && overridden.eIsProxy()) {
			InternalEObject oldOverridden = (InternalEObject)overridden;
			overridden = (EStructuralFeature)eResolveProxy(oldOverridden);
			if (overridden != oldOverridden) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExpressionsPackage.CONTEXTUAL_PROPERTY__OVERRIDDEN, oldOverridden, overridden));
			}
		}
		return overridden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature basicGetOverridden() {
		return overridden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOverridden(EStructuralFeature newOverridden) {
		EStructuralFeature oldOverridden = overridden;
		overridden = newOverridden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONTEXTUAL_PROPERTY__OVERRIDDEN, oldOverridden, overridden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OCLExpression getInitExpression() {
		return initExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInitExpression(OCLExpression newInitExpression, NotificationChain msgs) {
		OCLExpression oldInitExpression = initExpression;
		initExpression = newInitExpression;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION, oldInitExpression, newInitExpression);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitExpression(OCLExpression newInitExpression) {
		if (newInitExpression != initExpression) {
			NotificationChain msgs = null;
			if (initExpression != null)
				msgs = ((InternalEObject)initExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION, null, msgs);
			if (newInitExpression != null)
				msgs = ((InternalEObject)newInitExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION, null, msgs);
			msgs = basicSetInitExpression(newInitExpression, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION, newInitExpression, newInitExpression));
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
		  return visitorExt.visitContextualProperty(this);
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
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION:
				return basicSetInitExpression(null, msgs);
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
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__START_POSITION:
				return getStartPosition();
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__END_POSITION:
				return getEndPosition();
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__CONTEXT:
				if (resolve) return getContext();
				return basicGetContext();
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION:
				return getInitExpression();
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__OVERRIDDEN:
				if (resolve) return getOverridden();
				return basicGetOverridden();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__START_POSITION:
				setStartPosition((Integer)newValue);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__END_POSITION:
				setEndPosition((Integer)newValue);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__CONTEXT:
				setContext((EClass)newValue);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION:
				setInitExpression((OCLExpression)newValue);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__OVERRIDDEN:
				setOverridden((EStructuralFeature)newValue);
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
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__START_POSITION:
				setStartPosition(START_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__END_POSITION:
				setEndPosition(END_POSITION_EDEFAULT);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__CONTEXT:
				setContext((EClass)null);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION:
				setInitExpression((OCLExpression)null);
				return;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__OVERRIDDEN:
				setOverridden((EStructuralFeature)null);
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
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__START_POSITION:
				return startPosition != START_POSITION_EDEFAULT;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__END_POSITION:
				return endPosition != END_POSITION_EDEFAULT;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__CONTEXT:
				return context != null;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__INIT_EXPRESSION:
				return initExpression != null;
			case ExpressionsPackage.CONTEXTUAL_PROPERTY__OVERRIDDEN:
				return overridden != null;
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
				case ExpressionsPackage.CONTEXTUAL_PROPERTY__START_POSITION: return UtilitiesPackage.AST_NODE__START_POSITION;
				case ExpressionsPackage.CONTEXTUAL_PROPERTY__END_POSITION: return UtilitiesPackage.AST_NODE__END_POSITION;
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
				case UtilitiesPackage.AST_NODE__START_POSITION: return ExpressionsPackage.CONTEXTUAL_PROPERTY__START_POSITION;
				case UtilitiesPackage.AST_NODE__END_POSITION: return ExpressionsPackage.CONTEXTUAL_PROPERTY__END_POSITION;
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
		result.append(')');
		return result.toString();
	}

} //ContextualPropertyImpl
