/**
 * <copyright>
 * </copyright>
 *
 * $Id: NumbersImpl.java,v 1.1 2008, 2019/11/03 10:05:39 radvorak Exp $
 */
package testqvt.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import testqvt.Numbers;
import testqvt.TestqvtPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Numbers</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link testqvt.impl.NumbersImpl#getBigInteger100 <em>Big Integer100</em>}</li>
 *   <li>{@link testqvt.impl.NumbersImpl#getBigDecimal100 <em>Big Decimal100</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NumbersImpl extends EObjectImpl implements Numbers {
	/**
	 * The default value of the '{@link #getBigInteger100() <em>Big Integer100</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBigInteger100()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger BIG_INTEGER100_EDEFAULT = new BigInteger("100");

	/**
	 * The cached value of the '{@link #getBigInteger100() <em>Big Integer100</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBigInteger100()
	 * @generated
	 * @ordered
	 */
	protected BigInteger bigInteger100 = BIG_INTEGER100_EDEFAULT;

	/**
	 * The default value of the '{@link #getBigDecimal100() <em>Big Decimal100</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBigDecimal100()
	 * @generated
	 * @ordered
	 */
	protected static final BigDecimal BIG_DECIMAL100_EDEFAULT = new BigDecimal("100");

	/**
	 * The cached value of the '{@link #getBigDecimal100() <em>Big Decimal100</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBigDecimal100()
	 * @generated
	 * @ordered
	 */
	protected BigDecimal bigDecimal100 = BIG_DECIMAL100_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NumbersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TestqvtPackage.Literals.NUMBERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getBigInteger100() {
		return bigInteger100;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBigInteger100(BigInteger newBigInteger100) {
		BigInteger oldBigInteger100 = bigInteger100;
		bigInteger100 = newBigInteger100;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestqvtPackage.NUMBERS__BIG_INTEGER100, oldBigInteger100, bigInteger100));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigDecimal getBigDecimal100() {
		return bigDecimal100;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBigDecimal100(BigDecimal newBigDecimal100) {
		BigDecimal oldBigDecimal100 = bigDecimal100;
		bigDecimal100 = newBigDecimal100;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TestqvtPackage.NUMBERS__BIG_DECIMAL100, oldBigDecimal100, bigDecimal100));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TestqvtPackage.NUMBERS__BIG_INTEGER100:
				return getBigInteger100();
			case TestqvtPackage.NUMBERS__BIG_DECIMAL100:
				return getBigDecimal100();
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
			case TestqvtPackage.NUMBERS__BIG_INTEGER100:
				setBigInteger100((BigInteger)newValue);
				return;
			case TestqvtPackage.NUMBERS__BIG_DECIMAL100:
				setBigDecimal100((BigDecimal)newValue);
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
			case TestqvtPackage.NUMBERS__BIG_INTEGER100:
				setBigInteger100(BIG_INTEGER100_EDEFAULT);
				return;
			case TestqvtPackage.NUMBERS__BIG_DECIMAL100:
				setBigDecimal100(BIG_DECIMAL100_EDEFAULT);
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
			case TestqvtPackage.NUMBERS__BIG_INTEGER100:
				return BIG_INTEGER100_EDEFAULT == null ? bigInteger100 != null : !BIG_INTEGER100_EDEFAULT.equals(bigInteger100);
			case TestqvtPackage.NUMBERS__BIG_DECIMAL100:
				return BIG_DECIMAL100_EDEFAULT == null ? bigDecimal100 != null : !BIG_DECIMAL100_EDEFAULT.equals(bigDecimal100);
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
		result.append(" (bigInteger100: ");
		result.append(bigInteger100);
		result.append(", bigDecimal100: ");
		result.append(bigDecimal100);
		result.append(')');
		return result.toString();
	}

} //NumbersImpl
