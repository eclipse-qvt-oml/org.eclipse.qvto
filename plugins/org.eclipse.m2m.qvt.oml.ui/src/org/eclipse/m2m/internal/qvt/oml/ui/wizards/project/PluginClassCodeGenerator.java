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
 *     Christopher Gerking - bugs 414662, 562175
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.ui.wizards.project;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.m2m.internal.qvt.oml.QvtPlugin;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.ui.QVTUIPlugin;

/**
 * @deprecated as per bug 562175. 
 * Use {@link org.eclipse.pde.internal.ui.wizards.plugin.PluginClassCodeGenerator} instead.
 */
@Deprecated
class PluginClassCodeGenerator {
	
	private final IProject fProject;
	private final String fQualifiedClassName;
	
	private final String fSourceFolderName;
	private final boolean fIsUIPlugin;
	private final String fOSGiFramework;
	private final String fPluginID;
	
	private boolean fIsPlugin;

	public PluginClassCodeGenerator(IProject project, NewProjectData projectData) {
		fProject = project;
		fQualifiedClassName = projectData.getClassName();
		fSourceFolderName = projectData.getSourceFolderName();
		fIsUIPlugin = false;
		fOSGiFramework = null;
		fPluginID = projectData.getID();
		fIsPlugin = projectData.isPlugin();
	}

	public IFile generate(IProgressMonitor monitor) throws CoreException {
		int nameloc = fQualifiedClassName.lastIndexOf(UnitProxy.NAMESPACE_SEP);
		String packageName = (nameloc == -1) ? "" : fQualifiedClassName.substring(0, nameloc); //$NON-NLS-1$
		String className = fQualifiedClassName.substring(nameloc + 1);

		IPath path = new Path(packageName.replace(UnitProxy.NAMESPACE_SEP, IPath.SEPARATOR));
		if (fSourceFolderName != null && fSourceFolderName.trim().length() > 0) {
			path = new Path(fSourceFolderName).append(path);			
		}

		IFile file = fProject.getFile(path.append(className + ".java")); //$NON-NLS-1$
		if(!file.getParent().exists()) {
			createFolder(fProject.getFolder(path));
		}
		
		StringWriter swriter = new StringWriter();
		PrintWriter writer = new PrintWriter(swriter);
		if (fOSGiFramework != null) {
			generateActivatorClass(packageName, className, writer);
		} else {
			generatePluginClass(packageName, className, writer);
		}
		
		writer.flush();
		try {
			swriter.close();
			ByteArrayInputStream stream = new ByteArrayInputStream(swriter.toString().getBytes(fProject.getDefaultCharset()));
			if (file.exists()) {
				file.setContents(stream, false, true, monitor);				
			} else {
				file.create(stream, false, monitor);
			}
			
			stream.close();
		} catch (IOException e) {
			QVTUIPlugin.log(e);
		}
		
		return file;
	}

	private void generatePluginClass(String packageName, String className, PrintWriter writer) {
		if (!packageName.equals("")) { //$NON-NLS-1$
			writer.println("package " + packageName + ";"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.println();
		}
		if (fIsUIPlugin) {
			writer.println("import org.eclipse.jface.resource.ImageDescriptor;"); //$NON-NLS-1$
			writer.println("import org.eclipse.ui.plugin.AbstractUIPlugin;"); //$NON-NLS-1$
		} else {
			writer.println("import org.eclipse.core.runtime.Plugin;"); //$NON-NLS-1$
		}
		writer.println("import org.osgi.framework.BundleContext;"); //$NON-NLS-1$
		writer.println();
		writer.println("/**"); //$NON-NLS-1$
		writer.println(" * The activator class controls the plug-in life cycle"); //$NON-NLS-1$
		writer.println(" */"); //$NON-NLS-1$
		if (fIsUIPlugin)
			writer.println("public class " + className + " extends AbstractUIPlugin {"); //$NON-NLS-1$ //$NON-NLS-2$
		else
			writer.println("public class " + className + " extends Plugin {"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.println();
		writer.println("\t// The plug-in ID"); //$NON-NLS-1$
		writer.println("\tpublic static final String PLUGIN_ID = \"" + fPluginID + "\";"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.println();
		writer.println("\t// The shared instance"); //$NON-NLS-1$
		writer.println("\tprivate static " + className + " plugin;"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.println("\t"); //$NON-NLS-1$
		writer.println("\t/**"); //$NON-NLS-1$
		writer.println("\t * The constructor"); //$NON-NLS-1$
		writer.println("\t */"); //$NON-NLS-1$
		writer.println("\tpublic " + className + "() {"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.println("\t}"); //$NON-NLS-1$
		writer.println();

		writer.println("\t/*"); //$NON-NLS-1$
		writer.println("\t * (non-Javadoc)"); //$NON-NLS-1$
		if (fIsUIPlugin)
			writer.println("\t * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)"); //$NON-NLS-1$
		else
			writer.println("\t * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)"); //$NON-NLS-1$
		writer.println("\t */"); //$NON-NLS-1$
		writer.println("\tpublic void start(BundleContext context) throws Exception {"); //$NON-NLS-1$
		writer.println("\t\tsuper.start(context);"); //$NON-NLS-1$
		writer.println("\t\tplugin = this;"); //$NON-NLS-1$
		writer.println("\t}"); //$NON-NLS-1$
		writer.println();

		writer.println("\t/*"); //$NON-NLS-1$
		writer.println("\t * (non-Javadoc)"); //$NON-NLS-1$
		if (fIsUIPlugin)
			writer.println("\t * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)"); //$NON-NLS-1$
		else
			writer.println("\t * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)"); //$NON-NLS-1$
		writer.println("\t */"); //$NON-NLS-1$
		writer.println("\tpublic void stop(BundleContext context) throws Exception {"); //$NON-NLS-1$
		writer.println("\t\tplugin = null;"); //$NON-NLS-1$
		writer.println("\t\tsuper.stop(context);"); //$NON-NLS-1$
		writer.println("\t}"); //$NON-NLS-1$
		writer.println();

		writer.println("\t/**"); //$NON-NLS-1$
		writer.println("\t * Returns the shared instance"); //$NON-NLS-1$
		writer.println("\t *"); //$NON-NLS-1$
		writer.println("\t * @return the shared instance"); //$NON-NLS-1$
		writer.println("\t */"); //$NON-NLS-1$
		writer.println("\tpublic static " + className + " getDefault() {"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.println("\t\treturn plugin;"); //$NON-NLS-1$
		writer.println("\t}"); //$NON-NLS-1$
		writer.println();
		if (fIsUIPlugin) {
			writer.println("\t/**"); //$NON-NLS-1$
			writer.println("\t * Returns an image descriptor for the image file at the given"); //$NON-NLS-1$
			writer.println("\t * plug-in relative path"); //$NON-NLS-1$
			writer.println("\t *"); //$NON-NLS-1$
			writer.println("\t * @param path the path"); //$NON-NLS-1$
			writer.println("\t * @return the image descriptor"); //$NON-NLS-1$
			writer.println("\t */"); //$NON-NLS-1$
			writer.println("\tpublic static ImageDescriptor getImageDescriptor(String path) {"); //$NON-NLS-1$
			writer.println("\t\treturn imageDescriptorFromPlugin(PLUGIN_ID, path);"); //$NON-NLS-1$ 
			writer.println("\t}"); //$NON-NLS-1$
		}
		writer.println("}"); //$NON-NLS-1$
	}

	private void generateActivatorClass(String packageName, String className, PrintWriter writer) {
		if (!packageName.equals("")) { //$NON-NLS-1$
			writer.println("package " + packageName + ";"); //$NON-NLS-1$ //$NON-NLS-2$
			writer.println();
		}
		writer.println("import org.osgi.framework.BundleActivator;"); //$NON-NLS-1$
		writer.println("import org.osgi.framework.BundleContext;"); //$NON-NLS-1$
		writer.println();
		writer.println("public class " + className + " implements BundleActivator {"); //$NON-NLS-1$ //$NON-NLS-2$
		writer.println();
		writer.println("\t/*"); //$NON-NLS-1$
		writer.println("\t * (non-Javadoc)"); //$NON-NLS-1$
		writer.println("\t * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)"); //$NON-NLS-1$
		writer.println("\t */"); //$NON-NLS-1$
		writer.println("\tpublic void start(BundleContext context) throws Exception {"); //$NON-NLS-1$
		writer.println("\t}"); //$NON-NLS-1$
		writer.println();
		writer.println("\t/*"); //$NON-NLS-1$
		writer.println("\t * (non-Javadoc)"); //$NON-NLS-1$
		writer.println("\t * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)"); //$NON-NLS-1$
		writer.println("\t */"); //$NON-NLS-1$
		writer.println("\tpublic void stop(BundleContext context) throws Exception {"); //$NON-NLS-1$
		writer.println("\t}"); //$NON-NLS-1$
		writer.println();
		writer.println("}"); //$NON-NLS-1$		
	}

	public PluginReference[] getDependencies() {
		ArrayList<PluginReference> result = new ArrayList<PluginReference>();
		if (fIsUIPlugin) {
			result.add(new PluginReference("org.eclipse.ui", null)); //$NON-NLS-1$
		}
		if (fOSGiFramework == null) {
			result.add(new PluginReference(Platform.PI_RUNTIME, null));
		}
		if (fIsPlugin) {
			result.add(new PluginReference(QvtPlugin.ID, null));
		}
		
		return (PluginReference[]) result.toArray(new PluginReference[result.size()]);
	}

	public String[] getImportPackages() {
		return fOSGiFramework != null ? new String[] { "org.osgi.framework;version=\"1.3.0\"" } //$NON-NLS-1$
				: new String[0];
	}

	private static void createFolder(IFolder folder) throws CoreException {
		if (!folder.exists()) {
			IContainer parent = folder.getParent();
			if (parent instanceof IFolder) {
				createFolder((IFolder) parent);
			}
			folder.create(true, true, null);
		}
	}	
}
