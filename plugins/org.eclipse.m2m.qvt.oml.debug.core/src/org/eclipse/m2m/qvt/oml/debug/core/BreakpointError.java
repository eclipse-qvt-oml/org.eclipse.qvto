/*******************************************************************************
 * Copyright (c) 2009 Eclipse Modeling Project and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Radek Dvorak - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.qvt.oml.debug.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class BreakpointError extends Status {
	
	public static final int BREAKPOINT_CONDITION_ERR = 200;
	
	private long fBreakpointID;
	
	BreakpointError(long breakpointID, String message) {
		super(IStatus.ERROR, QVTODebugCore.PLUGIN_ID, BREAKPOINT_CONDITION_ERR, message, null);
		
		fBreakpointID = breakpointID;
	}
	
	public long getfBreakpointID() {
		return fBreakpointID;
	}
}
