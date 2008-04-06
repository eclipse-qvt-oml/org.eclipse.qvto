/*******************************************************************************
 * Copyright (c) 2007 Borland Software Corporation
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.runtime.project;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.m2m.internal.qvt.oml.common.MdaException;
import org.eclipse.m2m.internal.qvt.oml.common.project.CompiledTransformation;
import org.eclipse.m2m.internal.qvt.oml.common.project.IRegistryConstants;
import org.eclipse.m2m.internal.qvt.oml.common.project.TransformationRegistry;

public class QvtTransformationRegistry extends TransformationRegistry { 
    private QvtTransformationRegistry() {
        super(POINT);
    }
    
    public static QvtTransformationRegistry getInstance() {
        return ourInstance;
    }

    public QvtCompiledTransformation getTransformationById(final String id) {
        return (QvtCompiledTransformation)getSingleTransformationById(id);
    }
    
    @Override
	protected CompiledTransformation makeTransformation(IConfigurationElement element) throws MdaException {
        String namespace = element.getNamespaceIdentifier();
        String id = element.getAttribute(IRegistryConstants.ID);
        String file = element.getAttribute(IRegistryConstants.FILE);
        if(IRegistryConstants.TRANSFORMATION.equals(element.getName())) {
//        	EClass in = getEClassElement(element, IRegistryConstants.INPUT);
//        	EClass out = getEClassElement(element, IRegistryConstants.OUTPUT);        	    
        	return new QvtCompiledTransformation(namespace, id, file);
        }
        return QvtCompiledTransformation.createLibraryModule(namespace, id, file);
    }
    
    private static final QvtTransformationRegistry ourInstance = new QvtTransformationRegistry();
    
    public static final String POINT = "org.eclipse.m2m.qvt.oml.runtime.qvtTransformation"; //$NON-NLS-1$
}
