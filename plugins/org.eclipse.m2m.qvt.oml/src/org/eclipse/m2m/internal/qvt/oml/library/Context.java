/*******************************************************************************
 * Copyright (c) 2007, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bug 431082
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.library;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.m2m.internal.qvt.oml.ExecutionDiagnosticImpl;
import org.eclipse.m2m.internal.qvt.oml.Messages;
import org.eclipse.m2m.internal.qvt.oml.NLS;
import org.eclipse.m2m.internal.qvt.oml.evaluator.QVTEvaluationOptions;
import org.eclipse.m2m.qvt.oml.ExecutionContext;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.util.EvaluationMonitor;
import org.eclipse.m2m.qvt.oml.util.IContext;
import org.eclipse.m2m.qvt.oml.util.ISessionData;
import org.eclipse.m2m.qvt.oml.util.Log;
import org.eclipse.m2m.qvt.oml.util.Trace;

/**
 * @noextend
 */
@SuppressWarnings("deprecation")
public class Context implements IContext {

    private final Map<String, Object>  myConfiguration;
    private SessionDataImpl myData;

    private Log myLog;
    private IProgressMonitor myMonitor;
    private Trace myTrace;
    private final ExecutionDiagnostic myDiagnostic = new ExecutionDiagnosticImpl(
			Diagnostic.OK, 0, NLS.bind(Messages.TransformationExecutionCompleted, null));
    
    public Context() {
    	this(Log.NULL_LOG, new NullProgressMonitor());
    }
    
    public Context(ExecutionContext context) {
    	this(context, context.getProgressMonitor());
    }
    
    public Context(ExecutionContext executionContext, IProgressMonitor monitor) {
    	this(executionContext.getLog(), monitor);
		
		for (String key : executionContext.getConfigPropertyNames()) {
			Object value = executionContext.getConfigProperty(key);
			setConfigProperty(key, value);
		}
		
		for (ISessionData.Entry<Object> key : executionContext.getSessionDataEntries()) {
			myData.setValue(key, executionContext.getSessionData().getValue(key));
		}
		
		org.eclipse.m2m.qvt.oml.util.Trace trace = executionContext.getSessionData().getValue(QVTEvaluationOptions.INCREMENTAL_UPDATE_TRACE);
		if (trace != null) {
			myTrace.setTraceContent(trace.getTraceContent());
		}
    }
    
    private Context(Log log, IProgressMonitor monitor) {
    	myLog = log;
		myMonitor = monitor;
		myConfiguration = new HashMap<String, Object>();
		myData = new SessionDataImpl();		
		myTrace = Trace.createEmptyTrace();
    }

    public void setProgressMonitor(IProgressMonitor monitor) {
    	if(monitor == null) {
    		throw new IllegalArgumentException("Non-null monitor required"); //$NON-NLS-1$
    	}

		this.myMonitor = monitor;
	}
    
    public IProgressMonitor getProgressMonitor() {    
    	return myMonitor;
    }
    
    /**
     * @deprecated Use getProgressMonitor() method
     */
    public EvaluationMonitor getMonitor() {    
    	return EvaluationMonitor.EvaluationMonitorWrapper.convert(myMonitor);
    }
    
    public void setLog(Log log) {
    	if(log == null) {
    		throw new IllegalArgumentException("Non-null logger required"); //$NON-NLS-1$
    	}
		this.myLog = log;
	}
    
    public Log getLog() {    	
    	return myLog;
    }
            
    public Trace getTrace() {    	
    	return myTrace;
    }
            
    public void setSessionData(ISessionData sessionData) {    
    	myData = new SessionDataImpl((SessionDataImpl) sessionData);
    }
    
    public ISessionData getSessionData() {    
    	return myData;
    }
    
    public Map<String, Object> getConfigProperties() {    
    	return Collections.unmodifiableMap(myConfiguration);
    }
    
    public Object getConfigProperty(String name) {
    	if(name == null) {
    		throw new IllegalArgumentException("null config property name"); //$NON-NLS-1$
    	}
    	return myConfiguration.get(name);
    }
    
    public void setConfigProperty(String name, Object value) {    
    	if(name == null) {
    		throw new IllegalArgumentException("null config property name"); //$NON-NLS-1$
    	}
    	myConfiguration.put(name, value);
    }
    
    /**
	 * @since 3.10
	 */
	public ExecutionDiagnostic getExecutionDiagnostic() {
		return myDiagnostic;
	}
    
    
    public static class SessionDataImpl implements ISessionData {
    	
    	private final Map<Entry<Object>, Object> fData;
    		
    	public SessionDataImpl(Map<Entry<Object>, Object> data) {
    		fData = data;
    	}
    	
    	SessionDataImpl() {
    		this(new HashMap<Entry<Object>, Object>());
    	}
    	
    	SessionDataImpl(SessionDataImpl sessionData) {
    		this(new HashMap<Entry<Object>, Object>(sessionData.fData));
    	}
    	
        @SuppressWarnings("unchecked")
    	public <T> T getValue(Entry<T> entry) {
        	if(fData.containsKey(entry)) {
        		return (T) fData.get(entry);
        	}
        	return entry.defaultValue();
        }
        
        @SuppressWarnings("unchecked")
		public <T> void setValue(Entry<T> key, T value) {
        	fData.put((Entry<Object>) key, value);
        }
    }    
}