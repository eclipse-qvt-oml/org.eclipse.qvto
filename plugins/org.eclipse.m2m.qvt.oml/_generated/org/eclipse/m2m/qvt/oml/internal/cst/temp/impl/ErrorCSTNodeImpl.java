/**
 * Copyright (c) 2007 Borland Software Corporation
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 * 
 * 
 *
 * $Id: ErrorCSTNodeImpl.java,v 1.3.2.1 2007/12/02 22:34:05 radvorak Exp $
 */
package org.eclipse.m2m.qvt.oml.internal.cst.temp.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.m2m.qvt.oml.internal.cst.temp.ErrorCSTNode;
import org.eclipse.m2m.qvt.oml.internal.cst.temp.TempPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Error CST Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.m2m.qvt.oml.internal.cst.temp.impl.ErrorCSTNodeImpl#getFullStartOffset <em>Full Start Offset</em>}</li>
 *   <li>{@link org.eclipse.m2m.qvt.oml.internal.cst.temp.impl.ErrorCSTNodeImpl#getFullEndOffset <em>Full End Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ErrorCSTNodeImpl extends EObjectImpl implements ErrorCSTNode {
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v1.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v10.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation\r\n\r\n"; //$NON-NLS-1$

    /**
	 * The default value of the '{@link #getFullStartOffset() <em>Full Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFullStartOffset()
	 * @generated
	 * @ordered
	 */
    protected static final int FULL_START_OFFSET_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getFullStartOffset() <em>Full Start Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFullStartOffset()
	 * @generated
	 * @ordered
	 */
    protected int fullStartOffset = FULL_START_OFFSET_EDEFAULT;

    /**
	 * The default value of the '{@link #getFullEndOffset() <em>Full End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFullEndOffset()
	 * @generated
	 * @ordered
	 */
    protected static final int FULL_END_OFFSET_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getFullEndOffset() <em>Full End Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #getFullEndOffset()
	 * @generated
	 * @ordered
	 */
    protected int fullEndOffset = FULL_END_OFFSET_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ErrorCSTNodeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
		return TempPackage.Literals.ERROR_CST_NODE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public int getFullStartOffset() {
		return fullStartOffset;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFullStartOffset(int newFullStartOffset) {
		int oldFullStartOffset = fullStartOffset;
		fullStartOffset = newFullStartOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TempPackage.ERROR_CST_NODE__FULL_START_OFFSET, oldFullStartOffset, fullStartOffset));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public int getFullEndOffset() {
		return fullEndOffset;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFullEndOffset(int newFullEndOffset) {
		int oldFullEndOffset = fullEndOffset;
		fullEndOffset = newFullEndOffset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TempPackage.ERROR_CST_NODE__FULL_END_OFFSET, oldFullEndOffset, fullEndOffset));
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TempPackage.ERROR_CST_NODE__FULL_START_OFFSET:
				return new Integer(getFullStartOffset());
			case TempPackage.ERROR_CST_NODE__FULL_END_OFFSET:
				return new Integer(getFullEndOffset());
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
			case TempPackage.ERROR_CST_NODE__FULL_START_OFFSET:
				setFullStartOffset(((Integer)newValue).intValue());
				return;
			case TempPackage.ERROR_CST_NODE__FULL_END_OFFSET:
				setFullEndOffset(((Integer)newValue).intValue());
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
			case TempPackage.ERROR_CST_NODE__FULL_START_OFFSET:
				setFullStartOffset(FULL_START_OFFSET_EDEFAULT);
				return;
			case TempPackage.ERROR_CST_NODE__FULL_END_OFFSET:
				setFullEndOffset(FULL_END_OFFSET_EDEFAULT);
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
			case TempPackage.ERROR_CST_NODE__FULL_START_OFFSET:
				return fullStartOffset != FULL_START_OFFSET_EDEFAULT;
			case TempPackage.ERROR_CST_NODE__FULL_END_OFFSET:
				return fullEndOffset != FULL_END_OFFSET_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (fullStartOffset: "); //$NON-NLS-1$
		result.append(fullStartOffset);
		result.append(", fullEndOffset: "); //$NON-NLS-1$
		result.append(fullEndOffset);
		result.append(')');
		return result.toString();
	}

} //ErrorCSTNodeImpl
