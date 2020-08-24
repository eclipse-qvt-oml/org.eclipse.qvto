package org.eclipse.m2m.tests.qvt.oml.bbox;

import java.util.Date;

import org.eclipse.emf.ecore.xml.type.internal.XMLCalendar;

public class Bug566216_Library {
	
	public static Date toDate(XMLCalendar self) {
		return self.getDate();
	}
	 		
	public Number simpleCreateBigInt(String value) {
		return Float.parseFloat(value);
	}
	
}
