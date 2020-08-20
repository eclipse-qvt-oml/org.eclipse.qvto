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
 *     Christopher Gerking - bugs 431082, 566216
 *******************************************************************************/
package org.eclipse.m2m.tests.qvt.oml.bbox;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.stdlib.MutableListImpl;
import org.eclipse.m2m.qvt.oml.blackbox.java.Module;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation;
import org.eclipse.m2m.qvt.oml.blackbox.java.Operation.Kind;
import org.eclipse.m2m.qvt.oml.blackbox.java.Parameter;
import org.eclipse.m2m.qvt.oml.util.Dictionary;
import org.eclipse.m2m.qvt.oml.util.IContext;
import org.eclipse.m2m.qvt.oml.util.MutableList;
import org.eclipse.m2m.qvt.oml.util.Utils;
import org.eclipse.ocl.util.Bag;
import org.eclipse.ocl.util.CollectionUtil;

import generics.GenericCls;
import generics.GenericsFactory;
import simpleuml.Model;

@Module(packageURIs={
		"http://www.eclipse.org/qvt/1.0.0/Operational/examples/simpleuml",	    		
    	"http://www.eclipse.org/emf/2002/Ecore",
		"http://www.eclipse.org/qvt/1.0.0/Operational/Expressions",
		"http://www.eclipse.org/m2m/qvt/oml/generics"})
public class AnnotatedJavaLibrary {
	
	private String myState;
	
	public AnnotatedJavaLibrary() {
		 super();
	}

	@Operation (kind=Kind.HELPER, contextual = true)
	public <T> T genericListElementAt(MutableList<T> self, int index) {
		return self.get(index - 1);
	}
	
	@Operation (kind=Kind.HELPER, contextual = false)
	public MutableList<Set<Boolean>> newListOfBooleanSetsFromBooleanCollection(Collection<Boolean> elements) {
		final MutableListImpl<Set<Boolean>> result = new MutableListImpl<Set<Boolean>>();
		result.add(new HashSet<Boolean>(elements));
		return result;
	}
		
	@Operation (kind=Kind.HELPER, contextual = false)
	public MutableList<String> newList(Collection<String> elements) {
		return new MutableListImpl<String>(elements);
	}	
	
	@Operation (kind=Kind.HELPER, contextual = false)
	public MutableList<String> echoList(MutableList<String> listType) {
		return new MutableListImpl<String>(listType);
	}
	
	@Operation (kind=Kind.HELPER, contextual = false)
	public ArrayList<Object> primitiveTypeArgumentsHelper(String strArg, Integer intArg, Double realArg, Boolean boolArg) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(strArg);
		result.add(intArg);
		result.add(realArg);
		result.add(boolArg);		
		return result;
	}
	
	@Operation (kind=Kind.QUERY, contextual = false)
	public ArrayList<Object> primitiveTypeArgumentsQuery(String strArg, Integer intArg, Double realArg, Boolean boolArg) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(strArg);
		result.add(intArg);
		result.add(realArg);
		result.add(boolArg);		
		return result;
	}
	
	@Operation(contextual = false)
	public ArrayList<Object> primitiveTypeArgumentsOperation(String strArg, Integer intArg, Double realArg, Boolean boolArg) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(strArg);
		result.add(intArg);
		result.add(realArg);
		result.add(boolArg);		
		return result;
	}

	public ArrayList<Object> primitiveTypeArgumentsDefault(String strArg, Integer intArg, Double realArg, Boolean boolArg) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(strArg);
		result.add(intArg);
		result.add(realArg);
		result.add(boolArg);		
		return result;
	}
	
	
	// contextual operations
	private Map<String, Integer> fCounters = new HashMap<String, Integer>();
	
	@Operation (contextual = true)
	public int getStrCounterX(String self) {
		Integer val = fCounters.get(self);
		if(val == null) {
			return 0;
		}
		return fCounters.get(self);
	}

	@Operation (contextual = true)
	public int incStrCounterX(String self) {
		Integer val = fCounters.get(self);
		if(val == null) {
			fCounters.put(self, 1);
		} else {
			fCounters.put(self, ++val);
		}
		return fCounters.get(self);
	}	
	
	@Operation (kind=Kind.HELPER, contextual = true)
	public static ArrayList<Object> primitiveTypeArgumentsHelperContextual(EAnnotation self, String strArg, Integer intArg, Double realArg, Boolean boolArg) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(strArg);
		result.add(intArg);
		result.add(realArg);
		result.add(boolArg);		
		return result;
	}
	
	@Operation (kind=Kind.QUERY, contextual = true)
	public static ArrayList<Object> primitiveTypeArgumentsQueryContextual(EAnnotation self, String strArg, Integer intArg, Double realArg, Boolean boolArg) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(strArg);
		result.add(intArg);
		result.add(realArg);
		result.add(boolArg);		
		return result;
	}
	
	@Operation (contextual = true)
	public static ArrayList<Object> primitiveTypeArgumentsOperationContextual(EAnnotation self, String strArg, Integer intArg, Double realArg, Boolean boolArg) {
		ArrayList<Object> result = new ArrayList<Object>();
		result.add(strArg);
		result.add(intArg);
		result.add(realArg);
		result.add(boolArg);		
		return result;
	}

	
	@Operation (kind=Kind.HELPER, contextual = true)
	public static String testContextualSuffixedName(
			@Parameter(name="eNamedElement") ENamedElement eNamedElement, 
			@Parameter(name="suffix") String suffix) {
		return eNamedElement.getName() + suffix;
	}
	
	@Operation (kind=Kind.HELPER)
	public String testEchoArgument(String param) {
		return param;
	}

	@Operation (kind=Kind.QUERY)
	public String staticTestEchoArgument(String param) {
		return param;
	}	
	
	@Operation(contextual=true)
	public static Set<ArrayList<String>> wrapInSet(ArrayList<String> sequence) {
		Set<ArrayList<String>> result = CollectionUtil.createNewSet();
		result.add(sequence);
		return result;
	}	
	@Operation(contextual=true)
	public static Set<ArrayList<Object>> commonCollectionInSet(Collection<Object> sequence) {
		Set<ArrayList<Object>> result = CollectionUtil.createNewSet();
		result.add(new ArrayList<Object>(sequence));
		return result;
	}		

	public Set<LinkedHashSet<List<Bag<String>>>> testComplexTypeCopy(Set<LinkedHashSet<ArrayList<Bag<String>>>> complexType) {
		String element = complexType.iterator().next().iterator()
			.next().iterator().next().iterator().next();
				
		final Bag<String> createNewBag = CollectionUtil.createNewBag();
		createNewBag.add(element);
		final List<Bag<String>> createNewSequence = CollectionUtil.createNewSequence();
		createNewSequence.add(createNewBag);
		final LinkedHashSet<List<Bag<String>>> createNewOrderedSet = CollectionUtil.createNewOrderedSet();
		createNewOrderedSet.add(createNewSequence);
		final Set<LinkedHashSet<List<Bag<String>>>> result = CollectionUtil.createNewSet();
		result.add(createNewOrderedSet);
		return result; 
	}
	
	public void noReturnType(String str) {
		// do nothing, testing void return type
	}
	
	public Model testUsedSimpleUMLModelPackage(Model model) {
		return model;
	}
	
	public String getEnumLiteralName(DirectionKind directionKind) {
		return directionKind.getName();
	}
	
	// TODO - add support for java big numbers
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Numeric types operation
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public BigInteger createBigInteger(String value) {
		return new BigInteger(value);
	}
		
	public BigInteger addBigInteger(BigInteger self, BigInteger big2) {
		return self.add(big2);
	}
	
	public BigDecimal createBigDecimal(String value) {
		return new BigDecimal(value);
	}
	
	public BigDecimal addBigDecimal(BigDecimal big1, BigDecimal big2) {
		return big1.add(big2);
	}

	// test lower precision types 
	public Long sumLowerPrecisions(Short s, Integer i, Long l) {
		// Byte b,
		return /*b.byteValue() + */ s.shortValue() + i.intValue() + l.longValue();		
	}

	// TODO - byte not mapped by MDT OCL, raise bugzilla 
	public long sumLowerPrecisionWithoutWrappers(/*byte b, */short s, int i, long l) {
		return /*b + */ s + i + l;
	}
	
	public Double sumLowerPrecisionDoubles(Float f, Double d) {
		return f.floatValue() + d.doubleValue();
	}

	public double sumLowerPrecisionDoublesWithoutWrappers(float f, double d) {
		return f + d;
	}
	
	@Operation(contextual=true)
	public static boolean theSameJavaBigIntegerInstance(BigInteger self, BigInteger another) {
		return self == another;
	}
	
	@Operation(contextual=true)
	public static String stringContextualEcho(String self) {
		return self;
	}

	@Operation(contextual=true)
	public static boolean nonObjectBoolContextualEcho(boolean self) {
		return self;
	}	
	
	@Operation(contextual=true)
	public static int nonObjectIntContextualEcho(int self) {
		return self;
	}
	
	@Operation(contextual=true)
	public static double nonObjectDoubleContextualEcho(double self) {
		return self;
	}
	
	@Operation(contextual=true)
	public static boolean boolContextualEcho(boolean self) {
		return self;
	}	
	
	@Operation(contextual=true)
	public static int intContextualEcho(int self) {
		return self;
	}
	
	@Operation(contextual=true)
	public static double doubleContextualEcho(double self) {
		return self;
	}

	// test data type not mapped by MDT OCL 
	public Date createDate(String dateStr) {
		return (Date)EcoreFactory.eINSTANCE.createFromString(EcorePackage.eINSTANCE.getEDate(), dateStr);
	}
	
	@Operation(contextual=true)
	public static boolean before(Date self, Date when) {
		return self.before(when);
	}		
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Collection type operation
	// TODO - MDT OCL does not support the type-checker extensibility fully yet to
	// realize collection operations  
	@Operation (kind=Kind.HELPER, contextual = true)
	public static <T> int countIgnoreUndefined(Collection<T> self) {
		int count = 0;
		if(self != null) {
			for (T element : self) {
				if(element != null) {
					count++;
				}
			}
		}

		return count;
	}
		
	// tests T, T2 parameterized types together
	@Operation (contextual = true)
	public static <T, T2> Collection<T2> shrinkAnotherCollectionToMySize(Collection<T> self, Collection<T2> another) {
		return new ArrayList<T2>(another).subList(0, self.size());
	}	
	
	public String foo() {
		return null;
	}
	
	
	@Operation (kind=Kind.HELPER, contextual = true)
	public static <T> T anyElement(Collection<T> self) {
		return self.iterator().next();
	}
	
	@Operation (kind=Kind.OPERATION, contextual = true)
	public static <T> Set<T> echoAsSet(Collection<T> self) {
		return new HashSet<T>(self);
	}
	
	public void setLibState(String newState) {
		myState = newState;
	}
	
	public String getLibState() {
		return myState;
	}
	
	// test we have access to the library state 
	@Operation (kind=Kind.HELPER, contextual = true)
	public String appendToLibState(String aString) {
		return myState = myState + aString; 
	}
	
	@Operation(contextual=true)
	public Dictionary<String, String> toString2StringDict(Dictionary<Integer, String> self) {
		Dictionary<String, String> result = Utils.createDictionary();
		for (Integer key : self.keys()) {
			result.put(key.toString(), self.get(key));
		}
		return result;
	}

	@Operation (kind=Kind.HELPER, withExecutionContext=true)
	public void logToConsole(IContext context, String param) {
		context.getLog().log(param);
	}

	@SuppressWarnings("deprecation")
	@Operation (withExecutionContext=true)
	public void cancelExecution(IContext context) {
		context.getMonitor().cancel();
		// context.getProgressMonitor().setCanceled(true);
	}
	
	@Operation (contextual=true, withExecutionContext=true)
	public String getConfigProperty(IContext context, String strContext) {
		return context.getConfigProperty(strContext).toString();
	}
	
	@Operation (kind=Kind.OPERATION, contextual=true, withExecutionContext=true)
	public String getConfigProperty2(IContext context, String strContext, String value) {
		return context.getConfigProperty(strContext+value).toString();
	}
	
	/*
	 * This library method is used for testing UML2-like behavior 
	 * of org.eclipse.uml2.uml.internal.operations.ElementOperations::applyStereotype(..)
	 */
	public void createStereotypeApplication(ENamedElement element) {
		if (element.eResource() != null) {
			EClass stereotypeApplication = (EClass) EcoreUtil.create(EcorePackage.Literals.ECLASS);
			stereotypeApplication.setName(element.getName() + " : stereotypeApplication"); //$NON-NLS-1$
			element.eResource().getContents().add(stereotypeApplication);
		}
	}
	
	@Operation (kind=Kind.QUERY)
	public void testThrowRuntimeException() {
		throw new RuntimeException("Exception from bbox library"); //$NON-NLS-1$
	}
	
	// EMF generic test
	//
	@Operation (kind=Kind.QUERY)
	public GenericCls<?> getGenericClsWildcard() {
		GenericCls<Integer> genericCls = GenericsFactory.eINSTANCE.<Integer>createGenericCls();
		genericCls.getValues().add(1);
		return genericCls;
	}

	@Operation (kind=Kind.QUERY)
	public GenericCls<Integer> getGenericClsInteger() {
		GenericCls<Integer> genericCls = GenericsFactory.eINSTANCE.<Integer>createGenericCls();
		genericCls.getValues().add(1);
		return genericCls;
	}

	@Operation (kind=Kind.QUERY)
	public <E extends Number> GenericCls<E> getGenericClsType() {
		return GenericsFactory.eINSTANCE.createGenericCls();
	}
			
	@Operation (kind=Kind.QUERY)
	public Time getTime() {
		return Time.valueOf(LocalTime.now());
	}
	
	@Operation (kind=Kind.QUERY)
	public String notifierToString(Notifier notifier) {
		return notifier.toString();
	}
}
