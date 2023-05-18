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
package org.eclipse.m2m.internal.qvt.oml.blackbox.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.m2m.internal.qvt.oml.ast.binding.ASTBindingHelper;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalModuleEnv;
import org.eclipse.m2m.internal.qvt.oml.compiler.CompilerUtils;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitContents.ModelContents;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitProxy;
import org.eclipse.m2m.internal.qvt.oml.compiler.UnitResolverFactory;
import org.eclipse.m2m.internal.qvt.oml.emf.util.EmfUtil;

public class BlackboxResourceFactory implements Resource.Factory {
	
	/**
	 * Creates an instance.
	 */
	public BlackboxResourceFactory() {
		super();
	}

	public Resource createResource(URI uri) {
		return new ResourceImpl(uri) {
			
			@Override
			public void load(Map<?, ?> options) throws IOException {
			    if (!isLoaded) {
					UnitProxy unitProxy = UnitResolverFactory.Registry.INSTANCE.getUnit(uri);
					if(unitProxy != null) {
						ResourceSet resourceSet = CompilerUtils.cloneRegistrations(getResourceSet());
					    getContents().addAll(loadBlackboxUnit(unitProxy, resourceSet));
					}

					setLoaded(true);
					
					eAdapters().add(EmfUtil.RESOURCE_PERSISTED_ADAPTER);
				}
			}
			@Override
			protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
				// do nothing we do no load from a stream
			}
		};
	}

    private List<? extends EObject> loadBlackboxUnit(UnitProxy unit, ResourceSet rs) throws IOException {    	
    	ModelContents contents = (ModelContents) unit.getContents();

    	List<? extends EObject> topElements = contents.loadElements(rs.getPackageRegistry());
//    	List<QvtOperationalModuleEnv> modelEnvs = new ArrayList<QvtOperationalModuleEnv>(topElements.size());
    	
    	for (EObject nextElement : topElements) {
    		QvtOperationalModuleEnv nextEnv = ASTBindingHelper.getEnvironment(nextElement, QvtOperationalModuleEnv.class);
    		if(nextEnv != null) {
    			// FIXME -
    			// clear the environment problems, for now we do not consider errors
    			// like duplicate operation definitions to cause the importing unit to fail
    			nextEnv.clearProblems();
//    			modelEnvs.add(nextEnv);
    			
//    			Resource resource = nextEnv.getModuleContextType().eResource();    			
//				resource.setURI(unit.getURI());				
    		}
		}
    	
		return topElements;
    }    
}
