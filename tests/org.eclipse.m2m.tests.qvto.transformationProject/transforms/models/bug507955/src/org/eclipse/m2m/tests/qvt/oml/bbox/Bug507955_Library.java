package org.eclipse.m2m.tests.qvt.oml.bbox;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.m2m.qvt.oml.blackbox.java.Module;

@Module(packageURIs={"http://www.eclipse.org/emf/2002/Ecore"})
public class Bug507955_Library {
	
	public Bug507955_Library() {}
	
	public EClass mirrorBug507955(EClass c) {
		return c;
	}

}
