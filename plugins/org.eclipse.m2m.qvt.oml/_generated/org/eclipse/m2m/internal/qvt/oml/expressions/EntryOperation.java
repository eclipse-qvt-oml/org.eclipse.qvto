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
 * $Id: EntryOperation.java,v 1.3 2009/01/28 11:47:26 radvorak Exp $
 */
package org.eclipse.m2m.internal.qvt.oml.expressions;

import org.eclipse.ocl.utilities.Visitor;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entry Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.m2m.internal.qvt.oml.expressions.ExpressionsPackage#getEntryOperation()
 * @model
 * @generated
 */
public interface EntryOperation extends ImperativeOperation {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String copyright = "Copyright (c) 2007 Borland Software Corporation\r\n\r\nAll rights reserved. This program and the accompanying materials\r\nare made available under the terms of the Eclipse Public License v2.0\r\nwhich accompanies this distribution, and is available at\r\nhttp://www.eclipse.org/legal/epl-v20.html\r\n  \r\nContributors:\r\n    Borland Software Corporation - initial API and implementation"; //$NON-NLS-1$

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	<T, U extends Visitor<T, ?, ?, ?, ?, ?, ?, ?, ?, ?>> T accept(U v);

} // EntryOperation
