/*******************************************************************************
 * Copyright (c) 2008, 2020 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Christopher Gerking - bug 566216
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.bbox;

import java.math.BigInteger;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.xml.type.internal.XMLCalendar;
import org.eclipse.m2m.qvt.oml.blackbox.java.Module;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation;


@Module(packageURIs={
	"http://www.eclipse.org/emf/2002/Ecore",
	"http://www.eclipse.org/emf/2003/XMLType"
	})
public class SimpleJavaLibrary {
	
	public SimpleJavaLibrary() {
		 super();
	}
	
	public String echoFromSimpleJavaLibrary(String str) {
		return str;
	}
	
	public Date simpleCreateDate(String dateStr) {
		return (Date) EcoreFactory.eINSTANCE.createFromString(EcorePackage.eINSTANCE.getEDate(), dateStr);
	}
	
	@Operation(contextual=true)
	public static boolean isBefore(Date self, Date when) {
		return self.before(when);
	}		
	
	@Operation(contextual=true)
	public static XMLGregorianCalendar toXMLCalendar(Date self) {
		return new XMLCalendar(self, XMLCalendar.DATETIME);
	}
		
	@Operation(contextual=true)
	public static Date toDate(XMLGregorianCalendar self) {
		if (self instanceof XMLCalendar) {
			return ((XMLCalendar) self).getDate();
		}
		
		return null;
	}
		
	public BigInteger simpleCreateBigInt(String value) {
		return new BigInteger(value);
	}

}
