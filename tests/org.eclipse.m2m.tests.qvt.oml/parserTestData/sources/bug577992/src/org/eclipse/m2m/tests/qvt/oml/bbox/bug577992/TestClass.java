/**
 */
package org.eclipse.m2m.tests.qvt.oml.bbox.bug577992;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass#getAttr <em>Attr</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.Bug577992Package#getTestClass()
 * @model
 * @generated
 */
public interface TestClass extends EObject {
	/**
	 * Returns the value of the '<em><b>Attr</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attr</em>' attribute.
	 * @see #setAttr(String)
	 * @see org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.Bug577992Package#getTestClass_Attr()
	 * @model
	 * @generated
	 */
	String getAttr();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.tests.qvt.oml.bbox.bug577992.TestClass#getAttr <em>Attr</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attr</em>' attribute.
	 * @see #getAttr()
	 * @generated
	 */
	void setAttr(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model parRequired="true"
	 * @generated
	 */
	TestClass op(TestClass par);

} // TestClass
