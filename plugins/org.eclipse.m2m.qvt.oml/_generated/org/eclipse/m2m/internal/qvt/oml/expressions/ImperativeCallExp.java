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
 * $Id: ImperativeCallExp.java,v 1.2 2009/01/28 11:47:26 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions;

import org.eclipse.m2m.qvt.oml.ecore.ImperativeOCL.ImperativeExpression;
import org.eclipse.ocl.ecore.OperationCallExp;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Imperative Call Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeCallExp#isIsVirtual <em>Is Virtual</em>}</li>
 * </ul>
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeCallExp()
 * @model
 * @generated
 */
public interface ImperativeCallExp extends OperationCallExp, ImperativeExpression {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * Returns the value of the '<em><b>Is Virtual</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Virtual</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Virtual</em>' attribute.
	 * @see #setIsVirtual(boolean)
	 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getImperativeCallExp_IsVirtual()
	 * @model default="true"
	 * @generated
	 */
	boolean isIsVirtual();

	/**
	 * Sets the value of the '{@link org.eclipse.m2m.internal.qvt.oml.expressions.ImperativeCallExp#isIsVirtual <em>Is Virtual</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Virtual</em>' attribute.
	 * @see #isIsVirtual()
	 * @generated
	 */
	void setIsVirtual(boolean value);

} // ImperativeCallExp
