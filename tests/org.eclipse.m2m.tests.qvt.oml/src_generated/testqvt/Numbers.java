/**
 * <copyright>
 * </copyright>
 *
 * $Id: Numbers.java,v 1.1 2008, 2019/11/03 10:05:39 radvorak Exp $
 */
package testqvt;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Numbers</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link testqvt.Numbers#getBigInteger100 <em>Big Integer100</em>}</li>
 *   <li>{@link testqvt.Numbers#getBigDecimal100 <em>Big Decimal100</em>}</li>
 * </ul>
 *
 * @see testqvt.TestqvtPackage#getNumbers()
 * @model
 * @generated
 */
public interface Numbers extends EObject {
	/**
	 * Returns the value of the '<em><b>Big Integer100</b></em>' attribute.
	 * The default value is <code>"100"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Big Integer100</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Big Integer100</em>' attribute.
	 * @see #setBigInteger100(BigInteger)
	 * @see testqvt.TestqvtPackage#getNumbers_BigInteger100()
	 * @model default="100"
	 * @generated
	 */
	BigInteger getBigInteger100();

	/**
	 * Sets the value of the '{@link testqvt.Numbers#getBigInteger100 <em>Big Integer100</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Big Integer100</em>' attribute.
	 * @see #getBigInteger100()
	 * @generated
	 */
	void setBigInteger100(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Big Decimal100</b></em>' attribute.
	 * The default value is <code>"100"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Big Decimal100</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Big Decimal100</em>' attribute.
	 * @see #setBigDecimal100(BigDecimal)
	 * @see testqvt.TestqvtPackage#getNumbers_BigDecimal100()
	 * @model default="100"
	 * @generated
	 */
	BigDecimal getBigDecimal100();

	/**
	 * Sets the value of the '{@link testqvt.Numbers#getBigDecimal100 <em>Big Decimal100</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Big Decimal100</em>' attribute.
	 * @see #getBigDecimal100()
	 * @generated
	 */
	void setBigDecimal100(BigDecimal value);

} // Numbers
