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
 * $Id: ConstructorImpl.java,v 1.2 2009/03/15 11:44:40 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.m2m.internal.qvt.oml.expressions.Constructor;
import org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage;
import org.eclipse.ocl.utilities.Visitor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constructor</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ConstructorImpl extends ImperativeOperationImpl implements Constructor {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConstructorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExpressionsPackage.Literals.CONSTRUCTOR;
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
		  return visitorExt.visitConstructor(this);
		}
		return org.eclipse.m2m.internal.qvt.oml.expressions.util.ForeignVisitorDefaultValue.getDefaultValueForVisitor(v);
	}

} //ConstructorImpl
