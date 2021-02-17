/*******************************************************************************
 * Copyright (c) 2009, 2020 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bugs 319078, 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ui.wizards.project;

import org.eclipse.core.runtime.IPath;

public class NewProjectData {	

	private String fName;
	
	private String fClassName;
	
	private boolean fIsPlugin;
	
	private boolean fDoGenerateClass;
	
	private String fSourceFolderName;
	
	private String fQVTSourceFolderName;
	
	private String fOutFolderName;
	
	private boolean fCreateJava;
	
	private String fProviderName;
	
	private String fID;
	
	private String fVersion;
	
	private IPath fLocation;
	
	private String fExecutionEnv;
	
	public NewProjectData() {
		super();
	}
	
	public String getName() {
		return fName;
	}

	public void setName(String name) {
		this.fName = name;
	}

	public String getClassName() {
		return fClassName;
	}

	public void setClassName(String className) {
		this.fClassName = className;
	}

	public boolean isPlugin() {
		return fIsPlugin;
	}

	public void setPlugin(boolean isPlugin) {
		this.fIsPlugin = isPlugin;
	}

	public boolean isDoGenerateClass() {
		return fDoGenerateClass;
	}

	public void setDoGenerateClass(boolean doGenerateClass) {
		this.fDoGenerateClass = doGenerateClass;
	}

	public String getSourceFolderName() {
		return fSourceFolderName;
	}

	public void setSourceFolderName(String sourceFolderName) {
		this.fSourceFolderName = sourceFolderName;
	}

	public String getOutFolderName() {
		return fOutFolderName;
	}

	public void setOutFolderName(String outFolderName) {
		this.fOutFolderName = outFolderName;
	}

	public boolean isCreateJava() {
		return fCreateJava;
	}

	public void setCreateJava(boolean createJava) {
		this.fCreateJava = createJava;
	}
	
	public String getProviderName() {
		return fProviderName;
	}

	public void setProviderName(String providerName) {
		fProviderName = providerName;
	}

	public String getID() {
		return fID;
	}

	public void setID(String fid) {
		fID = fid;
	}

	public String getVersion() {
		return fVersion;
	}

	public void setVersion(String version) {
		fVersion = version;
	}
	
	public String getQVTSourceFolderName() {
		return fQVTSourceFolderName;
	}

	public void setQVTSourceFolderName(String sourceFolderName) {
		fQVTSourceFolderName = sourceFolderName;
	}
	
	public IPath getLocation() {
		return fLocation;
	}

	public void setLocation(IPath location) {
		fLocation = location;
	}

	public String getfExecutionEnv() {
		return fExecutionEnv;
	}

	public void setfExecutionEnv(String executionEnv) {
		fExecutionEnv = executionEnv;
	}
}