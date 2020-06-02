/*******************************************************************************
 * Copyright (c) 2007, 2019 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.expressions.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingCallExp;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping Call Exp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.impl.MappingCallExpImpl#isIsStrict <em>Is Strict</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MappingCallExpImpl extends ImperativeCallExpImpl implements MappingCallExp {
	/**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

    /**
	 * The default value of the '{@link #isIsStrict() <em>Is Strict</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStrict()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_STRICT_EDEFAULT = false;

	/**
	 * The flag representing the value of the '{@link #isIsStrict() <em>Is Strict</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStrict()
	 * @generated
	 * @ordered
	 */
	protected static final int IS_STRICT_EFLAG = 1 << 11;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingCallExpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.MAPPING_CALL_EXP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsStrict() {
		return (eFlags & IS_STRICT_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsStrict(boolean newIsStrict) {
		boolean oldIsStrict = (eFlags & IS_STRICT_EFLAG) != 0;
		if (newIsStrict) eFlags |= IS_STRICT_EFLAG; else eFlags &= ~IS_STRICT_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExpressionsPackage.MAPPING_CALL_EXP__IS_STRICT, oldIsStrict, newIsStrict));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(final U v) {
		if(v instanceof org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) {
		  @SuppressWarnings("unchecked")    		
		  org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor<T> visitorExt = (org.eclipse.m2m.internal.qvt.oml.expressions.util.QVTOperationalVisitor) v;    	
		  return visitorExt.visitMappingCallExp(this);
		}
		return org.eclipse.m2m.internal.qvt.oml.expressions.util.ForeignVisitorDefaultValue.getDefaultValueForVisitor(v);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExpressionsPackage.MAPPING_CALL_EXP__IS_STRICT:
				return isIsStrict();
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
			case ExpressionsPackage.MAPPING_CALL_EXP__IS_STRICT:
				setIsStrict((Boolean)newValue);
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
			case ExpressionsPackage.MAPPING_CALL_EXP__IS_STRICT:
				setIsStrict(IS_STRICT_EDEFAULT);
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
			case ExpressionsPackage.MAPPING_CALL_EXP__IS_STRICT:
				return ((eFlags & IS_STRICT_EFLAG) != 0) != IS_STRICT_EDEFAULT;
		}
		return super.eIsSet(featureID);
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
		result.append(" (isStrict: "); //$NON-NLS-1$
		result.append((eFlags & IS_STRICT_EFLAG) != 0);
		result.append(')');
		return result.toString();
	}

} //MappingCallExpImpl
