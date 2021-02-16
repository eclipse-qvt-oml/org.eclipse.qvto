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
 *     Christopher Gerking - bug 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.jdt.ui.wizard.project;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstall2;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jdt.launching.environments.IExecutionEnvironmentsManager;
import org.eclipse.jface.util.Policy;
import org.eclipse.m2m.internal.qvt.oml.jdt.debug.ui.launch.DebugPDEMessages;
import org.eclipse.m2m.internal.qvt.oml.ui.wizards.project.NewProjectData;
import org.eclipse.pde.internal.core.ClasspathComputer;
import org.eclipse.pde.internal.core.util.CoreUtility;
import org.eclipse.swt.widgets.Combo;

@SuppressWarnings("restriction")
public class JdtProjectIntegrationImpl extends PdeProjectIntegrationImpl {

	private static Map<String, Integer> fSeverityTable;
	private static final int SEVERITY_ERROR = 3;
	private static final int SEVERITY_WARNING = 2;
	private static final int SEVERITY_IGNORE = 1;

	static {
		fSeverityTable = new HashMap<String, Integer>(3);
		fSeverityTable.put(JavaCore.IGNORE, Integer.valueOf(SEVERITY_IGNORE));
		fSeverityTable.put(JavaCore.WARNING, Integer.valueOf(SEVERITY_WARNING));
		fSeverityTable.put(JavaCore.ERROR, Integer.valueOf(SEVERITY_ERROR));
	}

	public void setupProject(IProject project, NewProjectData data, IProgressMonitor monitor) throws CoreException {
		
		try {
			SubMonitor subMonitor = SubMonitor.convert(monitor, 6);
			
			super.setupProject(project, data, subMonitor.split(1));
			
			if (data.isCreateJava()) {
				CoreUtility.addNatureToProject(project, JavaCore.NATURE_ID, subMonitor.split(1));
		
				IContainer srcContainer = getFolder(project, data.getSourceFolderName(), subMonitor.split(1));
				IContainer binContainer = getFolder(project, data.getOutFolderName(), subMonitor.split(1));
		
				IJavaProject javaProject = JavaCore.create(project);
				javaProject.setOutputLocation(binContainer.getFullPath(), subMonitor.split(1));
		
				subMonitor.subTask(DebugPDEMessages.Setup_SettingClasspath);
		
				IClasspathEntry[] entries = new IClasspathEntry[data.isPlugin() ? 3 : 1];
				if (data.isPlugin()) {
					String executionEnvironment = data.getfExecutionEnv();
					ClasspathComputer.setComplianceOptions(javaProject, executionEnvironment, true);
					entries[0] = ClasspathComputer.createJREEntry(executionEnvironment);
					entries[1] = ClasspathComputer.createContainerEntry();
				}
		
				entries[entries.length - 1] = JavaCore.newSourceEntry(srcContainer.getFullPath());
				javaProject.setRawClasspath(entries, subMonitor.split(1));
				
				if(data.isDoGenerateClass()) {
					generateTopLevelPluginClass(subMonitor.split(1));
				}
			}
			
		} finally {
			SubMonitor.done(monitor);
		}
	}

	public String getRequiredExecutionEnv(String executionEnv) {
		if (getEEnv(executionEnv) == null) {
			return null;
		}
		return executionEnv;
	}

	private static IExecutionEnvironment getEEnv(String ee) {
		if (ee != null) {
			IExecutionEnvironmentsManager manager = JavaRuntime.getExecutionEnvironmentsManager();
			return manager.getEnvironment(ee);
		}
		return null;
	}
	
	protected void createFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
		org.eclipse.jdt.internal.ui.util.CoreUtility.createFolder(folder, true, true, monitor);
	}

	public String getClassField(String id, String suffix) {
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < id.length(); i++) {
			char ch = id.charAt(i);
			if (buffer.length() == 0) {
				if (Character.isJavaIdentifierStart(ch)) {
					buffer.append(Character.toLowerCase(ch));
				}
			} else {
				if (Character.isJavaIdentifierPart(ch)) {
					buffer.append(ch);
				}
				else if (ch == '.'){
					IStatus status = validatePackageName(buffer.toString());
					if (status.getSeverity() == IStatus.ERROR) {
						buffer.append(suffix.toLowerCase());
					}
					buffer.append(ch);
				}
			}
		}

		StringTokenizer tok = new StringTokenizer(buffer.toString(), "."); //$NON-NLS-1$
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (!tok.hasMoreTokens()){
				IStatus status = validatePackageName(buffer.toString());
				if (status.getSeverity() == IStatus.ERROR) {
					buffer.append(suffix.toLowerCase());
				}
				buffer.append("." + Character.toUpperCase(token.charAt(0)) + token.substring(1) + suffix); //$NON-NLS-1$
			}
		}

		return buffer.toString();
	}

	public IStatus validateJavaTypeName(String name) {
		return JavaConventions.validateJavaTypeName(name.trim(), JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);
	}

	public void fillExecutionEnvironments(Combo combo) {
		IExecutionEnvironment[] fInstalledEEs = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
		Arrays.sort(fInstalledEEs, new java.util.Comparator<IExecutionEnvironment>() {
			public int compare(IExecutionEnvironment arg0, IExecutionEnvironment arg1) {
				return Policy.getComparator().compare(arg0.getId(), arg1.getId());
			}
		});

		String[] eeLabels = new String[fInstalledEEs.length];
		for (int i = 0; i < fInstalledEEs.length; i++) {
			eeLabels[i] = fInstalledEEs[i].getId();
		}
		combo.setItems(eeLabels);
		combo.setText(getDefaultEEName());
	}

	private static IStatus validatePackageName(String name) {
		return JavaConventions.validatePackageName(name, JavaCore.VERSION_1_3, JavaCore.VERSION_1_3);
	}

	private static String getDefaultEEName() {
		IVMInstall defaultVM = JavaRuntime.getDefaultVMInstall();

		IExecutionEnvironment[] environments = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
		if (defaultVM != null) {
			for (int i = 0; i < environments.length; i++) {
				IVMInstall eeDefaultVM = environments[i].getDefaultVM();
				if (eeDefaultVM != null && defaultVM.getId().equals(eeDefaultVM.getId()))
					return environments[i].getId();
			}
		}

		String defaultCC = "1.5"; //$NON-NLS-1$
		try {
			if (JavaModelUtil.class.getDeclaredField("VERSION_LATEST") != null) { //$NON-NLS-1$
				defaultCC = JavaModelUtil.VERSION_LATEST;
			}
		} catch (Exception e) {}

		if (defaultVM instanceof IVMInstall2)
			defaultCC = JavaModelUtil.getCompilerCompliance((IVMInstall2) defaultVM, defaultCC);

		for (int i = 0; i < environments.length; i++) {
			String eeCompliance = JavaModelUtil.getExecutionEnvironmentCompliance(environments[i]);
			if (defaultCC.endsWith(eeCompliance))
				return environments[i].getId();
		}
		
		return "J2SE-1.5"; //$NON-NLS-1$
	}
	
}
