/*******************************************************************************
 * Copyright (c) 2009, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.project.builder;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.m2m.internal.qvt.oml.common.MDAConstants;
import org.eclipse.m2m.internal.qvt.oml.compiler.BlackboxUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompositeUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.DelegatingUnitResolver;
import org.eclipse.m2m.internal.qvt.oml.compiler.ResolverUtils;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitContents;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProvider;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitResolver;
import org.eclipse.m2m.internal.qvt.oml.emf.util.URIUtils;
import org.eclipse.m2m.internal.qvt.oml.project.QVTOProjectPlugin;
import org.eclipse.m2m.internal.qvt.oml.project.QvtProjectUtil;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.DeployedImportResolver;
import org.eclipse.m2m.internal.qvt.oml.runtime.project.ProjectDependencyTracker;



public class WorkspaceUnitResolver extends DelegatingUnitResolver implements UnitProvider {

	private final List<IContainer> fRoots;
	
	public WorkspaceUnitResolver(List<IContainer> sourceContainers) {
		if(sourceContainers == null || sourceContainers.contains(null)) {
			throw new IllegalArgumentException("null source container"); //$NON-NLS-1$
		}
				
		IProject project = null;
		for (IContainer container : sourceContainers) {
			if(project != null) {
				if(project != container.getProject()) {
					throw new IllegalArgumentException("source containers must be from single project"); //$NON-NLS-1$
				}
			} else {
				project = container.getProject();
			}

			if(!container.exists()) {
				throw new IllegalArgumentException("Unexisting source container"); //$NON-NLS-1$
			}
		}
		
		fRoots = new ArrayList<IContainer>(sourceContainers);
		if(project != null) {
			setParent(new CompositeUnitResolver(
						getReferencedProjectsResolver(project, new HashMap<IProject, UnitResolver>()),
						new BlackboxUnitResolver(URIUtils.getResourceURI(project)),
						DeployedImportResolver.INSTANCE));
		}
	}
	
	private WorkspaceUnitResolver(IContainer sourceContainer, Map<IProject, UnitResolver> analizedProjects) {
		fRoots = Collections.singletonList(sourceContainer);

		IProject project = sourceContainer.getProject();
		setParent(new CompositeUnitResolver(
					getReferencedProjectsResolver(project, analizedProjects),
					new BlackboxUnitResolver(URIUtils.getResourceURI(project)),
					DeployedImportResolver.INSTANCE));
	}
	
	private UnitResolver getReferencedProjectsResolver(IProject project, Map<IProject, UnitResolver> analyzedProjects) {
		analyzedProjects.put(project, this);

		List<UnitResolver> resolvers = Collections.emptyList();
		try {
			Set<IProject> referencedProjects = ProjectDependencyTracker.getAllReferencedProjects(project, false);
			resolvers = new ArrayList<UnitResolver>(referencedProjects.size());

			for (IProject referenced : referencedProjects) {
				if (!QvtProjectUtil.isQvtProject(referenced)) {
					continue;
				}
				
				if (analyzedProjects.containsKey(referenced)) {
					resolvers.add(analyzedProjects.get(referenced));
					continue;
				}
				
				if (referenced == null || !referenced.isAccessible()) {
					continue;
				}

				IContainer sourceContainer = QVTOBuilderConfig.getConfig(referenced).getSourceContainer();
				if (sourceContainer == null || !sourceContainer.exists()) {
					continue;
				}

				resolvers.add(new WorkspaceUnitResolver(sourceContainer, analyzedProjects));
			}
		} catch (CoreException e) {
			QVTOProjectPlugin.log(e.getStatus());
		}

		return new CompositeUnitResolver(resolvers.toArray(new UnitResolver[resolvers.size()]));
	}
	
	@Override
	protected UnitProxy doResolveUnit(String qualifiedName) {
		if(qualifiedName == null) {
			throw new IllegalArgumentException("null qualified name"); //$NON-NLS-1$
		}

		IResource member = null;
		for (IContainer nextContainer : fRoots) {
			member = resolveResource(nextContainer, qualifiedName, true);
			if(member != null) {
				return createFileUnit(nextContainer.getFullPath(), member.getFullPath());
			}
		}

		return null;
	}
	
	public void accept(UnitProvider.UnitVisitor visitor, String scopeQualifiedName, int depth, boolean includeExternal) {
		if(visitor == null) {
			throw new IllegalArgumentException();
		}

		for (IContainer nextContainer : fRoots) {
			IResource resourceToVisit = nextContainer;
			if(scopeQualifiedName != null) {				
				resourceToVisit = resolveResource(nextContainer, scopeQualifiedName, false);
				if(resourceToVisit == null) {
					// nothing to visit
					continue; 
				}
			} 

			try {
				ResourceVisitor resourceVisitor = new ResourceVisitor(visitor, 
						nextContainer, resourceToVisit.getFullPath(), depth);
				
				resourceToVisit.accept(resourceVisitor, IResource.NONE);
			} catch(CoreException e) {				
				// FIXME - log the problem
			}
		}
	}

	private IResource resolveResource(IContainer container, String qualifiedName, boolean isUnit) {		
		IPath unitPath = qName2Path(qualifiedName);
		if(!isUnit) {
			unitPath = unitPath.removeFileExtension();
		}
		
		return container.findMember(unitPath);		
	}
	

	private FileUnit createFileUnit(IPath sourceContainer, IPath unitPath) {
		String unitName = unitPath.removeFileExtension().lastSegment();
		String namespace = ResolverUtils.getNamespace(unitPath.removeLastSegments(1), sourceContainer);
		URI uri = URI.createPlatformResourceURI(unitPath.toString(), true);

		return new FileUnit(namespace, unitName, uri);
	}
	
	private static IPath qName2Path(String qname) {
		assert qname != null;
		
		String pathStr = ResolverUtils.toNamespaceRelativeUnitFilePath(qname);
		return new Path(pathStr);
	}
	
	
	private class ResourceVisitor implements IResourceProxyVisitor {
		
		private UnitProvider.UnitVisitor fUnitVisitor;
		private IContainer fContainer;
		private IPath fScope;
		private int fDepth;
		
		ResourceVisitor(UnitProvider.UnitVisitor unitVisitor, IContainer srcContainer, IPath scope, int depth) {			
			assert unitVisitor != null;
			assert srcContainer != null;
			assert scope != null;			
			
			fUnitVisitor = unitVisitor;
			fContainer = srcContainer;
			fScope = scope;
			fDepth = depth;
		}

		public boolean visit(IResourceProxy proxy) throws CoreException {
			String name = proxy.getName();
			int type = proxy.getType();
			
			if(type == IResource.FILE && name.endsWith(MDAConstants.QVTO_FILE_EXTENSION_WITH_DOT)) {
				IPath path = proxy.requestFullPath();
				
				String unitName = path.removeFileExtension().lastSegment();
				String namespace = ResolverUtils.getNamespace(path.removeLastSegments(1), fContainer.getFullPath());
				URI uri = URI.createPlatformResourceURI(path.toString(), true);

				UnitProxy unit = new FileUnit(namespace, unitName, uri); 
				
				return fUnitVisitor.visitUnit(unit);
			} 
			else if(type == IResource.FOLDER || type == IResource.PROJECT) {
				if(fDepth == UnitProvider.UnitVisitor.DEPTH_ZERO) {
					return false;
				}
				
				if(fDepth == UnitProvider.UnitVisitor.DEPTH_ONE) {
					IPath visitedPath = proxy.requestFullPath();
					if(!fScope.equals(visitedPath)) {
						return false;
					}
				}
				
				return true;
			}
			
			return false;
		}		
	}
	

	private class FileUnit extends UnitProxy {
		
		public FileUnit(String namespace, String unitName, URI uri) {
			super(namespace, unitName, uri);
			
			assert uri.isPlatformResource();			
		}
		
		@Override
		public int getContentType() {		
			return UnitProxy.TYPE_CST_STREAM;
		}
		
		@Override
		public UnitResolver getResolver() {		
			return WorkspaceUnitResolver.this;
		}
		
		@Override
		public UnitContents getContents() throws IOException {
			URI uri = getURI(); 
			String wsRelativePath = uri.toPlatformString(true);

			final IFile sourceFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(wsRelativePath));
			if(!sourceFile.exists()) {
				throw new IOException("File does not exist, URI: " + sourceFile.getFullPath()); //$NON-NLS-1$
			}

			return new UnitContents.CSTContents() {
								
				public Reader getContents() throws IOException {
					try {
						return new InputStreamReader(sourceFile.getContents(), sourceFile.getCharset());
					} catch(CoreException e) {
						// FIME - log the problem
						throw new IOException(e.getLocalizedMessage());
					}
				}
			};
		}
	}
	
//	public static List<String> getRequiredPlugins(Bundle bundle) {
//		Dictionary<?, ?> headers = bundle.getHeaders();
//		String requireBundleHeader = headers.get(Constants.REQUIRE_BUNDLE).toString();
//		if(requireBundleHeader == null) {
//			return Collections.emptyList();
//		}
//		
//		ManifestElement[] parsedElements = null;		
//		try {
//	    	parsedElements = ManifestElement.parseHeader(Constants.REQUIRE_BUNDLE, requireBundleHeader);
//		} catch (BundleException e) {
//			// do nothing
//		}
//	
//		if(parsedElements == null) {
//			return Collections.emptyList();			
//		}
//	
//		List<String> bundles = new ArrayList<String>(parsedElements.length);
//		for (ManifestElement element : parsedElements) {
//			String value = element.getValue();
//			if(value != null) {
//				bundles.add(element.getValue());
//			}
//		}				
//	
//		return bundles;
//	}
	
}
