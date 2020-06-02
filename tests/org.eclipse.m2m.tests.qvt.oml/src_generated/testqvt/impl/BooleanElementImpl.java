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
package testqvt.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import testqvt.BooleanElement;
import testqvt.TestqvtPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Boolean Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link testqvt.impl.BooleanElementImpl#isIsSmallBoolean <em>Is Small Boolean</em>}</li>
 *   <li>{@link testqvt.impl.BooleanElementImpl#isSmallBoolean <em>Small Boolean</em>}</li>
 *   <li>{@link testqvt.impl.BooleanElementImpl#getIsLargeBoolean <em>Is Large Boolean</em>}</li>
 *   <li>{@link testqvt.impl.BooleanElementImpl#getLargeBoolean <em>Large Boolean</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BooleanElementImpl extends ElementImpl implements BooleanElement {
	/**
	 * The default value of the '{@link #isIsSmallBoolean() <em>Is Small Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsSmallBoolean()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_SMALL_BOOLEAN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsSmallBoolean() <em>Is Small Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsSmallBoolean()
	 * @generated
	 * @ordered
	 */
	protected boolean isSmallBoolean = IS_SMALL_BOOLEAN_EDEFAULT;

	/**
	 * The default value of the '{@link #isSmallBoolean() <em>Small Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSmallBoolean()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SMALL_BOOLEAN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSmallBoolean() <em>Small Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSmallBoolean()
	 * @generated
	 * @ordered
	 */
	protected boolean smallBoolean = SMALL_BOOLEAN_EDEFAULT;

	/**
	 * The default value of the '{@link #getIsLargeBoolean() <em>Is Large Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsLargeBoolean()
	 * @generated
	 * @ordered
	 */
	protected static final Boolean IS_LARGE_BOOLEAN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIsLargeBoolean() <em>Is Large Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIsLargeBoolean()
	 * @generated
	 * @ordered
	 */
	protected Boolean isLargeBoolean = IS_LARGE_BOOLEAN_EDEFAULT;

	/**
	 * The default value of the '{@link #getLargeBoolean() <em>Large Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLargeBoolean()
	 * @generated
	 * @ordered
	 */
	protected static final Boolean LARGE_BOOLEAN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLargeBoolean() <em>Large Boolean</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLargeBoolean()
	 * @generated
	 * @ordered
	 */
	protected Boolean largeBoolean = LARGE_BOOLEAN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BooleanElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestqvtPackage.Literals.BOOLEAN_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsSmallBoolean() {
		return isSmallBoolean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsSmallBoolean(boolean newIsSmallBoolean) {
		boolean oldIsSmallBoolean = isSmallBoolean;
		isSmallBoolean = newIsSmallBoolean;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestqvtPackage.BOOLEAN_ELEMENT__IS_SMALL_BOOLEAN, oldIsSmallBoolean, isSmallBoolean));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSmallBoolean() {
		return smallBoolean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSmallBoolean(boolean newSmallBoolean) {
		boolean oldSmallBoolean = smallBoolean;
		smallBoolean = newSmallBoolean;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestqvtPackage.BOOLEAN_ELEMENT__SMALL_BOOLEAN, oldSmallBoolean, smallBoolean));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Boolean getIsLargeBoolean() {
		return isLargeBoolean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsLargeBoolean(Boolean newIsLargeBoolean) {
		Boolean oldIsLargeBoolean = isLargeBoolean;
		isLargeBoolean = newIsLargeBoolean;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestqvtPackage.BOOLEAN_ELEMENT__IS_LARGE_BOOLEAN, oldIsLargeBoolean, isLargeBoolean));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Boolean getLargeBoolean() {
		return largeBoolean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLargeBoolean(Boolean newLargeBoolean) {
		Boolean oldLargeBoolean = largeBoolean;
		largeBoolean = newLargeBoolean;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestqvtPackage.BOOLEAN_ELEMENT__LARGE_BOOLEAN, oldLargeBoolean, largeBoolean));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_SMALL_BOOLEAN:
				return isIsSmallBoolean();
			case TestqvtPackage.BOOLEAN_ELEMENT__SMALL_BOOLEAN:
				return isSmallBoolean();
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_LARGE_BOOLEAN:
				return getIsLargeBoolean();
			case TestqvtPackage.BOOLEAN_ELEMENT__LARGE_BOOLEAN:
				return getLargeBoolean();
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
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_SMALL_BOOLEAN:
				setIsSmallBoolean((Boolean)newValue);
				return;
			case TestqvtPackage.BOOLEAN_ELEMENT__SMALL_BOOLEAN:
				setSmallBoolean((Boolean)newValue);
				return;
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_LARGE_BOOLEAN:
				setIsLargeBoolean((Boolean)newValue);
				return;
			case TestqvtPackage.BOOLEAN_ELEMENT__LARGE_BOOLEAN:
				setLargeBoolean((Boolean)newValue);
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
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_SMALL_BOOLEAN:
				setIsSmallBoolean(IS_SMALL_BOOLEAN_EDEFAULT);
				return;
			case TestqvtPackage.BOOLEAN_ELEMENT__SMALL_BOOLEAN:
				setSmallBoolean(SMALL_BOOLEAN_EDEFAULT);
				return;
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_LARGE_BOOLEAN:
				setIsLargeBoolean(IS_LARGE_BOOLEAN_EDEFAULT);
				return;
			case TestqvtPackage.BOOLEAN_ELEMENT__LARGE_BOOLEAN:
				setLargeBoolean(LARGE_BOOLEAN_EDEFAULT);
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
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_SMALL_BOOLEAN:
				return isSmallBoolean != IS_SMALL_BOOLEAN_EDEFAULT;
			case TestqvtPackage.BOOLEAN_ELEMENT__SMALL_BOOLEAN:
				return smallBoolean != SMALL_BOOLEAN_EDEFAULT;
			case TestqvtPackage.BOOLEAN_ELEMENT__IS_LARGE_BOOLEAN:
				return IS_LARGE_BOOLEAN_EDEFAULT == null ? isLargeBoolean != null : !IS_LARGE_BOOLEAN_EDEFAULT.equals(isLargeBoolean);
			case TestqvtPackage.BOOLEAN_ELEMENT__LARGE_BOOLEAN:
				return LARGE_BOOLEAN_EDEFAULT == null ? largeBoolean != null : !LARGE_BOOLEAN_EDEFAULT.equals(largeBoolean);
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
		result.append(" (isSmallBoolean: ");
		result.append(isSmallBoolean);
		result.append(", smallBoolean: ");
		result.append(smallBoolean);
		result.append(", isLargeBoolean: ");
		result.append(isLargeBoolean);
		result.append(", largeBoolean: ");
		result.append(largeBoolean);
		result.append(')');
		return result.toString();
	}

} //BooleanElementImpl
